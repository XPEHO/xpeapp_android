package com.xpeho.xpeapp.ui.components.ideaBox

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xpeho.xpeapp.R
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors
import com.xpeho.xpeho_ui_android.foundations.Fonts as XpehoFonts

@Composable
fun IdeaStatusBanner(
    message: String,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, XpehoColors.XPEHO_COLOR.copy(alpha = 0.3f)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.mega_phone),
                contentDescription = "Notification",
                tint = XpehoColors.XPEHO_COLOR,
                modifier = Modifier.size(26.dp),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                color = XpehoColors.CONTENT_COLOR,
                fontFamily = XpehoFonts.raleway,
                fontSize = 18.sp,
            )
        }
    }
}
