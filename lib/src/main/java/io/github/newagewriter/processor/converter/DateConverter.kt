package io.github.newagewriter.processor.converter

import java.util.*

@Converter(type = Date::class)
class DateConverter : GenericConverter<Date, Long>() {
    override fun toSimpleValue(entity: Date): Long {
        return entity.time
    }

    override fun toEntity(value: Long): Date {
        return Date(value)
    }
}
