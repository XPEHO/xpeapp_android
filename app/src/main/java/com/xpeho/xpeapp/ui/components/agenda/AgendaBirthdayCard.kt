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
import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AgendaBirthdayCard(
    birthday: AgendaBirthday,
    collapsable: Boolean = true,
    defaultOpen: Boolean = false
) {
    val tagColor = Color(0xFFFF7EEA)
    val eventTypeIcon = R.drawable.birthday

    CollapsableCard(
        label = "Anniversaire de ${birthday.firstName}",
        tags = getBirthdayTagsList(birthday, tagColor),
        icon = {
            Icon(
                painter = painterResource(id = eventTypeIcon),
                contentDescription = "birthday",
                tint = tagColor,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = collapsable,
        defaultOpen = defaultOpen
    )
}

@Composable
private fun getBirthdayTagsList(birthday: AgendaBirthday, color: Color): @Composable () -> Unit {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    return {
        birthday.birthdate?.takeIf { it.isNotEmpty() }?.let {
            val formattedDate = LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(formatter)
            TagPill(
                label = formattedDate,
                backgroundColor = color,
                size = 9.sp
            )
        }
    }
}