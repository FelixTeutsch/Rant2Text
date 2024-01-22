package it.teutsch.felix.rant2text.ui.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.data.dao.TextDao
import it.teutsch.felix.rant2text.data.model.RantTableModel
import it.teutsch.felix.rant2text.data.model.TextTableModel
import it.teutsch.felix.rant2text.ui.state.RantChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RantChatModel(private val rantdao: RantDao, private val textDao: TextDao) : ViewModel() {
    private val _rantChatState = MutableStateFlow(RantChatState("", "", RantTableModel()))
    val rantChatState = _rantChatState.asStateFlow()

    fun getRantById(rantId: Int) {
        viewModelScope.launch {
            rantdao.getRantById(rantId).collect { rant ->
                Log.d("personalErr", "there is an err with the rant: $rant")
//                Log.d("database", "the rant isv ${rant.title}")
                if (rant != null) {
                    _rantChatState.update {
                        it.copy(
                            title = rant.title,
                            text = rant.text,
                            rant = rant,
                            angerLevel = rant.angerLevel
                        )
                    }
                } else {
                    _rantChatState.update { it.copy(title = "Generic", text = "Generic text") }
                }
            }
        }
    }

    fun saveRantMsg(rantMsg: RantTableModel) {
        //TODO: update the save to creta e a new table instead of editing the passed rant
        _rantChatState.update { it.copy(rant = rantMsg) }
        Log.d("Personal", "${rantChatState.value.rant}")
        viewModelScope.launch {
            rantdao.updateRant(rantChatState.value.rant)
        }
    }

    fun saveTextMsg(textMsg: TextTableModel) {
        viewModelScope.launch {
            textDao.insertText(textMsg)
        }
    }

    fun getTextMsgs(rantId: Int) {
        viewModelScope.launch {
            textDao.getTextsByRantId(rantId).collect { texts ->
                Log.d("personalErr", "there is an err with the texts: $texts")
                if (texts != null) {
                    _rantChatState.update { it.copy(texts = texts) }
                } else {
                    _rantChatState.update { it.copy(texts = emptyList()) }
                }
            }
        }
    }

    fun updatetextMsg(textMsg: TextTableModel) {
        viewModelScope.launch {
            textDao.updateText(textMsg)
        }
    }

    fun deleteTextMsg(textMsg: TextTableModel) {
        viewModelScope.launch {
            textDao.deleteText(textMsg)
        }
    }

}
