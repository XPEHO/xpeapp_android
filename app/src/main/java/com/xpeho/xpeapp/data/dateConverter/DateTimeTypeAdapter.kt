package com.xpeho.xpeapp.data.dateConverter

import com.google.gson.*
import java.lang.reflect.Type
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DateTimeTypeAdapter : JsonSerializer<LocalTime?>, JsonDeserializer<LocalTime?> {
    private val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    override fun serialize(src: LocalTime?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return if (src == null) JsonNull.INSTANCE else JsonPrimitive(src.format(formatter))
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): LocalTime? {
        return if (json == null || json.isJsonNull) null else LocalTime.parse(json.asString, formatter)
    }
}