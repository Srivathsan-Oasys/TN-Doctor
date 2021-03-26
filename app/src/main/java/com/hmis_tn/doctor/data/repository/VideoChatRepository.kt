package com.hmis_tn.doctor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.hmis_tn.doctor.R
import com.hmis_tn.doctor.data.event.NetworkEvent
import com.hmis_tn.doctor.data.networking.api.VideoChatAPI
import com.hmis_tn.doctor.data.networking.api.common.ApiException
import com.hmis_tn.doctor.data.networking.api.res.ResVideoChat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private class VideoChatRepositoryImpl(val api: VideoChatAPI) : VideoChatRepository {
    private val _networkEvents = MutableLiveData<NetworkEvent>()
    override val networkEvents: LiveData<NetworkEvent> = _networkEvents

    override suspend fun videoChatAPI(randomUUID: String): LiveData<ResVideoChat> {
        _networkEvents.postValue(NetworkEvent.Loading)

        return liveData(Dispatchers.IO) {
            try {
                val response = api.videoCallAPI(randomUUID)
                if (response.isSuccessful && response.code() == 200) {
                    withContext(Dispatchers.Main) {
                        _networkEvents.postValue(NetworkEvent.Success)
                    }
                    val data = response.body() ?: throw IllegalStateException("Body is null")
                    emit(data)
                } else {
                    throw ApiException(response.code())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(
                        NetworkEvent.Failure(R.string.error_like_unlike_failed)
                    )
                }
            } catch (e: ApiException) {
                withContext(Dispatchers.Main) {
                    _networkEvents.postValue(NetworkEvent.Failure(R.string.error_like_unlike_failed))
                }
            }
        }
    }
}

interface VideoChatRepository {

    val networkEvents: LiveData<NetworkEvent>

    suspend fun videoChatAPI(randomUUID: String): LiveData<ResVideoChat>

    companion object {
        fun create(api: VideoChatAPI): VideoChatRepository {
            return VideoChatRepositoryImpl(api)
        }
    }

}