package com.example.notasapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notasapp.R
import com.example.notasapp.viewmodel.NotasViewModel

class ListadoActivity : AppCompatActivity() {
    private lateinit var viewModel: NotasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado)
        viewModel = ViewModelProvider(this)[NotasViewModel::class.java]
        val lista = findViewById<RecyclerView>(R.id.listaNotas)
        val adaptador = NotasAdapter()
        lista.layoutManager = LinearLayoutManager(this)
        lista.adapter = adaptador
        val barra = findViewById<ProgressBar>(R.id.listaCarga)
        val vacio = findViewById<TextView>(R.id.listaVacia)
        findViewById<Button>(R.id.volverListado).setOnClickListener { finish() }
        viewModel.listado.observe(this) { estado ->
            barra.visibility = if (estado.cargando) View.VISIBLE else View.GONE
            vacio.visibility = View.GONE
            if (estado.error != null) Toast.makeText(this, estado.error, Toast.LENGTH_LONG).show()
            if (estado.dato != null) {
                adaptador.actualizar(estado.dato)
                vacio.visibility = if (estado.dato.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.cargarNotas()
    }
}
