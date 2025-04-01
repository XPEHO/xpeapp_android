package com.xpeho.xpeapp.ui.components.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.data.model.agenda.AgendaEventType

@Composable
fun AgendaCardList(
    items: List<AgendaItem>,
    eventsTypes: List<AgendaEventType>,
    collapsable: Boolean = true
) {
    Column {
        for (item in items) {
            when (item) {
                is AgendaBirthdayItem -> {
                    AgendaBirthdayCard(
                        birthday = item.birthday,
                        collapsable = collapsable,
                        defaultOpen = true
                    )
                }
                is AgendaEventItem -> {
                    AgendaCard(
                        event = item.event,
                        eventTypes = eventsTypes,
                        collapsable = collapsable,
                        defaultOpen = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}