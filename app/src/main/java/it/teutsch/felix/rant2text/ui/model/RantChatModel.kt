package it.teutsch.felix.rant2text.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.teutsch.felix.rant2text.data.dao.RantDao
import it.teutsch.felix.rant2text.data.dao.TextDao
import it.teutsch.felix.rant2text.data.model.RantListTableModel
import it.teutsch.felix.rant2text.data.model.RantTextTableModel
import it.teutsch.felix.rant2text.ui.enumeration.EDialog
import it.teutsch.felix.rant2text.ui.state.RantChatState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RantChatModel(private val rantDao: RantDao, private val textDao: TextDao) : ViewModel() {
    private val _rantChatState = MutableStateFlow(
        RantChatState(
            "", "",
            RantListTableModel()
        )
    )
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

    fun getRandMessages(rantId: Int) {
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

    fun addRantMessage(rant: RantListTableModel, text: RantTextTableModel) {
        viewModelScope.launch {
            // Update the wordCount and charCount of the rant object
            rant.wordCount += text.text.split("\\s+".toRegex()).size
            rant.charCount += text.text.length
            rantDao.updateRant(rant)

            // Save the message
            textDao.insertText(text)
        }

        _rantChatState.update { it.copy(rant = rant, text = "") }
        viewModelScope.launch {
            rantDao.updateRant(rantChatState.value.rant)
        }
    }

    fun updateRantMessage(
        rantTextTableModel: RantTextTableModel,
        oldMessage: String = ""
    ) {
        val rant = _rantChatState.value.rant
        viewModelScope.launch {
            // Update Rant
            // Update the wordCount and charCount of the rant object
            rant.charCount -= oldMessage.split("\\s+".toRegex()).size
            rant.charCount -= oldMessage.length
            rant.wordCount += rantTextTableModel.text.split("\\s+".toRegex()).size
            rant.charCount += rantTextTableModel.text.length

            // Update last message (if needed)
            if (oldMessage.lowercase() == rant.text.lowercase())
                rant.text = rantTextTableModel.text
            rantDao.updateRant(rant)

            // Update message
            textDao.updateText(rantTextTableModel)
        }
        dismissDialog()
    }

    fun deleteRantMessage(rant: RantListTableModel, text: RantTextTableModel) {
        // TODO: check if deleted message is the newest one. If so replace the message in rant with the second newest or simply with "Latest message was deleted"

        viewModelScope.launch {
            // Update the wordCount and charCount of the rant object
            rant.wordCount -= text.text.split("\\s+".toRegex()).size
            rant.charCount -= text.text.length


            val lastTwoMessages = textDao.getLastTwoMessagesByRantId(rant.id)

            if (lastTwoMessages?.firstOrNull()?.id == text.id) {
                rant.text = lastTwoMessages.getOrNull(1)?.text ?: ""
            }
            rantDao.updateRant(rant)

            // Delete the message
            textDao.deleteText(text)
        }
        dismissDialog()
    }

    fun dismissDialog() {
        _rantChatState.update {
            it.copy(
                targetMessage = RantTextTableModel(),
                dialog = EDialog.NONE
            )
        }
    }

    fun clickDeleteRantMessage() {
        _rantChatState.update {
            it.copy(
                dialog = EDialog.DELETE_TEXT
            )
        }
    }

    fun clickEditRant(text: RantTextTableModel) {
        _rantChatState.update {
            it.copy(
                targetMessage = text,
                dialog = EDialog.EDIT_TEXT
            )
        }
    }

    fun clickDeleteRant() {
        _rantChatState.update {
            it.copy(
                dialog = EDialog.DELETE_RANT
            )
        }
    }

    fun deleteRant(rant: RantListTableModel) {
        viewModelScope.launch {
            rantDao.deleteRant(rant)
        }
        dismissDialog()
    }

    fun updateRant(rantListTableModel: RantListTableModel) {
        viewModelScope.launch {
            rantDao.updateRant(rantListTableModel)
        }
        dismissDialog()
    }

    fun clickUpdateRant() {
        _rantChatState.update {
            it.copy(
                dialog = EDialog.EDIT_RANT
            )
        }
    }
}
