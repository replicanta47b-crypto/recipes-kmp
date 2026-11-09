package com.example.restapi.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

/** Collects one-off events (navigation, messages) of a ViewModel while the screen is in the composition. */
@Composable
internal fun <T> ObserveEvents(events: Flow<T>, onEvent: (T) -> Unit) {
    val currentOnEvent by rememberUpdatedState(onEvent)
    LaunchedEffect(events) {
        events.collect { currentOnEvent(it) }
    }
}
