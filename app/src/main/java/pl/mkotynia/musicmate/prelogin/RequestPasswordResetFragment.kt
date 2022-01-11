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

class RequestPasswordResetFragment : Fragment(R.layout.fragment_request_password_reset) {

    private lateinit var _emailEditText: EditText
    private lateinit var _goToLoginTextView: TextView
    private lateinit var _resetPasswordButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "RequestPasswordResetFragment")
        findViews(view)
        setListeners()
    }

    private fun findViews(view: View) {
        _emailEditText = view.findViewById(emailEditText)
        _goToLoginTextView = view.findViewById(goToLoginTextView)
        _resetPasswordButton = view.findViewById(resetPasswordButton)
    }

    private fun setListeners() {
        _goToLoginTextView.setOnClickListener {
            findNavController().navigate(action_reset_password_request_to_login)
        }

        _resetPasswordButton.setOnClickListener {
           getAuth().sendPasswordResetEmail(_emailEditText.text.toString()).addOnCompleteListener { sendEmailTask ->
               val message = if(sendEmailTask.isSuccessful) R.string.email_send_success_text else R.string.email_send_failed_text
               Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}