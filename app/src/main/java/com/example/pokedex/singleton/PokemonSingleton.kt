package com.example.pokedex.singleton

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.application.PokemonApplication
import com.example.pokedex.model.Pokemon
import com.google.gson.Gson

object PokemonSingleton {
    var listaPokemon : MutableList<Pokemon?> = mutableListOf()
    var geracaoSelecionada : Int = 1
}