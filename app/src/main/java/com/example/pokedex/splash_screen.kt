package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pokedex.model.Pokemon
import com.example.pokedex.singleton.PokemonSingleton
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_splash_screen.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

class splash_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val firstPokemon = intent.getIntExtra("firstPoke", 1)
        val lastPokemon = intent.getIntExtra("lastPoke", 151)
        loading.max = lastPokemon - firstPokemon

        Thread(Runnable {

                PokemonSingleton.listaPokemon.clear()
                receberPokemons(firstPokemon, lastPokemon)

        }).start()
    }

    fun receberPokemons(
        firstPokemon: Int,
        lastPokemon: Int
    ){

        for (i in firstPokemon..lastPokemon) {
            val url = "https://pokeapi.co/api/v2/pokemon/${i}"

            val client = OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()
            client.dispatcher.maxRequestsPerHost = 5

            var response = client.newCall(request).execute()
            val body = response?.body?.string()
            val gson = GsonBuilder().create()
            val pokemonEscolhido = gson.fromJson(body, Pokemon::class.java)
            pokemonEscolhido.setPositionPoke(PokemonSingleton.listaPokemon.size)

            PokemonSingleton.listaPokemon.add(pokemonEscolhido)

            runOnUiThread {
                loading.progress = PokemonSingleton.listaPokemon.size
                txtLoading.text = "Carregando pokemon: ${pokemonEscolhido.name?.capitalize()}"
            }

            if (PokemonSingleton.listaPokemon.size == lastPokemon-firstPokemon) {
                Log.d("request finalizado", "Acabou")
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

}