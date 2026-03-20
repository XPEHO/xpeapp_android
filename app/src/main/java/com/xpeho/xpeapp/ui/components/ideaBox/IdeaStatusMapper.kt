package com.xpeho.xpeapp.ui.components.ideaBox

import androidx.compose.ui.graphics.Color
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

data class IdeaStatusUi(
    val label: String,
    val backgroundColor: Color,
)

object IdeaStatusMapper {
    fun toUi(status: String): IdeaStatusUi {
        return when (status.trim().uppercase()) {
            "APPROVED" -> IdeaStatusUi(
                label = "APPROUVÉ",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
            )

            "IMPLEMENTED" -> IdeaStatusUi(
                label = "IMPLEMENTÉE",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
            )

            "REJECTED", "REFUSED" -> IdeaStatusUi(
                label = "REJETÉE",
                backgroundColor = XpehoColors.RED_INFO_COLOR,
            )

            "PENDING", "IN_PROGRESS", "OPEN" -> IdeaStatusUi(
                label = "EN ATTENTE",
                backgroundColor = Color(0xFFFFA500),
            )

            else -> IdeaStatusUi(
                label = status.uppercase(),
                backgroundColor = XpehoColors.GRAY_LIGHT_COLOR,
            )
        }
    }
}
