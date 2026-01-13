package org.n27.stonks.presentation.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ViewModel(dispatcher: CoroutineDispatcher) {

    protected val viewModelScope = CoroutineScope(dispatcher + SupervisorJob())

    open fun onResult(result: Map<String, Any>) {}
}
