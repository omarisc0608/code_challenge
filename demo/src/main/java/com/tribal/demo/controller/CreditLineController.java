package com.tribal.demo.controller;

import com.tribal.demo.domain.CreditLineRequest;
import com.tribal.demo.domain.CreditLineResponse;
import com.tribal.demo.service.CreditLineFactory;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

import static com.tribal.demo.domain.BusinessMessages.MANY_TIMES_404_MESSAGE;
import static com.tribal.demo.domain.BusinessMessages.SALES_CONTACT;

@Slf4j
@RestController
@RequestMapping("/api/tribal")
public class CreditLineController {

    private final Bucket bucketApproved;
    private final Bucket bucketRejected;
    private final CreditLineFactory creditLineFactory;

    @Autowired
    public CreditLineController(CreditLineFactory creditLineFactory) {
        this.creditLineFactory = creditLineFactory;

        Bandwidth limitApproved = Bandwidth.classic(2, Refill.greedy(2, Duration.ofMinutes(2)));
        bucketApproved = Bucket4j.builder()
                .addLimit(limitApproved)
                .build();

        Bandwidth limitRejected = Bandwidth.classic(2, Refill.intervally(2, Duration.ofSeconds(30)));
        bucketRejected = Bucket4j.builder()
                .addLimit(limitRejected)
                .build();
    }

    @RequestMapping(value = "/validateCreditLine", method = RequestMethod.POST)
    public ResponseEntity<CreditLineResponse> validateCreditLine(@RequestBody CreditLineRequest creditLineRequest) {

        CreditLineResponse creditLineResponse = creditLineFactory.getStrategy(creditLineRequest.getFoundingType()).calculateCreditLine(creditLineRequest);

        if (!creditLineResponse.isStatusCreditLine()) {

            if (bucketRejected.tryConsume(1)) {
                return ResponseEntity.ok(creditLineResponse);

            } else {
                return ResponseEntity.ok(CreditLineResponse.builder().statusCreditLine(false).message(SALES_CONTACT.getMessage()).build());
            }

        } else {
            if (bucketApproved.tryConsume(1)) {
                return ResponseEntity.ok(creditLineResponse);
            } else {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(CreditLineResponse.builder().statusCreditLine(false).message(MANY_TIMES_404_MESSAGE.getMessage()).build());
            }
        }
    }
}
