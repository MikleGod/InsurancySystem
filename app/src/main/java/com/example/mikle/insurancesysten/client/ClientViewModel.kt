package com.example.mikle.insurancesysten.client

import android.app.Dialog
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.support.annotation.IdRes
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mikle.insurancesysten.InsuranceApplication
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.admin.helper.ChooseCasesAdapter
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.client.adapter.ChooseCategoryAdapter
import com.example.mikle.insurancesysten.client.adapter.ChoosePolicyAdapter
import com.example.mikle.insurancesysten.client.adapter.PaymentAdapter
import com.example.mikle.insurancesysten.client.adapter.PolicyAdapter
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.*
import com.example.mikle.insurancesysten.preferences.IAPreferences
import com.example.mikle.insurancesysten.util.DateTimeUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jxl.WorkbookSettings

class ClientViewModel : BaseViewModel() {



    val adapterLiveData: MutableLiveData<OwnerAdapter<*>> = MutableLiveData()
    val dialogLiveData: MutableLiveData<BottomSheetDialog> = MutableLiveData()

    private val policyDao = DaoHelper.db.insurancePolicyDao()
    private val paymentDao = DaoHelper.db.paymentDao()
    private val categoryDao = DaoHelper.db.insuranceCategoryDao()
    private val caseDao = DaoHelper.db.insuranceCaseDao()


    var individualClient: IndividualClient? = null
    var legalClient: LegalClient? = null


    fun onNavigationClicked(@IdRes itemId: Int, activity: BaseActivity) {
        val listener = object : PaymentAdapter.Listener {
            override fun onClick(payment: Payment, context: Context) {
                workWithPayment(payment, context)
            }
        }
        when (itemId) {
            R.id.navigation_policy -> {
                val data = policyDao.getByClient(getClientId(), legalClient != null)
                val policyAdapter = PolicyAdapter(
                    data,
                    LayoutInflater.from(InsuranceApplication.appContext),
                    legalClient == null
                )
                policyAdapter.setOwner(activity)
                adapterLiveData.value = policyAdapter
            }
            R.id.navigation_payment -> {
                val paymentAdapter = PaymentAdapter(
                    paymentDao.getByClient(getClientId(), legalClient != null),
                    LayoutInflater.from(InsuranceApplication.appContext),
                    listener
                )
                paymentAdapter.setOwner(activity)
                adapterLiveData.value = paymentAdapter
            }
        }

    }

    private fun workWithPayment(payment: Payment, context: Context) {
        if (!payment.isRejected && payment.paymentDate == null) {
            val dialog = BottomSheetDialog(ClientActivity.activity!!)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_payment_work, null)
            view.findViewById<Button>(R.id.reject).setOnClickListener {
                disposables.addAll(
                    Single.fromCallable {
                        payment.isRejected = true
                        paymentDao.update(payment)
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                            t1: Int?, t2: Throwable? ->
                            if (t2 != null) {
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }

                            backgroundLivaData.value = BackgroundResult.success("Заявка отклонена!")
                            dialog.dismiss()
                        }
                )
            }
            view.findViewById<Button>(R.id.pay).setOnClickListener {
                disposables.addAll(
                    Single.fromCallable {
                        payment.paymentDate = DateTimeUtil.getDateTime()
                        paymentDao.update(payment)
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe{
                                t1: Int?, t2: Throwable? ->
                            if (t2 != null) {
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }
                            backgroundLivaData.value = BackgroundResult.success("Заявка выплачена!")
                            dialog.dismiss()
                        }
                )
            }
            dialog.setContentView(view)
            dialog.show()
        }
    }

    fun onFab(@IdRes itemId: Int, activity: BaseActivity) {
        when (itemId) {
            R.id.navigation_policy -> dialogLiveData.value = createAddPolicyDialog(activity)
            R.id.navigation_payment -> dialogLiveData.value = createAddPaymentDialog(activity)
        }
    }

    private fun createAddPolicyDialog(activity: BaseActivity): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_policy_add, null)
        val rv = view.findViewById<RecyclerView>(R.id.casesRV)
        rv.layoutManager = LinearLayoutManager(activity)
        val data = categoryDao.getAll()
        val adapter = ChooseCategoryAdapter(data, activity.layoutInflater)
        adapter.setOwner(activity)
        rv.adapter = adapter
        view.findViewById<View>(R.id.save).setOnClickListener {
            onCreatePolicy(
                view.findViewById<EditText>(R.id.sum).text.toString(),
                adapter.chosen,
                dialog
            )
        }
        dialog.setContentView(view)
        return dialog
    }

    private fun onCreatePolicy(sum: String, categories: List<InsuranceCategory>, dialog: BottomSheetDialog) {
        if (categories.size != 1) {
            backgroundLivaData.value =
                    BackgroundResult.error("Неверное количество выбранных категорий: должна быть выбрана 1.")
            return
        }
        val policy = InsurancePolicy(
            0,
            categories[0].name,
            getClientId(),
            IAPreferences.getInt(IAPreferences.IS_LOGGED),
            sum.toInt(),
            DateTimeUtil.getDateTime(),
            legalClient != null
        )
        disposables.addAll(
            Single.fromCallable {
                policyDao.insert(policy)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1: Long?, t2: Throwable? ->
                    if (t2 != null) {
                        backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                        return@subscribe
                    }
                    backgroundLivaData.value = BackgroundResult.success("Полис создан!")
                    dialog.dismiss()
                }
        )
    }

    private fun getClientId() = if (individualClient == null) legalClient!!.id else individualClient!!.id


    private fun createAddPaymentDialog(activity: BaseActivity): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_policy_choose, null)
        val rv = view.findViewById<RecyclerView>(R.id.casesRV)
        rv.layoutManager = LinearLayoutManager(activity)
        val adapter =
            ChoosePolicyAdapter(policyDao.getByClient(getClientId(), legalClient != null), activity.layoutInflater)
        adapter.setOwner(activity)
        rv.adapter = adapter
        view.findViewById<View>(R.id.save).setOnClickListener {
            val chosen = adapter.chosen
            if (chosen.size != 1) {
                backgroundLivaData.value =
                        BackgroundResult.error("Нужно выбрать один полис!")
                return@setOnClickListener
            }

            val caseDialog = BottomSheetDialog(activity)
            val inflater = LayoutInflater.from(activity)
            val caseView = inflater.inflate(R.layout.dialog_case_choose, null)
            val caseAdapter = ChooseCasesAdapter(caseDao.getLiveByCategory(chosen[0].categoryName), inflater)
            caseAdapter.setOwner(activity)
            caseView.findViewById<RecyclerView>(R.id.casesRV).adapter = caseAdapter
            caseView.findViewById<RecyclerView>(R.id.casesRV).layoutManager = LinearLayoutManager(activity)
            caseView.findViewById<Button>(R.id.save).setOnClickListener {
                onCreatePayment(chosen[0], caseAdapter.cases, dialog, caseDialog)
            }
            caseDialog.setContentView(caseView)
            caseDialog.show()
        }
        dialog.setContentView(view)
        return dialog
    }

    //    @Transaction
    private fun onCreatePayment(
        policy: InsurancePolicy,
        cases: List<InsuranceCase>,
        policyDialog: BottomSheetDialog,
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
                    backgroundLivaData.value = BackgroundResult.success("Категория создана!")
                    caseDialog.dismiss()
                    policyDialog.dismiss()
                }
        )
    }


}