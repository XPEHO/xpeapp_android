package com.xpeho.xpeapp.ui.components.agenda

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun AgendaCard(
    event: AgendaEvent,
    collapsable: Boolean = true,
    defaultOpen: Boolean = false
) {
    CollapsableCard(
        label = event.title,
        tags = getTagsList(event = event),
            icon = {
            Icon(
                painter = painterResource(id = XpehoRes.qvst),
                contentDescription = "event",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier
                    .size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = collapsable,
        defaultOpen = defaultOpen
    )
}

// Get the tag pills for a campaign
private fun getTagsList(event: AgendaEvent): @Composable() (() -> Unit) {

    // Init the tagsList depending the data that we got
    val tagPillTheme: @Composable() () -> Unit = {
        TagPill(
            label = event.topic,
            backgroundColor = XpehoColors.GREEN_DARK_COLOR,
            size = 9.sp
        )
    }

    var tagPillDeadline: @Composable() () -> Unit

    // If the campaign is open we indicate the days remaining
    //  If it end in less or equal than 3 days and it hasn't been completed -> the color is red
    // Else we indicate the end date
//    if (event.status == "OPEN") {
//        tagPillDeadline = {
//            TagPill(
//                label =
//                when (campaign.remainingDays) {
//                    0 -> "Dernier jour"
//                    1 -> "${campaign.remainingDays} jour restant"
//                    else -> "${campaign.remainingDays} jours restants"
//                },
//                backgroundColor =
//                if (campaign.completed || (campaign.remainingDays > 3))
//                    XpehoColors.GREEN_DARK_COLOR
//                else
//                    XpehoColors.RED_INFO_COLOR,
//                size = 9.sp
//            )
//        }
//    } else {
//        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//       // val endDate = LocalDate.parse(campaign.endDate, DateTimeFormatter.ISO_DATE)
//        tagPillDeadline = {
//            TagPill(
//                label = "endDate",
//                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
//                size = 9.sp
//            )
//        }
//    }

    val tagPillCompletion: @Composable() () -> Unit

    // If campaign is completed we indicate that it has been complete
    // Else if it is always open we indicate it can always be completed
//    if (campaign.completed) {
//        tagPillCompletion = {
//            TagPill(
//                label = "Complétée",
//                backgroundColor = XpehoColors.XPEHO_COLOR,
//                size = 9.sp
//            )
//        }
//    } else if (campaign.status == "OPEN") {
//        tagPillCompletion = {
//            TagPill(
//                label = "À compléter",
//                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
//                size = 9.sp
//            )
//        }
//    } else {
//        tagPillCompletion = {}
//    }

    return {
        tagPillTheme.invoke()
        //tagPillDeadline.invoke()
        //tagPillCompletion.invoke()
    }
}