package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.application.PokemonApplication
import com.example.pokedex.model.Pokemon
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

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    lateinit var adapter : PokemonAdapter
    var filtrada : List<Pokemon?> = PokemonSingleton.listaPokemon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)

        geracaoArmazenada()
        initDrawer()
        buscadorAutomatico()
        setupActionBarName()
    }

    private fun initDrawer() {
        val drawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toogle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.primeiraGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 1
                    PokemonSingleton.firstPokemon = 1
                    PokemonSingleton.lastPokemon = 151
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Primeira geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.segundaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 2
                    PokemonSingleton.firstPokemon = 152
                    PokemonSingleton.lastPokemon = 251
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Segunda geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.terceiraGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 3
                    PokemonSingleton.firstPokemon = 252
                    PokemonSingleton.lastPokemon = 386
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Terceira geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.quartaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 4
                    PokemonSingleton.firstPokemon = 387
                    PokemonSingleton.lastPokemon = 493
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Quarta geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)
                    false
                }
                R.id.quintaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 5
                    PokemonSingleton.firstPokemon = 494
                    PokemonSingleton.lastPokemon = 649
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Quinta geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)

                    false
                }
                R.id.sextaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 6
                    PokemonSingleton.firstPokemon = 650
                    PokemonSingleton.lastPokemon = 721
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Sexta geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)

                    false
                }
                R.id.setimaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 7
                    PokemonSingleton.firstPokemon = 722
                    PokemonSingleton.lastPokemon = 809
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Setima geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)

                    false
                }
                R.id.oitavaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 8
                    PokemonSingleton.firstPokemon = 810
                    PokemonSingleton.lastPokemon = 898
                    geracaoArmazenada()
                    Toast.makeText(baseContext, "Carregando Oitava geração", Toast.LENGTH_SHORT).show()
                    drawerLayout.closeDrawer(GravityCompat.START)

                    false
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
                receberPokemons()
            }

        })
    }

    fun receberPokemons() {
        PokemonSingleton.listaPokemon.sortBy { it?.id }
        var nome: String = nomeBuscaPoke.text.toString().toLowerCase()

        if (!nome.isNullOrEmpty()) {
            filtrada = PokemonSingleton.listaPokemon.filter { it?.name!!.contains(nome) }
        }else{
            filtrada = PokemonSingleton.listaPokemon
        }

        adapter = PokemonAdapter(baseContext, filtrada) { onClickRecycler(it) }
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        recyclerViewPokemon.adapter = adapter
    }

    private fun geracaoArmazenada(){
        nomeBuscaPoke.setText("")
        setupActionBarName()
        PokemonSingleton.listaPokemon.clear()

        val firstPokemon = PokemonSingleton.firstPokemon
        val lastPokemon = PokemonSingleton.lastPokemon

        if(!verificarPokemonsSalvo()){
            llnLoading.visibility = View.VISIBLE
            loading.max = lastPokemon - firstPokemon
            loading.progress = 0
            txtLoading.setText("Carregando pokemon: ")

            Thread(Runnable {
                receberDadosAPI(firstPokemon, lastPokemon)
            }).start()
        }else{
            carregarPokemons()
        }
    }

    override fun onResume() {
        receberPokemons()
        super.onResume()
    }

    fun onClickRecycler(index : Int){
        val intent = Intent(this, Pokemon_Activity::class.java)
        intent.putExtra("idPokemon", index)
        startActivity(intent)
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

    fun receberDadosAPI(
        firstPokemon: Int,
        lastPokemon: Int
    ){

        for (i in firstPokemon..lastPokemon) {
            val url = "https://pokeapi.co/api/v2/pokemon/${i}"

            val client = OkHttpClient()
            val request = okhttp3.Request.Builder().url(url).build()
            client.dispatcher.maxRequestsPerHost = 50

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    val body = response?.body?.string()
                    val gson = GsonBuilder().create()
                    val pokemonEscolhido = gson.fromJson(body, Pokemon::class.java)

                    runOnUiThread {
                        loading.progress = PokemonSingleton.listaPokemon.size
                        txtLoading.text = "Carregando pokemon: ${pokemonEscolhido.name.capitalize()}"

                        var limit = 0
                        if(PokemonSingleton.geracaoSelecionada == 1){
                            limit = lastPokemon
                        }else{
                            limit = (lastPokemon-firstPokemon)+1

                        }

                        PokemonSingleton.adicionarPokemon(adapter, pokemonEscolhido, limit, llnLoading)
                    }

                }
            })
        }
    }

    fun verificarPokemonsSalvo() : Boolean{
        return PokemonApplication.instance.sharedPreferences.getBoolean("geracao ${PokemonSingleton.geracaoSelecionada} salva", false)
    }

    fun carregarPokemons(){
        Thread(Runnable {
            var json = PokemonApplication.instance.sharedPreferences.getString("lista ${PokemonSingleton.geracaoSelecionada} salva", null)
            var type = object : TypeToken<MutableList<Pokemon?>>() {}.type
            var lista : MutableList<Pokemon?> = Gson().fromJson(json, type)

            PokemonSingleton.listaPokemon = lista

            runOnUiThread {
                receberPokemons()
            }
        }).start()
    }
}