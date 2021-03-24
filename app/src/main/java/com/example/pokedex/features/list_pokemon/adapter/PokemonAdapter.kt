package com.example.pokedex.features.list_pokemon.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.model.Pokemon
import com.example.pokedex.model.TypeEnum
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pokemon_grid.view.*

class PokemonAdapter(
    private val context : Context,
    private val listaPokemon: List<Pokemon?>,
    private val onClickExibirPokemon : ((Int) -> Unit),
    private val onClickPokemonError : ((Int) -> Unit)
) : RecyclerView.Adapter<PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.pokemon_grid,parent,false)
        return PokemonViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listaPokemon.size

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val view = holder.itemView
        var pokemon = listaPokemon[position]


        val pokemonTypeCount = pokemon?.types?.size
        var pokemonTypeMain = pokemon?.types?.get(0)?.type?.name
        var pokemonTypeSecondary : String

        if(pokemonTypeCount == 2){
            pokemonTypeSecondary = pokemon?.types?.get(1)?.type?.name.toString()
            view.typePokemon2.visibility = View.VISIBLE

            TypeEnum.values().forEach {
                if(it.type == pokemonTypeSecondary){
                    view.typePokemon2.setBackgroundColor(ContextCompat.getColor(context, it.color))
                    view.typePokemon2.text = pokemonTypeSecondary.capitalize()
                }

                TypeEnum.values().forEach {
                    if(it.type == pokemonTypeMain){
                        view.typePokemon1.setBackgroundColor(ContextCompat.getColor(context, it.color))
                    }
                }
            }
        }else{
            view.typePokemon2.visibility = View.GONE

            TypeEnum.values().forEach {
                if(it.type == pokemonTypeMain){
                    view.typePokemon1.setBackgroundColor(ContextCompat.getColor(context, it.color))
                }
            }
        }

        view.idPokemon.text = "#${pokemon?.id}"
        view.nomePokemon.text = pokemon?.name?.capitalize()
        Picasso.get().load(pokemon?.sprites?.front_default).into(view.imgPokemon)
        view.typePokemon1.text = pokemonTypeMain?.capitalize()

        if(pokemon?.types == null){
            view.typePokemon1.text = "CLIQUE PARA TENTAR BAIXAR NOVAMENTE"
            view.typePokemon1.setTextColor(Color.RED)
            view.llnPokemon.setOnClickListener {
                var animation = AnimationUtils.loadAnimation(context, R.anim.animation)
                view.llnPokemon.startAnimation(animation)
                onClickPokemonError(pokemon!!.position)
            }
        }else{
            view.llnPokemon.setOnClickListener {
                var animation = AnimationUtils.loadAnimation(context, R.anim.animation)
                view.llnPokemon.startAnimation(animation)

                onClickExibirPokemon(pokemon!!.position)
            }
        }
    }
}

class PokemonViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

}
