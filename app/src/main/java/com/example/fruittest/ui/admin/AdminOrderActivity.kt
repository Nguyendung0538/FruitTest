package com.example.fruittest.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fruittest.databinding.ActivityAdminOrderBinding

class AdminOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminOrderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
