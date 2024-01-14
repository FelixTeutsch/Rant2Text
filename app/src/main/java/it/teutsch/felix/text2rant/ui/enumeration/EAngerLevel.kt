package it.teutsch.felix.text2rant.ui.enumeration

import androidx.compose.ui.graphics.Color

enum class EAngerLevel(val angerLevel: Int, val angerColor: Color, val angerName: String) {
    Low(0, Color(0xFF44CE1B), "Low"),
    MediumLow(1, Color(0xFFBBDB44), "MediumLow"),
    Medium(2, Color(0xFFF7E379), "Medium"),
    MediumHigh(3, Color(0xFFF2A134), "MediumHigh"),
    High(4, Color(0xFFE51F1F), "High"),
    ;

    companion object {
        fun fromInt(angerLevel: Int): EAngerLevel {
            return when (angerLevel) {
                0 -> Low
                1 -> MediumLow
                2 -> Medium
                3 -> MediumHigh
                4 -> High
                else -> throw IllegalArgumentException("Invalid anger level")
            }
        }
    }
}