package com.example.notasapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notasapp.R
import com.example.notasapp.sesion.Sesion
import com.example.notasapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val correo = findViewById<EditText>(R.id.loginCorreo)
        val clave = findViewById<EditText>(R.id.loginClave)
        val boton = findViewById<Button>(R.id.botonLogin)
        val barra = findViewById<ProgressBar>(R.id.loginCarga)

        boton.setOnClickListener {
            if (correo.text.isBlank() || clave.text.isBlank()) {
                Toast.makeText(this, "Completa correo y contraseña", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.entrar(correo.text.toString().trim(), clave.text.toString())
            }
        }
        findViewById<Button>(R.id.irRegistro).setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
        viewModel.login.observe(this) { estado ->
            barra.visibility = if (estado.cargando) View.VISIBLE else View.GONE
            boton.isEnabled = !estado.cargando
            if (estado.error != null) Toast.makeText(this, estado.error, Toast.LENGTH_LONG).show()
            if (estado.dato != null) {
                Sesion.guardar(this, estado.dato)
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            }
        }
    }
}
