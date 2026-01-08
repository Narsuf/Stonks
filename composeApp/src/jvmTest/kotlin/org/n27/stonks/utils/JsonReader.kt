package org.n27.stonks.utils

import java.io.InputStreamReader

fun getJson(path: String): String {
    val resource = ClassLoader.getSystemClassLoader().getResourceAsStream(path)
    val reader = InputStreamReader(resource!!)
    val content = reader.readText()
    reader.close()
    return content
}
