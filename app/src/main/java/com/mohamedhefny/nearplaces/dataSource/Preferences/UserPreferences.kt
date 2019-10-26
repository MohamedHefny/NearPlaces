package com.mohamedhefny.nearplaces.dataSource.Preferences

import android.content.Context
import androidx.preference.PreferenceManager

object UserPreferences {

    fun saveUpdateMode(context: Context, updateMode: Int) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putInt("updateMode", updateMode).apply()
    }

    fun getUpdateMode(context: Context) =
        PreferenceManager.getDefaultSharedPreferences(context)
            .getInt("updateMode", 0)
}