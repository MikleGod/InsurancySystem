package com.example.mikle.insurancesysten.user.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.mikle.insurancesysten.R
import kotlinx.android.synthetic.main.item_individual_client.view.*

class IndividualClientHolder(view: View) : RecyclerView.ViewHolder(view) {
    val photo: ImageView
    val fullName: TextView
    val birth: TextView
    init {
        photo = view.findViewById(R.id.photo)
        fullName = view.findViewById(R.id.fullName)
        birth = view.findViewById(R.id.birthDate)
    }
}