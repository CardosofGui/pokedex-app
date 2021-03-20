package com.example.pokedex.application

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import com.example.pokedex.model.Pokemon
import com.example.pokedex.singleton.PokemonSingleton
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

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