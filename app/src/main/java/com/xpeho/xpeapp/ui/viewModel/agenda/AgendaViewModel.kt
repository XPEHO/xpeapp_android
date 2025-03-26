package com.xpeho.xpeapp.ui.viewModel.agenda

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent

import kotlinx.coroutines.launch

class AgendaViewModel : ViewModel() {

    var state: AgendaViewModelState by mutableStateOf(AgendaViewModelState.EMPTY)

    private fun getAllEvents() {
        viewModelScope.launch {
            // Get All Events
            val result = XpeApp.appModule.wordpressRepository.getAllAgendaEvents()

            // Update state
            state = if (result != null) {
                if (result.isEmpty()) {
                    AgendaViewModelState.EMPTY
                } else {
                    AgendaViewModelState.SUCCESS(
                        agendaEvent = result
                    )
                }
            } else {
                AgendaViewModelState.ERROR("Erreur de chargement de tout les événements")
            }
        }
    }

    private fun resetState() {
        state = AgendaViewModelState.EMPTY
    }

    fun updateState() {
        resetState()
        getAllEvents()
    }

}

interface AgendaViewModelState {
    object EMPTY : AgendaViewModelState
    object LOADING : AgendaViewModelState
    data class ERROR(val error: String) : AgendaViewModelState
    data class SUCCESS(
        val agendaEvent: List<AgendaEvent>,
    ) : AgendaViewModelState
}