package com.example.mikle.insurancesysten.base.activity

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.mikle.insurancesysten.R
import com.example.mikle.insurancesysten.base.result.BackgroundResult
import com.example.mikle.insurancesysten.base.vm.BaseViewModel
import com.example.mikle.insurancesysten.login.LoginActivity
import com.example.mikle.insurancesysten.preferences.IAKey
import com.example.mikle.insurancesysten.preferences.IAPreferences

abstract class BaseActivity: AppCompatActivity() {


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val exitItem = menu?.add("Выйти")
        exitItem?.setIcon(R.drawable.ic_logout)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS or MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.title!! == "Выйти") {
            IAPreferences.put(IAKey.IS_LOGGED, IAPreferences.IS_LOGGED.default)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    protected abstract fun beginLoading()

    protected abstract fun stopLoading()
}