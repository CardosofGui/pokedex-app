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


        Thread(Runnable {

                receberPokemons()

            runOnUiThread {
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }).start()
    }

    fun receberPokemons() {

        for (i in 1..151){
            if(i == 90) { continue }
            val url = "https://pokeapi.co/api/v2/pokemon/${i}"

            val client = OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response?.body?.string()
                    val gson = GsonBuilder().create()

                    val pokemonEscolhido = gson.fromJson(body, Pokemon::class.java)

                    PokemonSingleton.listaPokemon[pokemonEscolhido.id] = pokemonEscolhido
                    loading.progress = PokemonSingleton.listaPokemon.size
                    Log.d("POkemon", pokemonEscolhido.name)
                    Log.d("tamanho", "${PokemonSingleton.listaPokemon.size}")
                }
            })
        }
    }

}