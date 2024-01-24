package it.teutsch.felix.rant2text.ui.enumeration

import androidx.compose.ui.graphics.Color

enum class EAngerLevel(val angerLevel: Int, val angerColor: Color, val angerName: String) {
    None(0, Color(0xFF64DD17), "Quite Chill"),
    Low(1, Color(0xFF8BC34A), "It's Ok"),
    MediumLow(2, Color(0xFFFFC107), "Mad"),
    Medium(3, Color(0xFFFF9800), "Big Mad"),
    MediumHigh(4, Color(0xFFFF5722), "Furious"),
    High(5, Color(0xFFD32F2F), "Burn it to the ground")
    ;

    companion object {
        fun fromInt(angerLevel: Int): EAngerLevel {
            return when (angerLevel) {
                1 -> Low
                2 -> MediumLow
                3 -> Medium
                4 -> MediumHigh
                5 -> High
                else -> None
            }
        }
    }
}