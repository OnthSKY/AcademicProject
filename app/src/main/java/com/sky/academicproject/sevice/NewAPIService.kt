package com.sky.academicproject.sevice

import com.sky.academicproject.model.Response
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class NewAPIService {

    private val BASE_URL = "https://newsapi.org/"
    private val API_KEY = "47c5d14a75c645988221b3ce3d5a17af"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(NewAPI::class.java)


    fun getData(word: String,pageSize: Int): Call<Response> {

        return api.getData(word,pageSize)
    }

    /*suspend fun getDataSuspend(word: String,pageSize: Int) : Single<Response>
    {
        return api.getDataSuspend(word,pageSize)
    }*/

    suspend fun getDatax(word: String, pageSize: Int) : Call<Response>
    {
        return api.getDatax(word,pageSize)
    }
}