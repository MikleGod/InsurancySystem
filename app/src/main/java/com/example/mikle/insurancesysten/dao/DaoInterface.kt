package com.example.mikle.insurancesysten.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.example.mikle.insurancesysten.entity.*
import io.reactivex.Flowable
import io.reactivex.Single

interface InsuranceDao<T>{
    @Insert
    fun insert(entity: T): Long
    @Update
    fun update(entity: T): Int
    @Delete
    fun delete(entity: T): Int
}


@Dao
interface CaseCategoryRelationDao: InsuranceDao<CaseCategoryRelation>{

    @Query("select * from case_category_relation")
    fun getAll(): LiveData<CaseCategoryRelation>
}

@Dao
interface UserDao : InsuranceDao<User>{

    @Query("select * from user")
    fun getAll(): LiveData<User>

    @Query("select * from user where id = :id")
    fun getBy(id: Int): LiveData<User?>

    @Query("select * from user where id = :id")
    fun getSingleBy(id: Int): Single<User?>

    @Query("select * from user where email = :email and password = :password")
    fun getBy(email: String, password: String): Single<User?>

    @Query("select * from user where email = :email")
    fun getBy(email: String):Single<User>
}

@Dao
interface IndividualClientDao : InsuranceDao<IndividualClient>{

    @Query("select * from individual_client")
    fun getLiveAll(): LiveData<List<IndividualClient>>

    @Query("select * from individual_client")
    fun getAll(): List<IndividualClient>

    @Query("select * from individual_client where full_name = :fullName and address = :address")
    fun getBy(fullName: String, address: String): Flowable<IndividualClient>?

    @Query("select * from individual_client where id = :id")
    fun getBy(id: Int): Single<IndividualClient>

    @Query("select * from individual_client where id = :name")
    fun getBy(name: String): Single<IndividualClient>

    @Query("select * from individual_client where id in (select client_id from insurance_policy where id = :id)")
    fun getByPolicy(id: Int): Flowable<IndividualClient>

    @Query("select * from individual_client where id in (select client_id from insurance_policy where id in (select policy_id from payment where id = :id))")
    fun getByPayment(id: Int): Flowable<IndividualClient>
}

@Dao
interface LegalClientDao : InsuranceDao<LegalClient>{

    @Query("select * from legal_client")
    fun getLiveAll(): LiveData<List<LegalClient>>

    @Query("select * from legal_client")
    fun getAll(): List<LegalClient>

    @Query("select * from legal_client where id = :id")
    fun getBy(id: Int): Single<LegalClient>

    @Query("select * from legal_client where unique_number = :uniqueNumber")
    fun getBy(uniqueNumber: String): Single<LegalClient>

    @Query("select * from legal_client where id in (select client_id from insurance_policy where id = :id)")
    fun getByPolicy(id: Int): Flowable<LegalClient>

    @Query("select * from legal_client where id in (select client_id from insurance_policy where id in (select policy_id from payment where id = :id))")
    fun getByPayment(id: Int): Flowable<LegalClient>
}

@Dao
interface InsuranceCategoryDao : InsuranceDao<InsuranceCategory>{

    @Query("select * from insurance_category")
    fun getAll(): LiveData<List<InsuranceCategory>>

    @Query("select * from insurance_category where name = :name")
    fun getBy(name: String): Single<InsuranceCategory>
}

@Dao
interface InsuranceCaseDao : InsuranceDao<InsuranceCase>{

    @Query("select * from insurance_case")
    fun getAll(): LiveData<List<InsuranceCase>>

    @Query("select * from insurance_case where name = :name")
    fun getBy(name: String): Single<InsuranceCase?>

    @Query("select * from insurance_case where name in (select case_name from case_category_relation where category_name = :name)")
    fun getByCategory(name: String): Single<List<InsuranceCase>>

    @Query("select * from insurance_case where name in (select case_name from case_category_relation where category_name = :name)")
    fun getLiveByCategory(name: String): LiveData<List<InsuranceCase>>
}

@Dao
interface InsurancePolicyDao : InsuranceDao<InsurancePolicy>{

    @Query("select * from insurance_policy")
    fun getAll(): LiveData<List<InsurancePolicy>>

    @Query("select * from insurance_policy where client_id = :id and is_legal = :isLegal")
    fun getByClient(id: Int, isLegal: Boolean): LiveData<List<InsurancePolicy>>

    @Query("select * from insurance_policy where id = :id")
    fun getBy(id: Int): Flowable<InsurancePolicy>
}

@Dao
interface PaymentDao : InsuranceDao<Payment>{

    @Query("select * from payment")
    fun getAll(): LiveData<List<Payment>>

    @Query("select * from payment where policy_id = :id")
    fun getByPolicy(id: Int): LiveData<List<Payment>>

    @Query("select * from payment where policy_id in (select id from insurance_policy where client_id = :id and is_legal = :isLegal)")
    fun getByClient(id: Int, isLegal: Boolean): LiveData<List<Payment>>

    @Query("select * from payment where id = :id")
    fun getBy(id: Int): Flowable<Payment>
}