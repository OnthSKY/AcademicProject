package com.sky.academicproject.viewmodel

import android.app.Application

import androidx.lifecycle.MutableLiveData
import com.sky.academicproject.model.Response
import com.sky.academicproject.sevice.NewAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class NewViewModel(application: Application) : BaseViewModel(application) {

    private val api = NewAPIService()
    private val disposable = CompositeDisposable()

    val response = MutableLiveData<Response>()


    fun getData(word: String, pageSize: Int)
    {
        getDataFromInternet(word, pageSize)
    }

    fun getDataWithAsync(word: String,pageSize: Int )
    {
        getDataAsync(word,pageSize)
    }


    fun getDataWithCoroutine(word: String, pageSize: Int)
    {
        getDataWithCoroutineV1(word, pageSize)
    }

    private fun getDataFromInternet( word: String,pageSize: Int)
    {
        val time = measureTimeMillis {
            disposable.add(
                api.getData(word, pageSize)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<Response>() {
                        override fun onSuccess(t: Response) {
                            response.value = t
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
                                response.value = t
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
                                response.value = t
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
    }


}