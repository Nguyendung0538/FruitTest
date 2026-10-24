package com.example.fruittest.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.fruittest.data.DatabaseHelper
import com.example.fruittest.data.model.Fruit

class FruitDao(context: Context) {
    private val dbHelper = DatabaseHelper.getInstance(context)

    fun insert(fruit: Fruit): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.FRUIT_COL_NAME, fruit.name)
            put(DatabaseHelper.FRUIT_COL_CATEGORY, fruit.category)
            put(DatabaseHelper.FRUIT_COL_PRICE, fruit.price)
            put(DatabaseHelper.FRUIT_COL_QUANTITY, fruit.quantity)
            put(DatabaseHelper.FRUIT_COL_EXPIRY_DATE, fruit.expiryDate)
            put(DatabaseHelper.FRUIT_COL_DESCRIPTION, fruit.description)
        }
        return db.insert(DatabaseHelper.TABLE_FRUIT, null, values)
    }

    fun update(fruit: Fruit): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.FRUIT_COL_NAME, fruit.name)
            put(DatabaseHelper.FRUIT_COL_CATEGORY, fruit.category)
            put(DatabaseHelper.FRUIT_COL_PRICE, fruit.price)
            put(DatabaseHelper.FRUIT_COL_QUANTITY, fruit.quantity)
            put(DatabaseHelper.FRUIT_COL_EXPIRY_DATE, fruit.expiryDate)
            put(DatabaseHelper.FRUIT_COL_DESCRIPTION, fruit.description)
        }
        return db.update(
            DatabaseHelper.TABLE_FRUIT,
            values,
            "${DatabaseHelper.FRUIT_COL_ID} = ?",
            arrayOf(fruit.id.toString())
        )
    }

    fun delete(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            DatabaseHelper.TABLE_FRUIT,
            "${DatabaseHelper.FRUIT_COL_ID} = ?",
            arrayOf(id.toString())
        )
    }

    fun getById(id: Int): Fruit? {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_FRUIT} WHERE ${DatabaseHelper.FRUIT_COL_ID} = ?",
            arrayOf(id.toString())
        )
        return cursor.use {
            if (it.moveToFirst()) mapCursorToFruit(it) else null
        }
    }

    fun getAll(): List<Fruit> {
        val list = mutableListOf<Fruit>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_FRUIT} ORDER BY ${DatabaseHelper.FRUIT_COL_ID} DESC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(mapCursorToFruit(it))
            }
        }
        return list
    }

    fun search(keyword: String): List<Fruit> {
        val list = mutableListOf<Fruit>()
        val db = dbHelper.readableDatabase
        val query = "%$keyword%"
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_FRUIT} WHERE ${DatabaseHelper.FRUIT_COL_NAME} LIKE ? OR ${DatabaseHelper.FRUIT_COL_CATEGORY} LIKE ? ORDER BY ${DatabaseHelper.FRUIT_COL_ID} DESC",
            arrayOf(query, query)
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(mapCursorToFruit(it))
            }
        }
        return list
    }

    fun getAvailable(): List<Fruit> {
        val list = mutableListOf<Fruit>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_FRUIT} WHERE ${DatabaseHelper.FRUIT_COL_QUANTITY} > 0 ORDER BY ${DatabaseHelper.FRUIT_COL_ID} DESC",
            null
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(mapCursorToFruit(it))
            }
        }
        return list
    }

    fun searchAvailable(keyword: String): List<Fruit> {
        val list = mutableListOf<Fruit>()
        val db = dbHelper.readableDatabase
        val query = "%$keyword%"
        val cursor = db.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_FRUIT} WHERE ${DatabaseHelper.FRUIT_COL_QUANTITY} > 0 AND (${DatabaseHelper.FRUIT_COL_NAME} LIKE ? OR ${DatabaseHelper.FRUIT_COL_CATEGORY} LIKE ?) ORDER BY ${DatabaseHelper.FRUIT_COL_ID} DESC",
            arrayOf(query, query)
        )
        cursor.use {
            while (it.moveToNext()) {
                list.add(mapCursorToFruit(it))
            }
        }
        return list
    }

    private fun mapCursorToFruit(cursor: Cursor): Fruit {
        return Fruit(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_NAME)),
            category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_CATEGORY)),
            price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_PRICE)),
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_QUANTITY)),
            expiryDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_EXPIRY_DATE)),
            description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FRUIT_COL_DESCRIPTION))
        )
    }
}
