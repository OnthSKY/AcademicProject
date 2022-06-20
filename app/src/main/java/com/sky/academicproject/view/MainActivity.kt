package com.sky.academicproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sky.academicproject.R
import com.sky.academicproject.viewmodel.ResponseViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var viewModel : ResponseViewModel
    private val job =  Job()

    var pageSize: Int = 0
    var loopSize: Int = 0
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[ResponseViewModel::class.java]

        loopSize =  10000
        pageSize = 100


      getData("dolar",pageSize,loopSize)

        button.setOnClickListener{
            it?.let {
                Toast.makeText(this, "Butona Tıklandı", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun getData(word:String, pageSize: Int, loopSize: Int)
    {
        var i = 0
        println("----------------------------")
        while(i < loopSize)
        {
            viewModel.getDataWithinThread("dolar",pageSize,loopSize)
            //viewModel.getDataWithinSuspend(word,pageSize)
            i++
        }

        observeLiveData()
    }

    private fun recyclerViewEnjection()
    {
        /*val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = RecyclerAdapter(com.sky.academicproject.model.NewResponse(
        null,null, null))
        recyclerView.adapter = adapter*/
    }
    private fun observeLiveData()
    {
        viewModel.responseNew.observe(this, Observer{ response->
            response?.let {
                if(it.status == "ok")
                {
                    textView.text = (pageSize * loopSize).toString()
                }
                else
                {
                    textView.text = it.status
                }
            }
        })
    }


}