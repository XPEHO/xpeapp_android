package com.xpeho.xpeapp.data.model.agenda

import com.google.gson.annotations.SerializedName

data class AgendaEvent(
    val id: String,
    val date: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("end_time") val endTime: String,
    val title: String,
    val location: String,
    @SerializedName("type_id") val typeId: String,
    val topic: String
)
