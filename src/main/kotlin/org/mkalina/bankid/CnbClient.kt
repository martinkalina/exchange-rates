package org.mkalina.bankid

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.math.BigDecimal
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class CnbClient(
    webClientBuilder: WebClient.Builder,
    @Value("\${cnb.url}") private val apiUrl: String,
) {

    private val webClient = webClientBuilder
        .baseUrl(apiUrl)
        .build()

    suspend fun load(): Map<String, BigDecimal> {
        val bytes = webClient.get()
            .accept(MediaType.APPLICATION_XML)
            .retrieve()
            .awaitBody<ByteArray>()
        val xmlMapper = XmlMapper()
        return xmlMapper.readValue(bytes, CnbResponse::class.java).responseTable.rows.map {
            it.code to  it.rate.replace(",", ".").toBigDecimal()
        }.toMap()
    }
}


@JacksonXmlRootElement(localName = "kurzy")
@JsonIgnoreProperties(ignoreUnknown = true)
data class CnbResponse (
    @JacksonXmlProperty(isAttribute = false, localName = "tabulka")
    val responseTable : ResponseTable
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ResponseTable (
        @JacksonXmlProperty(isAttribute = false, localName = "radek")
        @JacksonXmlElementWrapper(useWrapping = false)
        val rows  : List<ResponseTableRow>
    )
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ResponseTableRow (
        @JacksonXmlProperty(isAttribute = true, localName = "kod")
        val code: String,
        @JacksonXmlProperty(isAttribute = true, localName = "kurz")
        val rate: String,
    )
}
