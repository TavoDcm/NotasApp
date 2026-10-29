package com.example.notasapp.data.remote

import android.content.Context
import com.example.notasapp.sesion.Sesion
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://192.168.1.10:8000/"

    fun crear(context: Context): ApiService {
        val interceptor = Interceptor { cadena ->
            val token = Sesion.obtenerToken(context)
            val solicitud = cadena.request().newBuilder()
            if (token.isNotEmpty()) {
                solicitud.addHeader("Authorization", "Bearer $token")
            }
            cadena.proceed(solicitud.build())
        }
        val cliente = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(cliente)
            .build()
            .create(ApiService::class.java)
    }
}
