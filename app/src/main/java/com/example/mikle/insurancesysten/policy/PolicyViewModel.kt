package com.example.mikle.insurancesysten.policy

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.EmptyResultSetException
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import com.example.mikle.insurancesysten.InsuranceApplication
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.admin.helper.ChooseCasesAdapter
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.client.adapter.PaymentAdapter
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.*
import com.example.mikle.insurancesysten.util.DateTimeUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PolicyViewModel : BaseViewModel() {

    val dialogLiveData: MutableLiveData<BottomSheetDialog> = MutableLiveData()

    private val caseDao = DaoHelper.db.insuranceCaseDao()
    private val paymentDao = DaoHelper.db.paymentDao()
    private val legalClientDao = DaoHelper.db.legalClientDao()
    private val individualClientDao = DaoHelper.db.individualClientDao()
    private val userDao = DaoHelper.db.userDao()


    fun paymentAdapter(policyId: Int) = PaymentAdapter(
        paymentDao.getByPolicy(policyId),
        LayoutInflater.from(InsuranceApplication.appContext),
        null
    )


    fun createAddPaymentDialog(activity: BaseActivity, policy: InsurancePolicy) {

        val caseDialog = BottomSheetDialog(activity)
        val inflater = LayoutInflater.from(activity)
        val caseView = inflater.inflate(R.layout.dialog_case_choose, null)
        val caseAdapter = ChooseCasesAdapter(caseDao.getLiveByCategory(policy.categoryName), inflater)
        caseView.findViewById<RecyclerView>(R.id.casesRV).adapter = caseAdapter
        caseView.findViewById<Button>(R.id.save).setOnClickListener {
            onCreatePayment(policy, caseAdapter.cases, caseDialog)
        }
        caseDialog.setContentView(caseView)
        caseDialog.show()
    }

    private fun onCreatePayment(
        policy: InsurancePolicy,
        cases: List<InsuranceCase>,
        caseDialog: BottomSheetDialog
    ) {
        if (cases.size != 1) {
            backgroundLivaData.value = BackgroundResult.error("Нужно выбрать один страховой случай!")
            return
        }
        disposables.addAll(
            Single.fromCallable {
                paymentDao.insert(
                    Payment(
                        0,
                        policy.id,
                        policy.categoryName,
                        policy.sum * cases[0].percents / 100,
                        DateTimeUtil.getDateTime(),
                        null,
                        false
                    )
                )
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { _: Long?, t2: Throwable? ->
                    if (t2 != null) {
                        backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                        return@subscribe
                    }
                    backgroundLivaData.value = BackgroundResult.success("Обращение зарегистрировано!")
                    caseDialog.dismiss()
                }
        )
    }

    fun insertClientName(clientName: EditText?, isIndividual: Boolean, policy: InsurancePolicy) {
        if (isIndividual) {
            disposables.addAll(
                individualClientDao.getBy(policy.clientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { t1: IndividualClient?, t2: Throwable? ->
                        if (t2 !is EmptyResultSetException && t2 != null) {
                            backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                            return@subscribe
                        }
                        clientName!!.setText(t1!!.fullName)
                    })
        } else {
            disposables.addAll(
                legalClientDao.getBy(policy.clientId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { t1: LegalClient?, t2: Throwable? ->
                        if (t2 !is EmptyResultSetException && t2 != null) {
                            backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                            return@subscribe
                        }
                        clientName!!.setText(t1!!.name)
                    })
        }
    }

    fun insertUserName(userName: EditText?, policy: InsurancePolicy) {
        disposables.addAll(
            userDao.getSingleBy(policy.userId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1: User?, t2: Throwable? ->
                    if (t2 !is EmptyResultSetException && t2 != null) {
                        backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                        return@subscribe
                    }
                    userName!!.setText(t1!!.name)
                }
        )
    }
}