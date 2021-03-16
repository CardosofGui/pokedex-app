package com.example.pokedex.singleton

import com.example.pokedex.model.Pokemon

object PokemonSingleton {
    var listaPokemon : MutableMap<Int, Pokemon?> = mutableMapOf()
}