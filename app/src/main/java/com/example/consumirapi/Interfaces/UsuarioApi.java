package com.example.consumirapi.Interfaces;

import com.example.consumirapi.Models.ListaUsuario;
import com.example.consumirapi.Models.Usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UsuarioApi {
    @GET("public/v1/users")
    public Call<ListaUsuario>Listar();

}
