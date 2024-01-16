package it.teutsch.felix.rant2text.ui.enumeration

import androidx.compose.ui.graphics.Color

enum class EAngerLevel(val angerLevel: Int, val angerColor: Color, val angerName: String) {
    None(0, Color(0xFFFFFFFF), "Does not bother me"),
    Low(1, Color(0xFF44CE1B), "Low"),
    MediumLow(2, Color(0xFFBBDB44), "Medium Low"),
    Medium(3, Color(0xFFF7E379), "Medium"),
    MediumHigh(4, Color(0xFFF2A134), "Medium High"),
    High(5, Color(0xFFE51F1F), "High"),
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