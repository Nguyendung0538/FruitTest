package com.example.fruittest.ui.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fruittest.databinding.ActivityFruitFormBinding

class FruitFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFruitFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFruitFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
