package com.example.notasapp.model

data class Estado<T>(
    val cargando: Boolean = false,
    val dato: T? = null,
    val error: String? = null
)
