package org.mkalina.bankid

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.module.kotlin.readValue
import java.math.BigDecimal
import java.util.Locale
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class CurrencyListClient(
    webClientBuilder: WebClient.Builder,
    val objectMapper: ObjectMapper,
    @Value("\${currency-list.url}") private val apiUrl: String,
) {

    private val webClient = webClientBuilder
        .baseUrl(apiUrl)
        .build()

    suspend fun load(): Map<String, BigDecimal> = webClient.get()
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .awaitBody<CurrencyListResponse>()
        .eur.mapKeys {it.key.uppercase(Locale.getDefault())}
        .toMap()
}

data class CurrencyListResponse(
    val eur : Map<String, BigDecimal>
)
