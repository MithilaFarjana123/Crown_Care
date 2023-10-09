package com.crowncement.crowncement_complain_management.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crowncement.crowncement_complain_management.ui.viewmodel.UpdateSeenStatViewModel

class UpdateSeenStatViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateSeenStatViewModel::class.java)) {
            return UpdateSeenStatViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")

    }
}