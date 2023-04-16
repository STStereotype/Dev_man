package com.example.dev_man

import android.util.Xml
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Timeout
import org.w3c.dom.Document
import org.xml.sax.InputSource
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import java.io.InputStream
import java.io.StringReader
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory
import org.json.JSONObject
import org.json.XML
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister


@Root(name = "example")
data class ExampleXml(
    @field:Element(name = "name") var name: String?,
    @field:Element(name = "age") var age: Int?
)

private fun main() {
    setTemperature("192.168.190.136", 100.0f)
}

fun setTemperature(ip: String, temperature: Float) {
    val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .callTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val formBuilder = FormBody.Builder()
        .add("value", "100")
        .build()

    val request = Request.Builder()
        .post(formBuilder)
        .url("http://${ip}/setTemperature")
        .build()

    val call = client.newCall(request)

    val response = call.execute()

    if(response.isSuccessful) {
        println(response.body!!.string())
    }
}

fun okHt() {
    val loggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .callTimeout(100, TimeUnit.SECONDS)
        .readTimeout(100, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    val request = Request.Builder()
        .get()
        .url("http://192.168.190.136/getTemperature")
        .build()

    val call = client.newCall(request)

    val response = call.execute()

    if(response.isSuccessful) {
        if(response.body!!.string() == "inf")
            println(0.0f)
        else
            println(response.body!!.string().toFloat())
    }
}

data class Device(
    val deviceType: String,
    val friendlyName: String,
    val presentationURL: String,
    val serialNumber: String,
    val modelName: String,
    val modelNumber: String,
    val modelURL: String,
    val manufacturer: String,
    val manufacturerURL: String,
    val UDN: String
)

interface Api {
    @GET("/description.xml")
    suspend fun getDescription()
}

private fun parse(xmlData: String) {
    var inEntry = false
    var textValue = ""

    try {
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val xpp = factory.newPullParser()
        xpp.setInput(StringReader(xmlData))
        var eventType = xpp.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = xpp.name
            when (eventType) {
                XmlPullParser.START_TAG -> if ("URLBase".equals(tagName, ignoreCase = true)) {
                    inEntry = true
                    println("URLBase")
                }
                XmlPullParser.TEXT -> textValue = xpp.text
                XmlPullParser.END_TAG -> if (inEntry) {
                    if ("friendlyName".equals(tagName, ignoreCase = true)) {
                        println(textValue)
                        inEntry = false
                    }
                }
                else -> {}
            }
            eventType = xpp.next()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}