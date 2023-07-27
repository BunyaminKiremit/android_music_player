package com.example.musicplayer.models

data class MusicCategory(
    val baseTitle: String?,
    val items: List<İtem>?,
    var isExpanded: Boolean = false // Yeni özellik: Açılmış mı kapalı mı olduğunu tutar
)