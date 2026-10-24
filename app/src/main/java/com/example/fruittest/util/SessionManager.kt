package com.example.fruittest.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "fruit_shop_session"

        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_ROLE = "role"
        private const val KEY_CUSTOMER_ID = "customer_id"

        private const val KEY_REMEMBER_USER = "remember_user"
        private const val KEY_REMEMBER_PASS = "remember_pass"
    }

    fun saveSession(userId: Int, username: String, role: String, customerId: Int? = null) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_ROLE, role)
            putInt(KEY_CUSTOMER_ID, customerId ?: -1)
            apply()
        }
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, -1)

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun getRole(): String? = prefs.getString(KEY_ROLE, null)

    fun getCustomerId(): Int = prefs.getInt(KEY_CUSTOMER_ID, -1)

    fun saveRemember(username: String, pass: String) {
        prefs.edit().apply {
            putString(KEY_REMEMBER_USER, username)
            putString(KEY_REMEMBER_PASS, pass)
            apply()
        }
    }

    fun clearRemember() {
        prefs.edit().apply {
            remove(KEY_REMEMBER_USER)
            remove(KEY_REMEMBER_PASS)
            apply()
        }
    }

    fun getRememberUser(): String? = prefs.getString(KEY_REMEMBER_USER, null)

    fun getRememberPass(): String? = prefs.getString(KEY_REMEMBER_PASS, null)

    fun logout() {
        prefs.edit().apply {
            remove(KEY_IS_LOGGED_IN)
            remove(KEY_USER_ID)
            remove(KEY_USERNAME)
            remove(KEY_ROLE)
            remove(KEY_CUSTOMER_ID)
            apply()
        }
    }
}
