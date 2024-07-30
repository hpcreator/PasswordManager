package com.hpCreation.passwordManager.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import com.hpCreation.passwordManager.data.Password

@Composable
fun HomeScreen(
    passwordList: List<Password>, onView: (password: Password) -> Unit
) {
    if (passwordList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background),
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

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_NO, name = "LightPreview")
@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES, name = "DarkPreview")
@Composable
fun HomePreview() {
    HomeScreen(listOf()) {}
}
