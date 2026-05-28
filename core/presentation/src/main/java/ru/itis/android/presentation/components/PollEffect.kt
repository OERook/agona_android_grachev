package ru.itis.android.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun PollEffect(
    key: Any?,
    intervalMs: Long = 8_000L,
    enabled: Boolean = true,
    action: suspend () -> Unit
) {
    LaunchedEffect(key, enabled, intervalMs) {
        if (!enabled) return@LaunchedEffect
        while (true) {
            delay(intervalMs)
            action()
        }
    }
}
