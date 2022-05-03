package tk.pokatomnik.scpfoundation.components

import android.widget.NumberPicker
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun NumberPickerComponent(
    min: Int = 0,
    max: Int,
    onValueChange: (old: Int, new: Int) -> Unit = { _, _ -> },
) {
    fun NumberPicker.update() {
        setOnValueChangedListener { _, i, i2 ->
            onValueChange(i, i2)
        }
        minValue = min
        maxValue = max
    }

    AndroidView(
        factory = { context ->
            NumberPicker(context).apply { update() }
        },
        update = { it.update() }
    )
}