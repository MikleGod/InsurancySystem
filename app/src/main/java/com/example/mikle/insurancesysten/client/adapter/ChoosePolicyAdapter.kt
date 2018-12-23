package com.example.mikle.insurancesysten.client.adapter

import android.arch.lifecycle.LiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.admin.helper.ChooseCaseViewHolder
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.entity.InsurancePolicy

class ChoosePolicyAdapter(data: LiveData<List<InsurancePolicy>>, val inflater: LayoutInflater) :
    OwnerAdapter<InsurancePolicy>(data) {

    var chosen: ArrayList<InsurancePolicy> = ArrayList()

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        ChooseCaseViewHolder(
            inflater.inflate(
                R.layout.item_choose,
                null
            )
        )

    override fun getItemCount() =
        if (data.value != null) {
            data.value!!.size
        } else {
            0
        }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is ChooseCaseViewHolder) {
            val case = data.value!![p1]
            p0.case.text = "${case.categoryName} ${case.date}"
            p0.case.setOnClickListener {
                val checked = p0.case.isChecked
                if (checked) {
                    chosen.remove(case)
                } else {
                    chosen.add(case)
                }
                p0.case.isChecked = !checked
            }
        }
    }

}