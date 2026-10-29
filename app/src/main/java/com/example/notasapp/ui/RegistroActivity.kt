package com.example.notasapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notasapp.R
import com.example.notasapp.viewmodel.LoginViewModel

class RegistroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        val nombre = findViewById<EditText>(R.id.regNombre)
        val correo = findViewById<EditText>(R.id.regCorreo)
        val clave = findViewById<EditText>(R.id.regClave)
        val boton = findViewById<Button>(R.id.registrar)
        val barra = findViewById<ProgressBar>(R.id.regCarga)
        boton.setOnClickListener {
            if (nombre.text.length < 2 || correo.text.isBlank() || clave.text.length < 8) {
                Toast.makeText(this, "Escribe nombre, correo y clave de 8 caracteres", Toast.LENGTH_LONG).show()
            } else {
                viewModel.registrar(nombre.text.toString(), correo.text.toString().trim(), clave.text.toString())
            }
        }
        viewModel.registro.observe(this) { estado ->
            barra.visibility = if (estado.cargando) View.VISIBLE else View.GONE
            boton.isEnabled = !estado.cargando
            if (estado.error != null) Toast.makeText(this, estado.error, Toast.LENGTH_LONG).show()
            if (estado.dato != null) {
                Toast.makeText(this, "Cuenta creada, ahora inicia sesión", Toast.LENGTH_LONG).show()
                finish()
            }
        }
        findViewById<Button>(R.id.volverLogin).setOnClickListener { finish() }
    }
}
