package com.example.beyond_travel

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class PQR_Activity : AppCompatActivity() {
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var submitBtn: Button
    private lateinit var pqrsEditText: EditText
    private lateinit var pqrsTypeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_pqr)
// Configurar los listeners de clic para los botones de la botonera
        findViewById<AppCompatImageButton>(R.id.buttonActivity3).setOnClickListener {
            startActivity(Intent(this, Report_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity2).setOnClickListener {
            startActivity(Intent(this, Weather_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity1).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity5).setOnClickListener {
            startActivity(Intent(this, Info_Activity::class.java))
        }
        // Referencias a los componentes de la interfaz de usuario
        submitBtn = findViewById(R.id.submitBtn)
        pqrsEditText = findViewById(R.id.pqrEditText)
        pqrsTypeSpinner = findViewById(R.id.spinner)

        // Configuración del adaptador para el Spinner
        val pqrsOptions = listOf("Petición", "Queja", "Reclamo","Sugerencia")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, pqrsOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        pqrsTypeSpinner.adapter = adapter

        // Listener del botón de envío
        submitBtn.setOnClickListener {
            val pqrsContent = pqrsEditText.text.toString().trim()
            val pqrsType = pqrsTypeSpinner.selectedItem.toString()

            if (pqrsContent.isNotEmpty()) {
                uploadPQRS(pqrsType, pqrsContent)
            } else {
                // Si el contenido está vacío, muestra un mensaje de error
                Toast.makeText(this, "Por favor, ingresa tu petición, queja o reclamo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Método para subir la PQRS a Firestore
    private fun uploadPQRS(pqrsType: String, pqrsContent: String) {
        val pqrs = hashMapOf(
            "type" to pqrsType,
            "content" to pqrsContent
        )

        firestore.collection("pqrs")
            .add(pqrs)
            .addOnSuccessListener {
                // Éxito al subir a Firestore
                Toast.makeText(this, "PQRS enviada exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Error al subir a Firestore
                Toast.makeText(this, "Error al enviar la PQRS", Toast.LENGTH_SHORT).show()
            }
    }
}
