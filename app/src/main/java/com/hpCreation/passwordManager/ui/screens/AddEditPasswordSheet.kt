package com.hpCreation.passwordManager.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.ui.components.OutlinePasswordTextbox
import com.hpCreation.passwordManager.ui.components.OutlinedTextBox
import com.hpCreation.passwordManager.ui.components.RoundedButton
import com.hpCreation.passwordManager.viewmodel.PasswordViewModel
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPasswordSheet(
    viewModel: PasswordViewModel?, password: Password? = null, onDismiss: () -> Unit
) {
    var accountType by remember { mutableStateOf(password?.accountType?.trim() ?: "") }
    var username by remember { mutableStateOf(password?.username?.trim() ?: "") }
    var passwordValue by remember { mutableStateOf(password?.password?.trim() ?: "") }

    var accountTypeError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var passwordValueError by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var openBottomSheet by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(openBottomSheet) {
        if (openBottomSheet) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    LaunchedEffect(Unit) {
        viewModel?.eventFlow?.collectLatest { event ->
            when (event) {
                is PasswordViewModel.UiEvent.PasswordSaved -> onDismiss()
                is PasswordViewModel.UiEvent.PasswordDeleted -> {}
            }
        }
    }


    fun validateFields(): Boolean {
        accountTypeError = accountType.isBlank()
        usernameError = username.isBlank()
        passwordValueError = passwordValue.isBlank()
        return !accountTypeError && !usernameError && !passwordValueError
    }

    ModalBottomSheet(containerColor = MaterialTheme.colorScheme.surfaceContainer,
        sheetState = bottomSheetState,
        onDismissRequest = {
            openBottomSheet = false
            onDismiss()
        }) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextBox(
                value = accountType,
                onValueChange = {
                    accountType = it
                    accountTypeError = accountType.isBlank()
                },
                label = "Account Type",
                maxLines = 1,
                singleLine = true,
                isError = accountTypeError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                helperText = if (accountTypeError) "Account Type is required" else null
            )

            OutlinedTextBox(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = username.isBlank()
                },
                label = "Username",
                maxLines = 1,
                singleLine = true,
                isError = usernameError,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                helperText = if (accountTypeError) "Username is required" else null
            )

            OutlinePasswordTextbox(
                value = passwordValue,
                onValueChange = {
                    passwordValue = it
                    passwordValueError = passwordValue.isBlank()
                },
                label = "Password",
                singleLine = true,
                maxLines = 1,
                isError = passwordValueError,
                helperText = if (passwordValueError) "Password is required" else null
            )
            if (password != null) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    onClick = {
                        if (validateFields()) {
                            viewModel?.updatePassword(
                                password.copy(
                                    accountType = accountType,
                                    username = username,
                                    password = viewModel.encrypt(passwordValue)
                                )
                            )
                        }
                    }) {
                    Text(
                        "Update Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            } else {
                RoundedButton(text = "Add new Account", onClick = {
                    if (validateFields()) {
                        viewModel?.addPassword(
                            Password(
                                accountType = accountType,
                                username = username,
                                password = viewModel.encrypt(passwordValue)
                            )
                        )
                    }
                }, buttonColor = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true, name = "lightUi", uiMode = UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, showBackground = true, name = "darkUi", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
    AddEditPasswordSheet(viewModel = viewModel(), password = Password(
        accountType = "Google", username = "Harsh", password = "Google@123"
    ), onDismiss = {})
}