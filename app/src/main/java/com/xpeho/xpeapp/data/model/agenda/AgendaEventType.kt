package com.xpeho.xpeapp.data.model.agenda

import com.google.gson.annotations.SerializedName

data class AgendaEventType(
    val id: Int,
    val label: String,
    @SerializedName("color_code") val colorCode: String
)
