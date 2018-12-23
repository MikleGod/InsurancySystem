package com.example.mikle.insurancesysten.user.adapter

import android.arch.lifecycle.LiveData
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.entity.IndividualClient
import com.example.mikle.insurancesysten.client.ClientActivity
import com.example.mikle.insurancesysten.user.holder.IndividualClientHolder

class IndividualClientAdapter (
    clients: LiveData<List<IndividualClient>>,
    private val inflater: LayoutInflater
) : OwnerAdapter<IndividualClient>(clients){

    val listener = { pos: Int ->
        val intent = Intent(currentOwner, ClientActivity::class.java)
        intent.putExtra("Client", data.value!![pos])
        currentOwner?.startActivity(intent)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) = IndividualClientHolder(inflater.inflate(R.layout.item_individual_client, null))

    override fun getItemCount()= data.value?.size?:0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IndividualClientHolder){
            holder.birth.text = data.value?.get(position)!!.birthDate
            holder.fullName.text = data.value?.get(position)!!.fullName
            holder.itemView.setOnClickListener{listener(position)}
        }
    }

}