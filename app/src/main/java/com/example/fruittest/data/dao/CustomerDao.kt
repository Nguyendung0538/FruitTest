package com.example.fruittest.data.dao

import android.content.ContentValues
import android.content.Context
import com.example.fruittest.data.DatabaseHelper
import com.example.fruittest.data.model.Customer

class CustomerDao(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insert(customer: Customer): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.CUSTOMER_COL_FULLNAME, customer.fullname)
            put(DatabaseHelper.CUSTOMER_COL_PHONE, customer.phone)
            put(DatabaseHelper.CUSTOMER_COL_EMAIL, customer.email)
            put(DatabaseHelper.CUSTOMER_COL_ACCOUNT_ID, customer.accountId)
        }
        return db.insert(DatabaseHelper.TABLE_CUSTOMER, null, values)
    }

    fun findByAccountId(accountId: Int): Customer? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_CUSTOMER} WHERE ${DatabaseHelper.CUSTOMER_COL_ACCOUNT_ID} = ?",
            arrayOf(accountId.toString())
        )
        return cursor.use {
            if (it.moveToFirst()) {
                Customer(
                    id = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_COL_ID)),
                    fullname = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_COL_FULLNAME)),
                    phone = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_COL_PHONE)),
                    email = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_COL_EMAIL)),
                    accountId = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.CUSTOMER_COL_ACCOUNT_ID))
                )
            } else null
        }
    }
}
