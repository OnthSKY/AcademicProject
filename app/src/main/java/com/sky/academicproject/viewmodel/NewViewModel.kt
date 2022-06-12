package com.sky.academicproject.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sky.academicproject.model.NewResponse
import com.sky.academicproject.sevice.NewAPIService
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import retrofit2.Call
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class NewViewModel(application: Application) : BaseViewModel(application) {

    private val api = NewAPIService()
    private val disposable = CompositeDisposable()

    val responseNew = MutableLiveData<NewResponse>()


    fun getData(word: String, pageSize: Int)
    {
         val request = api.getData(word, pageSize)
                 request.enqueue(object : retrofit2.Callback<NewResponse> {
                     override fun onResponse(
                         call: Call<NewResponse>,
                         response: retrofit2.Response<NewResponse>
                     ) {
                         if (response.isSuccessful) {
                             response.body().let {
                                 responseNew.value = it
                             }
                         }
                     }

                     override fun onFailure(call: Call<NewResponse>, t: Throwable) {
                         t.printStackTrace()
                     }
                 })
    }

    fun getDataWithinThread(word: String,pageSize: Int ) : Long
    {
        var totalTime: Long = 0L
        thread {
            val time = measureTimeMillis {
                val request = api.getData(word, pageSize)
                request.enqueue(object : retrofit2.Callback<NewResponse> {
                    override fun onResponse(
                        call: Call<NewResponse>,
                        response: retrofit2.Response<NewResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body().let {
                                responseNew.value = it
                            }
                        }
                    }

                    override fun onFailure(call: Call<NewResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
                //Thread.sleep(2000L)
            }
            totalTime += time
            println("Thread ile total time : ${totalTime} ms")
           // println("Thread kullanımı ile geçen süre ${time} ms")
        }
        return totalTime
    }

    fun getDataWithLaunchCoroutine(word: String, pageSize: Int)
    {
        getDataWithinLaunch(word,pageSize)
    }
    private fun getDataWithinLaunch(word:String, pageSize: Int)
    {
        viewModelScope.launch(Dispatchers.Default) {
            val job: Job = launch {
                val request = api.getDataWithinSuspendCall(word, pageSize)
                request.enqueue(object : retrofit2.Callback<NewResponse> {
                    override fun onResponse(
                        call: Call<NewResponse>,
                        response: retrofit2.Response<NewResponse>
                    ) {
                        if (response.isSuccessful) {
                            response.body().let {
                                responseNew.value = it
                            }
                        }
                    }

                    override fun onFailure(call: Call<NewResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
            val time = measureTimeMillis {
                job.join()

            }
            println("ViewModelScope.Launch Süre is ${time} ms")

        }

    }

    fun getDataResponse()
    {
        val job: Job = CoroutineScope(Dispatchers.Main).launch {
            val response = api.getDataDirectWithinSuspend()
            val responseJob = withContext(Dispatchers.Default){
                if(response.isSuccessful){
                     response.body().let {
                         responseNew.value = it
                         println("İşlem Tamam")
                     }
                }
                else{
                    println("Hata ! ${response.message()}")
                }
            }
        }
        launch {
            job.join()
        }
    }

    fun getDataAsync(word: String, pageSize: Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            val time = measureTimeMillis {
                async {
                    val request = api.getDataWithinSuspendCall(word, pageSize)
                    request.enqueue(object : retrofit2.Callback<NewResponse> {
                        override fun onResponse(
                            call: Call<NewResponse>,
                            response: retrofit2.Response<NewResponse>
                        ) {
                            if (response.isSuccessful) {
                                response.body().let {
                                    responseNew.value = it
                                }
                            }
                        }

                        override fun onFailure(call: Call<NewResponse>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }.await()
            }

            println("Async time is ${time} ms")
        }
    }
    fun deneme()
    {
        launch{
            val  time = measureTimeMillis {
                val request = api.getAsync().await()
                if (request.isSuccessful) {
                    request.body().let {
                        responseNew.postValue(it)
                    }
                } else {
                    println("Hatalı")
                }
            }
            println("Deneme içerisinde  ${time} ms")
        }
    }
}

