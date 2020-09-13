package com.medina.juanantonio.githubto.common.extensions

fun <T> List<T>.toArrayList(): ArrayList<T> {
    val arrayList = arrayListOf<T>()
    arrayList.addAll(this)
    return arrayList
}