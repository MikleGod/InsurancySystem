package com.example.mikle.insurancesysten.admin

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.persistence.room.EmptyResultSetException
import android.arch.persistence.room.Transaction
import android.support.annotation.IdRes
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.mikle.insurancesysten.InsuranceApplication
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.admin.helper.CasesAdapter
import com.example.mikle.insurancesysten.admin.helper.CategoryAdapter
import com.example.mikle.insurancesysten.admin.helper.ChooseCasesAdapter
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.validator.ValidatorManager
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.CaseCategoryRelation
import com.example.mikle.insurancesysten.entity.InsuranceCase
import com.example.mikle.insurancesysten.entity.InsuranceCategory
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AdminViewModel : BaseViewModel() {

    val adapterLiveData: MutableLiveData<OwnerAdapter<*>> = MutableLiveData()
    val dialogLiveData: MutableLiveData<BottomSheetDialog> = MutableLiveData()

    private val caseDao = DaoHelper.db.insuranceCaseDao()
    private val categoryDao = DaoHelper.db.insuranceCategoryDao()
    private val caseCategoryRelationDao = DaoHelper.db.caseCategoryRelationDao()

    private val caseAdapter = CasesAdapter(
        caseDao.getAll(),
        LayoutInflater.from(InsuranceApplication.appContext)
    )
    private val categoryAdapter = CategoryAdapter(
        categoryDao.getAll(),
        LayoutInflater.from(InsuranceApplication.appContext)
    )



    fun onCaseCategoryReset(@IdRes itemId: Int) {
        when(itemId){
            R.id.navigation_cases -> adapterLiveData.value = caseAdapter
            R.id.navigation_categories -> adapterLiveData.value = categoryAdapter
        }

    }

    fun onFab(@IdRes itemId: Int, activity: AdminActivity) {
        when(itemId){
            R.id.navigation_cases -> dialogLiveData.value = createAddCaseDialog(activity)
            R.id.navigation_categories -> dialogLiveData.value = createAddCategoryDialog(activity)
        }
    }

    private fun createAddCaseDialog(activity: AdminActivity): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_case_add, null)
        dialog.setContentView(view)
        view.findViewById<View>(R.id.saveCase).setOnClickListener{
            onCreateCase(view.findViewById<EditText>(R.id.name).text.toString(), view.findViewById<EditText>(R.id.percent).text.toString(), dialog)
        }
        return dialog
    }

    private fun onCreateCase(name: String, percent: String, dialog: BottomSheetDialog) {
        val result = ValidatorManager.InsuranceCase.instance.validate(listOf(name, percent))
        if (!result.isValid){
            backgroundLivaData.value = BackgroundResult.error(result.message)
            return
        }
        disposables.addAll(caseDao.getBy(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ backgroundLivaData.value = BackgroundResult.loading()}
            .subscribe{
                    answer: InsuranceCase?, error: Throwable? ->
                if (error != null && error !is EmptyResultSetException){
                    backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                    return@subscribe
                }
                if (answer != null){
                    backgroundLivaData.value = BackgroundResult.error("Случай с таким именем уже есть!")
                    return@subscribe
                }
                disposables.addAll(
                    Single.fromCallable{
                        caseDao.insert(InsuranceCase(percent.toInt(), name))
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            t1: Long?, t2: Throwable? ->
                            if (t2 != null){
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }
                            backgroundLivaData.value = BackgroundResult.success("Случай создан!")
                            dialog.dismiss()
                        }
                )
            }
        )
    }


    private fun createAddCategoryDialog(activity: AdminActivity): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.dialog_category_add, null)
        var cases: List<String> = ArrayList<String>()
        dialog.setContentView(view)
        view.findViewById<Button>(R.id.cases).setOnClickListener{
            val casesDialog = BottomSheetDialog(activity)
            val casesView = inflater.inflate(R.layout.dialog_case_choose, null)
            val casesRV = casesView.findViewById<RecyclerView>(R.id.casesRV)
            casesRV.layoutManager = LinearLayoutManager(activity)
            val data = caseDao.getAll()
            val chooseCasesAdapter = ChooseCasesAdapter(data, inflater)
            data.observe(activity, Observer { chooseCasesAdapter.notifyDataSetChanged() })
            casesRV.adapter = chooseCasesAdapter
            casesView.findViewById<Button>(R.id.save).setOnClickListener{
                cases = chooseCasesAdapter.cases.asSequence().map {  insuranceCase -> insuranceCase.name }.toList()
                casesDialog.dismiss()
            }
            casesDialog.setContentView(casesView)
            casesDialog.show()
        }
        view.findViewById<Button>(R.id.saveCategory).setOnClickListener{
            onCreateCategory(
                view.findViewById<EditText>(R.id.name).text.toString(),
                view.findViewById<EditText>(R.id.maxCost).text.toString(),
                view.findViewById<EditText>(R.id.duration).text.toString(),
                cases, dialog)
        }
        return dialog
    }

    @Transaction
    private fun onCreateCategory(name: String, maxCost: String, duration: String, cases: List<String>, dialog: BottomSheetDialog) {
        val result = ValidatorManager.InsuranceCategory.instance.validate(listOf(name, maxCost, duration))
        if (!result.isValid){
            backgroundLivaData.value = BackgroundResult.error(result.message)
            return
        }
        if (cases.isEmpty()){
            backgroundLivaData.value = BackgroundResult.error("Нужно выбрать хотя бы один страховой случай!")
            return
        }
        disposables.addAll(categoryDao.getBy(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe{ backgroundLivaData.value = BackgroundResult.loading()}
            .subscribe{
                    answer: InsuranceCategory?, error: Throwable? ->
                if (error != null && error !is EmptyResultSetException){
                    backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                    return@subscribe
                }
                if (answer != null){
                    backgroundLivaData.value = BackgroundResult.error("Категория с таким именем уже есть!")
                    return@subscribe
                }
                disposables.addAll(
                    Single.fromCallable{
                        categoryDao.insert(InsuranceCategory(duration.toLong()*24*60*60*1000, maxCost.toInt(), name))
                        for (case in cases) {
                            caseCategoryRelationDao.insert(CaseCategoryRelation(0, name, case))
                        }
                    }.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                                _: Unit, t2: Throwable? ->
                            if (t2 != null){
                                backgroundLivaData.value = BackgroundResult.error("Ошибка!")
                                return@subscribe
                            }
                            backgroundLivaData.value = BackgroundResult.success("Категория создана!")
                            dialog.dismiss()
                        }
                )
            }
        )
    }

}