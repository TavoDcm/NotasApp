package com.example.notasapp.data.repository

import com.example.notasapp.model.*
import com.example.notasapp.data.remote.ApiService
import retrofit2.Call

class NotasRepository(private val api: ApiService) {
    fun guardar(titulo: String, contenido: String): Call<MensajeResponse> =
        api.crearNota(NotaRequest(titulo, contenido))

    fun listar(): Call<List<Nota>> = api.obtenerNotas()
}
