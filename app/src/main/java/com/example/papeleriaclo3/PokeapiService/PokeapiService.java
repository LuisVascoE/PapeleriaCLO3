package com.example.papeleriaclo3.PokeapiService;

import com.example.papeleriaclo3.models.PokemonRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PokeapiService {
    @GET("pokemon")
    Call<PokemonRespuesta> obtenerListaPokemon();
}
