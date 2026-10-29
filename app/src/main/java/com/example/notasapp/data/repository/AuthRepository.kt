package com.example.notasapp.data.repository

import com.example.notasapp.model.*
import com.example.notasapp.data.remote.ApiService
import retrofit2.Call

class AuthRepository(private val api: ApiService) {
    fun entrar(correo: String, clave: String): Call<LoginResponse> =
        api.login(LoginRequest(correo, clave))

    fun registrar(nombre: String, correo: String, clave: String): Call<MensajeResponse> =
        api.registro(RegistroRequest(nombre, correo, clave))
}
