package org.n27.stonks.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

object JsonReader {

    suspend fun getSymbols(): List<String>? {
        val sp = readSymbols("/sp.json") ?: emptyList()
        val stoxx = readSymbols("/stoxx.json") ?: emptyList()
        val nikkei = readSymbols("/nikkei.json") ?: emptyList()

        return (sp + stoxx + nikkei)
            .ifEmpty { null }
            ?.take(11)
    }

    private suspend fun readSymbols(fileName: String): List<String>? = runCatching {
        val symbols: List<String> = readJson(fileName)
        return symbols
    }.getOrNull()

    private suspend inline fun <reified T> readJson(fileName: String): T = withContext(Dispatchers.IO) {
        val inputStream: InputStream = object {}.javaClass.getResourceAsStream(fileName)
            ?: throw IllegalStateException()
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Json.decodeFromString(jsonString)
    }
}
