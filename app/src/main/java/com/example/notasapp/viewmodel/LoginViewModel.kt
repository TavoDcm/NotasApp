package com.example.notasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notasapp.model.*
import com.example.notasapp.data.remote.ApiClient
import com.example.notasapp.data.repository.AuthRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log


class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = AuthRepository(ApiClient.crear(application))

    private val _login = MutableLiveData<Estado<LoginResponse>>()
    val login: LiveData<Estado<LoginResponse>> = _login

    private val _registro = MutableLiveData<Estado<MensajeResponse>>()
    val registro: LiveData<Estado<MensajeResponse>> = _registro

    fun entrar(correo: String, clave: String) {
        _login.value = Estado(cargando = true)
        repo.entrar(correo, clave).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful && response.body() != null)
                    _login.value = Estado(dato = response.body())
                else _login.value = Estado(error = "Correo o contraseña incorrectos (${response.code()})")
            }
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _login.value = Estado(error = "No se pudo conectar con el servidor")
            }
        })
    }

    fun registrar(nombre: String, correo: String, clave: String) {
        _registro.value = Estado(cargando = true)
        repo.registrar(nombre, correo, clave).enqueue(object : Callback<MensajeResponse> {
            override fun onResponse(call: Call<MensajeResponse>, response: Response<MensajeResponse>) {
                if (response.isSuccessful) _registro.value = Estado(dato = response.body())
                else _registro.value = Estado(error = "No se pudo registrar (${response.code()}). Revisa los datos o el correo.")
            }

            override fun onFailure(
                call: Call<MensajeResponse>,
                t: Throwable
            ) {
                Log.e("NotasApp", "Error al registrar usuario", t)

                _registro.value = Estado(
                    error = "No se pudo conectar: ${t.message}"
                )
            }

        })
    }
}
