package com.example.mikle.insurancesysten.entity

data class RegistrationData(
    val email: String,
    val password: String,
    val name: String,
    val role: Role
)
data class LoginData(
    val email: String,
    val password: String
)

data class CategoryDto(
    val contractDuration: Long,
    val maxCost: Int,
    val name: String,
    val caseIds: List<Int>
)