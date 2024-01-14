package it.teutsch.felix.rant2text.ui.state

import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog

data class RantViewState(
    val rants: List<RantTableModel> = emptyList(),
    val targetRant: RantTableModel = RantTableModel(),
    override val dialog: EDialog = EDialog.NONE,
) : MasterState