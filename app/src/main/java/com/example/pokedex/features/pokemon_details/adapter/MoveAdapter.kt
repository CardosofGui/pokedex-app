package com.example.pokedex.features.pokemon_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.R
import com.example.pokedex.model.Moves
import kotlinx.android.synthetic.main.move_grid.view.*

class MoveAdapter(
    private val context: Context,
    private val listMove: List<Moves>

) : RecyclerView.Adapter<MoveViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.move_grid, parent, false)
        return MoveViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = listMove.size


    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        var move = listMove[position]
        val view = holder.itemView

        view.txtNomeMove.text = move.nome.capitalize()
        view.txtLvlMove.text = "Level: ${move.lvlUp}"
    }
}

class MoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)