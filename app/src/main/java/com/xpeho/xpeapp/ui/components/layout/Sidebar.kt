package com.xpeho.xpeapp.ui.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.enums.Screens
import com.xpeho.xpeapp.ui.Resources
import com.xpeho.xpeho_ui_android.R.drawable as XpehoRes
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun Sidebar(
    navigationController: NavController,
    sidebarVisible: MutableState<Boolean>,
    showDialog: MutableState<Boolean>
) {

    val ffManager = XpeApp.appModule.featureFlippingManager
    val isIdeaBoxExpanded = remember { mutableStateOf(false) }

    // Computed sidebar size
    val sidebarWidth by animateFloatAsState(
        targetValue = if (sidebarVisible.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 200,
            delayMillis = 100,
            easing = { fraction -> fraction } // Linear easing
        )
    )

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(sidebarWidth)
            .background(color = XpehoColors.XPEHO_COLOR)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {}
    ) {
        AnimatedVisibility(
            visible = sidebarVisible.value,
            enter = fadeIn(animationSpec = tween(durationMillis = 200, delayMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 100))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    IconButton(
                        onClick = {
                            sidebarVisible.value = !sidebarVisible.value
                        },
                        modifier = Modifier
                            .padding(top = 18.dp, start = 18.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = XpehoRes.crossclose),
                            contentDescription = "Close Sidebar",
                            tint = Color.White,
                            modifier = Modifier
                                .size(60.dp)
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.1f))
                    ) {
                        SidebarItemProfile(
                            navigationController = navigationController,
                        )
                    }

                    Spacer(
                        modifier = Modifier
                            .height(20.dp)
                    )

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 18.dp),
                    ) {
                        SidebarItem(
                            icon = painterResource(id = R.drawable.home),
                            label = "Accueil",
                            onClick = {
                                navigationController.navigate(Screens.Home.name)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                        for (menuItem in Resources().listOfMenu) {
                            if (ffManager.isFeatureEnabled(menuItem.featureFlippingId)) {
                                SidebarItem(
                                    icon = painterResource(id = menuItem.idImage),
                                    label = menuItem.title,
                                    onClick = {
                                        navigationController.navigate(menuItem.redirection)
                                    }
                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(20.dp)
                                )
                            }
                        }
                        if (ffManager.isFeatureEnabled(com.xpeho.xpeapp.data.FeatureFlippingEnum.IDEABOX)) {
                            SidebarIdeaBoxExpandableItem(
                                isExpanded = isIdeaBoxExpanded.value,
                                onToggle = {
                                    isIdeaBoxExpanded.value = !isIdeaBoxExpanded.value
                                },
                                onSubmitSuggestionClick = {
                                    navigationController.navigate(Screens.IdeaBox.name)
                                },
                                onMySuggestionsClick = {
                                    navigationController.navigate(Screens.MySuggestions.name)
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(20.dp)
                            )
                        }
                        SidebarItem(
                            icon = painterResource(id = R.drawable.building),
                            label = stringResource(id = R.string.agency_view_label),
                            onClick = {
                                navigationController.navigate(Screens.Agency.name)
                            }
                        )
                        Spacer(
                            modifier = Modifier
                                .height(20.dp)
                        )
                        SidebarItem(
                            icon = painterResource(id = R.drawable.about),
                            label = stringResource(id = R.string.about_view_about_label),
                            onClick = {
                                sidebarVisible.value = false
                                showDialog.value = true
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SidebarIdeaBoxExpandableItem(
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onSubmitSuggestionClick: () -> Unit,
    onMySuggestionsClick: () -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onToggle() }
        ) {
            Icon(
                painter = painterResource(id = XpehoRes.ideabulb),
                contentDescription = "Boite à idées Icon",
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Boite à idées",
                fontSize = 18.sp,
                fontFamily = XpehoFonts.raleway,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (isExpanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                contentDescription = "Boite à idées submenu",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "- Faire une suggestion",
                fontSize = 14.sp,
                fontFamily = XpehoFonts.raleway,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .clickable { onSubmitSuggestionClick() }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "- Mes suggestions",
                fontSize = 14.sp,
                fontFamily = XpehoFonts.raleway,
                color = Color.White,
                modifier = Modifier
                    .padding(start = 30.dp)
                    .clickable { onMySuggestionsClick() }
            )
        }
    }
}