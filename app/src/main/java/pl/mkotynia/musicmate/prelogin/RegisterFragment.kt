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
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.util.TextUtils.Companion.validatePassword
import pl.mkotynia.musicmate.util.getAuth
import pl.mkotynia.musicmate.util.refreshMyProfileInfo

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var _emailEditText: EditText
    private lateinit var _passwordEditText: EditText
    private lateinit var _confirmPasswordEditText: EditText
    private lateinit var _registerButton: Button
    private lateinit var _alreadyHaveAccountTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "RegisterFragment")
        findViews(view)
        setOnClickListeners()
    }

    private fun findViews(view: View) {
        _emailEditText = view.findViewById(R.id.emailEditText)
        _passwordEditText = view.findViewById(R.id.passwordEditText)
        _confirmPasswordEditText = view.findViewById(R.id.confirmPasswordEditText)
        _registerButton = view.findViewById(R.id.registerButton)
        _alreadyHaveAccountTextView = view.findViewById(R.id.alreadyHaveAccountTextView)
    }

    private fun setOnClickListeners() {
        _registerButton.setOnClickListener {
            if(validatePassword(_passwordEditText.text.toString(), _confirmPasswordEditText.text.toString())) {
                getAuth().createUserWithEmailAndPassword(
                    _emailEditText.text.toString(),
                    _passwordEditText.text.toString()
                ).addOnCompleteListener { registerTask ->
                        if (registerTask.isSuccessful) {
                            refreshMyProfileInfo().addOnCompleteListener { refreshProfileTask ->
                                if (refreshProfileTask.isSuccessful) {
                                    findNavController().navigate(R.id.action_register_to_edit_profile)
                                } else {
                                    showToast(R.string.registration_error_toast)
                                }
                            }
                        } else {
                            showToast(R.string.registration_error_toast)
                        }
                    }
            } else {
                showToast(R.string.passwords_not_equal_toast)
            }
        }

        _alreadyHaveAccountTextView.setOnClickListener {
            findNavController().navigate(R.id.action_registration_to_login)
        }

    }

    private fun showToast(messageResource: Int) {
        Toast.makeText(context, getString(messageResource), Toast.LENGTH_LONG)
            .show()
    }
}