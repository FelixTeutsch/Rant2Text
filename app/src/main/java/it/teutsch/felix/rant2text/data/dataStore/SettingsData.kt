package it.teutsch.felix.rant2text.data.dataStore

import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val openRantOnCreate: Boolean = true,
    val openRantOnEdit: Boolean = false,
    val confirmBeforeDelete: Boolean = true,
    val defaultAngerLevel: EAngerLevel = EAngerLevel.None,
    // TODO: add more settings
)
