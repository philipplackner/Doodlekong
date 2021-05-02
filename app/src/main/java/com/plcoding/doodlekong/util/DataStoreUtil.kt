package com.plcoding.doodlekong.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import java.util.*

val Context.dataStore by preferencesDataStore("settings")

suspend fun DataStore<Preferences>.clientId(): String {
    val clientIdKey = stringPreferencesKey("clientId")
    val preferences = data.first()
    val clientIdExists = preferences[clientIdKey] != null
    return if(clientIdExists) {
        preferences[clientIdKey] ?: ""
    } else {
        val newClientId = UUID.randomUUID().toString()
        edit { settings ->
            settings[clientIdKey] = newClientId
        }
        newClientId
    }
}