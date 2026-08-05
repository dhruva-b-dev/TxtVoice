package com.dhruva.txtvoice.features.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: Int,
    val description: Int,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val pages = listOf(
        OnboardingPage(
            R.string.onboarding_welcome_title,
            R.string.onboarding_welcome_desc,
            Icons.Default.WavingHand,
            MaterialTheme.colorScheme.primary
        ),
        OnboardingPage(
            R.string.onboarding_transcribe_title,
            R.string.onboarding_transcribe_desc,
            Icons.Default.Mic,
            Color(0xFF4CAF50)
        ),
        OnboardingPage(
            R.string.onboarding_speak_title,
            R.string.onboarding_speak_desc,
            Icons.Default.RecordVoiceOver,
            Color(0xFF2196F3)
        ),
        OnboardingPage(
            R.string.onboarding_privacy_title,
            R.string.onboarding_privacy_desc,
            Icons.Default.Shield,
            Color(0xFFFF9800)
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Skip Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            if (pagerState.currentPage < pages.size - 1) {
                TextButton(onClick = {
                    viewModel.completeOnboarding()
                    onFinish()
                }) {
                    Text(
                        text = stringResource(R.string.onboarding_skip),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            OnboardingPagerItem(pages[page])
        }

        // Bottom Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicators
            Row {
                repeat(pages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                    
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }

            // Next/Get Started Button
            Button(
                onClick = {
                    if (pagerState.currentPage < pages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        viewModel.completeOnboarding()
                        onFinish()
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == pages.size - 1)
                        stringResource(R.string.onboarding_get_started)
                    else
                        stringResource(R.string.onboarding_next)
                )
            }
        }
    }
}

@Composable
fun OnboardingPagerItem(page: OnboardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(150.dp),
            shape = CircleShape,
            color = page.color.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = page.icon,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = page.color
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(page.title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(page.description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    TxtVoiceTheme {
        OnboardingScreen(onFinish = {})
    }
}
