package com.example.beyond_travel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Register_Activity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        // Inicializar vistas
        emailEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Configurar listener para el botón de registro
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Registrar al usuario con Firebase
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // El usuario se ha registrado correctamente
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, Login_Activity::class.java))
                        finish() // Finalizar RegisterActivity para que no pueda volver atrás
                    } else {
                        // Si falla el registro, muestra un mensaje de error al usuario
                        Toast.makeText(this, "Error de registro", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
