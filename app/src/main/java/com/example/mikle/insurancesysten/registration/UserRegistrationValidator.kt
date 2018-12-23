package com.example.mikle.insurancesysten.registration

import android.text.TextUtils
import com.example.mikle.insurancesysten.base.validator.BaseValidator
import com.example.mikle.insurancesysten.base.validator.ValidationResult

class UserRegistrationValidator: BaseValidator {


    override fun validate(params: List<String>): ValidationResult {
        val name = params[0]
        val surname = params[1]
        val fatherName = params[2]
        val email = params[3]
        val password = params[4]
        val repeatPassword = params[5]
        val result = ValidationResult()
        if (!validateName(name)){
            result.isValid = false
            result.message = "Неправильное имя!"
        }
        if (!validateName(surname)){
            result.isValid = false
            result.message += "\nНеправильная фамилия!"
        }
        if (!validateName(fatherName)){
            result.isValid = false
            result.message += "\nНеправильное отчество!"
        }
        if (!validateEmail(email)){
            result.isValid = false
            result.message += "\nНеправильный e-email!"
        }
        if (!validatePas(password)){
            result.isValid = false
            result.message += "\nПароль должен быть длинее 4-х символов!"
        }

        if (password != repeatPassword){
            result.isValid = false
            result.message += "\nПароли не совпадают!"
        }
        return result
    }
}