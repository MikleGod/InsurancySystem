package com.example.mikle.insurancesysten.user.validator

import com.example.mikle.insurancesysten.base.validator.BaseValidator
import com.example.mikle.insurancesysten.base.validator.ValidationResult
import java.lang.Exception

class LegalClientValidator : BaseValidator {

    override fun validate(params: List<String>): ValidationResult {
        val name = params[0]
        val uniqueNumber = params[1]
        val director = params[2]
        val accountant = params[3]
        val address = params[4]
        val phone = params[5]
        val result = ValidationResult()

        if (!validateName(name)){
            result.isValid = false
            result.message = "Неверное название!\n"
        }
        if(!validateNumber(uniqueNumber)){
            result.isValid = false
            result.message = "Неверный уникальный номер!\n"
        }
        if(!validateManName(director)){
            result.isValid = false
            result.message += "Неверное имя директора\n"
            return result
        }
        if(!validateManName(accountant)){
            result.isValid = false
            result.message += "Неверное имя бухгалтера\n"
            return result
        }
        if (!validateAddress(address)){
            result.isValid = false
            result.message += "Неверный адрес!\n"
        }
        if (!validatePhone(phone)){
            result.isValid = false
            result.message += "Неверный телефон!\n"
        }
        return result
    }
}