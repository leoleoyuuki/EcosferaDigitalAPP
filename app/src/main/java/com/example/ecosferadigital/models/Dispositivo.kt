package com.example.ecosferadigital.models

data class UsuarioDispositivo(
    val id: Int,
    val nome: String
)

data class Dispositivo(
    val id: Int,
    val usuarioId: Int,
    val tipoDispositivo: String,
    val descricao: String,
    val status: String
)

data class DispositivoPost(
    val usuarioId: Int,
    val tipoDispositivo: String,
    val descricao: String,
    val status: String
)
