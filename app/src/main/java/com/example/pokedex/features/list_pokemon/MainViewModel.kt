package com.example.pokedex.features.list_pokemon

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.model.Pokemon

class MainViewModel : ViewModel(){
    var listaPokemons = MutableLiveData<List<Pokemon?>>()
}