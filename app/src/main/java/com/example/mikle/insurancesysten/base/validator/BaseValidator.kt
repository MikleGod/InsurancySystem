package com.example.mikle.insurancesysten.base.validator

import android.text.TextUtils
import com.example.mikle.insurancesysten.admin.helper.InsuranceCaseValidator
import com.example.mikle.insurancesysten.admin.helper.InsuranceCategoryValidator
import com.example.mikle.insurancesysten.registration.UserRegistrationValidator
import com.example.mikle.insurancesysten.user.validator.IndividualClientValidator
import com.example.mikle.insurancesysten.user.validator.LegalClientValidator
import java.lang.Exception


interface BaseValidator{
    fun validate(params: List<String>): ValidationResult
    fun validateName(name: String): Boolean{
        return name.matches(Regex(".+"))
    }
    fun validateNumber(number: String): Boolean{
        val intMaxCost: Int
        try {
            intMaxCost = number.toInt()
        } catch (e: Exception){
            return false
        }
        return true
    }
    fun validateManName(manName: String): Boolean{
        return validateName(manName)
    }
    fun validateAddress(address: String): Boolean{
        return validateName(address)
    }
    fun validatePhone(phone: String): Boolean{
        return validateName(phone)
    }
    fun validateDate(phone: String): Boolean{
        return validateName(phone)
    }
    fun validateSex(phone: String): Boolean{
        return phone == "Мужчина" || phone == "Женщина"
    }
    fun validateEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) // какая - нибудь регулярка для валидаци мыла
    }
    fun validatePas(password: String): Boolean {
        return !TextUtils.isEmpty(password) && password.length > 4
    }
}


class ValidationResult{
    var isValid: Boolean = true
    var message: String = ""
}


enum class ValidatorManager(val instance: BaseValidator) {
    InsuranceCase(InsuranceCaseValidator()),
    IndividualClient(IndividualClientValidator()),
    LegalClient(LegalClientValidator()),
    InsuranceCategory(InsuranceCategoryValidator()),
    UserRegistration(UserRegistrationValidator());
}