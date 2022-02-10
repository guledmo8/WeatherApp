package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // lateinit var conditionIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

     //   conditionIcon = findViewById(R.id.condition_icon)
        forecastButton.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }
}