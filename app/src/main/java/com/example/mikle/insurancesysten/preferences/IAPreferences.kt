package com.example.mikle.insurancesysten.preferences

import com.example.mikle.insurancesysten.InsuranceApplication

class IAPreferences {
    companion object {

        private val preferences = InsuranceApplication.preferences()
        val IS_LOGGED: IntKV = IntKV(-1, IAKey.IS_LOGGED)

        fun getString(key: StringKV): String? {
            return preferences.getString(key.key.name, key.default)
        }

        fun getBoolean(key: BoolKV): Boolean {
            return preferences.getBoolean(key.key.name, key.default)
        }

        fun getInt(key: IntKV): Int {
            return preferences.getInt(key.key.name, key.default)
        }

        fun put(key: IAKey, value: String) {
            preferences.edit().putString(key.name, value).apply()
        }

        fun put(key: IAKey, value: Boolean) {
            preferences.edit().putBoolean(key.name, value).apply()
        }

        fun put(key: IAKey, value: Int) {
            preferences.edit().putInt(key.name, value).apply()
        }
    }
}