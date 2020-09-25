package com.clevmania.facylops.overlay

import android.util.Size

/**
 * @author by Lawrence on 9/25/20.
 * for Facylops
 */
data class Frame(
    @Suppress("ArrayInDataClass") val data: ByteArray?,
    val rotation: Int,
    val size: Size,
    val format: Int,
    val lensFacing: LensFacing
)

enum class LensFacing { BACK, FRONT }