package com.sky.academicproject.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.sky.academicproject.R
import com.sky.academicproject.viewmodel.NewViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private lateinit var  viewModel : NewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(NewViewModel::class.java)

        println("----------------------------")



        /*val time = measureTimeMillis {
            viewModel.async {
                delay(2000L)
                println("async başladı")
                viewModel.getData("bitcoin", 100)
                println("Async bitti")
            }
        }
        println("MAIN içerisinde Async geçen  ${time}")*/

        viewModel.getDataWithCoroutine("dolar",100)
        observeLiveData()
        viewModel.getDataWithAsync("dolar", 100)
        viewModel.getData("dolar", 100)

    }

    private fun observeLiveData()
    {
        viewModel.response.observe(this, Observer{ response->
            response?.let {
                if(it.status == "ok")
                {
                    textView.text = it.articles!!.size.toString()
                }
                else
                {
                    textView.text = it.status
                }
            }
        })

    }
}