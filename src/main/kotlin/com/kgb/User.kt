package com.kgb

import com.kgb.processor.BaseUser
import com.kgb.processor.Mapper

@Mapper
class User(
    val name: String,
    val id: String
) : BaseUser()