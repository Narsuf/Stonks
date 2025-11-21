package org.n27.stonks.data.json

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.n27.stonks.domain.home.StockInfo
import java.io.File

object JsonStorage {

    private val json = Json { prettyPrint = true }
    private val file = File(System.getProperty("user.home"), "stonks.json")

    suspend fun load(): List<StockInfo> = withContext(Dispatchers.IO) {
        if (file.exists()) {
            json.decodeFromString(ListSerializer(StockInfo.serializer()), file.readText())
        } else {
            file.writeText(json.encodeToString(ListSerializer(StockInfo.serializer()), emptyList()))
            emptyList()
        }
    }

    suspend fun save(stocks: List<StockInfo>) = withContext(Dispatchers.IO) {
        file.writeText(json.encodeToString(ListSerializer(StockInfo.serializer()), stocks))
    }
}
