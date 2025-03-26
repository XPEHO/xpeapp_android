package com.xpeho.xpeapp.ui.components.agenda

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeapp.data.model.agenda.AgendaEventType
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeapp.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.graphics.toColorInt
import java.time.LocalTime

@Composable
fun AgendaCard(
    event: AgendaEvent,
    eventTypes: List<AgendaEventType>,
    collapsable: Boolean = true,
    defaultOpen: Boolean = false
) {
    val eventTypeColor = getEventTypeColor(event, eventTypes)
    val tagColor = getTagColor(eventTypeColor)
    val eventTypeIcon = getEventTypeIcon(event, eventTypes)

    CollapsableCard(
        label = event.title,
        tags = getTagsList(event, eventTypes, tagColor),
        icon = {
            Icon(
                painter = painterResource(id = eventTypeIcon),
                contentDescription = "event",
                tint = eventTypeColor,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = collapsable,
        defaultOpen = defaultOpen
    )
}

@Composable
private fun getTagsList(event: AgendaEvent, eventType: List<AgendaEventType>, color: Color): @Composable () -> Unit {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH-mm")
    val timeOfData = DateTimeFormatter.ofPattern("HH:mm:ss")

    return {
        if (event.date.isNotEmpty()) {
            val formattedDate = LocalDateTime.parse(event.date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).format(formatter)
            TagPill(
                label = formattedDate,
                backgroundColor = color,
                size = 9.sp
            )
        }
        event.startTime?.takeIf { it.isNotEmpty() }?.let {
            val formattedTime = LocalTime.parse(it, timeOfData).format(timeFormatter)
            TagPill(
                label = formattedTime,
                backgroundColor = color,
                size = 9.sp
            )
        }
        event.endTime?.takeIf { it.isNotEmpty() }?.let {
            val formattedTime = LocalTime.parse(it, timeOfData).format(timeFormatter)
            TagPill(
                label = formattedTime,
                backgroundColor = color,
                size = 9.sp
            )
        }
        eventType.firstOrNull { it.id == event.typeId }?.let {
            TagPill(
                label = it.label,
                backgroundColor = color,
                size = 9.sp
             )
        }
        event.location?.takeIf { it.isNotEmpty() }?.let {
            TagPill(
                label = it,
                backgroundColor = color,
                size = 9.sp
            )
        }
        event.topic?.takeIf { it.isNotEmpty() }?.let {
            TagPill(
                label = it,
                backgroundColor = color,
                size = 9.sp
            )
        }
    }
}

private fun getEventTypeColor(event: AgendaEvent, eventTypes: List<AgendaEventType>): Color {
    val eventType = eventTypes.firstOrNull { it.id == event.typeId }
    return if (eventType != null) {
        Color(("#" + eventType.colorCode.trimStart('#')).toColorInt())
    } else {
        XpehoColors.GREEN_DARK_COLOR
    }
}

private const val BASE_COLOR_HEX = 0xFF7C4000
private const val ALPHA_VALUE = 0.2f

private fun getTagColor(baseColor: Color): Color {
    val overlayColor = Color(BASE_COLOR_HEX).copy(alpha = ALPHA_VALUE)
    return overlayColor.compositeOver(baseColor)
}

private fun getEventTypeIcon(event: AgendaEvent, eventType: List<AgendaEventType>): Int {
     return when (eventType.firstOrNull { it.id == event.typeId }?.label) {
         "XpeUp" -> R.drawable.birthday
         "Event interne" -> R.drawable.building
         "Formation" -> R.drawable.study
         "RSE" -> R.drawable.leaf
         "Activité" -> R.drawable.gamepad
         "Event externe" -> R.drawable.outside
         else -> R.drawable.building
    }
}