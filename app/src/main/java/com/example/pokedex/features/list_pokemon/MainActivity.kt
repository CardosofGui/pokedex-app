package com.example.pokedex.features.list_pokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.features.list_pokemon.adapter.PokemonAdapter
import com.example.pokedex.application.PokemonApplication
import com.example.pokedex.features.pokemon_details.Pokemon_Activity
import com.example.pokedex.model.ClickAction
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.SharedPreferencesPokemon
import com.example.pokedex.singleton.PokemonSingleton
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_menu.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), ClickAction {

    lateinit var pokemonAdapter : PokemonAdapter
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)

        verificarPokemonsArmazenadosSharedPreferences()
        initDrawer()
        buscadorAutomatico()
        setupActionBarName()
    }

    private fun initDrawer() {
        setSupportActionBar(toolbar)

        val toogle = ActionBarDrawerToggle(this, drawer_layout, toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawer_layout.addDrawerListener(toogle)
        toogle.syncState()

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.primeiraGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_UM)
                }
                R.id.segundaGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_DOIS)
                }
                R.id.terceiraGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_TRES)
                }
                R.id.quartaGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_QUATRO)
                }
                R.id.quintaGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_QUINTA)
                }
                R.id.sextaGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_SEXTA)
                }
                R.id.setimaGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_SETIMA)
                }
                R.id.oitavaGeracao -> {
                    onClickNavigationMenu(SharedPreferencesPokemon.GERACAO_OITAVA)
                }
                else -> false
            }
        }
    }

    private fun setupActionBarName() {
        when(PokemonSingleton.geracaoSelecionada){
            1 -> supportActionBar?.title = "Primeira Geração"
            2 -> supportActionBar?.title = "Segunda Geração"
            3 -> supportActionBar?.title = "Terceira Geração"
            4 -> supportActionBar?.title = "Quarta Geração"
            5 -> supportActionBar?.title = "Quinta Geração"
            6 -> supportActionBar?.title = "Sexta Geração"
            7 -> supportActionBar?.title = "Setima Geração"
            8 -> supportActionBar?.title = "Oitava Geração"
        }
    }

    private fun buscadorAutomatico() {
        nomeBuscaPoke.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarPokemons()
            }

        })
    }

    private fun filtrarPokemons() {
        var nome: String = nomeBuscaPoke.text.toString().toLowerCase()
        var listaFiltrada : List<Pokemon?> = PokemonSingleton.listaPokemon


        if (!nome.isNullOrEmpty()) {
            listaFiltrada = PokemonSingleton.listaPokemon.filter {
                it?.name!!.contains(nome) || it?.id!!.toString().contains(nome)
            }
        }

        pokemonAdapter = PokemonAdapter(baseContext, listaFiltrada, onClickExibirPokemon = { onClickRecycler(it) }, onClickPokemonError = { onClickPokemonErro(it) })
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        recyclerViewPokemon.adapter = pokemonAdapter
    }

    private fun onClickPokemonErro(it: Int) {
        val pokemon = PokemonSingleton.listaPokemon[it]

        val url = "https://pokeapi.co/api/v2/pokemon/${pokemon?.id}"
        val request = okhttp3.Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(baseContext, "Erro de Conexão \nTente novamente outra hora", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val response = client.newCall(request).execute()
                    val body = response?.body?.string()
                    val gson = GsonBuilder().create()
                    val pokemonEscolhido = gson.fromJson(body, Pokemon::class.java)
                    runOnUiThread {
                        PokemonSingleton.listaPokemon[it] = pokemonEscolhido
                        pokemonAdapter.notifyDataSetChanged()
                    }
                }catch (e : Exception){
                    runOnUiThread {
                        Toast.makeText(baseContext, "Pokemon Não Encontrado \nTente novamente outra hora", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun verificarPokemonsArmazenadosSharedPreferences(){
        PokemonSingleton.listaPokemon.clear()
        nomeBuscaPoke.setText("")
        setupActionBarName()

        if(verificarDadosBaixados()){
            // Se já tiver essa geração armazenada ele irá carregar os dados
            carregarPokemonsSharedPreferences()
        }else{
            // Caso não tenha salva será solicitado a API os dados dos Pokemons
            llnLoading.visibility = View.VISIBLE
            txtLoading.text = getString(R.string.carregando_pokemon)

            drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            fetchPokeAPI(1, 898)
        }
    }

    private fun onClickRecycler(index : Int){
        val intent = Intent(this, Pokemon_Activity::class.java)
        intent.putExtra("idPokemon", index)
        startActivity(intent)
    }

    private fun fetchPokeAPI(
        firstPokemon: Int,
        lastPokemon: Int
    ){
        Thread(Runnable {
            for (index in firstPokemon..lastPokemon) {
                val url = "https://pokeapi.co/api/v2/pokemon/${index}"
                val request = okhttp3.Request.Builder().url(url).build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("Erro Conexao", "$e")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val body = response?.body?.string()
                            val gson = GsonBuilder().create()
                            val pokemonEscolhido = gson.fromJson(body, Pokemon::class.java)
                            PokemonSingleton.listaPokemon.adicionarPokemon(pokemonAdapter, pokemonEscolhido, (lastPokemon-firstPokemon)+1, llnLoading)

                            runOnUiThread {
                                txtLoading.text = "Carregando pokemon: ${pokemonEscolhido.name.capitalize()} \n ${PokemonSingleton.listaPokemon.size}/898"
                            }
                        }catch (e : Exception){
                            Log.d("Pokemon", "Pokemon não encontrado: ID:$index - Excepetion: $e")

                            val pokemonEscolhido = Pokemon(
                                index,
                                "Pokemon não encontado",
                                null,
                                null,
                                null
                            )


                            PokemonSingleton.listaPokemon.adicionarPokemon(pokemonAdapter, pokemonEscolhido, (lastPokemon-firstPokemon)+1, llnLoading)
                        }
                    }
                })
            }
        }).start()
    }

    private fun verificarDadosBaixados() : Boolean{
        return PokemonApplication.instance.sharedPreferences.getBoolean("geracao ${PokemonSingleton.geracaoSelecionada} salva", false)
    }

    private fun carregarPokemonsSharedPreferences(){
        llnLoading.visibility = View.VISIBLE
        txtLoading.visibility = View.GONE

        Thread(Runnable {
            var json = PokemonApplication.instance.sharedPreferences.getString("lista ${PokemonSingleton.geracaoSelecionada} salva", null)
            var type = object : TypeToken<MutableList<Pokemon?>>() {}.type
            var lista : MutableList<Pokemon?> = Gson().fromJson(json, type)

            PokemonSingleton.listaPokemon = lista

            runOnUiThread {
                filtrarPokemons()
                llnLoading.visibility = View.GONE
            }
        }).start()
    }

    private fun salvarPokemonsSharedPreferences(listPokemon : List<Pokemon?>, trocarDados : Boolean = false){
        if(trocarDados){
            var jsonTexto = Gson().toJson(listPokemon)

            PokemonApplication.instance.adicionarPreferences.putBoolean("geracao ${PokemonSingleton.geracaoSelecionada} salva", true)
            PokemonApplication.instance.adicionarPreferences.putString("lista ${PokemonSingleton.geracaoSelecionada} salva", jsonTexto)
            PokemonApplication.instance.adicionarPreferences.apply()
            carregarPokemonsSharedPreferences()
        }else{
            SharedPreferencesPokemon.values().forEach {
                var list = listPokemon.filter { pokemon: Pokemon? ->
                    pokemon!!.id >= it.firstPokemon && pokemon!!.id <= it.lastPokemon }

                var i = 0
                list.forEach {
                        pokemon : Pokemon? ->
                    pokemon?.setPositionPoke(i)
                    i++
                }

                var jsonTexto = Gson().toJson(list)

                PokemonApplication.instance.adicionarPreferences.putBoolean("geracao ${it.geracao} salva", true)
                PokemonApplication.instance.adicionarPreferences.putString("lista ${it.geracao} salva", jsonTexto)
                PokemonApplication.instance.adicionarPreferences.apply()

                if(it.geracao == 1){
                    carregarPokemonsSharedPreferences()
                }
            }
        }

        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun MutableList<Pokemon?>.adicionarPokemon(adapter: PokemonAdapter, pokemon: Pokemon, limit : Int, loading : LinearLayout){
        runOnUiThread {
            this.add(pokemon)

            if(this.size == limit){
                loading.visibility = View.GONE
                this.sortBy { it?.id }

                salvarPokemonsSharedPreferences(this)
            }
        }
    }

    override fun onResume() {
        filtrarPokemons()
        super.onResume()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onClickNavigationMenu(sharedPreferencesPokemon: SharedPreferencesPokemon) : Boolean {
        PokemonSingleton.geracaoSelecionada = sharedPreferencesPokemon.geracao

        Toast.makeText(baseContext, "Carregando ${sharedPreferencesPokemon.texto}", Toast.LENGTH_SHORT).show()
        drawer_layout.closeDrawer(GravityCompat.START)
        verificarPokemonsArmazenadosSharedPreferences()
        return false
    }

    companion object {
        val client = OkHttpClient().newBuilder().readTimeout(2, TimeUnit.MINUTES).connectTimeout(2, TimeUnit.MINUTES).build()
    }
}