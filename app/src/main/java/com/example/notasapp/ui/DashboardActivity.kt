package com.example.notasapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.notasapp.R
import com.example.notasapp.sesion.Sesion

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Sesion.obtenerToken(this).isEmpty()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        setContentView(R.layout.activity_dashboard)
        findViewById<TextView>(R.id.bienvenida).text =
            "Bienvenido, ${Sesion.obtenerNombre(this)}"
        findViewById<Button>(R.id.irCrear).setOnClickListener {
            startActivity(Intent(this, CrearNotaActivity::class.java))
        }
        findViewById<Button>(R.id.irListado).setOnClickListener {
            startActivity(Intent(this, ListadoActivity::class.java))
        }
        findViewById<Button>(R.id.salir).setOnClickListener {
            Sesion.cerrar(this)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
