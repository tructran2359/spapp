package com.spgroup.spapp.manager

import android.content.SharedPreferences
import com.google.gson.Gson
import com.spgroup.spapp.domain.model.ContactInfo

class AppConfigManager(
        private val mSharedPreferences: SharedPreferences,
        private val mGson: Gson
) {

    companion object {
        const val KEY_ON_BOADING_SHOWN = "KEY_ON_BOADING_SHOWN"
        const val KEY_REMEMBER_ME = "KEY_REMEMBER_ME"
        const val KEY_CONTACT_INFO = "KEY_CONTACT_INFO"
    }

    private fun putInt(key: String, value: Int) {
        mSharedPreferences.edit().putInt(key, value).apply()
    }

    private fun getInt(key: String, defaultValue: Int) = mSharedPreferences.getInt(key, defaultValue)

    private fun putBoolean(key: String, value: Boolean) {
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    private fun getBoolean(key: String, defaultValue: Boolean) = mSharedPreferences.getBoolean(key, defaultValue)

    private fun putString(key: String, value: String) {
        mSharedPreferences.edit().putString(key, value).apply()
    }

    private fun getString(key: String, defaultValue: String) = mSharedPreferences.getString(key, defaultValue)

    private fun clearKey(key: String) {
        mSharedPreferences.edit().remove(key).apply()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Public Function
    ///////////////////////////////////////////////////////////////////////////

    fun isOnBoadingShown() = getBoolean(KEY_ON_BOADING_SHOWN, false)

    fun setOnBoadingShown(shown: Boolean) {
        putBoolean(KEY_ON_BOADING_SHOWN, shown)
    }

    fun setRememberMe(contactInfo: ContactInfo) {
        putBoolean(KEY_REMEMBER_ME, true)
        val jsonString = mGson.toJson(contactInfo) ?: ""
        putString(KEY_CONTACT_INFO, jsonString)
    }

    fun clearRememberMe() {
        putBoolean(KEY_REMEMBER_ME, false)
        clearKey(KEY_CONTACT_INFO)
    }

    fun isRemembered() = getBoolean(KEY_REMEMBER_ME, false)

    fun getSavedContactInfo(): ContactInfo? {
        val jsonString = getString(KEY_CONTACT_INFO, "")
        if (jsonString == null || jsonString.isEmpty()) {
            return null
        }

        return mGson.fromJson(jsonString, ContactInfo::class.java)
    }
}