package com.example.beyond_travel

import android.content.pm.PackageManager
import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class Weather_Activity : AppCompatActivity() {

    private lateinit var textViewAgudas: TextView
    private lateinit var textViewCaldas: TextView

    private val apiKey = "e765798af61479b779c883433f2ca596"
    private val internetPermissionCode = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        textViewAgudas = findViewById(R.id.textViewAguadas)

        if (hasInternetPermission()) {
            getWeatherData()
        } else {
            requestInternetPermission()
        }
         findViewById<AppCompatImageButton>(R.id.buttonActivity1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            }
        findViewById<AppCompatImageButton>(R.id.buttonActivity3).setOnClickListener {
            startActivity(Intent(this, Report_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity2).setOnClickListener {
            startActivity(Intent(this, Weather_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity4).setOnClickListener {
            startActivity(Intent(this, PQR_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity5).setOnClickListener {
            startActivity(Intent(this, Info_Activity::class.java))
        }
    }

    private fun hasInternetPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestInternetPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), internetPermissionCode)
    }

    private fun getWeatherData() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.openweathermap.org/data/2.5/weather?q=Aguadas,Caldas,CO&appid=$apiKey")
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            val weatherData = parseWeatherData(responseBody)
            updateUI(weatherData)
        }
    }

    private fun parseWeatherData(responseBody: String?): WeatherData {
        val jsonObject = JSONObject(responseBody)
        val temperatureKelvin = jsonObject.getJSONObject("main").getDouble("temp")
        val temperatureCelsius = temperatureKelvin - 273.15
        val description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
        return WeatherData(temperatureCelsius, description)
    }

    private fun updateUI(weatherData: WeatherData) {
        runOnUiThread {
            val formattedTemperature = String.format("%.0f", weatherData.temperature)
            textViewAgudas.text = "Temperatura en Aguadas: $formattedTemperature°C, ${weatherData.description}"
        }
    }
}

data class WeatherData(val temperature: Double, val description: String)
