package com.example.mikle.insurancesysten.client.adapter

import android.arch.lifecycle.LiveData
import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.client.holder.PaymentViewHolder
import com.example.mikle.insurancesysten.entity.Payment

class PaymentAdapter(
    cases: LiveData<List<Payment>>,
    private val inflater: LayoutInflater,
    private val listener: Listener?
) : OwnerAdapter<Payment>(cases){

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int)
            =
        PaymentViewHolder(inflater.inflate(R.layout.item_payment, p0, false))

    override fun getItemCount(): Int =
        if (data.value != null){
            data.value!!.size
        } else {
            0
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        if (holder is PaymentViewHolder) {
            if (listener != null){
                holder.itemView.setOnClickListener{listener.onClick(data.value!![p1], holder.itemView.context)}
            }
            val payment = data.value!![p1]
            holder.requestDate.setText(payment.requestDate)
            holder.category.setText(payment.categoryName)
            holder.sum.setText(payment.sum.toString())
            if (payment.isRejected){
                holder.rejected.visibility = VISIBLE
                holder.paymentDate.visibility = GONE
            } else {
                if (payment.paymentDate != null){
                    holder.paymentDate.visibility = VISIBLE
                    holder.paymentDate.setText(payment.paymentDate)
                }
            }

        }
    }

    interface Listener{
        fun onClick(payment: Payment, context: Context)
    }

}