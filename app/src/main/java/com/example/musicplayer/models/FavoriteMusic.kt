package com.example.musicplayer.models

data class FavoriteMusic(
    val title: String?,
    val description: String?
    ){
    constructor() : this("", null)
}