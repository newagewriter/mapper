package com.newagewriter.example

import com.newagewriter.example.model.DataWithList
import io.github.newagewriter.processor.mapper.AbstractMapper

object ListTypeTest {
    @JvmStatic
    fun main(args: Array<String>) {
        println("test list type")
        javaClass.classLoader.getResourceAsStream("data_with_list.json")?.let { stream ->
            val dataWithList = AbstractMapper.fromJsonToObject(DataWithList::class.java, stream, Charsets.UTF_8)
            println("dataWithList: $dataWithList")
        }
    }
}
