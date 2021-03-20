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
    var firstPokemon = 1
    var lastPokemon = 151



    fun adicionarPokemon(adapter: PokemonAdapter, pokemon: Pokemon, limit : Int, loading : LinearLayout){
        listaPokemon.add(pokemon)
        listaPokemon.sortBy { it?.id }

        if(listaPokemon.size == limit){
            adapter.notifyDataSetChanged()
            loading.visibility = View.GONE

            var i = 0
            listaPokemon.forEach {
                it?.setPositionPoke(i)
                i++
            }

            salvarPokemons()
        }
    }

    fun salvarPokemons(){
        var jsonTexto = Gson().toJson(PokemonSingleton.listaPokemon)

        PokemonApplication.instance.adicionarPreferences.putBoolean("geracao ${PokemonSingleton.geracaoSelecionada} salva", true)
        PokemonApplication.instance.adicionarPreferences.putString("lista ${PokemonSingleton.geracaoSelecionada} salva", jsonTexto)
        PokemonApplication.instance.adicionarPreferences.apply()
    }
}