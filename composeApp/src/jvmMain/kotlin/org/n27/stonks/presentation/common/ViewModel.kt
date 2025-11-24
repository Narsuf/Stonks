package org.n27.stonks.presentation.common

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class ViewModel {
    protected val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    open fun onResult(result: String) {}
}
