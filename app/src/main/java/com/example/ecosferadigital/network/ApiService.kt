package com.example.ecosferadigital.network

import com.example.ecosferadigital.models.Dispositivo
import com.example.ecosferadigital.models.DispositivoPost
import com.example.ecosferadigital.models.Usuario
import com.example.ecosferadigital.models.UsuarioPost
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("Usuario")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @GET("Usuario/{id}")
    suspend fun getUsuarioById(@Path("id") id: Int): Response<Usuario>

    @POST("Usuario")
    suspend fun createUsuario(@Body usuario: UsuarioPost): Response<Usuario>

    @PUT("Usuario/{id}")
    suspend fun updateUsuario(@Path("id") id: Int, @Body usuario: UsuarioPost): Response<Void>

    @DELETE("Usuario/{id}")
    suspend fun deleteUsuario(@Path("id") id: Int): Response<Void>

    // Listar dispositivos
    @GET("dispositivo")
    suspend fun getDispositivos(): Response<List<Dispositivo>>

    // Criar dispositivo
    @POST("dispositivo")
    suspend fun createDispositivo(@Body dispositivoPost: DispositivoPost): Response<Dispositivo>

    // Editar dispositivo
    @PUT("dispositivo/{id}")
    suspend fun updateDispositivo(
        @Path("id") id: Int,
        @Body dispositivoPost: DispositivoPost
    ): Response<Dispositivo>

    // Deletar dispositivo
    @DELETE("dispositivo/{id}")
    suspend fun deleteDispositivo(@Path("id") id: Int): Response<Void>
}
