package com.sky.academicproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.academicproject.model.NewResponse
import com.sky.academicproject.sevice.NewAPIService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import kotlin.coroutines.CoroutineContext
import kotlin.system.measureTimeMillis


class ResponseViewModel: ViewModel(), CoroutineScope {


      private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
    private val api = NewAPIService()
    val responseNew = MutableLiveData<NewResponse>()


    fun getDataDirectWithinSuspend()
    {
        viewModelScope.launch(Dispatchers.Default){
            val responseJob = launch {
                val serviceRequest = api.getDataDirectSuspend()
               if(serviceRequest.totalResults != 0){
                   responseNew.postValue(serviceRequest)
               }
            }
            val time = measureTimeMillis {
                responseJob.join()
            }

            println("Launch ile geçen süre ${time} ms")

        }
    }

    fun getDataWithinSuspend(word: String, pageSize: Int)
    {
        println("Ana thread ismi ${Thread.currentThread().name}")
        viewModelScope.launch(Dispatchers.Default){
            println("İlk Launch Kullanılan Thread ${Thread.currentThread().name}")
            val responseJob = launch {
                println("Request Launch içi Kullanılan Thread ${Thread.currentThread().name}")
                val serviceRequest = api.getDataWithinSuspend(word,pageSize)
                if(serviceRequest.totalResults != 0){
                    responseNew.postValue(serviceRequest)
                }else{
                    println("Hata")
                }
            }
            val time = measureTimeMillis {
                responseJob.join()
            }

            println("Launch ile geçen süre ${time} ms")

        }
    }

    fun getDataWithinSuspendAsync(word: String, pageSize: Int) : Long
    {
        var a = 0L
        println("Ana thread ismi ${Thread.currentThread().name}")
        viewModelScope.launch(Dispatchers.Main) {
            println("İlk Launch Kullanılan Thread ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                viewModelScope.async(Dispatchers.Default) {
                   println("Async içi thread ismi ${Thread.currentThread().name}")
                    val serviceRequest = api.getDataDirectSuspend()
                    if(serviceRequest.totalResults != 0){
                        responseNew.postValue(serviceRequest)
                    }else{
                        println("Hata")
                    }

                }.await()
            }
            a = time
            println("Async içerisinde geçen süre ${time} ms")


        }
        return a
    }
    fun getDataWithinThread(word: String, pageSize:Int)
    {

    }
}
