package com.example.notasapp.model

data class RegistroRequest(val nombre: String, val correo: String, val contrasena: String)
data class LoginRequest(val correo: String, val contrasena: String)
data class LoginResponse(val token: String, val nombre: String, val correo: String)
data class MensajeResponse(val mensaje: String, val id: Int? = null)
data class NotaRequest(val titulo: String, val contenido: String)
data class Nota(val id: Int, val titulo: String, val contenido: String, val fecha_creacion: String)
