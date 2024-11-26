package com.example.ballonapppractice

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ballonapppractice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var balloonsManager: BalloonsManager
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        balloonsManager = BalloonsManager(this, binding.main)
        balloonsManager.createBalloons(
                count = 25,           // Number of balloons
                balloonWidth = 87,  // Width in pixels
                balloonHeight = 87, // Height in pixels
                speed = 5f           // Movement speed
            )
    }
}