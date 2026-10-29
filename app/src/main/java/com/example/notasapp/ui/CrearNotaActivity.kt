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
import com.example.notasapp.viewmodel.NotasViewModel

class CrearNotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_nota)
        val viewModel = ViewModelProvider(this)[NotasViewModel::class.java]
        val titulo = findViewById<EditText>(R.id.notaTitulo)
        val contenido = findViewById<EditText>(R.id.notaContenido)
        val boton = findViewById<Button>(R.id.guardarNota)
        val barra = findViewById<ProgressBar>(R.id.guardarCarga)
        boton.setOnClickListener {
            if (titulo.text.isBlank() || contenido.text.isBlank()) {
                Toast.makeText(this, "Completa el título y contenido", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.guardar(titulo.text.toString(), contenido.text.toString())
            }
        }
        findViewById<Button>(R.id.volverDashboard).setOnClickListener { finish() }
        viewModel.crear.observe(this) { estado ->
            barra.visibility = if (estado.cargando) View.VISIBLE else View.GONE
            boton.isEnabled = !estado.cargando
            if (estado.error != null) Toast.makeText(this, estado.error, Toast.LENGTH_LONG).show()
            if (estado.dato != null) {
                Toast.makeText(this, "Nota guardada correctamente", Toast.LENGTH_LONG).show()
                titulo.text.clear()
                contenido.text.clear()
            }
        }
    }
}
