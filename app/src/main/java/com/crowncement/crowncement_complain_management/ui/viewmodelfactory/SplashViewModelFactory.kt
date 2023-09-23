package com.crowncement.crowncement_complain_management.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.crowncement.crowncement_complain_management.ui.viewmodel.SplashViewModel


class SplashViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {

            return SplashViewModel() as T
        }
        throw IllegalArgumentException("UnknownViewModel")
    }
}