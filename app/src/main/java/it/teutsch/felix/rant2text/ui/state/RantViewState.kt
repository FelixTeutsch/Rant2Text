package it.teutsch.felix.rant2text.ui.state

import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog

data class RantViewState(
    val rants: List<RantListTableModel> = emptyList(),
    val targetRant: RantListTableModel = RantListTableModel(),
    var searchText: String = "",
    override val dialog: EDialog = EDialog.NONE,
) : MasterState