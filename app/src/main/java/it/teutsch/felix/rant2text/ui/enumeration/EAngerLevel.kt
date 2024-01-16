package it.teutsch.felix.rant2text.ui.enumeration

import androidx.compose.ui.graphics.Color

enum class EAngerLevel(val angerLevel: Int, val angerColor: Color, val angerName: String) {
    None(0, Color(0xFF1BCE48), "Quite Chill"),
    Low(1, Color(0xFF71DB44), "It's Ok"),
    MediumLow(2, Color(0xFFBBDB44), "Mad"),
    Medium(3, Color(0xFFF7E379), "Big Mad"),
    MediumHigh(4, Color(0xFFF2A134), "Furious"),
    High(5, Color(0xFFE51F1F), "Burn it to the ground")
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