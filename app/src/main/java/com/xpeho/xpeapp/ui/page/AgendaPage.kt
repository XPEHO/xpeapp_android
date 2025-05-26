package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.ui.components.AppLoader
import com.xpeho.xpeapp.ui.components.agenda.AgendaBirthdayItem
import com.xpeho.xpeapp.ui.components.agenda.AgendaCardList
import com.xpeho.xpeapp.ui.components.agenda.AgendaEventItem
import com.xpeho.xpeapp.ui.components.layout.NoContentPlaceHolder
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeapp.ui.uiState.AgendaUiState
import com.xpeho.xpeapp.ui.viewModel.agenda.AgendaViewModel

@Composable
fun AgendaPage(agendaViewModel : AgendaViewModel = viewModel()) {
    val agendaState = agendaViewModel.state

    sendAnalyticsEvent("agenda_page")

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        item {
            Title(label = "Agenda")
        }

        when (agendaState) {
            is AgendaUiState.SUCCESS -> {
                val events = agendaState.agendaEvent
                val birthdays = agendaState.agendaBirthday

                if (events.isEmpty() && birthdays.isEmpty()) {
                    item {
                        NoContentPlaceHolder()
                    }
                } else {
                    val eventItems = events.map { AgendaEventItem(it) }
                    val birthdayItems = birthdays.map { AgendaBirthdayItem(it) }
                    val items = (eventItems + birthdayItems).sortedBy { it.date }
                    item {
                        AgendaCardList(
                            items = items,
                            eventsTypes = agendaState.agendaEventType,
                            collapsable = true
                        )
                    }
                }
            }
            is AgendaUiState.ERROR -> {
                item {
                    NoContentPlaceHolder()
                    LaunchedEffect(Unit) {
                        agendaViewModel.updateStateForMonth()
                    }
                }
            }
            is AgendaUiState.LOADING -> {
                item {
                    LaunchedEffect(Unit) {
                        agendaViewModel.updateStateForMonth()
                    }
                    AppLoader()
                }
            }
            is AgendaUiState.EMPTY -> {
                item {
                    NoContentPlaceHolder()
                }
            }
        }
    }
}