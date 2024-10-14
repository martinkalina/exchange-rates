package org.mkalina.bankid

import java.math.BigDecimal
import java.math.RoundingMode
import org.springframework.stereotype.Service

@Service
class RatesService(
    private val cnbClient: CnbClient,
    private val currencyListClient: CurrencyListClient,
) {

    suspend fun getSupportedCurrencies(): Set<String> {
        val eurRates: Map<String, BigDecimal> = currencyListClient.getEurRates() + ("EUR" to 1.0.toBigDecimal())
        val czkRates: Map<String, BigDecimal> = cnbClient.getCzkRates()  + ("CZK" to 1.0.toBigDecimal())
        return czkRates.keys.intersect(eurRates.keys)
    }

    /**
     * Return ratio between CNB's and Currency List's conversion rate of selected currency pair.
     */
    suspend fun getCnbToCurrencyListRatio(from: String, to: String): BigDecimal {
        val eurRates: Map<String, BigDecimal> = currencyListClient.getEurRates() + ("EUR" to 1.toBigDecimal())
        val currencyListRate = requireNotNull(eurRates[from]) / requireNotNull(eurRates[to])

        val czkRates: Map<String, BigDecimal> = cnbClient.getCzkRates() + ("CZK" to 1.toBigDecimal())
        val cnbRate = requireNotNull(czkRates[from]) / requireNotNull(czkRates[to])
        return cnbRate.divide(currencyListRate, 10, RoundingMode.HALF_UP)
    }

}
