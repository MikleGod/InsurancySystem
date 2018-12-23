package com.example.mikle.insurancesysten.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "role_id") val roleId: Int
) : Serializable


//data class Client(
//    @PrimaryKey(autoGenerate = true) val id: Int,
//    @ColumnInfo(name = "full_name") val fullName: String,
//    @ColumnInfo(name = "address") val address: String,
//    @ColumnInfo(name = "phone") val phone: String
//)

enum class Role(val id: Int) {
    ADMIN(1), USER(2)
}

@Entity(tableName = "individual_client")
data class IndividualClient(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "full_name")
    val fullName: String,
    @ColumnInfo(name = "birth_date")
    val birthDate: String,
    @ColumnInfo(name = "sex")
    val sex: String,
    @ColumnInfo(name = "photo")
    val photo: String,
    @ColumnInfo(name = "driving_experience")
    val drivingExperience: Int,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "phone")
    val phone: String
) : Serializable

@Entity(tableName = "legal_client")
data class LegalClient(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "unique_number")
    val uniqueNumber: String,
    @ColumnInfo(name = "director")
    val director: String,
    @ColumnInfo(name = "accountant")
    val accountant: String,
    @ColumnInfo(name = "address")
    val address: String,
    @ColumnInfo(name = "phone")
    val phone: String
) : Serializable

@Entity(tableName = "insurance_category")
data class InsuranceCategory(
    @ColumnInfo(name = "contract_duration")
    val contractDuration: Long,
    @ColumnInfo(name = "max_cost")
    val maxCost: Int,
    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String
)

@Entity(tableName = "insurance_case")
data class InsuranceCase(

    @ColumnInfo(name = "percents")
    val percents: Int,

    @PrimaryKey
    @ColumnInfo(name = "name")
    val name: String
)

@Entity(tableName = "case_category_relation")
data class CaseCategoryRelation(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "case_name")
    val caseName: String
)

@Entity(tableName = "insurance_policy")
data class InsurancePolicy(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "client_id")
    val clientId: Int,
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "sum")
    val sum: Int,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "is_legal")
    val isLegal: Boolean
) : Serializable

@Entity
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "policy_id")
    val policyId: Int,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "sum")
    val sum: Int,
    @ColumnInfo(name = "request_date")
    val requestDate: String,
    @ColumnInfo(name = "payment_date")
    var paymentDate: String?,
    @ColumnInfo(name = "rejected")
    var isRejected: Boolean
)

