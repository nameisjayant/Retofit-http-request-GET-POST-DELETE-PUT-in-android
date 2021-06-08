package com.codingwithjks.retrofithttprequest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingwithjks.retrofithttprequest.data.Bus
import com.codingwithjks.retrofithttprequest.data.repository.MainRepository
import com.codingwithjks.retrofithttprequest.util.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val mainRepository: MainRepository) : ViewModel() {

    private val _busData: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val busData: StateFlow<ApiState> = _busData

    fun getAllData() = viewModelScope.launch {
        mainRepository.getAllBus()
            .onStart {
                _busData.value = ApiState.Loading
            }.catch { e ->
                _busData.value = ApiState.Failure(e)
            }.collect { response ->
                _busData.value = ApiState.Success(response)
            }
    }

    fun postBus(
        busNo: String,
        town: String
    ) = mainRepository.postBus(busNo, town)

    fun delete(
        bus_no: String
    ) = mainRepository.delete(bus_no)

    fun update(
        busId: String,
        busNo: String,
        town: String
    ) = mainRepository.update(busId, busNo, town)

}
