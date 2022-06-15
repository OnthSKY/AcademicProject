package com.sky.academicproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sky.academicproject.R
import com.sky.academicproject.adapter.RecyclerAdapter
import com.sky.academicproject.viewmodel.ResponseViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {



    private lateinit var adapter: RecyclerAdapter
    private lateinit var viewModel : ResponseViewModel
    private val job =  Job()


    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(com.sky.academicproject.model.NewResponse(
            null,null, null))
        recyclerView.adapter = adapter*/

        println("----------------------------")

        viewModel = ViewModelProvider(this)[ResponseViewModel::class.java]
        println("Main Activity içerisinde Thread ${Thread.currentThread().id}")
        viewModel.getDataWithinLaunchLoop("dolar", 100,100)
        observeLiveData()

        button.setOnClickListener{
            it?.let {
                Toast.makeText(this, "Butona Tıklandı", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getData(word:String, pageSize: Int) = runBlocking(Dispatchers.Main) {
        viewModel.getDataWithinSuspend(word, pageSize)
        observeLiveData()
    }
    private fun observeLiveData()
    {
        viewModel.responseNew.observe(this, Observer{ response->
            response?.let {
                if(it.status == "ok")
                {
                    textView.text = it.articles!!.size.toString()
                   // adapter.refreshData(it)
                }
                else
                {
                    textView.text = it.status
                }
            }
        })
    }


}