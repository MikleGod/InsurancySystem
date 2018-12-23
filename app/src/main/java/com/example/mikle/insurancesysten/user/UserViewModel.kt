package com.example.mikle.insurancesysten.user

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.EmptyResultSetException
import android.support.annotation.IdRes
import android.support.design.widget.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.mikle.insurancesysten.InsuranceApplication
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.validator.ValidatorManager
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.IndividualClient
import com.example.mikle.insurancesysten.entity.LegalClient
import com.example.mikle.insurancesysten.user.adapter.IndividualClientAdapter
import com.example.mikle.insurancesysten.user.adapter.LegalClientAdapter
import com.example.mikle.insurancesysten.util.FileExporter
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class UserViewModel : BaseViewModel() {

    val adapterLiveData: MutableLiveData<OwnerAdapter<*>> = MutableLiveData()
    val dialogLiveData: MutableLiveData<BottomSheetDialog> = MutableLiveData()
    @IdRes
    var checkedId: Int = R.id.nav_individual

    private val individualClientDao = DaoHelper.db.individualClientDao()
    private val legalClientDao = DaoHelper.db.legalClientDao()

    private val individualClientAdapter =
        IndividualClientAdapter(individualClientDao.getLiveAll(), LayoutInflater.from(InsuranceApplication.appContext))
    val clients = legalClientDao.getLiveAll().value
    private val legalClientAdapter =
        LegalClientAdapter(legalClientDao.getLiveAll(), LayoutInflater.from(InsuranceApplication.appContext))


    fun onNavigationItemSelected(@IdRes itemId: Int, owner: BaseActivity) {
        when (itemId) {
            R.id.nav_individual -> {
                adapterLiveData.value = individualClientAdapter
                checkedId = itemId
            }
            R.id.nav_legal -> {
                adapterLiveData.value = legalClientAdapter
                checkedId = itemId
            }
            R.id.nav_send -> {
                System.exit(0)
            }
            R.id.nav_share -> {
                makeExport(owner)
            }
        }
        adapterLiveData.value!!.setOwner(owner)
    }

    private fun makeExport(owner: BaseActivity) {
        disposables.addAll(
            Single.fromCallable {
                val lClients = legalClientDao.getAll()
                val iClients = individualClientDao.getAll()
                FileExporter.ClientsDTO(lClients, iClients)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                t1: FileExporter.ClientsDTO?, t2: Throwable? ->
                    try {
                        FileExporter.export(t1)
                        Toast.makeText(owner, "Успешно. Можете найти отчет в корневой папке.", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception){
                        Toast.makeText(owner, "Ошибка!", Toast.LENGTH_SHORT).show()
                    }
            }
        )
    }

    fun onFab(activity: BaseActivity) {
        when (checkedId) {
            R.id.nav_legal -> dialogLiveData.value = createAddLegalClientDialog(activity)
            R.id.nav_individual -> dialogLiveData.value = createAddIndividualClientDialog(activity)
        }
    }

    private fun createAddLegalClientDialog(activity: BaseActivity): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_legal_add, null)
        dialog.setContentView(view)
        view.findViewById<View>(R.id.registrationButton).setOnClickListener {
            onCreateLegal(
                view.findViewById<EditText>(R.id.name).text.toString(),
                view.findViewById<EditText>(R.id.uniqueNumber).text.toString(),
                view.findViewById<EditText>(R.id.director).text.toString(),
                view.findViewById<EditText>(R.id.accountant).text.toString(),
                view.findViewById<EditText>(R.id.address).text.toString(),
                view.findViewById<EditText>(R.id.phone).text.toString(),
                dialog
            )
        }
        return dialog
    }

    private fun onCreateLegal(
        name: String,
        uniqueNumber: String,
        director: String,
        accountant: String,
        address: String,
        phone: String,
        dialog: BottomSheetDialog
    ) {
        val result = ValidatorManager.LegalClient.instance.validate(
            listOf(
                name,
                uniqueNumber,
                director,
                accountant,
                address,
                phone
            )
        )
        if (!result.isValid) {
            backgroundLivaData.value = BackgroundResult.error(result.message)
            return
        }
        disposables.addAll(legalClientDao.getBy(uniqueNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { backgroundLivaData.value = BackgroundResult.loading() }
            .subscribe { answer: LegalClient?, error: Throwable? ->
                if (error != null && error !is EmptyResultSetException) {
                    backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                    return@subscribe
                }
                if (answer != null) {
                    backgroundLivaData.value =
                            BackgroundResult.error("Клиент с таким уникальным номером уже зарегистрирован!")
                    return@subscribe
                }
                disposables.addAll(
                    Single.fromCallable {
                        legalClientDao.insert(
                            LegalClient(
                                0,
                                name,
                                uniqueNumber,
                                director,
                                accountant,
                                address,
                                phone
                            )
                        )
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { t1: Long?, t2: Throwable? ->
                            if (t2 != null) {
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }
                            backgroundLivaData.value = BackgroundResult.success("Клиент зарегистрирован!")
                            dialog.dismiss()
                        }
                )
            }
        )
    }

    private fun createAddIndividualClientDialog(activity: BaseActivity): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_individual_add, null)
        dialog.setContentView(view)
        view.findViewById<View>(R.id.registrationButton).setOnClickListener {
            onCreateIndividual(
                view.findViewById<EditText>(R.id.name).text.toString(),
                view.findViewById<EditText>(R.id.birthDate).text.toString(),
                view.findViewById<EditText>(R.id.sex).text.toString(),
                view.findViewById<EditText>(R.id.drivingExperience).text.toString(),
                view.findViewById<EditText>(R.id.address).text.toString(),
                view.findViewById<EditText>(R.id.phone).text.toString(),
                dialog
            )
        }
        return dialog
    }

    private fun onCreateIndividual(
        name: String,
        birthDate: String,
        sex: String,
        drivingExperience: String,
        address: String,
        phone: String,
        dialog: BottomSheetDialog
    ) {
        val result = ValidatorManager.IndividualClient.instance.validate(
            listOf(
                name,
                birthDate,
                sex,
                drivingExperience,
                address,
                phone
            )
        )
        if (!result.isValid) {
            backgroundLivaData.value = BackgroundResult.error(result.message)
            return
        }
        disposables.addAll(individualClientDao.getBy(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { backgroundLivaData.value = BackgroundResult.loading() }
            .subscribe { answer: IndividualClient?, error: Throwable? ->
                if (error != null && error !is EmptyResultSetException) {
                    backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                    return@subscribe
                }
                if (answer != null) {
                    backgroundLivaData.value = BackgroundResult.error("Клиент с таким именем уже зарегистрирован!")
                    return@subscribe
                }
                disposables.addAll(
                    Single.fromCallable {
                        individualClientDao.insert(
                            IndividualClient(
                                0,
                                name,
                                birthDate,
                                sex,
                                "",
                                drivingExperience.toInt(),
                                address,
                                phone
                            )
                        )
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { t1: Long?, t2: Throwable? ->
                            if (t2 != null) {
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }
                            backgroundLivaData.value = BackgroundResult.success("Клиент зарегистрирован!")
                            dialog.dismiss()
                        }
                )
            }
        )
    }
}