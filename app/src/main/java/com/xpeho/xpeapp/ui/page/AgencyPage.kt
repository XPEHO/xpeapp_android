package com.xpeho.xpeapp.ui.page

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.ui.components.layout.Title
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.CollapsableCard
import com.xpeho.xpeho_ui_android.TagPill
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

@Composable
fun AgencyPage() {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val showdigiCode = remember { mutableStateOf(false) }
    val showalarmCode = remember { mutableStateOf(false) }

    sendAnalyticsEvent("agency_page")

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .fillMaxSize()
    ) {
        item {
            Title(label = "Informations importantes :")
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            EveningInstructionsCard()
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            WifiCard(clipboardManager, context)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            GuestWifiCard(clipboardManager, context)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            PrinterCard()
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            AlarmCard(showalarmCode)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            DigicodeCard(showdigiCode)
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            WaterFountainCard()
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            CleaningCard()
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            OwnersCard()
        }
    }
}

@Composable
private fun EveningInstructionsCard() {
    CollapsableCard(
        label = "Consignes du soir",
        tags = {
            TagPill(label = "BAISSER LES STORES",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR, size = 9.sp)
            TagPill(label = "FERMER LES PORTES À CLEF",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR, size = 9.sp)
            TagPill(label = "FERMER L'IMPRIMANTE",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR, size = 9.sp)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.building),
                contentDescription = "Building",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = false
    )
}

@Composable
private fun WifiCard(clipboardManager: ClipboardManager, context: Context) {
    CollapsableCard(
        label = "Wifi",
        tags = {
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR, size = 9.sp)
        },
        button = {
            ClickyButton(
                label = "COPIER MDP",
                size = 14.sp,
                verticalPadding = 3.dp,
                horizontalPadding = 40.dp,
                backgroundColor = XpehoColors.XPEHO_COLOR,
                labelColor = Color.White
            ) {
                clipboardManager.setText(AnnotatedString(
                    ""))
                android.widget.Toast.makeText(context,
                    "Wifi copié", android.widget.Toast.LENGTH_SHORT).show()
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.wifi),
                contentDescription = "Wifi",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun GuestWifiCard(clipboardManager: ClipboardManager, context: Context) {
    CollapsableCard(
        label = "Wifi invité",
        tags = {
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
        },
        button = {
            ClickyButton(
                label = "COPIER MDP",
                size = 14.sp,
                verticalPadding = 3.dp,
                horizontalPadding = 40.dp,
                backgroundColor = XpehoColors.XPEHO_COLOR,
                labelColor = Color.White
            ) {
                clipboardManager.setText(AnnotatedString(
                    ""))
                android.widget.Toast.makeText(context,
                    "Wifi invité copié",
                    android.widget.Toast.LENGTH_SHORT).show()
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.wifi),
                contentDescription = "Wifi",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun PrinterCard() {
    CollapsableCard(
        label = "Imprimante de l'agence",
        tags = {
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.print),
                contentDescription = "Print",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun AlarmCard(showAlarmcode: MutableState<Boolean>) {
    CollapsableCard(
        label = "Alarme bâtiment",
        tags = {
            TagPill(
                label = if (showAlarmcode.value) ""
                else "•••••••",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp
            )
        },
        button = {
            ClickyButton(
                label = if (showAlarmcode.value) "MASQUER"
                else "AFFICHER",
                size = 14.sp,
                verticalPadding = 3.dp,
                horizontalPadding = 40.dp,
                backgroundColor = XpehoColors.XPEHO_COLOR,
                labelColor = Color.White
            ) {
                showAlarmcode.value = !showAlarmcode.value
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.alarm),
                contentDescription = "Alarm",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun DigicodeCard(showDigicode: MutableState<Boolean>) {
    CollapsableCard(
        label = "Digicode portail Synergie",
        tags = {
            TagPill(
                label = if (showDigicode.value) "" else "•••••••", 
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp
            )
        },
        button = {
            ClickyButton(
                label = if (showDigicode.value) "MASQUER" else "AFFICHER",
                size = 14.sp,
                verticalPadding = 3.dp,
                horizontalPadding = 40.dp,
                backgroundColor = XpehoColors.XPEHO_COLOR,
                labelColor = Color.White
            ) {
                showDigicode.value = !showDigicode.value
            }
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.barrier),
                contentDescription = "Barrier",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun WaterFountainCard() {
    CollapsableCard(
        label = "Société fontaine à eau",
        tags = {
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.water_fountain),
                contentDescription = "Water fountain",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun CleaningCard() {
    CollapsableCard(
        label = "Ménage (un vendredi sur deux)",
        tags = {
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.cleaning),
                contentDescription = "Cleaning",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}

@Composable
private fun OwnersCard() {
    CollapsableCard(
        label = "Propriétaires",
        tags = {
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
            TagPill(label = "",
                backgroundColor = XpehoColors.GREEN_DARK_COLOR,
                size = 9.sp)
        },
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.owner),
                contentDescription = "Owner",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(22.dp)
            )
        },
        size = 16.sp,
        collapsable = true,
        defaultOpen = false
    )
}
