package org.mkalina.bankid

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RatesServiceTest {

    private val cnbClient = mock<CnbClient>()
    private val currencyListClient = mock<CurrencyListClient>()
    private val ratesService = RatesService(
        cnbClient,
        currencyListClient,
    )

    @Test
    fun `get supported currencies`() = runBlocking {
        assertThat(ratesService.getSupportedCurrencies()).containsExactly("EUR", "USD")
        Unit
    }

    @Test
    fun `get rates`() = runBlocking {
        assertThat( ratesService.getCnbToCurrencyListRatio("USD", "EUR")).isEqualByComparingTo(1.toBigDecimal())
        Unit
    }

    @BeforeEach
    fun setup() = runBlocking {
        whenever(cnbClient.getCzkRates()).thenReturn(
            mapOf(
                "EUR" to 25.00.toBigDecimal(),
                "USD" to 20.00.toBigDecimal(),
                "PLN" to 5.8.toBigDecimal()
            )
        )
        whenever(currencyListClient.getEurRates()).thenReturn(
            mapOf(
                "USD" to 0.80.toBigDecimal(),
                "LINK" to 0.10328152.toBigDecimal(),
            )
        )
        Unit
    }
}
