package com.sky.academicproject.sevice

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sky.academicproject.model.NewResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewAPIService {

    private val BASE_URL = "https://newsapi.org/"
    private val API_KEY = "47c5d14a75c645988221b3ce3d5a17af"
    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(NewAPI::class.java)


    fun getData(word: String,pageSize: Int): Call<NewResponse> {

        return api.getData(word,pageSize)
    }

    suspend fun getDataDirectSuspend() : NewResponse
    {
        return api.getDataDirectWithinSuspendCall()
    }

    suspend fun getDataWithinSuspendCall(word: String, pageSize: Int) : Call<NewResponse>
    {
        return api.getDatax(word,pageSize)
    }


    suspend fun getDataDirectWithinSuspend() : retrofit2.Response<NewResponse?>
    {
        return api.getDataDirectWithinSuspend()
    }

    suspend fun getDataWithinSuspend(word:String, pageSize: Int) : NewResponse
    {
        return api.getDataSuspend(word,pageSize)
    }
     fun getAsync() : Deferred<retrofit2.Response<NewResponse?>>
    {
        return api.getAsync()
    }
}