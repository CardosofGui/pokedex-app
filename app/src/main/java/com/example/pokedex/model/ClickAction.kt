package com.example.pokedex.model

interface ClickAction {

    fun onClickNavigationMenu(sharedPreferencesPokemon: SharedPreferencesPokemon) : Boolean
}