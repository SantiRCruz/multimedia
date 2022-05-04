package com.example.estrellitas.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.estrellitas.core.Result
import com.example.estrellitas.data.local.MusicDao
import com.example.estrellitas.data.models.MusicEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn


class MusicViewModel(private val dao : MusicDao): ViewModel() {
    fun fetchMusic(): StateFlow<Result<List<MusicEntity>>> = flow {
        kotlin.runCatching {
            dao.getMusic()
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

    fun saveMusic(musicEntity: MusicEntity): StateFlow<Result<Long>> = flow {
        kotlin.runCatching {
            dao.insertMusic(musicEntity)
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

    fun deleteMusic(musicEntity: MusicEntity): StateFlow<Result<Int>> = flow {
        kotlin.runCatching {
            dao.deleteMusic(musicEntity)
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

class MusicViewModelFactory(private val dao : MusicDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(MusicDao::class.java).newInstance(dao)
    }

}