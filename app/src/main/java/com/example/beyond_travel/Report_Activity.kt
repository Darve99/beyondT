package com.example.beyond_travel

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.net.wifi.hotspot2.pps.HomeSp
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class Report_Activity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var descriptionEditText: EditText
    private lateinit var takePhotoButton: Button
    private lateinit var saveButton: Button
    private lateinit var homeButton: AppCompatImageButton
    private val CAMERA_PERMISSION_CODE = 101
    private val IMAGE_CAPTURE_CODE = 102
    private var imageUri: Uri? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)

        imageView = findViewById(R.id.imageView)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        takePhotoButton = findViewById(R.id.takePhotoButton)
        saveButton = findViewById(R.id.saveButton)
        homeButton = findViewById(R.id.buttonActivity1)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        storageReference = FirebaseStorage.getInstance().reference

        takePhotoButton.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }
        // Configurar los listeners de clic para los botones de la botonera
        findViewById<AppCompatImageButton>(R.id.buttonActivity1).setOnClickListener {
            startActivity(Intent(this,  MainActivity::class.java))
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
        saveButton.setOnClickListener {
            saveReport()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imageView.setImageURI(imageUri)
        }
    }

    private fun saveReport() {
        val description = descriptionEditText.text.toString().trim()
        if (description.isEmpty()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }
        if (imageUri != null) {
            try {
                // Obtener la imagen en forma de Bitmap
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

                // Convertir la imagen a un array de bytes para almacenarla en Firebase Storage
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val imageData: ByteArray = baos.toByteArray()

                // Obtener la ubicación actual
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
                    return
                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                    // Guardar la imagen en Firebase Storage
                    saveToStorage(imageData, description, location)
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to get location: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to save report: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "No photo taken", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToStorage(imageData: ByteArray, description: String, location: Location?) {
        val imagesRef = storageReference.child("images/${UUID.randomUUID()}.jpg")

        imagesRef.putBytes(imageData)
            .addOnSuccessListener { taskSnapshot ->
                // Obtiene la URL de la imagen cargada en Firebase Storage
                imagesRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    // Guardar la URL de la imagen junto con la descripción y la ubicación en Firestore
                    saveToFirestore(description, location, imageUrl)
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to get image URL: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to upload image to storage: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveToFirestore(description: String, location: Location?, imageUrl: String) {
        val firestore = FirebaseFirestore.getInstance()
        val reportsCollection = firestore.collection("reports")

        val reportData = hashMapOf(
            "description" to description,
            "latitude" to location?.latitude,
            "longitude" to location?.longitude,
            "imageUrl" to imageUrl
        )

        reportsCollection.add(reportData)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Report saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save report to Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
