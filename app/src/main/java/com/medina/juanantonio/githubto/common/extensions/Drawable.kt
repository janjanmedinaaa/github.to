package com.medina.juanantonio.githubto.common.extensions

import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable

/**
 * ImageView Color Inverter
 * source: https://stackoverflow.com/a/56648302/12714892
 */
fun Drawable.toNegative() {
    val negative = floatArrayOf(
        -1.0f, .0f, .0f, .0f, 255.0f,
        .0f, -1.0f, .0f, .0f, 255.0f,
        .0f, .0f, -1.0f, .0f, 255.0f,
        .0f, .0f, .0f, 1.0f, .0f
    )

    this.colorFilter = ColorMatrixColorFilter(negative)
}