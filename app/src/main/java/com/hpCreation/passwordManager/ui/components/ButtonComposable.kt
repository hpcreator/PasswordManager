package com.hpCreation.passwordManager.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoundedButton(
    modifier: Modifier = Modifier,
    text: String,
    textSize: TextUnit = 18.sp,
    onClick: () -> Unit,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    cornerRadius: Dp = 10.dp
) {
    Button(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
        contentPadding = contentPadding,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Text(
            text,
            fontSize = textSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun OutlineButton(
    modifier: Modifier = Modifier,
    text: String,
    textSize: TextUnit = 18.sp,
    onClick: () -> Unit,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    cornerRadius: Dp = 10.dp
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp),
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = borderColor),
        contentPadding = contentPadding,
        shape = RoundedCornerShape(cornerRadius)
    ) {
        Text(
            text,
            fontSize = textSize,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(5.dp)
        )
    }
}
