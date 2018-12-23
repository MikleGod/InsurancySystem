package com.example.mikle.insurancesysten.admin.helper

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.CheckedTextView
import android.widget.TextView
import com.example.mikle.insurancesysten.R

class CategoryViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)
    val duration: TextView = view.findViewById(R.id.duration)
    val maxCost: TextView = view.findViewById(R.id.maxCost)
    val cases: AutoCompleteTextView = view.findViewById(R.id.cases)

}

class CaseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val name: TextView = view.findViewById(R.id.name)

    val percent: TextView = view.findViewById(R.id.percent)

}

class ChooseCaseViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val case: CheckedTextView = view.findViewById(R.id.choose)

}