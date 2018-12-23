package com.example.mikle.insurancesysten

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class InsuranceApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {

        var appContext: Context? = null

        fun preferences(): SharedPreferences {
            return appContext!!.getSharedPreferences("INSURE_APP_PREF", 0)
        }
    }

}