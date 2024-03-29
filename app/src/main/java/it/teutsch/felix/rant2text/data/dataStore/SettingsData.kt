package it.teutsch.felix.rant2text.data.dataStore

import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import kotlinx.serialization.Serializable

@Serializable
data class SettingsData(
    val openRantOnCreate: Boolean = true,
    val openRantOnEdit: Boolean = false,
    val editRantMessages: Boolean = true,
    val confirmBeforeDeleteRantList: Boolean = true,
    val confirmBeforeDeleteRantMessage: Boolean = true,
    val defaultAngerLevel: EAngerLevel = EAngerLevel.None,
    val showTimeStampsForMessages: Boolean = false,
    val scrollNavigation: Boolean = true,
    // TODO: add more settings
)
