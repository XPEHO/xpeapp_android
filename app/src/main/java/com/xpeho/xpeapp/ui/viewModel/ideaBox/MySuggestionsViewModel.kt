package com.xpeho.xpeapp.ui.viewModel.ideaBox

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.IdeaStatusBanner
import com.xpeho.xpeapp.data.model.ideaBox.IdeaStatus
import kotlinx.coroutines.launch
import java.util.Calendar

class MySuggestionsViewModel : ViewModel() {

    private val wordpressRepository = XpeApp.appModule.wordpressRepository
    private val datastorePref = XpeApp.appModule.datastorePref

    val ideas = mutableStateOf(emptyList<IdeaStatus>())
    val selectedIdea = mutableStateOf<IdeaStatus?>(null)
    val activeBanner = mutableStateOf<IdeaStatusBanner?>(null)

    var state: MySuggestionsState by mutableStateOf(MySuggestionsState.EMPTY)

    init {
        loadBannerState()
    }

    fun syncIdeas(openIdeaId: String? = null) {
        state = MySuggestionsState.LOADING

        viewModelScope.launch {
            val fetchedIdeas = wordpressRepository.getIdeas()
            ideas.value = fetchedIdeas
            selectedIdea.value = openIdeaId?.let { id -> fetchedIdeas.firstOrNull { it.id == id } }
            detectStatusChangesAndPersistBanner(fetchedIdeas)
            loadBannerState()

            state = MySuggestionsState.SUCCESS
        }
    }

    fun openSuggestion(idea: IdeaStatus) {
        selectedIdea.value = idea
    }

    fun closeSuggestion() {
        selectedIdea.value = null
    }

    fun loadBannerState() {
        viewModelScope.launch {
            activeBanner.value = datastorePref.getIdeaStatusBanner()
        }
    }

    private suspend fun detectStatusChangesAndPersistBanner(fetchedIdeas: List<IdeaStatus>) {
        val previousStatusesById = datastorePref.getIdeaStatusesSnapshot()
        val latestStatusesById = fetchedIdeas.associate { it.id to it.status }

        // First sync: persist current statuses silently.
        if (previousStatusesById.isEmpty()) {
            datastorePref.setIdeaStatusesSnapshot(latestStatusesById)
            return
        }

        val changedIdea = fetchedIdeas.firstOrNull { idea ->
            val previousStatus = previousStatusesById[idea.id]
            previousStatus != null && previousStatus != idea.status
        }

        if (changedIdea != null) {
            datastorePref.setIdeaStatusBanner(
                message = BANNER_MESSAGE,
                targetIdeaId = changedIdea.id,
                expirationMillis = computeBannerExpirationMillis(),
            )
        }

        datastorePref.setIdeaStatusesSnapshot(latestStatusesById)
    }

    private fun computeBannerExpirationMillis(): Long {
        return Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, BANNER_DURATION_DAYS)
        }.timeInMillis
    }

    companion object {
        private const val BANNER_DURATION_DAYS = 7
        private const val BANNER_MESSAGE = "Du nouveau sur ta suggestion :\nCliques pour avoir des infos"
    }
}

sealed interface MySuggestionsState {
    object EMPTY : MySuggestionsState
    object LOADING : MySuggestionsState
    object SUCCESS : MySuggestionsState
}
