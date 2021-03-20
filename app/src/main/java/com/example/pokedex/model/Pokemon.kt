package com.example.pokedex.model

class Pokemon(val id: Int, val name : String, val types : List<index>, val sprites: sprites, val moves : List<IndexMoves>?){
    var position = 0

    fun setPositionPoke(index : Int){
        position = index
    }
}

class IndexMoves(val move : Move, val version_group_details : List<IndexLevel>)

class Move(val name : String)

class IndexLevel(val level_learned_at : Int)

class index(val type : type)
class type(val name : String)
class sprites(val front_default : String)