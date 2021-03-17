package com.example.pokedex.model

class Pokemon(val id: Int, val name : String, val order : Int, val types : List<index>, val sprites: sprites, val moves : List<indexMoves>)

class indexMoves (val move : Move, val version_group_details : List<lvlUp>)
class lvlUp (val level_learned_at : Int)
class Move(val name : String)

class index(val type : type)
class type(val name : String)
class sprites(val front_default : String)