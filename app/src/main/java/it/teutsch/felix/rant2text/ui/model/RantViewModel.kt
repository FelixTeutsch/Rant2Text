package it.teutsch.felix.rant2text.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog
import it.teutsch.felix.rant2text.ui.state.RantViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RantViewModel(private val dao: RantDao) : ViewModel() {
    private val _rantViewState = MutableStateFlow(RantViewState())
    val rantViewState = _rantViewState.asStateFlow()

    fun getRants() {
        viewModelScope.launch {
            dao.getRants().collect { rants ->
                _rantViewState.update { it.copy(rants = rants) }
            }
        }
    }

    fun deleteRant(rant: RantTableModel) {
        dismissDialog()
        viewModelScope.launch {
            dao.deleteRant(rant)
        }
    }

    fun editRant(rant: RantTableModel? = null) {
        _rantViewState.update {
            it.copy(
                targetRant = rant ?: RantTableModel(),
                dialog = EDialog.EDIT_RANT
            )
        }
    }

    fun saveRant(rant: RantTableModel) {
        dismissDialog()
        viewModelScope.launch {
            dao.insertRant(rant)
        }
    }

    fun updateRant(rant: RantTableModel) {
        dismissDialog()
        viewModelScope.launch {
            dao.updateRant(rant)
        }
    }

    fun dismissDialog() {
        _rantViewState.update {
            it.copy(
                targetRant = RantTableModel(),
                dialog = EDialog.NONE
            )
        }
    }

}