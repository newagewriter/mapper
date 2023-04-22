package com.newagewriter.example.model

import io.github.newagewriter.mapper.Mapper

@Mapper
data class DataWithList(
    val dataString: List<String>,
    val dataInts: List<Int>,
    val dataObject: List<User>
)
