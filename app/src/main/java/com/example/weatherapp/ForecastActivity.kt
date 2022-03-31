package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityForecastBinding
import com.example.weatherapp.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Forecast"

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
        }
        viewModel.loadData("54437")
    }

    private fun bindData(foreCast: Forecast) {
        binding.recyclerView.adapter = MyAdapter(foreCast.list)
    }
}