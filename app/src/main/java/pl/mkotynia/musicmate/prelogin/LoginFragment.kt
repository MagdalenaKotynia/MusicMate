package pl.mkotynia.musicmate.prelogin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mkotynia.musicmate.R
import com.mkotynia.musicmate.R.id.*
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.util.getAuth

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var _emailEditText: EditText
    private lateinit var _passwordEditText: EditText
    private lateinit var _loginButton: Button
    private lateinit var _registerTextView: TextView
    private lateinit var _passwordResetTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "LoginFragment")
        findViews(view)
        setListeners()
    }

    private fun findViews(view: View) {
        _emailEditText = view.findViewById(emailEditText)
        _passwordEditText = view.findViewById(passwordEditText)
        _loginButton = view.findViewById(loginButton)
        _registerTextView = view.findViewById(registerTextView)
        _passwordResetTextView = view.findViewById(forgotPasswordTextView)
    }

    private fun setListeners() {
        _loginButton.setOnClickListener {
            getAuth().signInWithEmailAndPassword(_emailEditText.text.toString(), _passwordEditText.text.toString()).addOnCompleteListener { loginTask ->
                if(loginTask.isSuccessful) {
                    findNavController().navigate(action_login_to_search)
                } else {
                    Toast.makeText(context, getString(R.string.login_error_toast), Toast.LENGTH_LONG).show()
                }
            }
        }

        _registerTextView.setOnClickListener {
            findNavController().navigate(action_login_to_register)
        }

        _passwordResetTextView.setOnClickListener {
            findNavController().navigate(action_login_to_request_password_reset)
        }
    }
}