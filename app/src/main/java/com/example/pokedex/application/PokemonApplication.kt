package com.example.pokedex.application

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class PokemonApplication : Application(){

    lateinit var sharedPreferences: SharedPreferences
    lateinit var adicionarPreferences: SharedPreferences.Editor

    companion object{
        lateinit var instance : PokemonApplication
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        sharedPreferences = getSharedPreferences("Pokemons", Context.MODE_PRIVATE)
        adicionarPreferences = sharedPreferences.edit()
    }
}