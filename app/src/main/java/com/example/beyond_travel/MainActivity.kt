package com.example.beyond_travel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        // Inicializar RecyclerView y adaptador
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PhotoAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener la lista de fotos y pasarla al adaptador
        val photosList = getPhotosList()
        adapter.setData(photosList)

        // Configurar los listeners de clic para los botones de la botonera
        findViewById<AppCompatImageButton>(R.id.buttonActivity3).setOnClickListener {
            startActivity(Intent(this, Report_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity2).setOnClickListener {
            startActivity(Intent(this, Weather_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity4).setOnClickListener {
            startActivity(Intent(this, Login_Activity::class.java))
        }

        findViewById<AppCompatImageButton>(R.id.buttonActivity5).setOnClickListener {
            startActivity(Intent(this, Info_Activity::class.java))
        }
    }

    // Método de ejemplo para obtener la lista de fotos desde Firestore u otra fuente de datos
    private fun getPhotosList(): List<Photo> {
        // Aquí deberías realizar la lógica para obtener la lista de fotos desde tu fuente de datos
        // y devolverla como una lista de objetos Photo
        // Por ejemplo:
        val photo1 = Photo("url_foto_1", "Descripción de la foto 1")
        val photo2 = Photo("url_foto_2", "Descripción de la foto 2")
        // Agregar más fotos si es necesario
        return listOf(photo1, photo2)
    }
}
