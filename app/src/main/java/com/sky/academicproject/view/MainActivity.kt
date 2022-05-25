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

class MainActivity : AppCompatActivity() {
    private lateinit var  viewModel : NewViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(NewViewModel::class.java)
        //viewModel.getData("bitcoin",5)
        viewModel.getDataWihtCoroutine("bitcoin",5)
        observeLiveData()
    }

    private fun observeLiveData()
    {
        println("Observe Live Data içerisi")
        viewModel.response.observe(this, Observer{ response->
            response?.let {
                println("Let içerisi")
                if(it.status == "ok")
                {
                    println("Ok içerisi")
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