package it.teutsch.felix.rant2text.serializer

import androidx.datastore.core.Serializer
import it.teutsch.felix.rant2text.data.dataStore.SettingsData
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SettingsSerializer : Serializer<SettingsData> {
    override val defaultValue: SettingsData
        get() = SettingsData()

    override suspend fun readFrom(input: InputStream): SettingsData {
        return try {
            Json.decodeFromString(
                deserializer = SettingsData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SettingsData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}