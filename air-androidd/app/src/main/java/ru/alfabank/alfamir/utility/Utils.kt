package ru.alfabank.alfamir.utility


fun Int.toBoolean(): Boolean {
    return this == 1
}

fun Boolean.toInt(): Int {
    return if (this) 1 else 0
}

fun String.toBoolean(): Boolean{
    return this == "1"
}