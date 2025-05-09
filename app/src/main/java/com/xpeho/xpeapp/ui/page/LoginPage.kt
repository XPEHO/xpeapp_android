package com.xpeho.xpeapp.ui.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xpeho.xpeapp.R
import com.xpeho.xpeapp.XpeApp
import com.xpeho.xpeapp.ui.components.CustomDialog
import com.xpeho.xpeapp.ui.sendAnalyticsEvent
import com.xpeho.xpeapp.ui.uiState.WordpressUiState
import com.xpeho.xpeapp.ui.viewModel.WordpressViewModel
import com.xpeho.xpeapp.ui.viewModel.viewModelFactory
import com.xpeho.xpeho_ui_android.ClickyButton
import com.xpeho.xpeho_ui_android.InputText
import com.xpeho.xpeho_ui_android.foundations.Colors as XpehoColors

/**
 * Login page
 * @param onLoginSuccess: Callback when login is successful
 */
@Composable
fun LoginPage(onLoginSuccess: () -> Unit) {
    val wordpressViewModel = viewModel<WordpressViewModel>(
        factory = viewModelFactory {
            WordpressViewModel(XpeApp.appModule.authenticationManager)
        }
    )

    sendAnalyticsEvent("login_page")

    // If login is successful, notify of login success
    LaunchedEffect(wordpressViewModel.wordpressState) {
        if (wordpressViewModel.wordpressState is WordpressUiState.SUCCESS) {
            onLoginSuccess()
        }
    }

    // If login fails, show error dialog
    if (wordpressViewModel.wordpressState is WordpressUiState.ERROR) {
        CustomDialog(
            title = stringResource(id = R.string.login_page_error_title),
            message = (wordpressViewModel.wordpressState as WordpressUiState.ERROR).error,
            closeDialog = { wordpressViewModel.wordpressState = WordpressUiState.EMPTY })
    }

    LoginPageContent(
        wordpressViewModel = wordpressViewModel,
    )
}

@Composable
private fun LoginPageContent(
    wordpressViewModel: WordpressViewModel,
) {
    LoginPageContentColumn(
        wordpressViewModel,
    )
}

@Composable
private fun LoginPageContentColumn(
    wordpressViewModel: WordpressViewModel,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LoginPageIcon()
        Spacer(modifier = Modifier.height(100.dp))
        LoginPageInputFields(
            wordpressViewModel,
        )
        Spacer(modifier = Modifier.height(32.dp))
        LoginPageButton(
            wordpressViewModel,
        )
    }
}

@Composable
private fun LoginPageIcon() {
    Icon(
        painter = painterResource(id = R.drawable.app_icon_cropped),
        tint = XpehoColors.XPEHO_COLOR,
        contentDescription = stringResource(id = R.string.xpeho_logo_content),
        modifier = Modifier.width(200.dp)
    )
}

@Composable
private fun LoginPageInputFields(
    wordpressViewModel: WordpressViewModel,
) {
    val focusManager = LocalFocusManager.current
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    InputText(
        label = stringResource(id = R.string.login_page_email),
        defaultInput = wordpressViewModel.usernameInput,
        labelSize = 14.sp,
        inputSize = 18.sp,
        focusRequester = usernameFocusRequester,
        keyboardAction = ImeAction.Next,
        onKeyboardAction = {
            passwordFocusRequester.requestFocus()
        },
        onInput = {
            wordpressViewModel.usernameInput = it
            wordpressViewModel.usernameInError = false
        }
    )
    CheckInputField(wordpressViewModel.usernameInError, R.string.login_page_enter_email_warning)
    Spacer(modifier = Modifier.height(10.dp))
    InputText(
        label = stringResource(id = R.string.login_page_password),
        defaultInput = wordpressViewModel.passwordInput,
        labelSize = 14.sp,
        inputSize = 18.sp,
        password = true,
        focusRequester = passwordFocusRequester,
        keyboardAction = ImeAction.Done,
        onKeyboardAction = {
            focusManager.clearFocus()
            wordpressViewModel.onLogin()
        },
        onInput = {
            wordpressViewModel.passwordInput = it
            wordpressViewModel.passwordInError = false
        }
    )
    CheckInputField(wordpressViewModel.passwordInError, R.string.login_page_enter_password_warning)
}

@Composable
private fun LoginPageButton(
    wordpressViewModel: WordpressViewModel,
) {
    ClickyButton(
        label = "SE CONNECTER",
        size = 18.sp,
        verticalPadding = 10.dp,
        horizontalPadding = 50.dp,
        backgroundColor = XpehoColors.XPEHO_COLOR,
        labelColor = Color.White,
        enabled = !(wordpressViewModel.wordpressState is WordpressUiState.LOADING ||
                wordpressViewModel.wordpressState is WordpressUiState.SUCCESS)
    ) {
        wordpressViewModel.onLogin()
    }
}

@Composable
private fun CheckInputField(
    errorTextFieldUser: Boolean,
    stringId: Int,
) {
    if (errorTextFieldUser) {
        ErrorTextMessage(
            message = stringResource(id = stringId)
        )
    }
}

@Composable
fun ErrorTextMessage(message: String) {
    Text(
        text = message,
        color = Color.Red,
        textAlign = TextAlign.Start,
        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    )
}

@Preview
@Composable
fun ErrorTextMessagePreview() {
    ErrorTextMessage(message = "Veuillez entrer votre email")
}
