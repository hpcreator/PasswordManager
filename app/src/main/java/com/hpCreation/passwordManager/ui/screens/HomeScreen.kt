package com.hpCreation.passwordManager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.ui.theme.colorBackground

@Composable
fun HomeScreen(
    passwordList: List<Password>, onView: (password: Password) -> Unit
) {

    //val passwordList by viewModel.allPasswords.collectAsState()

    if (passwordList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "No Records found.\nPlease add new recode by clicking + button !",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(passwordList) { password ->
                PasswordItem(password = password, onViewClick = { onView(password) })
            }
        }
    }
}

@Preview(name = "HomeScreen", showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    HomeScreen(viewModel()) {}
}
