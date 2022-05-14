package com.example.myapplication.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.myapplication.MainApplication

private const val ACCESS_TOKEN = "access_token" // Token
private const val EXPIRES_IN = "expires_in" // Expiration data

object SharedPreferenceUtils {

    fun saveToken(accessToken: String, expiresIn: Int) {
        with(getSharedPreference().edit()) {
            putString(ACCESS_TOKEN, accessToken)
            putInt(EXPIRES_IN, ((System.currentTimeMillis() / 1000) + expiresIn).toInt())
            commit()
        }
    }

    fun getAccessToken(): String? {
        var accessToken: String? = null

        with(getSharedPreference()) {
            /* Verify if token exist */
            if (contains(ACCESS_TOKEN) && contains(EXPIRES_IN)) {
                val expiresIn = getInt(EXPIRES_IN, 0)

                /* Verify if a valid token*/
                if (expiresIn > (System.currentTimeMillis()) / 1000) {
                    accessToken = getString(ACCESS_TOKEN, "")
                }
            }
        }

        return accessToken
    }

    private fun getSharedPreference(): SharedPreferences {
        val context = MainApplication.getApplicationContext()
        return context.getSharedPreferences("main", Context.MODE_PRIVATE)
    }
}