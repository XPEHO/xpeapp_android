package com.xpeho.xpeapp.ui.components.agenda

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import java.text.SimpleDateFormat
import java.util.Locale

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
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)

    return {
        TagPill(
            label = dateFormat.format(birthday.birthdate),
            backgroundColor = color,
            size = 9.sp
        )
    }
}