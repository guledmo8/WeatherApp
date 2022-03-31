package com.example.weatherapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityForecastBinding
import com.example.weatherapp.databinding.FragmentForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast) {
    private val args by navArgs<ForecastFragmentArgs>()
    private lateinit var binding: FragmentForecastBinding
    @Inject lateinit var viewModel: ForecastViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForecastBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
        }

        try {
            viewModel.loadData(args.zipCode)
        } catch (exception: HttpException) {
            ErrorDialogFragment().show(childFragmentManager, "")
        }
    }

    private fun bindData(foreCast: Forecast) {
        binding.recyclerView.adapter = MyAdapter(foreCast.list)
    }

}