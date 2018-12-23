package com.example.mikle.insurancesysten.admin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.login.LoginActivity
import com.example.mikle.insurancesysten.preferences.IAKey
import com.example.mikle.insurancesysten.preferences.IAPreferences
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : BaseActivity() {

    private var viewModel: AdminViewModel? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        viewModel!!.onCaseCategoryReset(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        adminRV.layoutManager = LinearLayoutManager(this)
        viewModel = ViewModelProviders.of(this).get(AdminViewModel::class.java)
        viewModel!!.backgroundLivaData.observe(this, Observer { onBackground() })
        viewModel!!.adapterLiveData.observe(this, Observer { onAdapterChanged() })
        viewModel!!.dialogLiveData.observe(this, Observer { onDialogChanged() })
        viewModel!!.onCaseCategoryReset(navigation.selectedItemId)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    protected fun onBackground() {
        val value = viewModel!!.backgroundLivaData.value!!
        when (value.status) {
            BackgroundResult.Status.LOADING -> {
                beginLoading()
            }
            BackgroundResult.Status.ERROR -> {
                stopLoading()
                Toast.makeText(this, value.error, Toast.LENGTH_SHORT).show()
            }
            BackgroundResult.Status.SUCCESS -> {
                stopLoading()
                Toast.makeText(this, value.success, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onFab(view: View) {
        viewModel!!.onFab(navigation.selectedItemId, this)
    }

    private fun onAdapterChanged() {
        val adapter = viewModel!!.adapterLiveData.value
        adapter?.setOwner(this)
        adminRV.adapter = adapter

    }

    private fun onDialogChanged() {
        viewModel!!.dialogLiveData.value!!.show()
    }

    override fun beginLoading() {
        progress.visibility = View.VISIBLE
    }

    override fun stopLoading() {
        progress.visibility = View.GONE
    }

}
