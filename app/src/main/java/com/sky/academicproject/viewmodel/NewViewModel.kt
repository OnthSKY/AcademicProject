package com.sky.academicproject.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.sky.academicproject.model.Response
import com.sky.academicproject.sevice.NewAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class NewViewModel(application: Application) : BaseViewModel(application) {

    private val api = NewAPIService()
    private val disposable = CompositeDisposable()

    val responseA = MutableLiveData<Response>()


    fun getData(word: String, pageSize: Int)
    {
         val request = api.getData(word, pageSize)
                 request.enqueue(object : retrofit2.Callback<Response> {
                     override fun onResponse(
                         call: Call<Response>,
                         response: retrofit2.Response<Response>
                     ) {
                         if (response.isSuccessful) {
                             response.body().let {
                                 responseA.value = it
                             }
                         }
                     }

                     override fun onFailure(call: Call<Response>, t: Throwable) {
                         t.printStackTrace()
                     }
                 })
    }

    fun getDataWithinThread(word: String,pageSize: Int )
    {
        thread {
            val time = measureTimeMillis {
               /* val request = api.getData(word, pageSize)
                request.enqueue(object : retrofit2.Callback<Response> {
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        if (response.isSuccessful) {
                            response.body().let {
                                responseA.value = it
                            }
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        t.printStackTrace()
                    }
                })*/
                Thread.sleep(2000L)
            }
            println("Thread kullanımı ile geçen süre ${time} ms")
        }

    }

    fun getDataWithLaunchCoroutine(word: String, pageSize: Int)
    {
        getDataWithinLaunch(word,pageSize)
    }
    private fun getDataWithinLaunch(word:String, pageSize: Int)
    {
        launch {
            val job: Job = launch {
                val request = api.getDatax(word, pageSize)
                request.enqueue(object : retrofit2.Callback<Response> {
                    override fun onResponse(
                        call: Call<Response>,
                        response: retrofit2.Response<Response>
                    ) {
                        if (response.isSuccessful) {
                            response.body().let {
                                responseA.value = it
                                1
                            }
                        }
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
            val time = measureTimeMillis {
                job.join()

            }
            println("Süre is ${time} ms")

        }

    }
   /* private fun getDataFromInternet( word: String,pageSize: Int)
    {
        val time = measureTimeMillis {
            disposable.add(
                api.getData(word, pageSize)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response>() {
                        override fun onSuccess(t: Response) {
                            responseA.value = t
                           // println("Normal Thread name is ${Thread.currentThread().id}")
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })
            )
        }
        println("Normal çağırma işleminde geçen süre ${time} ms")
    }

    private fun getDataWithCoroutineV1(word:String,pageSize: Int)
    {
        val time = measureTimeMillis {

            launch {
                delay(1000L)
              //  println("Coroutine Launch içerisine girdi")
                disposable.add(
                    api.getDataSuspend(word, pageSize)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Response>() {
                            override fun onSuccess(t: Response) {
                                responseA.value = t
                              //  println("Coroutine Thread name is ${Thread.currentThread().id}")
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
                )
            }
        }
        println("Coroutine içerisinde Geçen süre ${time} ms")
    }
    private fun getDataAsync(word: String, pageSize: Int)
    {
        var time = measureTimeMillis {
        async {
                disposable.add(
                    api.getDataSuspend(word,pageSize)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(object : DisposableSingleObserver<Response>(){
                            override fun onSuccess(t: Response) {
                                responseA.value = t
                               // println("Async Thread name is ${Thread.currentThread().state}")
                            }

                            override fun onError(e: Throwable) {
                                e.printStackTrace()
                            }
                        })
                )
            }
        }
        println("Async içerisinde geçen süre ${time} ms ")
    }*/


}