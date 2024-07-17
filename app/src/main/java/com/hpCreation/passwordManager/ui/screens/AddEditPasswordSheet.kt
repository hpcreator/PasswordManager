package com.hpCreation.passwordManager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hpCreation.passwordManager.R
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.ui.theme.colorGray
import com.hpCreation.passwordManager.ui.theme.lightGray
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
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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

    ModalBottomSheet(containerColor = lightGray, sheetState = bottomSheetState, onDismissRequest = {
        openBottomSheet = false
        onDismiss()
    }) {
        Column(
            modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
                value = accountType,
                onValueChange = {
                    accountType = it
                    accountTypeError = accountType.isBlank()
                },
                label = { Text("Account Type") },
                isError = accountTypeError,
                supportingText = {
                    if (accountTypeError) {
                        Text(
                            text = "Account Type is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                })
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
                value = username,
                onValueChange = {
                    username = it
                    usernameError = username.isBlank()
                },
                label = { Text("Username") },
                isError = usernameError,
                supportingText = {
                    if (usernameError) {
                        Text(
                            text = "Username is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                })
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                value = passwordValue,
                onValueChange = {
                    passwordValue = it
                    passwordValueError = passwordValue.isBlank()
                },
                label = { Text("Password") },
                isError = passwordValueError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                supportingText = {
                    if (passwordValueError) {
                        Text(
                            text = "Password is required",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(horizontal = 5.dp)
                        )
                    }
                },
                trailingIcon = {
                    val image = if (passwordVisible) R.drawable.ic_show
                    else R.drawable.ic_hide
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(image), description)
                    }
                })
            if (password != null) {
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorGray),
                    onClick = {
                        if (validateFields()) {
                            viewModel?.update(password)
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
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorGray),
                    onClick = {
                        if (validateFields()) {
                            viewModel?.insert(
                                Password(
                                    accountType = accountType,
                                    username = username,
                                    password = passwordValue
                                )
                            )
                        }
                    }) {
                    Text(
                        "Add new Account",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
        }
    }
}

@Preview(name = "AddEditPassword", showBackground = true)
@Composable
fun DefaultPreview() {
    AddEditPasswordSheet(viewModel = viewModel(), password = Password(
        accountType = "Google", username = "Harsh", password = "Google@123"
    ), onDismiss = {})
}