package com.example.fruittest.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.fruittest.data.DatabaseHelper
import com.example.fruittest.data.model.Order
import com.example.fruittest.util.Constants
import com.example.fruittest.util.DateUtil

class OrderDao(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun getAll(): List<Order> {
        val list = mutableListOf<Order>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_ORDERS} ORDER BY ${DatabaseHelper.ORDERS_COL_ID} DESC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(mapCursorToOrder(it))
            }
        }
        return list
    }

    fun getByCustomer(customerId: Int): List<Order> {
        val list = mutableListOf<Order>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_ORDERS} WHERE ${DatabaseHelper.ORDERS_COL_CUSTOMER_ID} = ? ORDER BY ${DatabaseHelper.ORDERS_COL_ID} DESC",
            arrayOf(customerId.toString())
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(mapCursorToOrder(it))
            }
        }
        return list
    }

    fun updateStatus(id: Int, newStatus: String): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.ORDERS_COL_STATUS, newStatus)
        }
        return db.update(
            DatabaseHelper.TABLE_ORDERS,
            values,
            "${DatabaseHelper.ORDERS_COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun placeOrder(customerId: Int, fruitId: Int, qty: Int): Long {
        if (qty <= 0) throw IllegalStateException("INVALID_QTY")

        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val cursor = db.rawQuery(
                "SELECT * FROM ${DatabaseHelper.TABLE_FRUIT} WHERE ${DatabaseHelper.FRUIT_COL_ID} = ?",
                arrayOf(fruitId.toString())
            )

            val fruitPrice: Double
            val currentQty: Int
            val expiryDate: String

            cursor.use {
                if (!it.moveToFirst()) throw IllegalStateException("NOT_FOUND")
                fruitPrice = it.getDouble(it.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_PRICE))
                currentQty = it.getInt(it.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_QUANTITY))
                expiryDate = it.getString(it.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_EXPIRY_DATE))
            }

            if (DateUtil.isExpired(expiryDate)) {
                throw IllegalStateException("EXPIRED")
            }

            if (currentQty < qty) {
                throw IllegalStateException("OUT_OF_STOCK")
            }

            val fruitValues = ContentValues().apply {
                put(DatabaseHelper.FRUIT_COL_QUANTITY, currentQty - qty)
            }
            db.update(
                DatabaseHelper.TABLE_FRUIT,
                fruitValues,
                "${DatabaseHelper.FRUIT_COL_ID} = ?",
                arrayOf(fruitId.toString())
            )

            val orderValues = ContentValues().apply {
                put(DatabaseHelper.ORDERS_COL_CUSTOMER_ID, customerId)
                put(DatabaseHelper.ORDERS_COL_ORDER_DATE, DateUtil.now())
                put(DatabaseHelper.ORDERS_COL_TOTAL_AMOUNT, fruitPrice * qty)
                put(DatabaseHelper.ORDERS_COL_STATUS, Constants.STATUS_PENDING)
            }

            val orderId = db.insert(DatabaseHelper.TABLE_ORDERS, null, orderValues)
            db.setTransactionSuccessful()
            return orderId
        } finally {
            db.endTransaction()
        }
    }

    private fun mapCursorToOrder(cursor: Cursor): Order {
        return Order(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ORDERS_COL_ID)),
            customerId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ORDERS_COL_CUSTOMER_ID)),
            orderDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ORDERS_COL_ORDER_DATE)),
            totalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.ORDERS_COL_TOTAL_AMOUNT)),
            status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ORDERS_COL_STATUS))
        )
    }
}
