package tk.pokatomnik.scpfoundation.pages

open class State(
    val error: Throwable?,
    val loading: Boolean,
    val items: List<PageInfo>,
) {
    companion object {
        fun initial(): State {
            return State(
                error = null,
                loading = false,
                items = listOf(),
            )
        }

        fun loading(): State {
            return State(
                error = null,
                loading = true,
                items = listOf(),
            )
        }

        fun error(throwable: Throwable, items: List<PageInfo>): State {
            return State(
                error = throwable,
                loading = false,
                items = items,
            )
        }

        fun data(items: List<PageInfo>): State {
            return State(
                error = null,
                loading = false,
                items = items,
            )
        }
    }
}