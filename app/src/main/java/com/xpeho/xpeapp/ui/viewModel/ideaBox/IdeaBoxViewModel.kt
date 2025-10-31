package com.xpeho.xpeapp.ui.viewModel.ideaBox

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.model.ideaBox.IdeaSubmission
import kotlinx.coroutines.launch

class IdeaBoxViewModel : ViewModel() {

    private val wordpressRepository = XpeApp.appModule.wordpressRepository

    val isLoading: MutableState<Boolean> = mutableStateOf(false)

    // Champs de la page
    val contextText: MutableState<String> = mutableStateOf("")
    val descriptionText: MutableState<String> = mutableStateOf("")

    // Etat de la page (vide / loading / saved / error)
    var state: IdeaBoxState by mutableStateOf(IdeaBoxState.EMPTY)


    fun submitIdea() {
        // Validation minimale
        if (descriptionText.value.isBlank()) {
            state = IdeaBoxState.ERROR("Merci de décrire votre idée")
            return
        }

        isLoading.value = true
        state = IdeaBoxState.LOADING

        viewModelScope.launch {
            val submission = IdeaSubmission(
                context = contextText.value.trim(),
                description = descriptionText.value.trim()
            )

            val result = wordpressRepository.submitIdea(submission)

            if (result) {
                state = IdeaBoxState.SAVED
                clearInputs()
            } else {
                state = IdeaBoxState.ERROR("Oups, une erreur est survenue lors de l'envoi. Réessayez.")
            }

            isLoading.value = false
        }
    }

    fun clearInputs() {
        contextText.value = ""
        descriptionText.value = ""
    }

    fun resetState() {
        state = IdeaBoxState.EMPTY
    }
}

sealed interface IdeaBoxState {
    object EMPTY : IdeaBoxState
    object LOADING : IdeaBoxState
    data class ERROR(val error: String) : IdeaBoxState
    object SAVED : IdeaBoxState
}