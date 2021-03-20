package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pokedex.application.PokemonApplication
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.SharedPreferencesPokemon
import com.example.pokedex.singleton.PokemonSingleton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
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
        supportActionBar?.hide()

        val firstPokemon = intent.getIntExtra("firstPoke", 1)
        val lastPokemon = intent.getIntExtra("lastPoke", 151)
        loading.max = lastPokemon - firstPokemon

        Thread(Runnable {

            if(!verificarPokemonsSalvo()){
                PokemonSingleton.listaPokemon.clear()
                receberPokemons(firstPokemon, lastPokemon)
            }else{
                PokemonSingleton.listaPokemon.clear()
                carregarPokemons()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

        }).start()
    }

    fun verificarPokemonsSalvo() : Boolean{
        return PokemonApplication.instance.sharedPreferences.getBoolean("geracao ${PokemonSingleton.geracaoSelecionada} salva", false)
    }



    fun receberPokemons(
        firstPokemon: Int,
        lastPokemon: Int
    ){

        for (i in firstPokemon..lastPokemon) {
            val url = "https://pokeapi.co/api/v2/pokemon/${i}"

            val client = OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()
            client.dispatcher.maxRequestsPerHost = 50

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
        }

        Log.d("request finalizado", "Acabou")
        val intent = Intent(baseContext, MainActivity::class.java)
        salvarPokemons()
        startActivity(intent)
        finish()
    }

    fun salvarPokemons(){
        var jsonTexto = Gson().toJson(PokemonSingleton.listaPokemon)

        PokemonApplication.instance.adicionarPreferences.putBoolean("geracao ${PokemonSingleton.geracaoSelecionada} salva", true)
        PokemonApplication.instance.adicionarPreferences.putString("lista ${PokemonSingleton.geracaoSelecionada} salva", jsonTexto)
        PokemonApplication.instance.adicionarPreferences.apply()
    }

    fun carregarPokemons(){

        var json = PokemonApplication.instance.sharedPreferences.getString("lista ${PokemonSingleton.geracaoSelecionada} salva", null)
        var type = object : TypeToken<MutableList<Pokemon?>>() {}.type
        var lista : MutableList<Pokemon?> = Gson().fromJson(json, type)

        PokemonSingleton.listaPokemon = lista

    }
}