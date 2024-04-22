package com.example.beyond_travel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Login_Activity : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var registroButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        // Inicializar vistas
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registroButton = findViewById(R.id.ResgistButton)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()
         registroButton.setOnClickListener {
            startActivity(Intent(this, Register_Activity::class.java))}
        // Configurar listener para el botón de inicio de sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Autenticar al usuario con Firebase
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // El usuario ha iniciado sesión correctamente
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // Finalizar LoginActivity para que no pueda volver atrás
                    } else {
                        // Si falla la autenticación, muestra un mensaje de error al usuario
                        Toast.makeText(this, "Error de inicio de sesión", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
