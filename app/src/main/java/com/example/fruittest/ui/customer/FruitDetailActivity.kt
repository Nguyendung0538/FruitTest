package com.example.fruittest.ui.customer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fruittest.databinding.ActivityFruitDetailBinding

class FruitDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFruitDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
