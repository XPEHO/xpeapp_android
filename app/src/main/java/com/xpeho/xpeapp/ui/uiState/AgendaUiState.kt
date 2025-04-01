package com.xpeho.xpeapp.ui.uiState

import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeapp.data.model.agenda.AgendaEventType

interface AgendaUiState {
    object EMPTY : AgendaUiState
    object LOADING : AgendaUiState
    data class ERROR(val error: String) : AgendaUiState
    data class SUCCESS(
        val agendaEvent: List<AgendaEvent> = emptyList(),
        val agendaEventType: List<AgendaEventType> = emptyList(),
        val agendaBirthday: List<AgendaBirthday> = emptyList()
    ) : AgendaUiState
}