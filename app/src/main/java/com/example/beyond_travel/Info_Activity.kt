package com.example.beyond_travel

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.beyond_travel.databinding.ActivityInfoBinding

class Info_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar los listeners de clic para los botones de la botonera
        findViewById<AppCompatImageButton>(R.id.buttonActivity3).setOnClickListener {
            startActivity(Intent(this, Report_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity2).setOnClickListener {
            startActivity(Intent(this, Weather_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity4).setOnClickListener {
            startActivity(Intent(this, PQR_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Habilitar el modo de pantalla completa
        enableEdgeToEdge()

        // Establecer el padding para el contenido bajo las barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Asignar texto a los campos de texto
        binding.infoTextView.text = "Aquí puedes mostrar la información que desees."
        binding.contactTextView.text = "Contacto: example@example.com"
        binding.termsTextView.text = "Términos y Condiciones: Lorem ipsum dolor sit amet"
        binding.aboutTextView.text = "¿Qué es la aplicación?: Breve descripción de la aplicación."
    }
}
