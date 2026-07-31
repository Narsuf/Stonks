package org.n27.stonks.data.persistence

import java.util.prefs.Preferences

interface PreferencesStore {
    fun putLong(key: String, value: Long)
    fun getLong(key: String, default: Long): Long
    fun putDouble(key: String, value: Double)
    fun getDouble(key: String, default: Double): Double
    fun put(key: String, value: String)
    fun get(key: String, default: String?): String?
}

class SystemPreferencesStore(
    private val prefs: Preferences = Preferences.userNodeForPackage(SystemPreferencesStore::class.java)
) : PreferencesStore {

    override fun putLong(key: String, value: Long) = prefs.putLong(key, value)

    override fun getLong(key: String, default: Long): Long = prefs.getLong(key, default)

    override fun putDouble(key: String, value: Double) = prefs.putDouble(key, value)

    override fun getDouble(key: String, default: Double): Double = prefs.getDouble(key, default)

    override fun put(key: String, value: String) = prefs.put(key, value)

    override fun get(key: String, default: String?): String? = prefs.get(key, default)
}
