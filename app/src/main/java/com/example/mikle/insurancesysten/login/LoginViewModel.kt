package com.example.mikle.insurancesysten.login

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.persistence.room.EmptyResultSetException
import android.support.annotation.MainThread
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.User
import com.example.mikle.insurancesysten.preferences.IAKey
import com.example.mikle.insurancesysten.preferences.IAPreferences
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscriber

class LoginViewModel: BaseViewModel(){

    private val userDao = DaoHelper.db.userDao()
    var user: User? = null

    fun userById(id: Int) = userDao.getBy(id)


    fun login(email: String, password: String) {
        disposables.addAll(
            userDao.getBy(email, password).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    t1: User?, t2: Throwable? ->
                    if (t2 is EmptyResultSetException){
                        backgroundLivaData.value = BackgroundResult.error("Неверный e-mail или пароль!")
                        return@subscribe
                    }
                    if (t2 != null){
                        backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                        return@subscribe
                    }
                    user = t1
                    IAPreferences.put(IAKey.IS_LOGGED, t1!!.id)
                    backgroundLivaData.value = BackgroundResult.success("Успешно!")
                }
        )
    }
}
