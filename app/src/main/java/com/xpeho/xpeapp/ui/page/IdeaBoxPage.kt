package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.viewModel.ideaBox.IdeaBoxState
import com.xpeho.xpeapp.ui.viewModel.ideaBox.IdeaBoxViewModel
import com.xpeho.xpeapp.utils.AnalyticsEventName
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.InputText
import java.util.UUID

@Composable
fun IdeaBoxPage(
    ideaBoxViewModel: IdeaBoxViewModel = viewModel()
) {
    sendAnalyticsEvent(AnalyticsEventName.IDEABOX_PAGE)
    var inputsResetKey by remember { mutableStateOf(UUID.randomUUID().toString()) }

    // Quand l’état passe à SAVED, on réinitialise le formulaire
    LaunchedEffect(ideaBoxViewModel.state) {
        if (ideaBoxViewModel.state is IdeaBoxState.SAVED) {
            ideaBoxViewModel.clearInputs()
            inputsResetKey = UUID.randomUUID().toString()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Title(label = "Partage ton idée")
            Spacer(modifier = Modifier.height(50.dp))
        }

        item {
            Text("Contexte : ")
            Spacer(modifier = Modifier.height(24.dp))

            key(inputsResetKey) {
                InputText(
                    label = "Thématique (ex : Agence, en mission, formation)",
                    labelSize = 14.sp,
                    inputSize = 16.sp,
                    defaultInput = ideaBoxViewModel.contextText.value,
                    onInput = { ideaBoxViewModel.contextText.value = it },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Text("Description : ")
            Spacer(modifier = Modifier.height(24.dp))

            key(inputsResetKey) {
                InputText(
                    label = "Mon idée/Ma suggestion",
                    labelSize = 14.sp,
                    inputSize = 16.sp,
                    defaultInput = ideaBoxViewModel.descriptionText.value,
                    onInput = { ideaBoxViewModel.descriptionText.value = it },
                )
            }

            Spacer(modifier = Modifier.height(62.dp))
        }

        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (ideaBoxViewModel.isLoading.value) {
                    com.xpeho.xpeapp.ui.components.AppLoader()
                } else {
                    ClickyButton(
                        label = "SOUMETTRE",
                        size = 14.sp,
                        verticalPadding = 12.dp,
                        horizontalPadding = 48.dp,
                        labelColor = Color.White,
                        enabled = !ideaBoxViewModel.isLoading.value
                    ) {
                        ideaBoxViewModel.submitIdea()
                    }
                }

                when (val s = ideaBoxViewModel.state) {
                    is IdeaBoxState.ERROR -> CustomDialog(
                        title = "Erreur",
                        message = s.error,
                        closeDialog = { ideaBoxViewModel.resetState() }
                    )

                    IdeaBoxState.SAVED -> CustomDialog(
                        title = "Merci",
                        message = "Merci ! Votre idée a bien été envoyée.",
                        closeDialog = {
                            ideaBoxViewModel.resetState()
                            ideaBoxViewModel.clearInputs()
                            inputsResetKey = UUID.randomUUID().toString()
                        }
                    )

                    else -> Unit
                }
            }
        }
    }
}
