package com.xpeho.xpeapp.data.model.agenda

import com.google.gson.annotations.SerializedName
import java.time.LocalTime
import java.util.Date

data class AgendaEvent(
    val id: Int,
    val date: Date,
    @SerializedName("end_date") val endDate: Date?,
    @SerializedName("start_time") val startTime: LocalTime?,
    @SerializedName("end_time") val endTime: LocalTime?,
    val title: String,
    val location: String?,
    @SerializedName("type_id") val typeId: String,
    val topic: String?
)
