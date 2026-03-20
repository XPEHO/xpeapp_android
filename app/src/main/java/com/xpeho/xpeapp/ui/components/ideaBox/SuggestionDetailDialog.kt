package com.xpeho.xpeapp.ui.components.ideaBox

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.data.model.ideaBox.IdeaStatus
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun SuggestionDetailDialog(
    suggestion: IdeaStatus,
    onDismiss: () -> Unit,
) {
    val statusUi = IdeaStatusMapper.toUi(suggestion.status)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = suggestion.context.ifBlank { "Suggestion" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    fontFamily = XpehoFonts.raleway,
                )
            }
        },
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "idée soumise : ${suggestion.description.ifBlank { "-" }}",
                    fontSize = 16.sp,
                    fontFamily = XpehoFonts.raleway,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Date : ${IdeaDateFormatter.format(suggestion.createdAt)}",
                    fontSize = 16.sp,
                    fontFamily = XpehoFonts.raleway,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Text(
                        text = "État de l'idée : ",
                        fontSize = 16.sp,
                        fontFamily = XpehoFonts.raleway,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                    )
                    Text(
                        text = statusUi.label.lowercase(),
                        fontSize = 16.sp,
                        fontFamily = XpehoFonts.raleway,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                    )
                }
                suggestion.reason?.takeIf { it.isNotBlank() }?.let { reason ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Row {
                        Text(
                            text = "Message: ",
                            fontSize = 16.sp,
                            fontFamily = XpehoFonts.raleway,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black,
                        )
                        Text(
                            text = reason,
                            fontSize = 16.sp,
                            fontFamily = XpehoFonts.raleway,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }
                }
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                ClickyButton(
                    label = "OK",
                    size = 16.sp,
                    backgroundColor = XpehoColors.XPEHO_COLOR,
                    labelColor = Color.White,
                    verticalPadding = 8.dp,
                    horizontalPadding = 16.dp,
                    enabled = true,
                    onPress = onDismiss
                )
            }
        }
    )
}
