package com.vidovicbranimir.githubdemo.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vidovicbranimir.githubdemo.R
import com.vidovicbranimir.githubdemo.data.UserPreferences


class MainActivity : AppCompatActivity() {

    private val clientId = "07f1a1a2948e882fa91f"
    private val clientSecret = "0ba762306e0f0e15bbd7f7c99df817c1ef9d4773"
    private val redirectUri = "githubdemo://callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


}