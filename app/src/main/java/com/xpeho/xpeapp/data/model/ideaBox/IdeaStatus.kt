package com.xpeho.xpeapp.data.model.ideaBox

import com.google.gson.annotations.SerializedName
import java.util.Date

data class IdeaStatus(
    val id: String,
    val status: String,
    val reason: String? = null,
    val context: String = "",
    @SerializedName(value = "description", alternate = ["idea"])
    val description: String = "",
    @SerializedName(value = "created_at", alternate = ["date"])
    val createdAt: Date? = null,
)
