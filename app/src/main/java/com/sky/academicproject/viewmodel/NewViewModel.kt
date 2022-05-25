package com.sky.academicproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sky.academicproject.model.Response
import com.sky.academicproject.sevice.NewAPIService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class NewViewModel : ViewModel() {

    private val api = NewAPIService()
    private val disposable = CompositeDisposable()

    val response = MutableLiveData<Response>()

    fun getData(word: String,pageSize: Int )
    {
        getDataFromInternet(word,pageSize)
    }
    fun getDataDirect()
    {
        disposable.add(
            api.getDataDirect()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response>(){
                    override fun onSuccess(t: Response) {
                        response.value = t
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }
    private fun getDataFromInternet( word: String,pageSize: Int)
    {

        disposable.add(
            api.getData(word,pageSize)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<Response>(){
                    override fun onSuccess(t: Response) {
                        response.value = t
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }
                })
        )
    }


}