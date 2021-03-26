package com.hmis_tn.doctor.data.event

import androidx.annotation.StringRes
import com.hmis_tn.doctor.R

typealias AppEvent = BaseEvent<Status>

/**
 * Event wrapper, Use it with LiveData only for UI change
 */
open class BaseEvent<T>(private val content: T) {

    /**
     * Return true if the content have been already handled, false otherwise
     */
    var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

enum class Status {
    None,
    Running,
    Success,
    Failure;
}

class Error(@StringRes val res: Int = R.string.error_generic_message) : AppEvent(Status.Failure)