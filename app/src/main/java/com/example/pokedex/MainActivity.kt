package com.example.pokedex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.model.Pokemon
import com.example.pokedex.singleton.PokemonSingleton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PokemonSingleton.dadosCarregadosSucesso = true
        buscadorAutomatico()
        setupActionBarName()
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

    override fun onResume() {
        super.onResume()
        receberPokemons()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, splash_screen::class.java)
        when (item.itemId) {
            R.id.itemRefresh -> {
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.primeiraGeracao -> {
                PokemonSingleton.geracaoSelecionada = 1
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.segundaGeracao -> {
                PokemonSingleton.geracaoSelecionada = 2
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.terceiraGeracao -> {
                PokemonSingleton.geracaoSelecionada = 3
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.quartaGeracao -> {
                PokemonSingleton.geracaoSelecionada = 4
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.quintaGeracao -> {
                PokemonSingleton.geracaoSelecionada = 5
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.sextaGeracao -> {
                PokemonSingleton.geracaoSelecionada = 6
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.setimaGeracao -> {
                PokemonSingleton.geracaoSelecionada = 7
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
            R.id.oitavaGeracao -> {
                PokemonSingleton.geracaoSelecionada = 8
                PokemonSingleton.executarCodigoGeracao(intent, this)
            }
        }


        return super.onOptionsItemSelected(item)
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