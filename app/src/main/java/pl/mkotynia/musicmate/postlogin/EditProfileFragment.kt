package pl.mkotynia.musicmate.postlogin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.CollectionReference
import com.mkotynia.musicmate.R
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.MusicMateApplication.Companion.USERS_COLLECTION
import pl.mkotynia.musicmate.model.User
import pl.mkotynia.musicmate.util.*

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {

    private val statusesMap: HashMap<String, Int> = hashMapOf("Szukam muzyka" to 0, "Szukam zespołu" to 1, "Nie szukam" to 2)
    private val levelsMap: HashMap<String, Int> = hashMapOf("Początkujący" to 0, "Średniozaawansowany" to 1, "Zaawansowany" to 2)

    private lateinit var _userNameTextView: TextView
    private lateinit var _userNameEditText: EditText
    private lateinit var _emailEditText: EditText
    private lateinit var _statusSpinner: Spinner
    private lateinit var _levelSpinner: Spinner
    private lateinit var _instrumentEditText: EditText
    private lateinit var _rehearsalSpaceCheckBox: CheckBox
    private lateinit var _saveButton: Button
    private lateinit var _signOutButton: Button
    private lateinit var _bottomNavigationView: BottomNavigationView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "EditProfileFragment")
        findViews(view)
        setUi()
        setOnClickListeners()
    }

    private fun findViews(view: View) {
        _userNameTextView = view.findViewById(R.id.userNameTitleTextView)
        _userNameEditText = view.findViewById(R.id.userNameEditText)
        _emailEditText = view.findViewById(R.id.emailEditText)
        _statusSpinner= view.findViewById(R.id.expirienceSpinner)
        _levelSpinner = view.findViewById(R.id.levelSpinner)
        _rehearsalSpaceCheckBox = view.findViewById(R.id.rehearsalSpaceCheckBox)
        _saveButton = view.findViewById(R.id.save_button)
        _signOutButton = view.findViewById(R.id.signOutButton)
        _bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        _instrumentEditText = view.findViewById(R.id.instrumentEditText)
    }

    private fun setOnClickListeners() {
        _saveButton.setOnClickListener {
            getAuth().currentUser?.uid?.let { uid ->
                val users: CollectionReference = getStore().collection(USERS_COLLECTION)
                val user = User(
                    uid,
                    _userNameEditText.text.toString(),
                    _emailEditText.text.toString(),
                    _statusSpinner.selectedItem.toString(),
                    _instrumentEditText.text.toString(),
                    _levelSpinner.selectedItem.toString(),
                    _rehearsalSpaceCheckBox.isChecked
                )
                users.document(uid).set(user).addOnCompleteListener { editUserTask ->
                    if(editUserTask.isSuccessful) {
                        _userNameTextView.text = _userNameEditText.text
                        Toast.makeText(context, getString(R.string.changes_saved_toast), Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, getString(R.string.error_toast), Toast.LENGTH_LONG).show()
                    }
                }
            } ?: Toast.makeText(context, getString(R.string.error_toast), Toast.LENGTH_LONG).show()

        }

        _bottomNavigationView.selectedItemId = R.id.edit_profile
        _bottomNavigationView.setOnItemSelectedListener {
                item ->
            when(item.itemId) {
                R.id.search -> {
                    findNavController().navigate(R.id.action_edit_to_search_profile)
                    true
                }
                R.id.edit_profile -> {
                    true
                }
                else -> false
            }
        }

        _signOutButton.setOnClickListener {
            findNavController().navigate(R.id.action_log_out)
            saveUserSearch(null)
            getAuth().signOut()
        }

    }

    private fun setUi() {
        refreshMyProfileInfo().addOnCompleteListener { getUserTask ->
            if(getUserTask.isSuccessful) {
                getUserTask.result?.let { user ->
                    _userNameTextView.text = user.userName
                    _userNameEditText.setText(user.userName)
                    _instrumentEditText.setText(user.instrument)
                    _emailEditText.setText(user.email)
                    _rehearsalSpaceCheckBox.isChecked = user.rehearsalSpace

                    statusesMap[user.status]?.let {
                        _statusSpinner.setSelection(it)
                    }

                    levelsMap[user.level]?.let {
                        _levelSpinner.setSelection(it)
                    }
                }
            }
        }
    }
}