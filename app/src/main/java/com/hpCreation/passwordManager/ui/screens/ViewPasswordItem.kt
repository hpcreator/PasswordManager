package com.hpCreation.passwordManager.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hpCreation.passwordManager.R
import com.hpCreation.passwordManager.data.Password
import com.hpCreation.passwordManager.ui.theme.colorGray
import com.hpCreation.passwordManager.ui.theme.colorGray50
import com.hpCreation.passwordManager.ui.theme.colorPrimary
import com.hpCreation.passwordManager.ui.theme.colorSecondary
import com.hpCreation.passwordManager.util.decryptPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPasswordItem(
    password: Password,
    onDismiss: () -> Unit,
    onDelete: (password: Password) -> Unit,
    onEdit: (password: Password) -> Unit
) {

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var openBottomSheet by rememberSaveable { mutableStateOf(true) }

    var showPassword by remember { mutableStateOf(false) }

    LaunchedEffect(openBottomSheet) {
        if (openBottomSheet) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }

    ModalBottomSheet(sheetState = bottomSheetState, onDismissRequest = {
        openBottomSheet = false
        onDismiss()
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Account Details",
                style = MaterialTheme.typography.headlineMedium,
                color = colorPrimary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )
            password.let {
                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    text = "Account Type",
                    color = colorGray50,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.accountType,
                    color = colorGray,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    text = "Username/Email",
                    color = colorGray50,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it.username,
                    color = colorGray,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                    text = "Password",
                    color = colorGray50,
                    style = MaterialTheme.typography.labelMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = if (showPassword) password.password.decryptPassword() else "*".repeat(
                            password.password.decryptPassword().length
                        ),
                        color = colorGray,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    IconButton(onClick = { showPassword = !showPassword }) {
                        val image = if (showPassword) R.drawable.ic_hide
                        else R.drawable.ic_show
                        Icon(
                            painter = painterResource(id = image), contentDescription = "View"
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorGray),
                        onClick = {
                            Log.e("TAG", "password item is: $password")
                            onEdit(password)
                        }) {
                        Text(
                            "Edit",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = colorSecondary),
                        onClick = { onDelete(password) }) {
                        Text(
                            "Delete",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                }
            }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ViewPasswordPreview() {
    ViewPasswordItem(password = Password(
        accountType = "Google", username = "Harsh Patel", password = "Harsh@123"
    ), onDismiss = {}, onDelete = {}, onEdit = {})
}