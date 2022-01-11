package pl.mkotynia.musicmate.postlogin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mkotynia.musicmate.R
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.model.User
import pl.mkotynia.musicmate.postlogin.SearchProfileFragment.Companion.USER_NAV_ARGUMENT

class ViewProfileFragment : Fragment(R.layout.fragment_view_profile) {

    private var _user: User? = null

    private lateinit var _returnButton: Button
    private lateinit var _userNameText: TextView
    private lateinit var _userNameTextView: TextView
    private lateinit var _emailTextView: TextView
    private lateinit var _instrumentTextView: TextView
    private lateinit var _levelTextView: TextView
    private lateinit var _statusTextView: TextView
    private lateinit var _rehearsalValueTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "ViewProfileFragment")
        getUserFromArguments()
        findViews(view)
        setClickListeners()
        setUpUi()
    }

    private fun setClickListeners() {
        _returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_view_to_search_profile)
        }
    }

    private fun findViews(view: View) {
        _returnButton = view.findViewById(R.id.return_button)
        _userNameText = view.findViewById(R.id.userNameText)
        _userNameTextView = view.findViewById(R.id.userNameValueTextView)
        _emailTextView = view.findViewById(R.id.emailValueTextView)
        _instrumentTextView = view.findViewById(R.id.instrumentValueTextView)
        _levelTextView = view.findViewById(R.id.levelValueTextView)
        _statusTextView = view.findViewById(R.id.statusValueTextView)
        _rehearsalValueTextView = view.findViewById(R.id.rehearsalValueTextView)
    }

    private fun getUserFromArguments() {
        if (arguments != null) {
            _user = arguments?.getParcelable(USER_NAV_ARGUMENT) as User?
        }
    }

    private fun setUpUi() {
        _user?.let {
            _userNameText.text = it.userName
            _userNameTextView.text = it.userName
            _emailTextView.text = it.email
            _instrumentTextView.text = it.instrument
            _levelTextView.text = it.level
            _statusTextView.text = it.status
            _rehearsalValueTextView.setText(if( it.rehearsalSpace) R.string.yes_text else R.string.no_text)
        }
    }
}