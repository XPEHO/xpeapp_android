package com.xpeho.xpeapp.ui.components.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeapp.data.model.agenda.AgendaEventType
@Composable
fun AgendaCardList(
    events: List<AgendaEvent>,
    birthdays: List<AgendaBirthday>,
    eventsTypes: List<AgendaEventType>,
    collapsable: Boolean = true
) {
    Column {
        for (birthday in birthdays) {
            AgendaBirthdayCard(
                birthday = birthday,
                collapsable = collapsable,
                defaultOpen = true
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        for (event in events) {
            AgendaCard(
                event = event,
                eventTypes = eventsTypes,
                collapsable = collapsable,
                defaultOpen = true
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}