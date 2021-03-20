package com.example.pokedex

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.application.PokemonApplication
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.SharedPreferencesPokemon
import com.example.pokedex.singleton.PokemonSingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_menu.*

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.drawer_menu)

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
            val intent = Intent(this, splash_screen::class.java)
            when (it.itemId) {
                R.id.primeiraGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 1
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.segundaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 2
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.terceiraGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 3
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.quartaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 4
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.quintaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 5
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.sextaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 6
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.setimaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 7
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
                }
                R.id.oitavaGeracao -> {
                    PokemonSingleton.geracaoSelecionada = 8
                    PokemonSingleton.executarCodigoGeracao(intent, this)
                    true
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
        var filtrada: List<Pokemon?> = PokemonSingleton.listaPokemon

        if (!nome.isNullOrEmpty()) {
            filtrada = PokemonSingleton.listaPokemon.filter { it?.name!!.contains(nome) }
        }

        val adapter = PokemonAdapter(baseContext, filtrada) { onClickRecycler(it) }
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        recyclerViewPokemon.adapter = adapter
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

}