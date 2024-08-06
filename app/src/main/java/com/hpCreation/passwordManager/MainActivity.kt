package com.hpCreation.passwordManager

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.ui.screens.AddEditPasswordSheet
import com.hpCreation.passwordManager.ui.screens.HomeScreen
import com.hpCreation.passwordManager.ui.screens.ViewPasswordItem
import com.hpCreation.passwordManager.ui.theme.PasswordManagerTheme
import com.hpCreation.passwordManager.viewmodel.PasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            /*val isAuthenticated = remember { mutableStateOf(false) }
            if (isAuthenticated.value) {
                PasswordManagerApp()
            } else {
                authenticateFingerprint(isAuthenticated)
            }*/
            PasswordManagerApp()
        }
    }

    private fun authenticateFingerprint(isSuccess: MutableState<Boolean>) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(this@MainActivity, errString.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    isSuccess.value = true
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(this@MainActivity, "Fails......", Toast.LENGTH_SHORT).show()
                }
            })

        val promptForBiometricOnly =
            BiometricPrompt.PromptInfo.Builder().setTitle("Unlock Sample App")
                .setDescription("Use your fingerprint to continue")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setNegativeButtonText("Enter App PIN").build()

        val promptForBiometricOrCredential =
            BiometricPrompt.PromptInfo.Builder().setTitle("Unlock Sample App")
                .setDescription("Confirm your phone screen lock pattern, PIN or password to continue.")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
        biometricPrompt.authenticate(promptForBiometricOrCredential)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordManagerApp() {
    PasswordManagerTheme {
        val viewModel = hiltViewModel<PasswordViewModel>()

        val isBottomSheetVisible by viewModel.isAddEditBottomSheetVisible.collectAsState()
        val isViewBottomSheetVisible by viewModel.isBottomSheetVisible.collectAsState()

        var password by remember { mutableStateOf<Password?>(null) }

        val allPassword = viewModel.allPasswords.collectAsState().value

        Scaffold(topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }, floatingActionButton = {
            FloatingActionButton(containerColor = MaterialTheme.colorScheme.primary, onClick = {
                password = null
                viewModel.onShowAddEditBottomSheet()
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Password")
            }

        }) { innerPadding ->
            if (viewModel.loading.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(1F)
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }

            }
            Box(
                modifier = Modifier.padding(innerPadding)
            ) {
                HomeScreen(allPassword) {
                    password = it
                    viewModel.onShowBottomSheet()
                }

                if (isBottomSheetVisible) {
                    AddEditPasswordSheet(viewModel = viewModel,
                        password = password?.copy(password = viewModel.decrypt(password!!.password)),
                        onDismiss = { viewModel.onDismissAddEditBottomSheet() })
                }

                if (isViewBottomSheetVisible) {
                    password?.let {
                        ViewPasswordItem(password = it.copy(
                            accountType = it.accountType,
                            username = it.username,
                            password = viewModel.decrypt(it.password)
                        ), onDismiss = {
                            viewModel.onDismissBottomSheet()
                        }, onDelete = {
                            viewModel.onDismissBottomSheet()
                            viewModel.deletePassword(passwordId = password!!.id)
                        }, onEdit = {
                            viewModel.onDismissBottomSheet()
                            viewModel.onShowAddEditBottomSheet()
                        })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    PasswordManagerApp()
}