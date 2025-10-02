package com.xpeho.xpeapp.data.model.qvst

import com.google.gson.annotations.SerializedName

data class QvstCampaign(
    val id: String,
    val name: String,
    @SerializedName("themes") val themes: List<QvstTheme> = emptyList(),
    val status: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("action") val action: String,
    @SerializedName("participation_rate") val participationRate: String
)