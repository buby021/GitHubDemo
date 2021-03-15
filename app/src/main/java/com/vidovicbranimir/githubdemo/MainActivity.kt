package com.vidovicbranimir.githubdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vidovicbranimir.githubdemo.data.network.RestClient
import com.vidovicbranimir.githubdemo.ui.login.LoginActivity
import com.vidovicbranimir.githubdemo.ui.repository.RepositoryListActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val clientId = "07f1a1a2948e882fa91f"
    private val clientSecret = "0ba762306e0f0e15bbd7f7c99df817c1ef9d4773"
    private val redirectUri = "githubdemo://callback"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
//            delay(1500)
        }.invokeOnCompletion {
        finish()
        startActivity(Intent(this, RepositoryListActivity::class.java))
        }
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize" + "?clinet_id=" + clientId + "&redirect_uri=" + redirectUri))
//        startActivity(intent)
    }
}