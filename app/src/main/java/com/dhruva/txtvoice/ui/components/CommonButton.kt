package com.dhruva.txtvoice.ui.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.dhruva.txtvoice.ui.theme.LightGray
import com.dhruva.txtvoice.ui.theme.Mustard
import com.dhruva.txtvoice.ui.theme.Typography
import com.dhruva.txtvoice.ui.theme.YellowPrimary

@Composable
fun CommonYellowButton(
    leadingIcon: ImageVector,
    @StringRes text: Int,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.padding(4.dp),
        onClick = { onButtonClick() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = YellowPrimary,
        )
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = stringResource(text),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(8.dp)) // Adds gap between icon and text
        Text(
            text = stringResource(text),
            color = Mustard,
            style = Typography.bodyLarge
        )
    }
}


@Composable
fun CommonBlackButton(
    leadingIcon: ImageVector,
    @StringRes text: Int,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier.padding(4.dp),
        onClick = { onButtonClick() },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.White)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = stringResource(text),
            modifier = Modifier.size(18.dp),
            tint = Color.White
        )
        Spacer(modifier = Modifier.size(8.dp)) // Adds gap between icon and text
        Text(
            text = stringResource(text),
            color = Color.White,
            style = Typography.bodyLarge
        )
    }
}