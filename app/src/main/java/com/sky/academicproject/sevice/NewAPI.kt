package com.sky.academicproject.sevice

import com.sky.academicproject.model.Response
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewAPI {

    // url -> https://newsapi.org/v2/everything?q=bitcoin&pageSize=1&apiKey=47c5d14a75c645988221b3ce3d5a17af
    // base url -> https://newsapi.org/


    @GET(" v2/everything?apiKey=47c5d14a75c645988221b3ce3d5a17af")
    fun getData

                ( @Query("q") word: String,
                  @Query("pageSize") pageSize: Int
                 ) : Single<Response>

    @GET("v2/everything?q=bitcoin&pageSize=1&apiKey=47c5d14a75c645988221b3ce3d5a17af")
    fun getDataDirect() :Single<Response>

}