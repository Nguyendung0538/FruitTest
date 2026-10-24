package com.example.fruittest.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.fruittest.util.Constants
import com.example.fruittest.util.DateUtil
import com.example.fruittest.util.HashUtil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "fruit_shop.db"
        private const val DATABASE_VERSION = 1

        // Table Account
        const val TABLE_ACCOUNT = "Account"
        const val ACCOUNT_COL_ID = "id"
        const val ACCOUNT_COL_USERNAME = "username"
        const val ACCOUNT_COL_PASSWORD = "password"
        const val ACCOUNT_COL_ROLE = "role"

        // Table Customer
        const val TABLE_CUSTOMER = "Customer"
        const val CUSTOMER_COL_ID = "id"
        const val CUSTOMER_COL_FULLNAME = "fullname"
        const val CUSTOMER_COL_PHONE = "phone"
        const val CUSTOMER_COL_EMAIL = "email"
        const val CUSTOMER_COL_ACCOUNT_ID = "accountId"

        // Table Fruit
        const val TABLE_FRUIT = "Fruit"
        const val FRUIT_COL_ID = "id"
        const val FRUIT_COL_NAME = "name"
        const val FRUIT_COL_CATEGORY = "category"
        const val FRUIT_COL_PRICE = "price"
        const val FRUIT_COL_QUANTITY = "quantity"
        const val FRUIT_COL_EXPIRY_DATE = "expiryDate"
        const val FRUIT_COL_DESCRIPTION = "description"

        // Table Orders
        const val TABLE_ORDERS = "Orders"
        const val ORDERS_COL_ID = "id"
        const val ORDERS_COL_CUSTOMER_ID = "customerId"
        const val ORDERS_COL_ORDER_DATE = "orderDate"
        const val ORDERS_COL_TOTAL_AMOUNT = "totalAmount"
        const val ORDERS_COL_STATUS = "status"

        @Volatile
        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createAccountTable = """
            CREATE TABLE $TABLE_ACCOUNT (
                $ACCOUNT_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $ACCOUNT_COL_USERNAME TEXT NOT NULL UNIQUE,
                $ACCOUNT_COL_PASSWORD TEXT NOT NULL,
                $ACCOUNT_COL_ROLE TEXT NOT NULL
            );
        """.trimIndent()

        val createCustomerTable = """
            CREATE TABLE $TABLE_CUSTOMER (
                $CUSTOMER_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $CUSTOMER_COL_FULLNAME TEXT NOT NULL,
                $CUSTOMER_COL_PHONE TEXT,
                $CUSTOMER_COL_EMAIL TEXT,
                $CUSTOMER_COL_ACCOUNT_ID INTEGER NOT NULL REFERENCES $TABLE_ACCOUNT($ACCOUNT_COL_ID) ON DELETE CASCADE
            );
        """.trimIndent()

        val createFruitTable = """
            CREATE TABLE $TABLE_FRUIT (
                $FRUIT_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $FRUIT_COL_NAME TEXT NOT NULL,
                $FRUIT_COL_CATEGORY TEXT,
                $FRUIT_COL_PRICE REAL NOT NULL,
                $FRUIT_COL_QUANTITY INTEGER NOT NULL DEFAULT 0,
                $FRUIT_COL_EXPIRY_DATE TEXT NOT NULL,
                $FRUIT_COL_DESCRIPTION TEXT
            );
        """.trimIndent()

        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                $ORDERS_COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $ORDERS_COL_CUSTOMER_ID INTEGER NOT NULL REFERENCES $TABLE_CUSTOMER($CUSTOMER_COL_ID) ON DELETE CASCADE,
                $ORDERS_COL_ORDER_DATE TEXT NOT NULL,
                $ORDERS_COL_TOTAL_AMOUNT REAL NOT NULL,
                $ORDERS_COL_STATUS TEXT NOT NULL DEFAULT '${Constants.STATUS_PENDING}'
            );
        """.trimIndent()

        db.execSQL(createAccountTable)
        db.execSQL(createCustomerTable)
        db.execSQL(createFruitTable)
        db.execSQL(createOrdersTable)

        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOMER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FRUIT")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ACCOUNT")
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        // 1. Seed Accounts
        db.insert(TABLE_ACCOUNT, null, ContentValues().apply {
            put(ACCOUNT_COL_USERNAME, "admin")
            put(ACCOUNT_COL_PASSWORD, HashUtil.sha256("admin123"))
            put(ACCOUNT_COL_ROLE, Constants.ROLE_ADMIN)
        })

        val customerAccountId = db.insert(TABLE_ACCOUNT, null, ContentValues().apply {
            put(ACCOUNT_COL_USERNAME, "customer")
            put(ACCOUNT_COL_PASSWORD, HashUtil.sha256("123456"))
            put(ACCOUNT_COL_ROLE, Constants.ROLE_CUSTOMER)
        })

        // 2. Seed Customer record linked to customerAccountId
        if (customerAccountId != -1L) {
            db.insert(TABLE_CUSTOMER, null, ContentValues().apply {
                put(CUSTOMER_COL_FULLNAME, "Nguyễn Văn A")
                put(CUSTOMER_COL_PHONE, "0901234567")
                put(CUSTOMER_COL_EMAIL, "customer@gmail.com")
                put(CUSTOMER_COL_ACCOUNT_ID, customerAccountId.toInt())
            })
        }

        // 3. Seed Fruits (Calculate relative dates for reliable demo)
        val sdf = SimpleDateFormat(DateUtil.DATE_FORMAT, Locale.US)

        val calNormal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 30) }
        val dateNormal = sdf.format(calNormal.time)

        val calExpiringSoon = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 2) }
        val dateExpiringSoon = sdf.format(calExpiringSoon.time)

        val calExpired = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -2) }
        val dateExpired = sdf.format(calExpired.time)

        val fruits = listOf(
            ContentValues().apply {
                put(FRUIT_COL_NAME, "Táo Envy Mỹ")
                put(FRUIT_COL_CATEGORY, "Nhập khẩu")
                put(FRUIT_COL_PRICE, 85000.0)
                put(FRUIT_COL_QUANTITY, 50)
                put(FRUIT_COL_EXPIRY_DATE, dateNormal)
                put(FRUIT_COL_DESCRIPTION, "Táo Envy mọng nước, giòn ngọt tự nhiên.")
            },
            ContentValues().apply {
                put(FRUIT_COL_NAME, "Cam Sành Tiền Giang")
                put(FRUIT_COL_CATEGORY, "Trái cây Việt")
                put(FRUIT_COL_PRICE, 35000.0)
                put(FRUIT_COL_QUANTITY, 100)
                put(FRUIT_COL_EXPIRY_DATE, dateNormal)
                put(FRUIT_COL_DESCRIPTION, "Cam sành mọng nước, nhiều vitamin C.")
            },
            ContentValues().apply {
                put(FRUIT_COL_NAME, "Chuối Laba Đà Lạt")
                put(FRUIT_COL_CATEGORY, "Trái cây Việt")
                put(FRUIT_COL_PRICE, 25000.0)
                put(FRUIT_COL_QUANTITY, 80)
                put(FRUIT_COL_EXPIRY_DATE, dateNormal)
                put(FRUIT_COL_DESCRIPTION, "Chuối Laba dẻo thơm chuẩn vị Đà Lạt.")
            },
            ContentValues().apply {
                put(FRUIT_COL_NAME, "Dưa Hấu Không Hạt")
                put(FRUIT_COL_CATEGORY, "Trái cây Việt")
                put(FRUIT_COL_PRICE, 20000.0)
                put(FRUIT_COL_QUANTITY, 30)
                put(FRUIT_COL_EXPIRY_DATE, dateExpiringSoon) // Sắp hết hạn
                put(FRUIT_COL_DESCRIPTION, "Dưa hấu mọng nước, giá ưu đãi (sắp hết hạn).")
            },
            ContentValues().apply {
                put(FRUIT_COL_NAME, "Nho Mẫu Đơn Hàn Quốc")
                put(FRUIT_COL_CATEGORY, "Nhập khẩu")
                put(FRUIT_COL_PRICE, 250000.0)
                put(FRUIT_COL_QUANTITY, 15)
                put(FRUIT_COL_EXPIRY_DATE, dateExpired) // Đã hết hạn
                put(FRUIT_COL_DESCRIPTION, "Nho mẫu đơn thơm ngọt (đã quá hạn sử dụng).")
            },
            ContentValues().apply {
                put(FRUIT_COL_NAME, "Xoài Cát Hòa Lộc")
                put(FRUIT_COL_CATEGORY, "Trái cây Việt")
                put(FRUIT_COL_PRICE, 60000.0)
                put(FRUIT_COL_QUANTITY, 0) // Hết hàng
                put(FRUIT_COL_EXPIRY_DATE, dateNormal)
                put(FRUIT_COL_DESCRIPTION, "Xoài cát Hòa Lộc chín vàng thơm lừng (hiện hết hàng).")
            }
        )

        for (fruit in fruits) {
            db.insert(TABLE_FRUIT, null, fruit)
        }
    }
}
