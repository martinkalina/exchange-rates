package org.mkalina.bankid

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RatesControllerTest {
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockBean
    private lateinit var ratesService: RatesService

    @Test
    fun `get supported currencies`() = runBlocking {
        whenever(ratesService.getSupportedCurrencies()).thenReturn(setOf(USD, EUR))
        webTestClient
            .get()
            .uri("/currencies")
            .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
            .accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.supportedCurrencies[0]").isEqualTo(USD)
            .jsonPath("$.supportedCurrencies[1]").isEqualTo(EUR)
        Unit
    }

    @Test
    fun `get rate difference`() = runBlocking {
        whenever(ratesService.getCnbToCurrencyListDifference(USD, EUR)).thenReturn(-(0.01).toBigDecimal())
        webTestClient
            .post()
            .uri("/rateDifference")
            .bodyValue(RateDifferenceRequest(USD, EUR))
            .header("Authorization", "Basic dXNlcjpwYXNzd29yZA==")
            .accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$").isEqualTo("-0.01")
        Unit
    }


}
