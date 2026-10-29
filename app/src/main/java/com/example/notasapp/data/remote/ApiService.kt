package com.example.notasapp.data.remote

import com.example.notasapp.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("registro") fun registro(@Body datos: RegistroRequest): Call<MensajeResponse>
    @POST("login") fun login(@Body datos: LoginRequest): Call<LoginResponse>
    @POST("notas") fun crearNota(@Body datos: NotaRequest): Call<MensajeResponse>
    @GET("notas") fun obtenerNotas(): Call<List<Nota>>
}
