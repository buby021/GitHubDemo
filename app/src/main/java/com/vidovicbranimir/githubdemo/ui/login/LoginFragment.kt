package com.vidovicbranimir.githubdemo.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.vidovicbranimir.githubdemo.R
import com.vidovicbranimir.githubdemo.data.network.ApiService
import com.vidovicbranimir.githubdemo.data.network.RestClient
import com.vidovicbranimir.githubdemo.databinding.FragmentLoginBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private val clientId = "07f1a1a2948e882fa91f"
    private val clientSecret = "0ba762306e0f0e15bbd7f7c99df817c1ef9d4773"
    private val redirectUri = "githubdemo://callback"
    lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflater2 = inflater.inflate(R.layout.fragment_login, container, false)
        binding = getFragmentBinding(inflater, container)

        return inflater2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button = view.findViewById(R.id.buttonLogin)


            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://github.com/login/oauth/authorize" + "?clinet_id=" + clientId + "&redirect_uri=" + redirectUri)
            )
            startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent

        // the intent filter defined in AndroidManifest will handle the return from ACTION_VIEW intent
        val uri = activity?.intent?.data
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            val code = uri.getQueryParameter("code")
            if (code != null) {
                // get access token
                // we'll do that in a minute
                Toast.makeText(activity, code.toString(), Toast.LENGTH_LONG)
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
                Toast.makeText(activity, "ERROR", Toast.LENGTH_LONG)
            }
        }
    }

    fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)
}