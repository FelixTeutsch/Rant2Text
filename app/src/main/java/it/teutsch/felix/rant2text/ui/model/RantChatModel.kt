package it.teutsch.felix.rant2text.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.ui.state.RantChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RantChatModel(private val dao: RantDao) : ViewModel() {
    private val _rantChatState = MutableStateFlow(RantChatState("", ""))
    val rantChatState = _rantChatState.asStateFlow()

    fun getRantById(rantId: Int) {
        viewModelScope.launch {
            dao.getRantById(rantId).collect { rant ->
                Log.d("database", "the rant isv ${rant.title}")
                _rantChatState.update { it.copy(title = rant.title, text = rant.text) }
            }
        }
    }

    //for testing inserted one rant into db
//    fun insertRant(rant: RantTableModel) {
//        viewModelScope.launch {
//            dao.insertRant(rant)
//        }
//    }
}