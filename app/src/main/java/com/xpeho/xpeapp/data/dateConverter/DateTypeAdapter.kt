package com.xpeho.xpeapp.data.dateConverter

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTypeAdapter : JsonSerializer<Date>, JsonDeserializer<Date> {
    // Format for input dates
    private val inputFormats = listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH),
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH),
    )
    // Format for output dates
    private val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src?.let { outputFormat.format(it) })
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        val dateStr = json?.asString
        if (dateStr != null) {
            for (format in inputFormats) {
                try {
                    return format.parse(dateStr)
                } catch (e: ParseException) {
                    // Ignore and try the next format
                    println("Failed to parse date: $e")
                }
            }
        }
        throw JsonParseException("Unparseable date in dateTypeAdapter: \"$dateStr\"")
    }
}