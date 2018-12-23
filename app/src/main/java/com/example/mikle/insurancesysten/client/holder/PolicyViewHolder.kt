package com.example.mikle.insurancesysten.client.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.mikle.insurancesysten.R

class PolicyViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val category: TextView = view.findViewById(R.id.category)
    val date: TextView = view.findViewById(R.id.date)

}