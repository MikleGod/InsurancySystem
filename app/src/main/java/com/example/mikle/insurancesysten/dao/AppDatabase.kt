package com.example.mikle.insurancesysten.dao

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.mikle.insurancesysten.entity.*

@Database(
    entities = [
        User::class,
        IndividualClient::class,
        LegalClient::class,
        InsuranceCase::class,
        InsuranceCategory::class,
        InsurancePolicy::class,
        Payment::class,
        CaseCategoryRelation::class
    ],
    version = 2
)
abstract class AppDatabase :RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun caseCategoryRelationDao(): CaseCategoryRelationDao
    abstract fun individualClientDao(): IndividualClientDao
    abstract fun legalClientDao(): LegalClientDao
    abstract fun insuranceCaseDao(): InsuranceCaseDao
    abstract fun insuranceCategoryDao(): InsuranceCategoryDao
    abstract fun insurancePolicyDao(): InsurancePolicyDao
    abstract fun paymentDao(): PaymentDao
}