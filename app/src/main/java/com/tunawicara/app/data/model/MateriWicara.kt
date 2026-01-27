package com.tunawicara.app.data.model

/**
 * Data model for materi_wicara from Firestore
 */
data class MateriWicara(
    val id: String = "",
    val teks: String = "",
    val tipe: String = "",
    val urutan: Int = 0,
    val imageUrl: String? = null
) {
    // No-arg constructor for Firestore
    constructor() : this("", "", "", 0, null)
}
