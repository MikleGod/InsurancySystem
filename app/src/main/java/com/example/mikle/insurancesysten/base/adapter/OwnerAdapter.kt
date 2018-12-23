package com.example.mikle.insurancesysten.base.adapter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.support.v7.widget.RecyclerView
import com.example.mikle.insurancesysten.base.activity.BaseActivity

abstract class OwnerAdapter<T>(val data: LiveData<List<T>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currentOwner : BaseActivity? = null

    fun setOwner(owner: BaseActivity) {
        currentOwner = owner
        data.observe(owner, Observer {
            notifyDataSetChanged()
        })
    }
}