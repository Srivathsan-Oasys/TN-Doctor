package com.hmis_tn.doctor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.hmis_tn.doctor.data.networking.api.res.ResVideoChat
import com.hmis_tn.doctor.data.repository.VideoChatRepository

class VideoChatViewModel(
    private val repository: VideoChatRepository
) : ViewModel() {
    val networkEvent = repository.networkEvents

    fun videoChatAPI(randomUUID: String): LiveData<ResVideoChat> {
        return liveData {
            emitSource(repository.videoChatAPI(randomUUID))
        }
    }

}