package com.newagewriter.example

import io.github.newagewriter.processor.converter.Converter
import io.github.newagewriter.processor.converter.GenericConverter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Converter(type = Date::class)
class DateConverter : GenericConverter<Date, String> {
    private val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun toSimpleValue(entity: Date): String {
        return dateFormat.format(entity)
    }

    override fun toEntity(value: String): Date {
        return dateFormat.parse(value)
    }
}
