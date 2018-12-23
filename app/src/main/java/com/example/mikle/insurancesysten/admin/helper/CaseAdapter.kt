package com.example.mikle.insurancesysten.admin.helper

import android.arch.lifecycle.LiveData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.entity.InsuranceCase

class CasesAdapter(
    val cases: LiveData<List<InsuranceCase>>,
    private val inflater: LayoutInflater
) : OwnerAdapter<InsuranceCase>(cases){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int)
            =
        CaseViewHolder(inflater.inflate(R.layout.item_case, p0, false))

    override fun getItemCount(): Int =
        if (cases.value != null){
            cases.value!!.size
        } else {
            0
        }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        if (p0 is CaseViewHolder) {
            p0.name.text = cases.value?.get(p1)!!.name
            p0.percent.text = cases.value?.get(p1)!!.percents.toString()
        }
    }

}