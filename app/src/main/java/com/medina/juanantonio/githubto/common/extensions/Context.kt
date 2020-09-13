package com.medina.juanantonio.githubto.common.extensions

import android.content.Context
import android.util.DisplayMetrics.DENSITY_DEFAULT

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DENSITY_DEFAULT)
}