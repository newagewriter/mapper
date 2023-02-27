package com.newagewriter.example

import com.newagewriter.processor.converter.Converter
import com.newagewriter.processor.converter.GenericConverter
import java.util.*

@Converter(type = Date::class)
class DateConverter : GenericConverter<Date, Long> {
    override fun toSimpleValue(entity: Date): Long {
        return entity.time
    }

    override fun toEntity(value: Long): Date {
        return Date(value)
    }
}