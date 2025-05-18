package com.hpCreation.passwordManager.ui.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hpCreation.passwordManager.R
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.ui.components.OutlineButton
import com.hpCreation.passwordManager.ui.components.RoundedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPasswordItem(
    password: Password,
    onDismiss: () -> Unit,
    onDelete: (Password) -> Unit,
    onEdit: (Password) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var openBottomSheet by rememberSaveable { mutableStateOf(true) }
    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(openBottomSheet) {
        if (openBottomSheet) bottomSheetState.show()
        else bottomSheetState.hide()
    }

    ModalBottomSheet(
        sheetState = bottomSheetState, onDismissRequest = {
            openBottomSheet = false
            onDismiss()
        }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = stringResource(R.string.account_details),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            Spacer(Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.account_type),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = password.accountType,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.username_email),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = password.username,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.password),
                style = MaterialTheme.typography.labelMedium
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val pass = password.password
                Text(
                    modifier = Modifier.weight(1f),
                    text = if (showPassword) pass else "*".repeat(pass.length),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
                IconButton(
                    onClick = { showPassword = !showPassword }) {
                    val image = if (showPassword) R.drawable.ic_hide else R.drawable.ic_show
                    Icon(
                        painter = painterResource(id = image), contentDescription = stringResource(
                            if (showPassword) R.string.hide_password else R.string.show_password
                        )
                    )
                }
            }
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlineButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.edit),
                    onClick = { onEdit(password) })
                RoundedButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.delete),
                    onClick = { onDelete(password) })
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true, name = "lightUi", uiMode = UI_MODE_NIGHT_NO)
@Preview(showSystemUi = true, showBackground = true, name = "darkUi", uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ViewPasswordPreview() {
    ViewPasswordItem(
        password = Password(
            accountType = "Google", username = "Harsh Patel", password = "Harsh@123"
        ), onDismiss = {}, onDelete = {}, onEdit = {})
}