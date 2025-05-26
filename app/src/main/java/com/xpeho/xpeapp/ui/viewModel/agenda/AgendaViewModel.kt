package com.xpeho.xpeapp.ui.viewModel.agenda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.ui.uiState.AgendaUiState

import kotlinx.coroutines.launch

class AgendaViewModel : ViewModel() {

    var state: AgendaUiState by mutableStateOf(AgendaUiState.LOADING)

    private fun getAllEvents(page: String = "") {
        viewModelScope.launch {
            // Get All Events
            val result = XpeApp.appModule.wordpressRepository.getAllEvents(page)

            // Update state
            state = if (result != null) {
                val currentState = state as? AgendaUiState.SUCCESS ?: AgendaUiState.SUCCESS()
                currentState.copy(agendaEvent = result)
            } else {
                AgendaUiState.ERROR("Erreur de chargement de tous les événements")
            }
        }
    }

    private fun getAllEventsTypes() {
        viewModelScope.launch {
            // Get All Events Types
            val result = XpeApp.appModule.wordpressRepository.getAllEventsTypes()

            // Update state
            state = if (result != null) {
                val currentState = state as? AgendaUiState.SUCCESS ?: AgendaUiState.SUCCESS()
                currentState.copy(agendaEventType = result)
            } else {
                AgendaUiState.ERROR("Erreur de chargement de tous les types d'événements")
            }
        }
    }

    private fun getAllBirthdays(page: String = "") {
        viewModelScope.launch {
            // Get All Birthdays
            val result = XpeApp.appModule.wordpressRepository.getAllBirthdays(page)

            // Update state
            state = if (result != null) {
                val currentState = state as? AgendaUiState.SUCCESS ?: AgendaUiState.SUCCESS()
                currentState.copy(agendaBirthday = result)
            } else {
                AgendaUiState.ERROR("Erreur de chargement de tous les anniversaires")
            }
        }
    }

    fun resetState() {
        state = AgendaUiState.EMPTY
    }

    fun updateStateForMonth() {
        resetState()
        getAllEvents("month")
        getAllEventsTypes()
        getAllBirthdays("month")
    }

    fun updateStateForWeek() {
        resetState()
        getAllEvents("week")
        getAllEventsTypes()
        getAllBirthdays("week")
    }
}

