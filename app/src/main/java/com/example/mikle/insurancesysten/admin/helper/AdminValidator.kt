package com.example.mikle.insurancesysten.admin.helper

import android.text.TextUtils
import com.example.mikle.insurancesysten.base.validator.BaseValidator
import com.example.mikle.insurancesysten.base.validator.ValidationResult
import java.lang.Exception

class InsuranceCaseValidator: BaseValidator {

    override fun validate(params: List<String>): ValidationResult {
        val name: String = params[0]
        val percent: String = params[1]
        val result = ValidationResult()
        if (!validate(name)){
            result.isValid = false
            result.message = "Неверное название! "
        }
        var intPer: Int? = null
        try {
            intPer = percent.toInt()
        } catch (e: Exception){
            result.isValid = false
            result.message += "Неверный формат процентов выплаты! "
            return result
        }
        if (!validate(intPer)){
            result.isValid = false
            result.message += "Процент выплаты может быть равен от 1 до 25!"
        }
        return result
    }

    private fun validate(name: String): Boolean{
        return !TextUtils.isEmpty(name)
    }

    private fun validate(percent: Int): Boolean{
        return percent in 1..25
    }
}

class InsuranceCategoryValidator: BaseValidator {

    override fun validate(params: List<String>): ValidationResult {
        val name: String = params[0]
        val maxCost: String = params[1]
        val duration: String = params[2]
        val result = ValidationResult()
        if (!validate(name)){
            result.isValid = false
            result.message = "Неверное название!\n"
        }
        val intMaxCost: Int
        try {
            intMaxCost = maxCost.toInt()
        } catch (e: Exception){
            result.isValid = false
            result.message += "Неверный формат максимальной цены!\n"
            return result
        }
        val intDuration: Int
        try {
            intDuration = duration.toInt()
        } catch (e: Exception){
            result.isValid = false
            result.message += "Неверный формат длительности договора\n"
            return result
        }
        if (!validate(intMaxCost)){
            result.isValid = false
            result.message += "Максимальная цена может быть от 1000 до 25000!\n"
        }
        if (!validateD(intDuration)){
            result.isValid = false
            result.message += "Длительность может быть от 30 до 365 суток!\n"
        }
        return result
    }

    private fun validate(name: String): Boolean{
        return !TextUtils.isEmpty(name)
    }

    private fun validate(maxCost: Int): Boolean{
        return maxCost in 1000..25000
    }


    private fun validateD(duration: Int): Boolean{
        return duration in 30..365
    }
}