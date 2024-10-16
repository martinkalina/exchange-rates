package org.mkalina.bankid

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.get
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.wiremock.spring.ConfigureWireMock
import org.wiremock.spring.EnableWireMock

@SpringBootTest
@EnableWireMock(ConfigureWireMock(port = 9999))
@TestPropertySource(properties = ["cnb.url=http://localhost:9999"])
class CnbClientTest(
    @Autowired val cnbClient: CnbClient
) {

    @Test
    fun `load rates`() = runBlocking {
        mockResponse()
        assertThat(cnbClient.getCzkRates().get("AUD")).isEqualTo("15.557".toBigDecimal())
        Unit
    }

    private fun mockResponse() {
        WireMock.stubFor(
            get("/").willReturn(
                aResponse().withStatus(200).withBody(
                    body.trimIndent()
                )
            )
        )
    }

    @Language("XML")
    val body = """
            <kurzy banka="CNB" datum="10.10.2024" poradi="198">
                <tabulka typ="XML_TYP_CNB_KURZY_DEVIZOVEHO_TRHU">
                    <radek kod="AUD" mena="dolar" mnozstvi="1" kurz="15,557" zeme="Austrálie"/>
                    <radek kod="BRL" mena="real" mnozstvi="1" kurz="4,147" zeme="Brazílie"/>
                    <radek kod="BGN" mena="lev" mnozstvi="1" kurz="12,946" zeme="Bulharsko"/>
                    <radek kod="CNY" mena="žen-min-pi" mnozstvi="1" kurz="3,272" zeme="Čína"/>
                    <radek kod="DKK" mena="koruna" mnozstvi="1" kurz="3,395" zeme="Dánsko"/>
                    <radek kod="EUR" mena="euro" mnozstvi="1" kurz="25,320" zeme="EMU"/>
                    <radek kod="PHP" mena="peso" mnozstvi="100" kurz="40,341" zeme="Filipíny"/>
                    <radek kod="HKD" mena="dolar" mnozstvi="1" kurz="2,980" zeme="Hongkong"/>
                    <radek kod="INR" mena="rupie" mnozstvi="100" kurz="27,582" zeme="Indie"/>
                    <radek kod="IDR" mena="rupie" mnozstvi="1000" kurz="1,478" zeme="Indonesie"/>
                    <radek kod="ISK" mena="koruna" mnozstvi="100" kurz="17,051" zeme="Island"/>
                    <radek kod="ILS" mena="nový šekel" mnozstvi="1" kurz="6,141" zeme="Izrael"/>
                    <radek kod="JPY" mena="jen" mnozstvi="100" kurz="15,547" zeme="Japonsko"/>
                    <radek kod="ZAR" mena="rand" mnozstvi="1" kurz="1,316" zeme="Jižní Afrika"/>
                    <radek kod="CAD" mena="dolar" mnozstvi="1" kurz="16,842" zeme="Kanada"/>
                    <radek kod="KRW" mena="won" mnozstvi="100" kurz="1,713" zeme="Korejská republika"/>
                    <radek kod="HUF" mena="forint" mnozstvi="100" kurz="6,323" zeme="Maďarsko"/>
                    <radek kod="MYR" mena="ringgit" mnozstvi="1" kurz="5,397" zeme="Malajsie"/>
                    <radek kod="MXN" mena="peso" mnozstvi="1" kurz="1,191" zeme="Mexiko"/>
                    <radek kod="XDR" mena="ZPČ" mnozstvi="1" kurz="31,031" zeme="MMF"/>
                    <radek kod="NOK" mena="koruna" mnozstvi="1" kurz="2,147" zeme="Norsko"/>
                    <radek kod="NZD" mena="dolar" mnozstvi="1" kurz="14,069" zeme="Nový Zéland"/>
                    <radek kod="PLN" mena="zlotý" mnozstvi="1" kurz="5,882" zeme="Polsko"/>
                    <radek kod="RON" mena="leu" mnozstvi="1" kurz="5,088" zeme="Rumunsko"/>
                    <radek kod="SGD" mena="dolar" mnozstvi="1" kurz="17,704" zeme="Singapur"/>
                    <radek kod="SEK" mena="koruna" mnozstvi="1" kurz="2,228" zeme="Švédsko"/>
                    <radek kod="CHF" mena="frank" mnozstvi="1" kurz="26,970" zeme="Švýcarsko"/>
                    <radek kod="THB" mena="baht" mnozstvi="100" kurz="68,945" zeme="Thajsko"/>
                    <radek kod="TRY" mena="lira" mnozstvi="100" kurz="67,691" zeme="Turecko"/>
                    <radek kod="USD" mena="dolar" mnozstvi="1" kurz="23,160" zeme="USA"/>
                    <radek kod="GBP" mena="libra" mnozstvi="1" kurz="30,251" zeme="Velká Británie"/>
                </tabulka>
            </kurzy>
                """

}
