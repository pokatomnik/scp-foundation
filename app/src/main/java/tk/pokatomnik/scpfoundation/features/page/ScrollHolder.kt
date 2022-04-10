package tk.pokatomnik.scpfoundation.features.page

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private fun debounce(
    waitMs: Long = 300L,
    coroutineScope: CoroutineScope,
    destinationFunction: () -> Unit
): () -> Unit {
    var debounceJob: Job? = null
    return {
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction()
        }
    }
}

internal class ScrollHolder(private val coroutineScope: CoroutineScope) {
    private var value: Int? = null

    private var _onScrollTop: () -> Unit = {}

    private var _onScrollBottom: () -> Unit = {}

    private var lastCalledFn: (() -> Unit)? = null

    fun set(newValue: Int) {
        val currentVal = value ?: return
        val callBackToInvoke = when {
            (newValue > currentVal) -> _onScrollBottom
            (newValue < currentVal) -> _onScrollTop
            else -> lastCalledFn
        }
        if (lastCalledFn != callBackToInvoke) {
            callBackToInvoke?.invoke()
            lastCalledFn = callBackToInvoke
        }
        value = newValue
    }

    fun init(
        initialValue: Int,
        onScrollTop: () -> Unit,
        onScrollBottom: () -> Unit
    ) {
        value = initialValue
        _onScrollBottom = debounce(
            destinationFunction = onScrollBottom,
            coroutineScope = coroutineScope,
        )
        _onScrollTop = debounce(
            destinationFunction = onScrollTop,
            coroutineScope = coroutineScope,
        )
    }
}