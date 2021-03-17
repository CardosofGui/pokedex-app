package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.application.PokemonApplication
import com.example.pokedex.model.Pokemon
import com.example.pokedex.singleton.PokemonSingleton
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupClicks()
    }

    private fun setupRecyclerView() {
        val adapter = PokemonAdapter(baseContext, PokemonSingleton.geracaoSelecionada)
        recyclerViewPokemon.layoutManager = LinearLayoutManager(this)
        recyclerViewPokemon.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    fun setupClicks(){
        btn1.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 1)
            intent.putExtra("lastPoke", 151)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 1
            finish()
        }
        btn2.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 152)
            intent.putExtra("lastPoke", 251)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 2
            finish()
        }
        btn3.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 252)
            intent.putExtra("lastPoke", 386)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 3
            finish()
        }
        btn4.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 387)
            intent.putExtra("lastPoke", 493)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 4
            finish()
        }
        btn5.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 494)
            intent.putExtra("lastPoke", 649)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 5
            finish()
        }
        btn6.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 650)
            intent.putExtra("lastPoke", 721)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 6
            finish()
        }
        btn7.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 722)
            intent.putExtra("lastPoke", 809)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 7
            finish()
        }
        btn8.setOnClickListener {
            val intent = Intent(this, splash_screen::class.java)
            intent.putExtra("firstPoke", 810)
            intent.putExtra("lastPoke", 898)
            startActivity(intent)
            PokemonSingleton.geracaoSelecionada = 8
            finish()
        }
    }
}