package org.mkalina.bankid

import java.math.BigDecimal
import java.math.MathContext
import mu.KLogging
import org.springframework.stereotype.Service

/**
 * The precision of division operations.
 */
private const val precision = 10

@Service
class RatesService(
    private val cnbClient: CnbClient,
    private val currencyListClient: CurrencyListClient,
) {
    private val mathContext = MathContext(precision)

    suspend fun getSupportedCurrencies(): Set<String> {
        val eurRates: Map<String, BigDecimal> = getFromEurRates()
        val czkRates: Map<String, BigDecimal> = getToCzkRates()
        return czkRates.keys.intersect(eurRates.keys)
    }

    /**
     * Return ratio between CNB's and Currency List's conversion rate of selected currency pair.
     */
    suspend fun getCnbToCurrencyListDifference(from: String, to: String): BigDecimal {
        val currencyListFromEurRates: Map<String, BigDecimal> = getFromEurRates()
        val currencyListRate = requireNotNull(currencyListFromEurRates[to])
            .divide(requireNotNull(currencyListFromEurRates[from]), mathContext)

        val cnbToCzkRates: Map<String, BigDecimal> = getToCzkRates()
        val cnbRate = requireNotNull(cnbToCzkRates[from])
            .divide(requireNotNull(cnbToCzkRates[to]), mathContext)

        val result = cnbRate - currencyListRate
        logger.debug { "action=getCnbToCurrencyListDifference from='$from' to='$to' currencyListRate=$currencyListRate cnbRate=$cnbRate result=$result" }
        return result
    }

    private suspend fun getToCzkRates() = cnbClient.getCzkRates() + ("CZK" to BigDecimal.ONE)

    private suspend fun getFromEurRates() = currencyListClient.getEurRates() + ("EUR" to BigDecimal.ONE)

    companion object : KLogging()
}
