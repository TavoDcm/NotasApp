package com.example.notasapp.sesion

import android.content.Context
import com.example.notasapp.model.LoginResponse

object Sesion {
    fun guardar(context: Context, datos: LoginResponse) {
        context.getSharedPreferences("sesion", Context.MODE_PRIVATE).edit()
            .putString("token", datos.token)
            .putString("nombre", datos.nombre)
            .apply()
    }
    fun obtenerToken(context: Context): String =
        context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
            .getString("token", "") ?: ""

    fun obtenerNombre(context: Context): String =
        context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
            .getString("nombre", "Usuario") ?: "Usuario"

    fun cerrar(context: Context) {
        context.getSharedPreferences("sesion", Context.MODE_PRIVATE).edit().clear().apply()
    }
}
