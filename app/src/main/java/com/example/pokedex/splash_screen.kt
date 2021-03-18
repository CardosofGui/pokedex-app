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

        for (i in firstPokemon..lastPokemon){
            val url = "https://pokeapi.co/api/v2/pokemon/${i}"

            val client = OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("Erro", "não deu certo")
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        val body = response?.body?.string()
                        val gson = GsonBuilder().create()

                        val pokemonEscolhido = gson.fromJson(body, Pokemon::class.java)

                        PokemonSingleton.listaPokemon.add(pokemonEscolhido)

                        loading.progress = PokemonSingleton.listaPokemon.size

                        runOnUiThread {
                            txtLoading.text = "Carregando pokemon: ${pokemonEscolhido.name.capitalize()}"

                            if(PokemonSingleton.listaPokemon.size >= lastPokemon-firstPokemon){
                                val intent = Intent(baseContext, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                        Log.d("POkemon", pokemonEscolhido.name)
                        Log.d("tamanho", "${PokemonSingleton.listaPokemon.size}")
                        Log.d("level", "${ pokemonEscolhido.moves[0].version_group_details[0].level_learned_at }")
                        Log.d("move", pokemonEscolhido.moves[0].move.name)

                    }catch (ex : Exception){
                        Log.d("erro", "${ex}")
                    }
                }
            })
        }
    }

}