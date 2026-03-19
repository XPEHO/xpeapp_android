package com.xpeho.xpeapp.ui.components.ideaBox

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.ideaBox.IdeaStatus
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes

@Composable
fun SuggestionCard(
    idea: IdeaStatus,
    onViewMoreClick: () -> Unit,
) {
    val statusUi = IdeaStatusMapper.toUi(idea.status)

    CollapsableCard(
        label = idea.context.ifBlank { "Suggestion" },
        tags = {
            TagPill(
                label = statusUi.label,
                backgroundColor = statusUi.backgroundColor,
                size = 9.sp,
            )
        },
        button = {
            ClickyButton(
                label = "VOIR PLUS",
                size = 14.sp,
                verticalPadding = 3.dp,
                horizontalPadding = 40.dp,
                backgroundColor = XpehoColors.XPEHO_COLOR,
                onPress = onViewMoreClick,
            )
        },
        icon = {
            Icon(
                painter = painterResource(id = XpehoRes.ideabulb),
                contentDescription = "Suggestion",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp),
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false,
    )
}
