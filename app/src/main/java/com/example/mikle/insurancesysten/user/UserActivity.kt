package com.example.mikle.insurancesysten.user

import android.Manifest
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.activity.BaseActivity
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.app_bar_user.*
import kotlinx.android.synthetic.main.content_user.*

class UserActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {


    private var viewModel: UserViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        viewModel!!.adapterLiveData.observe(this, Observer{onAdapterChanged()})
        viewModel!!.dialogLiveData.observe(this, Observer{onDialogCame()})
        viewModel!!.backgroundLivaData.observe(this, Observer{onBackground()})
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener{view: View -> onFab(view)}
        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = viewModel!!.adapterLiveData.value
        if (adapter != null){
            recycler.adapter = adapter
            nav_view.setCheckedItem(viewModel!!.checkedId)
        } else {
            onNavigationItemSelected(nav_view.menu.getItem(0))
            nav_view.setCheckedItem(R.id.nav_individual)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        viewModel!!.onNavigationItemSelected(item.itemId, this)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun onFab(view: View){
        viewModel!!.onFab( this)
    }

    override fun beginLoading() {
    }

    override fun stopLoading() {
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

    private fun onDialogCame() {
        viewModel!!.dialogLiveData.value?.show()
    }

    private fun onAdapterChanged(){
        val adapter = viewModel!!.adapterLiveData.value
        if (adapter != null){
            recycler.adapter = adapter
        }
    }
}
