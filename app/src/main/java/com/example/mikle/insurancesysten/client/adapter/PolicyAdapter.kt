package com.example.mikle.insurancesysten.client.adapter

import android.arch.lifecycle.LiveData
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.client.holder.PolicyViewHolder
import com.example.mikle.insurancesysten.entity.InsurancePolicy
import com.example.mikle.insurancesysten.policy.PolicyActivity

class PolicyAdapter(
    data: LiveData<List<InsurancePolicy>>,
    val inflater: LayoutInflater,
    val isIndividual: Boolean
) : OwnerAdapter<InsurancePolicy>(data) {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder
            = PolicyViewHolder(inflater.inflate(R.layout.item_policy, p0, false))

    override fun getItemCount(): Int =
        if (data.value != null){
            data.value!!.size
        } else {
            0
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        if (holder is PolicyViewHolder) {
            val insurancePolicy = data.value!![p1]
            holder.category.text = insurancePolicy.categoryName
            holder.date.setText(insurancePolicy.date)
            holder.itemView.setOnClickListener{
                val intent = Intent(currentOwner, PolicyActivity::class.java)
                intent.putExtra("Policy", insurancePolicy)
                intent.putExtra("IsIndividual", isIndividual)
                currentOwner?.startActivity(intent)
            }
        }
    }
}
