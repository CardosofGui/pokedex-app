package com.example.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokedex.R
import com.example.pokedex.adapter.MoveAdapter
import com.example.pokedex.adapter.PokemonAdapter
import com.example.pokedex.model.Moves
import com.example.pokedex.model.TypeEnum
import com.example.pokedex.singleton.PokemonSingleton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pokemon_.*
import kotlinx.android.synthetic.main.pokemon_grid.view.*

class Pokemon_Activity : AppCompatActivity() {

    var idPokemon = 1
    var listaMoves : MutableList<Moves> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_)

        idPokemon = intent.getIntExtra("idPokemon", 1)

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

        setupActivity()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        listaMoves.sortBy { it.lvlUp }
        val adapter = MoveAdapter(this, listaMoves)
        recyclerViewMoves.layoutManager = GridLayoutManager(this, 2)
        recyclerViewMoves.adapter = adapter
    }

    private fun setupActivity() {
        val pokemonSelecionado = PokemonSingleton.listaPokemon[idPokemon]!!

        Picasso.get().load(pokemonSelecionado.sprites.front_default).into(imgExibirPokemon)
        txtExibirNomePokemon.text = pokemonSelecionado.name.capitalize()
        txtExibirType1.text = pokemonSelecionado.types[0].type.name.capitalize()

        TypeEnum.values().forEach {
            if(it.type == pokemonSelecionado.types[0].type.name){
                txtExibirType1.setBackgroundColor(ContextCompat.getColor(this, it.color))
                txtExibirType1.text = pokemonSelecionado.types[0].type.name.capitalize()
            }
        }

        if(pokemonSelecionado.types.size == 2){
            txtExibirType2.text = pokemonSelecionado.types[1].type.name.capitalize()

            TypeEnum.values().forEach {
                if(it.type == pokemonSelecionado.types[1].type.name){
                    txtExibirType2.setBackgroundColor(ContextCompat.getColor(this, it.color))
                    txtExibirType2.text = pokemonSelecionado.types[1].type.name.capitalize()
                }
            }
        }else{
            txtExibirType2.visibility = View.GONE
        }
    }
}