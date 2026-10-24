package com.example.fruittest.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.fruittest.data.DatabaseHelper
import com.example.fruittest.data.model.Account

class AccountDao(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insert(account: Account): Long {
        if (findByUsername(account.username) != null) return -1L

        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.ACCOUNT_COL_USERNAME, account.username)
            put(DatabaseHelper.ACCOUNT_COL_PASSWORD, account.password)
            put(DatabaseHelper.ACCOUNT_COL_ROLE, account.role)
        }
        return db.insert(DatabaseHelper.TABLE_ACCOUNT, null, values)
    }

    fun findByUsername(username: String): Account? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_ACCOUNT} WHERE ${DatabaseHelper.ACCOUNT_COL_USERNAME} = ?",
            arrayOf(username)
        )
        return cursor.use {
            if (it.moveToFirst()) {
                Account(
                    id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_ID)),
                    username = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_USERNAME)),
                    password = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_PASSWORD)),
                    role = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_ROLE))
                )
            } else null
        }
    }

    fun login(username: String, passwordHash: String): Account? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_ACCOUNT} WHERE ${DatabaseHelper.ACCOUNT_COL_USERNAME} = ? AND ${DatabaseHelper.ACCOUNT_COL_PASSWORD} = ?",
            arrayOf(username, passwordHash)
        )
        return cursor.use {
            if (it.moveToFirst()) {
                Account(
                    id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_ID)),
                    username = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_USERNAME)),
                    password = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_PASSWORD)),
                    role = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.ACCOUNT_COL_ROLE))
                )
            } else null
        }
    }
}
