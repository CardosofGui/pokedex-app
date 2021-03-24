package com.example.pokedex.singleton

import com.example.pokedex.model.Pokemon

object PokemonSingleton {
    var listaPokemon : MutableList<Pokemon?> = mutableListOf()
    var geracaoSelecionada : Int = 1
}