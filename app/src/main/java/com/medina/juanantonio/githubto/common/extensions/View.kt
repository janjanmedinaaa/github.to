package com.medina.juanantonio.githubto.common.extensions

import android.view.View
import android.view.ViewGroup

fun View.setMargin(
    marginLeft: Int? = null,
    marginTop: Int? = null,
    marginRight: Int? = null,
    marginBottom: Int? = null
) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        marginLeft ?: layoutParams.leftMargin,
        marginTop ?: layoutParams.topMargin,
        marginRight ?: layoutParams.rightMargin,
        marginBottom ?: layoutParams.bottomMargin
    )
    this.layoutParams = layoutParams
}