package com.example.pokedex.features.pokemon_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pokedex.R
import com.example.pokedex.features.pokemon_details.adapter.MoveAdapter
import com.example.pokedex.model.Moves
import com.example.pokedex.model.TypeEnum
import com.example.pokedex.singleton.PokemonSingleton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pokemon_.*

class Pokemon_Activity : AppCompatActivity() {

    var idPokemon = 1
    var listaMoves : MutableList<Moves> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_)

        idPokemon = intent.getIntExtra("idPokemon", 1)


        setupMoves()
        setupActivity()
        setupRecyclerView()
        setupClicks()
    }

    private fun setupMoves(){
        listaMoves.clear()
        PokemonSingleton.listaPokemon[idPokemon]?.moves?.forEach {
            var nome = it.move.name
            var lvl = it.version_group_details[0].level_learned_at

            if(lvl > 0){
                val move = Moves(
                    nome,
                    lvl
                )

                listaMoves.add(move)
            }
        }

        listaMoves.sortBy { it.lvlUp }
    }

    private fun setupClicks() {
        var animation = AnimationUtils.loadAnimation(baseContext, R.anim.animation)

        nextPokemon.setOnClickListener {
            nextPokemon.startAnimation(animation)

            idPokemon++

            if(idPokemon >= 0 && idPokemon < PokemonSingleton.listaPokemon.size){
                setupMoves()
                setupActivity()
                setupRecyclerView()


            }else{
                idPokemon--
                Toast.makeText(baseContext, "Não existe proximo pokemon", Toast.LENGTH_LONG).show()
            }
        }

        backPokemon.setOnClickListener {
            backPokemon.startAnimation(animation)

            idPokemon--

            if(idPokemon >= 0 && idPokemon <= PokemonSingleton.listaPokemon.size){
                setupMoves()
                setupActivity()
                setupRecyclerView()


            }else{
                idPokemon++
                Toast.makeText(baseContext, "Não existe pokemon anterior", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupRecyclerView() {
        val adapter =
            MoveAdapter(
                this,
                listaMoves
            )
        recyclerViewMoves.layoutManager = GridLayoutManager(this, 2)

        recyclerViewMoves.adapter = adapter
    }

    private fun setupActivity() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = PokemonSingleton.listaPokemon[idPokemon]!!.name.capitalize()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val pokemonSelecionado = PokemonSingleton.listaPokemon[idPokemon]!!

        Picasso.get().load(pokemonSelecionado.sprites?.front_default).into(imgExibirPokemon)
        txtExibirNomePokemon.text = pokemonSelecionado.name.capitalize()
        txtExibirType1.text = pokemonSelecionado.types?.get(0)?.type?.name?.capitalize() ?: "No Type"

        TypeEnum.values().forEach {
            if(it.type == pokemonSelecionado.types?.get(0)?.type?.name ?: "fire"){
                txtExibirType1.setBackgroundColor(ContextCompat.getColor(this, it.color))
                txtExibirType1.text = pokemonSelecionado.types?.get(0)?.type?.name?.capitalize() ?: "No Type"
            }
        }

        if(pokemonSelecionado.types?.size ?: 0 == 2){
            txtExibirType2.text = pokemonSelecionado.types?.get(1)?.type?.name?.capitalize() ?: "No Type"

            TypeEnum.values().forEach {
                if(it.type == pokemonSelecionado.types?.get(1)?.type?.name ?: "No Type"){
                    txtExibirType2.setBackgroundColor(ContextCompat.getColor(this, it.color))
                    txtExibirType2.text =
                        pokemonSelecionado.types?.get(1)?.type?.name?.capitalize() ?: "No Type"
                }
            }
        }else{
            txtExibirType2.visibility = View.GONE
        }

        if(pokemonSelecionado.moves?.size == 0 || pokemonSelecionado.moves?.size == null){
            txtSemMove.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                this.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
    }
}