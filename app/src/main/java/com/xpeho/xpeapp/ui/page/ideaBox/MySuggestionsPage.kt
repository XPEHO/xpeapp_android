package com.xpeho.xpeapp.ui.page.ideaBox

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.ui.components.ideaBox.SuggestionCard
import com.xpeho.xpeapp.ui.components.ideaBox.SuggestionDetailDialog
import com.xpeho.xpeapp.ui.components.layout.NoContentPlaceHolder
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.viewModel.ideaBox.MySuggestionsState
import com.xpeho.xpeapp.ui.viewModel.ideaBox.MySuggestionsViewModel

@Composable
fun MySuggestionsPage(
    targetIdeaId: String? = null,
    mySuggestionsViewModel: MySuggestionsViewModel = viewModel(),
) {
    val currentState = mySuggestionsViewModel.state

    LaunchedEffect(targetIdeaId) {
        mySuggestionsViewModel.syncIdeas(openIdeaId = targetIdeaId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 10.dp)
    ) {
        item {
            Title(label = "Mes suggestions :")
            Spacer(modifier = Modifier.height(10.dp))
        }

        when (currentState) {
            is MySuggestionsState.LOADING -> {
                item {
                    com.xpeho.xpeapp.ui.components.AppLoader()
                }
            }

            else -> {
                if (mySuggestionsViewModel.ideas.value.isEmpty()) {
                    item {
                        NoContentPlaceHolder()
                    }
                } else {
                    items(
                        count = mySuggestionsViewModel.ideas.value.size,
                        key = { index -> mySuggestionsViewModel.ideas.value[index].id }
                    ) { index ->
                        SuggestionCard(
                            idea = mySuggestionsViewModel.ideas.value[index],
                            onViewMoreClick = {
                                mySuggestionsViewModel.openSuggestion(mySuggestionsViewModel.ideas.value[index])
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }

    mySuggestionsViewModel.selectedIdea.value?.let { selected ->
        SuggestionDetailDialog(
            suggestion = selected,
            onDismiss = { mySuggestionsViewModel.closeSuggestion() }
        )
    }
}
