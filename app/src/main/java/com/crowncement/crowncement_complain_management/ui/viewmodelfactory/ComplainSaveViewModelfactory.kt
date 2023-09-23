package com.crowncement.crowncement_complain_management.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crowncement.crowncement_complain_management.ui.viewmodel.ComplainSaveViewModel


class ComplainSaveViewModelfactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ComplainSaveViewModel::class.java)) {
            return ComplainSaveViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")

    }
}