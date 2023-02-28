package com.newagewriter.example

import com.newagewriter.processor.converter.Converter
import com.newagewriter.processor.converter.GenericConverter
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Converter(type = Date::class)
class DateConverter : GenericConverter<Date, String> {
    private val dateFormat: DateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale("pl", "PL"))
    override fun toSimpleValue(entity: Date): String {
        return dateFormat.format(entity)
    }

    override fun toEntity(value: String): Date {
        return dateFormat.parse(value)
    }
}