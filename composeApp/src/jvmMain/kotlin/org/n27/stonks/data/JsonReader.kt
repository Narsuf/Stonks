package org.n27.stonks.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.InputStream

object JsonReader {

    private var symbols: List<String>? = null

    suspend fun getSymbolsPage(from: Int, size: Int): List<String>? = getSymbols()?.drop(from)?.take(size)

    private suspend fun getSymbols(): List<String>? {
        symbols?.let { return it }

        val sp = readSymbols("/sp.json") ?: emptyList()
        val stoxx = readSymbols("/stoxx.json") ?: emptyList()
        val nikkei = readSymbols("/nikkei.json") ?: emptyList()

        symbols = (sp + stoxx + nikkei).ifEmpty { null }
        return symbols
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
