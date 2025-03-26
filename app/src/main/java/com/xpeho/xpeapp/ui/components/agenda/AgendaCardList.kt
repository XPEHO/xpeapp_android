package com.xpeho.xpeapp.ui.components.agenda

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeapp.ui.components.layout.NoContentPlaceHolder

@Composable
fun AgendaCardList(
    events: List<AgendaEvent>,
    collapsable: Boolean = true
) {
    if (events.isEmpty()) {
        // Display a placeholder if there is no events
        NoContentPlaceHolder()
    } else {
        // Display the list of events
        Column {
            for (event in events) {
                AgendaCard(
                    event = event,
                    collapsable = collapsable,
                    defaultOpen = true
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}