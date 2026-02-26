package com.bksd.core.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<A, E> : ViewModel() {

    private val eventChannel = Channel<E>(capacity = Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    open fun onAction(action: A) {}
    protected fun launch(block: suspend () -> Unit) = viewModelScope.launch { block() }
    protected fun sendEvent(event: E) { eventChannel.trySend(event) }
}