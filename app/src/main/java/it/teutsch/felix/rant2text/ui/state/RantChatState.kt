package it.teutsch.felix.rant2text.ui.state

import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.data.model.RantTextTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EAngerLevel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog

data class RantChatState(
    val title: String = "",
    var text: String = "",
    val rant: RantListTableModel = RantListTableModel(),
    val angerLevel: EAngerLevel = EAngerLevel.None,
    val texts: List<RantTextTableModel> = emptyList(),
    override val dialog: EDialog = EDialog.NONE,
    var targetMessage: RantTextTableModel = RantTextTableModel()
) : MasterState
