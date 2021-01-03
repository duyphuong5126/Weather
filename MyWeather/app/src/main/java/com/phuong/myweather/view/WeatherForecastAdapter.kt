package com.phuong.myweather.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.phuong.myweather.R
import kotlinx.android.synthetic.main.item_weather_forecast.view.text_average_temp
import kotlinx.android.synthetic.main.item_weather_forecast.view.text_date
import kotlinx.android.synthetic.main.item_weather_forecast.view.text_description
import kotlinx.android.synthetic.main.item_weather_forecast.view.text_humidity
import kotlinx.android.synthetic.main.item_weather_forecast.view.text_pressure

class WeatherForecastAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val forecastList = ArrayList<WeatherForecastUiModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return WeatherForecastViewHolder(
            layoutInflater.inflate(
                R.layout.item_weather_forecast,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WeatherForecastViewHolder -> {
                holder.bindTo(forecastList[position])
            }

            else -> {
            }
        }
    }

    override fun getItemCount(): Int = forecastList.size

    fun updateWeatherForecast(weatherForecastList: List<WeatherForecastUiModel>) {
        val weatherForecastDiffUtil = WeatherForecastDiffUtil(forecastList, weatherForecastList)
        val diffResult = DiffUtil.calculateDiff(weatherForecastDiffUtil)
        forecastList.clear()
        forecastList.addAll(weatherForecastList)
        diffResult.dispatchUpdatesTo(this)
    }

    private class WeatherForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val date: TextView = itemView.text_date
        private val averageTemp: TextView = itemView.text_average_temp
        private val pressure: TextView = itemView.text_pressure
        private val humidity: TextView = itemView.text_humidity
        private val description: TextView = itemView.text_description

        fun bindTo(forecastUiModel: WeatherForecastUiModel) {
            val context = itemView.context
            date.text = context.getString(R.string.date_template, forecastUiModel.date)
            averageTemp.text =
                context.getString(
                    R.string.average_temp_template,
                    forecastUiModel.averageTemperature
                )
            pressure.text = context.getString(R.string.pressure_template, forecastUiModel.pressure)
            humidity.text = context.getString(R.string.humidity_template, forecastUiModel.humidity)
            description.text =
                context.getString(R.string.description_template, forecastUiModel.description)
        }
    }
}