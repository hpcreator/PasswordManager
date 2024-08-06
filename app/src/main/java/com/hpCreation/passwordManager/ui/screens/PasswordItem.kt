package com.hpCreation.passwordManager.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hpCreation.passwordManager.R
import com.hpCreation.passwordManager.data.Password

@Composable
fun PasswordItem(password: Password, onViewClick: (password: Password) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onViewClick(password)
            },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.widthIn(min = 0.dp, max = 200.dp),
                    text = password.accountType,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 24.sp,
                )
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = "*".repeat(5),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

            }
            IconButton(onClick = { onViewClick(password) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_right),
                    contentDescription = "View",
                )
            }
        }
    }
}

@Preview(
    showSystemUi = false, showBackground = true, uiMode = UI_MODE_NIGHT_NO, name = "LightPreview"
)
@Preview(
    showSystemUi = false, showBackground = true, uiMode = UI_MODE_NIGHT_YES, name = "DarkPreview"
)
@Composable
private fun PreviewPasswordItem() {
    PasswordItem(
        password = Password(
            username = "harsh Patel", password = "Harsh@123", accountType = "Google"
        )
    ) {}
}