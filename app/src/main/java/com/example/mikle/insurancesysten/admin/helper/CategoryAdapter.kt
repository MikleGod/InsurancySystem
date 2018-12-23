package com.example.mikle.insurancesysten.admin.helper

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.dao.DaoHelper
import com.example.mikle.insurancesysten.entity.InsuranceCase
import com.example.mikle.insurancesysten.entity.InsuranceCategory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CategoryAdapter(
    val categories: LiveData<List<InsuranceCategory>>,
    private val inflater: LayoutInflater
) : OwnerAdapter<InsuranceCategory>(categories) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        CategoryViewHolder(
            inflater.inflate(
                R.layout.item_category,
                p0,
                false
            )
        )

    override fun getItemCount(): Int =
        if (categories.value != null) {
            categories.value!!.size
        } else {
            0
        }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is CategoryViewHolder) {
            val name = categories.value!![p1].name
            DaoHelper.db.insuranceCaseDao().getByCategory(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1: List<InsuranceCase>?, _: Throwable? ->
                    if (t1 != null) {
                        p0.cases.setText("")
                        p0.name.text = name
                        p0.duration.text = (categories.value!![p1].contractDuration / 1000 / 60 / 60 / 24).toString()
                        p0.maxCost.text = categories.value!![p1].maxCost.toString()
                        for (insuranceCase in t1) {
                            val prevText = p0.cases.text.toString()
                            p0.cases.setText(
                                if (!prevText.isEmpty()) {
                                    prevText + "\n" + insuranceCase.name
                                } else {
                                    insuranceCase.name
                                }
                            )
                        }
                    }
                }

        }
    }

}