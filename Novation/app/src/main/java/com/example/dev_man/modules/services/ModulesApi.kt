package com.example.dev_man.modules.services

import com.example.dev_man.modules.models.Module
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.w3c.dom.Document
import org.xml.sax.InputSource
import java.io.IOException
import java.io.StringReader
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*
import java.util.concurrent.TimeUnit
import javax.xml.parsers.DocumentBuilderFactory

class ModulesApi {
    var host: String = ""

    init {
        host = accessPointIpSearch()
    }

    fun searchModule(host: String): Module? {
        try {
            val timeout = 1000
            val ip = InetAddress.getByName(host)
            if (ip.isReachable(timeout)) {
                val description = getDescriptionXml(ip.hostName)
                return if (description != null) {
                    Module(
                        ip = ip.hostName,
                        host = "${this.host}0",
                        nameModule = description.getElementsByTagName("modelName")
                            .item(0).textContent.toString(),
                        batteryLevel = 0,
                        temperature = getTemperature(host)
                    )
                } else {
                    null
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun accessPointIpSearch(): String {
        try {
            val enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface = enumNetworkInterfaces.nextElement()
                val enumInetAddress = networkInterface.inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress = enumInetAddress.nextElement()
                    val ipAddress = inetAddress.address
                    if (ipAddress.size == 4) {
                        val octets =
                            Objects.requireNonNull(inetAddress.hostAddress)
                                .split(".").toTypedArray()
                        if (Objects.equals(octets[0], "192") &&
                            Objects.equals(octets[1], "168") &&
                            !Objects.equals(octets[2], "0")
                        ) {
                            host = inetAddress.hostAddress as String
                            return "192.168." + octets[2] + "."
                        }
                    }
                }
            }
            throw Exception("Empty")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getDescriptionXml(ip: String): Document? {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .callTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val request = Request.Builder()
            .get()
            .url("http://${ip}/description.xml")
            .build()

        val call = client.newCall(request)

        val response = call.execute()

        if (response.isSuccessful) {
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val xmlInput = InputSource(StringReader(response.body!!.string()))

            return dBuilder.parse(xmlInput)
        }

        return null
    }

    fun getTemperature(ip: String): Float {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .callTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val request = Request.Builder()
            .get()
            .url("http://${ip}/getTemperature")
            .build()

        val call = client.newCall(request)

        val response = call.execute()

        if (response.isSuccessful) {
            if (response.body!!.string() == "inf")
                return 0.0f
        }
        return response.body!!.string().toFloat()
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
    }
}