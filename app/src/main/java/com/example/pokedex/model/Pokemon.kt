package com.example.pokedex.model

class Pokemon(val id: Int, val name : String, val order : Int, val types : List<index>, val sprites: sprites)

class index(val type : type)
class type(val name : String)
class sprites(val front_default : String)