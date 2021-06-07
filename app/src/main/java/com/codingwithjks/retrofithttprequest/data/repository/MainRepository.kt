package com.codingwithjks.retrofithttprequest.data.repository

import com.codingwithjks.retrofithttprequest.data.Bus
import com.codingwithjks.retrofithttprequest.data.Network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class MainRepository
@Inject
constructor(private val apiService: ApiService) {

    fun getAllBus(): Flow<List<Bus>> = flow {
        emit(apiService.getAllBus())
    }.flowOn(Dispatchers.IO)


    fun postBus(
        busNo: String,
        town: String
    ): Flow<Bus> = flow {
        emit(apiService.postBus(busNo, town))
    }.flowOn(Dispatchers.IO)

    fun delete(bus_no: String): Flow<Response<Unit>> = flow {
        emit(apiService.delete(bus_no))
    }.flowOn(Dispatchers.IO)

}