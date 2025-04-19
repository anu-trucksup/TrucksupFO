package com.trucksup.field_officer.presenter.view.activity.other.vml

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.trucksup.field_officer.domain.usecases.APIUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(val apiUseCase: APIUseCase,
                                          application: Application): AndroidViewModel(application) {
    private var loggedInStatus: MutableLiveData<Int> = MutableLiveData<Int>()
    val loggedInStatusLD: LiveData<Int> = loggedInStatus

    @SuppressLint("SuspiciousIndentation")
    fun checkLoggedInStatus() {
        CoroutineScope(Dispatchers.IO).launch {
            val isUserLoggedIn = apiUseCase.isUserLoggedIn()
            if (isUserLoggedIn) {
                loggedInStatus.postValue(1)
            } else {
                loggedInStatus.postValue(0)
            }
        }
    }

}