package com.example.mikle.insurancesysten.client.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import com.example.mikle.insurancesysten.R

class PaymentViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val rejected: TextView = view.findViewById(R.id.rejected)
    val category: EditText = view.findViewById(R.id.category)
    val sum: EditText = view.findViewById(R.id.sum)
    val requestDate: EditText = view.findViewById(R.id.requestDate)
    val paymentDate: EditText = view.findViewById(R.id.paymentDate)

}