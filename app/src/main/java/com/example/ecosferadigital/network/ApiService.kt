package com.example.ecosferadigital.network

import com.example.ecosferadigital.models.Usuario
import com.example.ecosferadigital.models.UsuarioPost
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("usuarios/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<Usuario>

    @POST("usuarios")
    suspend fun createUsuario(@Body usuario: UsuarioPost): Response<Usuario>

    @PUT("usuarios/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioPost): Response<Void>

    @DELETE("usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void>
}
