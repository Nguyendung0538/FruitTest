package com.example.fruittest.ui.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fruittest.databinding.ActivityCustomerMainBinding

class CustomerMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
