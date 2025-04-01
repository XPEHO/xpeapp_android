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
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.agenda.AgendaBirthdayItem
import com.xpeho.xpeapp.ui.components.agenda.AgendaCardList
import com.xpeho.xpeapp.ui.components.agenda.AgendaEventItem
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeapp.ui.uiState.AgendaUiState
import com.xpeho.xpeapp.ui.uiState.QvstUiState
import com.xpeho.xpeapp.ui.viewModel.agenda.AgendaViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory

@Composable
fun AgendaPage() {
    val agendaViewModel = viewModel<AgendaViewModel>(
        factory = viewModelFactory {
            AgendaViewModel()
        }
    )

    sendAnalyticsEvent("agenda_page")

    LaunchedEffect(Unit) {
        agendaViewModel.updateState()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize(),
    ) {
        when (agendaViewModel.state) {
            is AgendaUiState.SUCCESS -> {
                item {
                    Title(label = "Agenda")
                    val state = agendaViewModel.state as AgendaUiState.SUCCESS
                    val events = state.agendaEvent.map { AgendaEventItem(it) }
                    val birthdays = state.agendaBirthday.map { AgendaBirthdayItem(it) }
                    val items = (events + birthdays).sortedBy { it.date }
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
        }
    }
}