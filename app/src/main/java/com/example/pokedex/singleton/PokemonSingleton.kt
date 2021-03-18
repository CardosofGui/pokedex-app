package com.example.pokedex.singleton

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon
import com.example.pokedex.splash_screen

object PokemonSingleton {
    var listaPokemon : MutableList<Pokemon?> = mutableListOf()
    var geracaoSelecionada : Int = 1
    var dadosCarregadosSucesso = false

    fun executarCodigoGeracao(intent : Intent, activity: Activity){
        when(geracaoSelecionada) {
            1 -> {
                intent.putExtra("firstPoke", 1)
                intent.putExtra("lastPoke", 151)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            2 -> {
                intent.putExtra("firstPoke", 152)
                intent.putExtra("lastPoke", 251)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            3 -> {
                intent.putExtra("firstPoke", 252)
                intent.putExtra("lastPoke", 386)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            4 -> {
                intent.putExtra("firstPoke", 387)
                intent.putExtra("lastPoke", 493)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            5 -> {
                intent.putExtra("firstPoke", 494)
                intent.putExtra("lastPoke", 649)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            6 -> {
                intent.putExtra("firstPoke", 650)
                intent.putExtra("lastPoke", 721)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            7 -> {
                intent.putExtra("firstPoke", 722)
                intent.putExtra("lastPoke", 809)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
            8 -> {
                intent.putExtra("firstPoke", 810)
                intent.putExtra("lastPoke", 898)
                activity.startActivity(intent)
                activity.finish()
                PokemonSingleton.dadosCarregadosSucesso = false
            }
        }
    }
}