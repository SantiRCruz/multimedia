package com.example.estrellitas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.local.NoteDao
import com.example.estrellitas.data.models.NoteEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn


class NoteViewModel(private val dao : NoteDao): ViewModel() {
    fun fetchNotes(): StateFlow<Result<List<NoteEntity>>> = flow {
        kotlin.runCatching {
            dao.getNotes()
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )

    fun saveNote(noteEntity: NoteEntity): StateFlow<Result<Long>> = flow {
        kotlin.runCatching {
            dao.insertNote(noteEntity)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )

    fun updateNote(noteEntity: NoteEntity): StateFlow<Result<Int>> = flow {
        kotlin.runCatching {
            dao.updateNote(noteEntity)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )

    fun deleteNote(noteEntity: NoteEntity): StateFlow<Result<Int>> = flow {
        kotlin.runCatching {
            dao.deleteNote(noteEntity)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )
}

class NoteViewModelFactory(private val dao : NoteDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NoteDao::class.java).newInstance(dao)
    }

}