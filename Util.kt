package com.example.semestralka

import kotlin.math.roundToInt


fun getTimeStringFromIntInSecond(timeInSeconds: Int): String {

    val hours = timeInSeconds / 3600
    val minutes = timeInSeconds % 3600 / 60
    val seconds = timeInSeconds % 3600 % 60

    return makeTime(hours, minutes, seconds)
}

fun getTimeInSecondsFromString(text: String): Int {
    var hours = text.substring(0, text.indexOf(":")).toInt()
    var minutes = text.substring(text.indexOf(":") + 1, text.lastIndexOf(":")).toInt()
    var seconds = text.substring(text.lastIndexOf(":") + 1).toInt()


    return  (hours * 3600) + (minutes * 60) + seconds
}

private fun makeTime(hour: Int, min: Int, sec : Int): String = String.format("%02d:%02d:%02d", hour, min, sec)