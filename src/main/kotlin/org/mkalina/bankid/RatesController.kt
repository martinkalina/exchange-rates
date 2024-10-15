package org.mkalina.bankid

import java.math.BigDecimal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RatesController(
    private val ratesService: RatesService,
) {
    @GetMapping("currencies")
    suspend fun getSupportedCurrencies(): SupportedCurrenciesResponse =
        SupportedCurrenciesResponse(ratesService.getSupportedCurrencies())

    @PostMapping("rateDifference")
    suspend fun getRateDifference(
        @RequestBody rateDifferenceRequest: RateDifferenceRequest
    ) = RatesDifferenceResponse(
        ratesService.getCnbToCurrencyListDifference(rateDifferenceRequest.from, rateDifferenceRequest.to)
    )
}

data class SupportedCurrenciesResponse(
    val supportedCurrencies: Set<String>
)

data class RateDifferenceRequest(
    val from: String,
    val to: String,
)

data class RatesDifferenceResponse(val cnbToCurrencyListDifference: BigDecimal)
