package it.teutsch.felix.rant2text.ui.state

import it.teutsch.felix.rant2text.data.model.RantTableModel

data class RantChatState(
    val title: String = "Generic",
    val text: String = "Generic Text",
    val rant: RantTableModel
)
