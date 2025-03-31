package com.xpeho.xpeapp.data.model.agenda

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AgendaBirthday(
    val id: Int,
    @SerializedName("first_name") val firstName: String,
    val birthdate: Date,
    val email: String,
)
