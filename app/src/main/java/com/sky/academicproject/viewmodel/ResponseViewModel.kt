package com.sky.academicproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sky.academicproject.model.NewResponse
import com.sky.academicproject.sevice.NewAPIService
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread
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
    private val disposable = CompositeDisposable()
    val responseNew = MutableLiveData<NewResponse>()

    fun getDataSuspendResponse(word: String, pageSize: Int)
    {
        viewModelScope.launch(Dispatchers.Main) {
            println("Kullanılan Thread ${Thread.currentThread().name}")
            val jobA:Job =  launch(Dispatchers.IO){
                println("Kullanılan Thread ${Thread.currentThread().name}")
                  api.getDataSuspendResponse(word, pageSize).body().apply {
                       this?.let {
                           responseNew.postValue(it)
                       }
                   }
               }
            measureTimeMillis {
                jobA.join()
            }.apply {
                println("Geçen Süre ${this} ms")
            }
        }
    }
    fun getDataDirectWithinSuspend()
    {
        viewModelScope.launch(Dispatchers.Default){
            println("Default Dispatchers içerisinde = ${Thread.currentThread().name} ")
            val responseJob = launch(Dispatchers.IO) {
                val serviceRequest = api.getDataDirectSuspend()
               if(serviceRequest.totalResults != 0){
                   println("IO Dispatchers içerisinde = ${Thread.currentThread().name} ")
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
        //println("Ana thread ismi ${Thread.currentThread().name}")
        viewModelScope.launch(Dispatchers.Main){
            //println("Dispatcher Main Launch Thread =  ${Thread.currentThread().name}")
            val responseJob = launch(Dispatchers.IO) {
               // println("Dispatcher IO Launch içi Kullanılan Thread ${Thread.currentThread().name}")
                val serviceRequest = api.getDataWithinSuspend(word,pageSize)
                if(serviceRequest.totalResults != 0){
                   // println("İşin ortasında = ${Thread.currentThread().name}")
                    responseNew.postValue(serviceRequest)
                }else{
                 //   println("Hata")
                }
            }
            val time = measureTimeMillis {
                responseJob.join()
            }

            println("Launch ile geçen süre ${time} ms")

        }
    }
    fun getDataWithinLaunchLoop(word: String, pageSize: Int, loopSize: Int?) = runBlocking()
    {
        println("Temel Run Blocking içerisnde isim = ${Thread.currentThread().name} ve id = ${Thread.currentThread().id}")
        val jobA: Job = viewModelScope.launch(Dispatchers.IO) {
            println("Launch içerisinde isim = ${Thread.currentThread().name} ve id = ${Thread.currentThread().id}")


            loopSize?.let {
            var i = 0
                while(i<loopSize) {
                    val serviceRequest = api.getDataWithinSuspend(word, pageSize)
                    if (serviceRequest.totalResults != 0) {
                        println("döngü sırası : ${i}")
                        responseNew.postValue(serviceRequest)
                    } else {
                        println("Data çekerken hata")
                    }
                    i++
                }
            }
        }
        measureTimeMillis {
            jobA.join()
        }.apply {
            println("Geçen süre ${this}")
        }

    }
    fun getDataWithinSuspendAsync(word: String, pageSize: Int)
    {
        println("Ana thread ismi ${Thread.currentThread().name}")
        viewModelScope.launch(Dispatchers.Default) {
            println("İlk Launch Kullanılan Thread = ${Thread.currentThread().name}")
            val time = measureTimeMillis {
                viewModelScope.async(Dispatchers.IO) {
                   println("Async içi thread ismi =  ${Thread.currentThread().name}")
                    val serviceRequest = api.getDataWithinSuspend(word,pageSize)
                    if(serviceRequest.totalResults != 0){
                        responseNew.postValue(serviceRequest)
                    }else{
                        println("Hata")
                    }

                }.await()
            }
            println("Async içerisinde geçen süre ${time} ms")


        }
    }

    fun getDataWithinThread(word: String, pageSize:Int)
    {
        println("Ana Thread ismi ${Thread.currentThread().name}")
        thread {
            println("Yeni Thread bloğu içerisinde Thread ismi ${Thread.currentThread().name}")
           val time = measureTimeMillis {
               Thread.sleep(300L)
            }

            println("Geçen Süre ${time.toString()} ms")
        }
    }

    fun getData(word:String, pageSize:Int)
    {
        println("Fonksiyon başında Thread ${Thread.currentThread().id}")
        runBlocking {
            println("Run Blocking içerisinde =${Thread.currentThread().name} ${Thread.currentThread().id}")
            thread{
                api.getData(word, pageSize).enqueue(object : Callback<NewResponse> {
                    override fun onResponse(call: Call<NewResponse>, response: Response<NewResponse>) {
                        println("Fonksiyon içerisinde Tanımlama yaparken kullılan Thread ${Thread.currentThread().name}")
                        response.body()?.let {
                            responseNew.postValue(it)
                        }
                    }

                    override fun onFailure(call: Call<NewResponse>, t: Throwable) {
                        t.printStackTrace()
                    }

                })
            }
        }
    }
    fun getDataDeferredResponse(word:String, pageSize: Int)
    {
        viewModelScope.launch(Dispatchers.Main) {
            println("First Launch = ${Thread.currentThread().name}")
            val jobA = launch(Dispatchers.IO) {
                println("Second Launch = ${Thread.currentThread().name}")
                 val apiRequest = api.getDataDeferredAsync(word,pageSize).await()
                if(apiRequest.isSuccessful)
                {
                    apiRequest.body()?.let {
                        responseNew.postValue(it)
                    }
                }
            }

            measureTimeMillis {
                jobA.join()
            }.apply {
                println("Geçen Süre ${this} MS")
            }
        }
    }
}
