package com.spgroup.spapp.manager

import android.content.Context
import android.content.SharedPreferences

class AppConfigManager(context: Context) {

    companion object {
        const val PREF_NAME = "SPApp_Pref"
        const val KEY_ON_BOADING_SHOWN = "KEY_ON_BOADING_SHOWN"
    }

    private val mSharedPreferences: SharedPreferences

    init {
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
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

    ///////////////////////////////////////////////////////////////////////////
    // Public Function
    ///////////////////////////////////////////////////////////////////////////

    fun isOnBoadingShown() = getBoolean(KEY_ON_BOADING_SHOWN, false)

    fun setOnBoadingShown(shown: Boolean) {
        putBoolean(KEY_ON_BOADING_SHOWN, shown)
    }
}