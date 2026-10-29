package com.example.notasapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notasapp.model.*
import com.example.notasapp.data.remote.ApiClient
import com.example.notasapp.data.repository.NotasRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotasViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = NotasRepository(ApiClient.crear(application))

    private val _crear = MutableLiveData<Estado<MensajeResponse>>()
    val crear: LiveData<Estado<MensajeResponse>> = _crear

    private val _listado = MutableLiveData<Estado<List<Nota>>>()
    val listado: LiveData<Estado<List<Nota>>> = _listado

    fun guardar(titulo: String, contenido: String) {
        _crear.value = Estado(cargando = true)
        repo.guardar(titulo, contenido).enqueue(object : Callback<MensajeResponse> {
            override fun onResponse(call: Call<MensajeResponse>, response: Response<MensajeResponse>) {
                if (response.isSuccessful) _crear.value = Estado(dato = response.body())
                else _crear.value = Estado(error = "Error al guardar (${response.code()})")
            }
            override fun onFailure(call: Call<MensajeResponse>, t: Throwable) {
                _crear.value = Estado(error = "Error de conexión al guardar")
            }
        })
    }

    fun cargarNotas() {
        _listado.value = Estado(cargando = true)
        repo.listar().enqueue(object : Callback<List<Nota>> {
            override fun onResponse(call: Call<List<Nota>>, response: Response<List<Nota>>) {
                if (response.isSuccessful) _listado.value = Estado(dato = response.body() ?: emptyList())
                else _listado.value = Estado(error = "No se pudieron cargar las notas (${response.code()})")
            }
            override fun onFailure(call: Call<List<Nota>>, t: Throwable) {
                _listado.value = Estado(error = "No hay conexión con el servidor")
            }
        })
    }
}
