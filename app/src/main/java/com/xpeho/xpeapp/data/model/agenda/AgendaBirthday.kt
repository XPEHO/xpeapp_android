package com.xpeho.xpeapp.data.model.agenda

import com.google.gson.annotations.SerializedName

data class AgendaBirthday(
    val id: String?,
    @SerializedName("first_name") val firstName: String?,
    val birthdate: String?,
    val email: String,
)
