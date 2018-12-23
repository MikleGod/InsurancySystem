package com.example.mikle.insurancesysten.base.vm

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel: ViewModel() {

    val backgroundLivaData: MutableLiveData<BackgroundResult> = MutableLiveData()

    protected val disposables = CompositeDisposable()


    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}