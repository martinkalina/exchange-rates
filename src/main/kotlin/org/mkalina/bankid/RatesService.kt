package org.mkalina.bankid

import java.math.BigDecimal
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class RatesService(
    private val cnbClient: CnbClient,
    private val currencyListClient: CurrencyListClient,
) {
    private val one = "1.00000000".toBigDecimal()

    suspend fun getSupportedCurrencies(): Set<String> {
        val eurRates: Map<String, BigDecimal> = currencyListClient.getEurRates() + ("EUR" to one)
        val czkRates: Map<String, BigDecimal> = cnbClient.getCzkRates() + ("CZK" to one)
        return czkRates.keys.intersect(eurRates.keys)
    }

    /**
     * Return ratio between CNB's and Currency List's conversion rate of selected currency pair.
     */
    suspend fun getCnbToCurrencyListDifference(from: String, to: String): BigDecimal {
        val eurRates: Map<String, BigDecimal> = currencyListClient.getEurRates() + ("EUR" to one)
        val currencyListRate = requireNotNull(eurRates[to]) / requireNotNull(eurRates[from])

        val czkRates: Map<String, BigDecimal> = cnbClient.getCzkRates() + ("CZK" to one)
        val cnbRate = (requireNotNull(czkRates[from]) / requireNotNull(czkRates[to]))
        val result = cnbRate - currencyListRate
        logger.info { "action=getCnbToCurrencyListDifference from='$from' and to='$to' currencyListRate=$currencyListRate cnbRate=$cnbRate result=$result" }
        return result
    }

    companion object : KLogging()
}
