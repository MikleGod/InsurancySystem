package com.example.mikle.insurancesysten.dao

import android.arch.persistence.room.Room
import com.example.mikle.insurancesysten.InsuranceApplication

class DaoHelper {
    companion object {
        val db = Room.databaseBuilder(InsuranceApplication.appContext!!, AppDatabase::class.java, "jio").build()
    }
}