package com.xpeho.xpeapp.ui.viewModel.agenda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeapp.data.model.agenda.AgendaEventType

import kotlinx.coroutines.launch

class AgendaViewModel : ViewModel() {

    var state: AgendaViewModelState by mutableStateOf(AgendaViewModelState.EMPTY)

    private fun getAllEvents(page: String = "") {
        viewModelScope.launch {
            // Get All Events
            val result = XpeApp.appModule.wordpressRepository.getAllEvents(page)

            // Update state
            state = if (result != null) {
                if (result.isEmpty()) {
                    AgendaViewModelState.EMPTY
                } else {
                    val currentState = state as? AgendaViewModelState.SUCCESS ?: AgendaViewModelState.SUCCESS()
                    currentState.copy(agendaEvent = result)
                }
            } else {
                AgendaViewModelState.ERROR("Erreur de chargement de tous les événements")
            }
        }
    }

    private fun getAllEventsTypes() {
        viewModelScope.launch {
            // Get All Events Types
            val result = XpeApp.appModule.wordpressRepository.getAllEventsTypes()

            // Update state
            state = if (result != null) {
                if (result.isEmpty()) {
                    AgendaViewModelState.EMPTY
                } else {
                    val currentState = state as? AgendaViewModelState.SUCCESS ?: AgendaViewModelState.SUCCESS()
                    currentState.copy(agendaEventType = result)
                }
            } else {
                AgendaViewModelState.ERROR("Erreur de chargement de tous les types d'événements")
            }
        }
    }

    private fun getAllBirthdays(page: String = "") {
        viewModelScope.launch {
            // Get All Birthdays
            val result = XpeApp.appModule.wordpressRepository.getAllBirthdays(page)

            // Update state
            state = if (result != null) {
                if (result.isEmpty()) {
                    AgendaViewModelState.EMPTY
                } else {
                    val currentState = state as? AgendaViewModelState.SUCCESS ?: AgendaViewModelState.SUCCESS()
                    currentState.copy(agendaBirthday = result)
                }
            } else {
                AgendaViewModelState.ERROR("Erreur de chargement de tous les anniversaires")
            }
        }
    }

    fun resetState() {
        state = AgendaViewModelState.EMPTY
    }

    fun updateState() {
        resetState()
        getAllEvents()
        getAllEventsTypes()
        getAllBirthdays()
    }

    fun updateStateForWeek() {
        resetState()
        getAllEvents("week")
        getAllEventsTypes()
        getAllBirthdays("week")
    }
}

interface AgendaViewModelState {
    object EMPTY : AgendaViewModelState
    object LOADING : AgendaViewModelState
    data class ERROR(val error: String) : AgendaViewModelState
    data class SUCCESS(
        val agendaEvent: List<AgendaEvent> = emptyList(),
        val agendaEventType: List<AgendaEventType> = emptyList(),
        val agendaBirthday: List<AgendaBirthday> = emptyList()
    ) : AgendaViewModelState
}