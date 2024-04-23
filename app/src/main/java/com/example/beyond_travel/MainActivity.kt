package com.example.beyond_travel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReportAdapter
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        firestore = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerView)
        adapter = ReportAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

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

        findViewById<AppCompatImageButton>(R.id.buttonActivity5).setOnClickListener {
            startActivity(Intent(this, Info_Activity::class.java))
        }

        // Obtener y mostrar los reportes
        fetchReports()
    }

    private fun fetchReports() {
        GlobalScope.launch(Dispatchers.IO) {
            val reportsList = mutableListOf<Report>()

            // Obtener los documentos de la colección "reports" de Firestore
            firestore.collection("reports")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val data = document.data
                        val description = data["description"] as String
                        val latitude = data["latitude"] as Double
                        val longitude = data["longitude"] as Double
                        val imageUrl = data["imageUrl"] as String

                        val report = Report(description, latitude, longitude, imageUrl)
                        reportsList.add(report)
                    }
                    // Pasar la lista de reportes al adaptador para mostrarlos en el RecyclerView
                    runOnUiThread {
                        adapter.setData(reportsList)
                    }
                }
                .addOnFailureListener { exception ->
                    println("Error getting documents: $exception")
                }
        }
    }
}
