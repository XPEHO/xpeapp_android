package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.components.AppLoader
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.agenda.AgendaBirthdayItem
import com.xpeho.xpeapp.ui.components.agenda.AgendaCardList
import com.xpeho.xpeapp.ui.components.agenda.AgendaEventItem
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
                val state = agendaState
                val events = state.agendaEvent.map { AgendaEventItem(it) }
                val birthdays = state.agendaBirthday.map { AgendaBirthdayItem(it) }
                val items = (events + birthdays).sortedBy { it.date }
                item {
                    AgendaCardList(
                        items = items,
                        eventsTypes = state.agendaEventType,
                        collapsable = true
                    )
                }
            }
            is AgendaUiState.ERROR -> {
                item {
                    CustomDialog(
                        title = stringResource(id = R.string.login_page_error_title),
                        message = (agendaViewModel.state as AgendaUiState.ERROR).error,
                    ) {
                        agendaViewModel.resetState()
                    }
                }
            }

            is AgendaUiState.LOADING -> {
                item {
                    LaunchedEffect(Unit) {
                        agendaViewModel.updateState()
                    }
                    AppLoader()
                }
            }
        }
    }
}