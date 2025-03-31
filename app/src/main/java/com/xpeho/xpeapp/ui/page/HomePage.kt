package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.data.FeatureFlippingEnum
import com.xpeho.xpeapp.data.entity.QvstCampaignEntity
import com.xpeho.xpeapp.data.model.agenda.AgendaBirthday
import com.xpeho.xpeapp.data.model.agenda.AgendaEvent
import com.xpeho.xpeapp.data.model.agenda.AgendaEventType
import com.xpeho.xpeapp.domain.FeatureFlippingState
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.components.agenda.AgendaCardList
import com.xpeho.xpeapp.ui.components.layout.NoContentPlaceHolder
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.components.newsletter.NewsletterPreview
import com.xpeho.xpeapp.ui.components.qvst.QvstCardList
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeapp.ui.uiState.QvstActiveUiState
import com.xpeho.xpeapp.ui.viewModel.FeatureFlippingViewModel
import com.xpeho.xpeapp.ui.viewModel.agenda.AgendaViewModel
import com.xpeho.xpeapp.ui.viewModel.agenda.AgendaViewModelState
import com.xpeho.xpeapp.ui.viewModel.newsletter.NewsletterViewModel
import com.xpeho.xpeapp.ui.viewModel.qvst.QvstActiveCampaignsViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory

@Composable
@Suppress("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(navigationController: NavController) {

    // Feature Flipping
    val ffManager = XpeApp.appModule.featureFlippingManager
    val ffViewModel = viewModel<FeatureFlippingViewModel>(
        factory = viewModelFactory {
            FeatureFlippingViewModel(featureFlippingManager = ffManager)
        }
    )
    val ffState by ffViewModel.featureFlippingState.collectAsState()

    val campaignActiveViewModel = viewModel<QvstActiveCampaignsViewModel>(
        factory = viewModelFactory {
            QvstActiveCampaignsViewModel(
                wordpressRepo = XpeApp.appModule.wordpressRepository,
                authManager = XpeApp.appModule.authenticationManager
            )
        }
    )

    val newsletterViewModel = viewModel<NewsletterViewModel>(
        factory = viewModelFactory {
            NewsletterViewModel()
        }
    )

    val agendaViewModel = viewModel<AgendaViewModel>(
        factory = viewModelFactory {
            AgendaViewModel()
        }
    )

    sendAnalyticsEvent("home_page")

    LaunchedEffect(Unit) {
        campaignActiveViewModel.updateState()
        agendaViewModel.updateStateForWeek()
        newsletterViewModel.updateState()
        ffViewModel.updateState()
    }

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        when (ffState) {
            is FeatureFlippingState.LOADING -> {
                item {
                    Spacer(modifier = Modifier.height(25.dp))
                }
            }

            is FeatureFlippingState.ERROR -> {
                item {
                    CustomDialog(
                        title = stringResource(id = R.string.login_page_error_title),
                        message = (ffState as FeatureFlippingState.ERROR).error,
                    ) {
                        ffViewModel.updateState()
                    }
                }
            }

            is FeatureFlippingState.SUCCESS -> {
                // If the newsletters are enabled, they are loaded, and there is at least one newsletter
                if (ffViewModel.isFeatureEnabled(FeatureFlippingEnum.NEWSLETTERS)
                    && !newsletterViewModel.isLoading.value
                    && newsletterViewModel.state.value.isNotEmpty()
                ) {
                    item {
                        // Get the last newsletter as the first element of the list sorted descending by date
                        val lastNewsletter = newsletterViewModel.lastNewsletter.value

                        lastNewsletter?.let {
                            Title(label = "Dernière publication")

                            val lastNewsletterPreview = newsletterViewModel.lastNewsletterPreview.value
                            NewsletterPreview(newsletter = lastNewsletter, preview = lastNewsletterPreview)

                            Spacer(modifier = Modifier.height(25.dp))
                        }
                    }
                }

                if (ffViewModel.isFeatureEnabled(FeatureFlippingEnum.QVST)) {
                    // When we have loaded the qvst campaigns
                    when (campaignActiveViewModel.state) {

                        // If we successfully loaded the campaigns
                        is QvstActiveUiState.SUCCESS -> {
                            item {
                                Title(label = "À ne pas manquer !")
                                val campaigns: List<QvstCampaignEntity> =
                                    (campaignActiveViewModel.state as QvstActiveUiState.SUCCESS).qvst
                                QvstCardList(
                                    navigationController = navigationController,
                                    campaigns = campaigns,
                                    collapsable = false
                                )
                            }
                        }

                        // If there was an error loading the campaigns
                        is QvstActiveUiState.ERROR -> {
                            item {
                                CustomDialog(
                                    title = stringResource(id = R.string.login_page_error_title),
                                    message = (campaignActiveViewModel.state as QvstActiveUiState.ERROR).error,
                                ) {
                                    campaignActiveViewModel.resetState()
                                }
                            }
                        }
                    }
                }

                if (ffViewModel.isFeatureEnabled(FeatureFlippingEnum.AGENDA)) {
                    // When we have loaded the agenda events
                    when (agendaViewModel.state) {

                        // If we successfully loaded the events
                        is AgendaViewModelState.SUCCESS -> {
                            item {
                                val events: List<AgendaEvent> =
                                    (agendaViewModel.state as AgendaViewModelState.SUCCESS).agendaEvent
                                val eventsTypes: List<AgendaEventType> =
                                    (agendaViewModel.state as AgendaViewModelState.SUCCESS).agendaEventType
                                val birthdays: List<AgendaBirthday> =
                                    (agendaViewModel.state as AgendaViewModelState.SUCCESS).agendaBirthday
                                AgendaCardList(
                                    events = events,
                                    eventsTypes = eventsTypes,
                                    birthdays = birthdays,
                                    collapsable = false
                                )
                            }
                        }

                        // If there was an error loading the events
                        is AgendaViewModelState.ERROR -> {
                            item {
                                CustomDialog(
                                    title = stringResource(id = R.string.login_page_error_title),
                                    message = (agendaViewModel.state as AgendaViewModelState.ERROR).error,
                                ) {
                                    agendaViewModel.resetState()
                                }
                            }
                        }
                    }
                }

                if (!ffViewModel.isFeatureEnabled(FeatureFlippingEnum.NEWSLETTERS)
                    && !ffViewModel.isFeatureEnabled(FeatureFlippingEnum.QVST) && !ffViewModel.isFeatureEnabled(
                        FeatureFlippingEnum.AGENDA
                    )
                ) {
                    item { NoContentPlaceHolder() }
                }
            }
        }
    }
}