package com.phuong.myweather.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL as VERTICAL_DIVIDER
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.phuong.myweather.R
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.presentation.viewmodel.WeatherForecastViewModel
import com.phuong.myweather.presentation.viewmodel.WeatherForecastViewModelImpl
import com.phuong.myweather.view.WeatherForecastAdapter
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherForecastViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var searchLayout: TextInputLayout
    private lateinit var searchInput: TextInputEditText
    private lateinit var daysRangeLayout: TextInputLayout
    private lateinit var daysRangeInput: TextInputEditText
    private lateinit var buttonGetWeather: Button
    private lateinit var weatherForecastList: RecyclerView
    private lateinit var searchError: TextView

    private var weatherForecastAdapter: WeatherForecastAdapter? = null

    private var daysRange = DEFAULT_DAYS_RANGE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpDI()
        setUpUI()

        viewModel.weatherForecastLiveData.observe(this, {
            weatherForecastAdapter?.updateWeatherForecast(it)
        })
        viewModel.searchValidationErrorLiveData.observe(this, {
            searchLayout.error = it
        })
        viewModel.daysRangeErrorLiveData.observe(this, {
            daysRangeLayout.error = it
        })
        viewModel.searchErrorLiveData.observe(this, {
            if (it != null) {
                searchError.visibility = View.VISIBLE
                searchError.text = it
            } else {
                searchError.visibility = View.GONE
                searchError.text = ""
            }
        })
    }

    private fun setUpUI() {
        searchLayout = findViewById(R.id.layout_search)
        searchInput = findViewById(R.id.input_search)
        daysRangeLayout = findViewById(R.id.layout_days_range)
        daysRangeInput = findViewById(R.id.input_days_range)
        buttonGetWeather = findViewById(R.id.button_get_weather)
        weatherForecastList = findViewById(R.id.list_weather_forecast)
        searchError = findViewById(R.id.text_error)

        buttonGetWeather.setOnClickListener {
            viewModel.getWeatherForecast(searchInput.text?.toString().orEmpty(), daysRange, Celsius)
        }

        daysRangeInput.setText("$DEFAULT_DAYS_RANGE")
        daysRangeInput.addTextChangedListener(afterTextChanged = {
            daysRange = it?.toString()?.toIntOrNull() ?: DEFAULT_DAYS_RANGE
        })

        weatherForecastAdapter = WeatherForecastAdapter()
        weatherForecastList.adapter = weatherForecastAdapter
        weatherForecastList.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        weatherForecastList.addItemDecoration(DividerItemDecoration(this, VERTICAL_DIVIDER))
    }

    private fun setUpDI() {
        AndroidInjection.inject(this)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory)[WeatherForecastViewModelImpl::class.java]
    }

    companion object {
        private const val DEFAULT_DAYS_RANGE = 7
    }
}