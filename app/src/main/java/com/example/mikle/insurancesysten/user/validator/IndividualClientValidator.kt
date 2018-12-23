package com.example.mikle.insurancesysten.user.validator

import com.example.mikle.insurancesysten.base.validator.BaseValidator
import com.example.mikle.insurancesysten.base.validator.ValidationResult

class IndividualClientValidator : BaseValidator {
    override fun validate(params: List<String>): ValidationResult {

        val name = params[0]
        val birthDate = params[1]
        val sex = params[2]
        val drivingExperience = params[3]
        val address = params[4]
        val phone = params[5]
        val result = ValidationResult()

        if (!validateName(name)){
            result.isValid = false
            result.message = "Неверное название!\n"
        }
        if(!validateDate(birthDate)){
            result.isValid = false
            result.message = "Неверная дата рождения!\n"
        }
        if(!validateSex(sex)){
            result.isValid = false
            result.message += "Неверный пол\n"
            return result
        }
        if(!validateNumber(drivingExperience)){
            result.isValid = false
            result.message += "Неверный опыт вождения\n"
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