package com.hmis_tn.doctor.data.event

import androidx.annotation.StringRes
import com.hmis_tn.doctor.R

sealed class NetworkEvent(content: Status) : AppEvent(content) {

    /**
     * Object that represents a Network None
     */
    object None : NetworkEvent(
        Status.None
    )

    /**
     * Object that represents a Network Success
     */
    object Success : NetworkEvent(
        Status.Success
    )

    /**
     * Object that represents a Network Loading
     */
    object Loading : NetworkEvent(
        Status.Running
    )

    /**
     * Object that represents a Network Error
     */
    class Failure(
        @StringRes val res: Int = R.string.error_generic_message
    ) : NetworkEvent(Status.Failure)


    class ApiMessage(
        val msg: String
    ) : NetworkEvent(Status.Success)

    class ResMessage(
        @StringRes val res: Int
    ) : NetworkEvent(Status.Success)

}
