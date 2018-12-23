package com.example.mikle.insurancesysten.user.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.mikle.insurancesysten.R

class LegalClientHolder(view: View) : RecyclerView.ViewHolder(view) {
    val uniqueNumber: TextView = view.findViewById(R.id.uniqueNumber)
    val name: TextView = view.findViewById(R.id.name)

}