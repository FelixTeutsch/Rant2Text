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

class RantChatModel(private val rantDao: RantDao, private val textDao: TextDao) : ViewModel() {
    private val _rantChatState = MutableStateFlow(RantChatState("", "", RantTableModel()))
    val rantChatState = _rantChatState.asStateFlow()

    fun getRantById(rantId: Int) {
        viewModelScope.launch {
            rantDao.getRantById(rantId).collect { rant ->
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
            rantDao.updateRant(rantChatState.value.rant)
        }
    }


    fun getTextMsgs(rantId: Int) {
        viewModelScope.launch {
            textDao.getTextsByRantId(rantId).collect { texts ->
                if (texts != null) {
                    _rantChatState.update { it.copy(texts = texts) }
                } else {
                    _rantChatState.update { it.copy(texts = emptyList()) }
                }
            }
        }
    }

    fun saveTextMsg(rant: RantTableModel, text: TextTableModel) {
        viewModelScope.launch {
            // Update the wordCount and charCount of the rant object
            rant.wordCount += text.text.split("\\s+".toRegex()).size
            rant.charCount += text.text.length
            rantDao.updateRant(rant)

            // Save the message
            textDao.insertText(text)
        }
    }

    fun updateTextMsg(rant: RantTableModel, oldText: String, newText: TextTableModel) {
        viewModelScope.launch {
            // Update the wordCount and charCount of the rant object
            rant.wordCount -= oldText.split("\\s+".toRegex()).size
            rant.charCount -= oldText.length
            rant.wordCount += newText.text.split("\\s+".toRegex()).size
            rant.charCount += newText.text.length

            rant.text = newText.text
            rantDao.updateRant(rant)


            textDao.updateText(newText)
        }
    }

    fun deleteTextMsg(rant: RantTableModel, text: TextTableModel) {
        viewModelScope.launch {
            // Update the wordCount and charCount of the rant object
            rant.wordCount -= text.text.split("\\s+".toRegex()).size
            rant.charCount -= text.text.length


            val lastTwoMessages = textDao.getLastTwoMessagesByRantId(rant.id)

            if (lastTwoMessages?.firstOrNull()?.textId == text.textId) {
                rant.text = lastTwoMessages.getOrNull(1)?.text ?: ""
            }
            rantDao.updateRant(rant)

            // Delete the message
            textDao.deleteText(text)
        }
    }


}
