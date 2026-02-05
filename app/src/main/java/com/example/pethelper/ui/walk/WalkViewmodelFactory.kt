package com.example.pethelper.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pethelper.data.session.AppSession
import com.example.pethelper.ui.walk.WalkViewModel

class WalkViewModelFactory(
    private val orderId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WalkViewModel(orderId = orderId, rtdbRepository = AppSession.rtdbRepository) as T
    }
}