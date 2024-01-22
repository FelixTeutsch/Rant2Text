package it.teutsch.felix.rant2text.ui.state

import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.data.model.TextTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel

data class RantChatState(
    val title: String = "Generic",
    val text: String = "Generic Text",
    val rant: RantTableModel = RantTableModel(),
    val angerLevel: EAngerLevel = EAngerLevel.None,
    val texts: List<TextTableModel> = emptyList()

)
