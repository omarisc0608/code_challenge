package com.tribal.demo.controller

import com.tribal.demo.service.CreditLineFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static com.tribal.demo.domain.BusinessMessages.SALES_CONTACT
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CreditLineControllerTest extends Specification {

    MockMvc mockMvc
    CreditLineController controller
    CreditLineFactory creditLineFactory;

    def setup() {
        creditLineFactory = new CreditLineFactory()
        controller = new CreditLineController(creditLineFactory)
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build()
    }

    def "Credit Line Request Authorized is Due To Rate Limit"() {
        expect: "Status is 429 and the response is 'Many Times'"
        successfulWebRequestSMEApproved("/api/tribal/validateCreditLine")
        successfulWebRequestSMEApproved("/api/tribal/validateCreditLine")
        blockedWebRequestDueToRateLimitApproved("/api/tribal/validateCreditLine")
    }

    def "Credit Line Request Rejected response Sales Agent will contact you"() {
        expect: "Status is 429 and the response is 'Sales Agent will contact'"
        successfulWebRequestStartupRejected("/api/tribal/validateCreditLine")
        successfulWebRequestStartupRejected("/api/tribal/validateCreditLine")
        blockedWebRequestDueToRateLimitRejected("/api/tribal/validateCreditLine")
    }

    def successfulWebRequestSMEApproved(url) {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"foundingType": "SME", "cashBalance": 1335.30, "monthlyRevenue": 135.45,"requestedCreditLine": 100}'))
    }

    def successfulWebRequestStartupRejected(url) {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"foundingType": "STARTUP", "cashBalance": 100, "monthlyRevenue": 100,"requestedCreditLine": 100}'))
    }

    def blockedWebRequestDueToRateLimitApproved(String url) {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"foundingType": "SME", "cashBalance": 1335.30, "monthlyRevenue": 1235.45,"requestedCreditLine": 100}'))
                .andExpect(status().is(HttpStatus.TOO_MANY_REQUESTS.value()))

    }

    def blockedWebRequestDueToRateLimitRejected(String url) {
        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content('{"foundingType": "SME", "cashBalance": 100, "monthlyRevenue": 100,"requestedCreditLine": 100}'))
                .andExpect(jsonPath('$.message').value(SALES_CONTACT.message))
    }
}