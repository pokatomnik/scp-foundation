package tk.pokatomnik.scpfoundation.features.inputfocusmanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalTextInputService

interface FocusManager {
    val focusRequester: FocusRequester
    fun requestFocus()
    fun freeFocus()
}

@Composable
fun useInputFocusManager(): FocusManager {
    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalTextInputService.current

    return object : FocusManager {
        override val focusRequester: FocusRequester
            get() = focusRequester

        override fun requestFocus() {
            focusRequester.requestFocus()
            keyboardController?.showSoftwareKeyboard()
        }

        override fun freeFocus() {
            focusRequester.freeFocus()
            keyboardController?.hideSoftwareKeyboard()
        }
    }
}