package com.example.mikle.insurancesysten.registration

import android.arch.persistence.room.EmptyResultSetException
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.validator.ValidatorManager
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.User
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RegistrationViewModel : BaseViewModel(){

    val userDao = DaoHelper.db.userDao()

    fun register(name: String, surname:String, fatherName: String, email: String, password: String, repeatPassword: String){
        val result = ValidatorManager.UserRegistration.instance.validate(listOf(name, surname, fatherName, email, password, repeatPassword))
        if (!result.isValid){
            backgroundLivaData.value = BackgroundResult.error(result.message)
            return
        }
        disposables.addAll(
            userDao.getBy(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                        t1: User?, t2: Throwable? ->
                    if (t1 != null){
                        backgroundLivaData.value = BackgroundResult.error("E-mail занят!")
                        return@subscribe
                    }
                    if (t2 != null && t2 !is EmptyResultSetException){
                        backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                        return@subscribe
                    }
                    Single.fromCallable { userDao.insert(User(0, "$name $surname $fatherName", email, password, 2)) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                                _: Long?, t2: Throwable? ->
                            if (t2 != null){
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }
                            backgroundLivaData.value = BackgroundResult.success("Пользователь успешно зарегистрирован!")
                        }
                }
        )
        disposables.addAll()
    }
}
