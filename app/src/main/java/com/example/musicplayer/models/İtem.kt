package com.example.musicplayer.models


data class İtem(
    val baseCat: Int = 0,
    val title: String = "",
    val url: String = ""
) {
    constructor() : this(0, "", "")
}