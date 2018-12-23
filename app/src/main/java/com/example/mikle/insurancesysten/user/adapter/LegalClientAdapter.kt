package com.example.mikle.insurancesysten.user.adapter

import android.arch.lifecycle.LiveData
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.adapter.OwnerAdapter
import com.example.mikle.insurancesysten.client.ClientActivity
import com.example.mikle.insurancesysten.entity.LegalClient
import com.example.mikle.insurancesysten.user.holder.LegalClientHolder

class LegalClientAdapter(
    clients: LiveData<List<LegalClient>>,
    private val inflater: LayoutInflater
) : OwnerAdapter<LegalClient>(clients) {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        LegalClientHolder(inflater.inflate(R.layout.item_legal_client, p0, false))

    override fun getItemCount(): Int =
        if (data.value != null){
            data.value!!.size
        } else {
            0
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is LegalClientHolder) {
            holder.name.text = data.value?.get(position)!!.name
            holder.uniqueNumber.text = data.value?.get(position)!!.uniqueNumber
            holder.itemView.setOnClickListener{

                val intent = Intent(currentOwner, ClientActivity::class.java)
                intent.putExtra("Client", data.value!![position])
                intent.putExtra("isIndividual", false)
                currentOwner?.startActivity(intent)
            }
        }
    }
}