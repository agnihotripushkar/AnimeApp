package com.devpush.animeapp.core.presentation

import androidx.annotation.StringRes

sealed interface UiText {
    data class DynamicString(val value: String) : UiText
    class StringResource(@StringRes val id: Int, val args: Array<Any> = emptyArray()) : UiText
}
