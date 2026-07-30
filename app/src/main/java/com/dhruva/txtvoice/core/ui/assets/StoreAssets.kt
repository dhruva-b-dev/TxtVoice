package com.dhruva.txtvoice.core.ui.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.core.ui.theme.DarkBackground
import com.dhruva.txtvoice.core.ui.theme.DarkSurface
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme
import com.dhruva.txtvoice.core.ui.theme.YellowPrimary

/**
 * Google Play Store Icon (512x512)
 * Instructions: Use Android Studio Preview at 512x512dp and export as PNG.
 */
@Composable
fun GooglePlayStoreIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(512.dp)
            .background(YellowPrimary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(380.dp)
        )
    }
}

/**
 * Google Play Feature Graphic (1024x500)
 * Instructions: Use Android Studio Preview at 1024x500dp and export as PNG.
 */
@Composable
fun FeatureGraphic(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 1024.dp, height = 500.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            // App Icon
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(YellowPrimary),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = stringResource(id = R.string.app_tagline),
                style = MaterialTheme.typography.headlineSmall,
                color = YellowPrimary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(
    name = "512dp x 512dp",
    widthDp = 512,
    heightDp = 512
)
@Preview(name = "Store Icon", widthDp = 512, heightDp = 512)
@Composable
fun GooglePlayStoreIconPreview() {
    TxtVoiceTheme {
        GooglePlayStoreIcon()
    }
}

@Preview(name = "Feature Graphic", widthDp = 1024, heightDp = 500)
@Composable
fun FeatureGraphicPreview() {
    TxtVoiceTheme {
        FeatureGraphic()
    }
}
