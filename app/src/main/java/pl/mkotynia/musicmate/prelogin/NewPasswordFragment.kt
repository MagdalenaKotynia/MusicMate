package pl.mkotynia.musicmate.prelogin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mkotynia.musicmate.R
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.util.getAuth

class NewPasswordFragment : Fragment(R.layout.fragment_password_reset) {

    companion object {
        const val OOB_CODE_NAV_ARGUMENT = "oobToken"
    }

    private var _oobCode: String? = null

    private lateinit var _resetPasswordButton: Button
    private lateinit var _newPasswordEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "NewPasswordFragment")
        findViews(view)
        getOobTokenFromArguments()
        setListeners()
    }

    private fun getOobTokenFromArguments() {
        if (arguments != null) {
            _oobCode = arguments?.getString(OOB_CODE_NAV_ARGUMENT)
        }
    }

    private fun setListeners() {
        _resetPasswordButton.setOnClickListener {
            _oobCode?.let {

                getAuth().confirmPasswordReset(it,_newPasswordEditText.text.toString()).addOnCompleteListener { confirmPasswordResetTask ->
                    if (confirmPasswordResetTask.isSuccessful) {
                        findNavController().navigate(R.id.action_new_password_to_login)
                    } else {
                        showFailMessage()
                    }
                }
            } ?: showFailMessage()

        }
    }

    private fun findViews(view: View) {
        _resetPasswordButton = view.findViewById(R.id.resetPasswordButton)
        _newPasswordEditText = view.findViewById(R.id.newPasswordEditText)
    }

    private fun showFailMessage() {
        Toast.makeText(context, getString(R.string.password_reset_fail_text), Toast.LENGTH_LONG).show()
    }
}