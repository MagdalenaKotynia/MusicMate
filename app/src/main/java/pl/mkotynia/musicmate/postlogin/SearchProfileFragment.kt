package pl.mkotynia.musicmate.postlogin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mkotynia.musicmate.R

import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.MusicMateApplication.Companion.USERS_COLLECTION
import pl.mkotynia.musicmate.model.User
import pl.mkotynia.musicmate.model.UserSearch
import pl.mkotynia.musicmate.util.*


class SearchProfileFragment : Fragment(R.layout.fragment_search_profile) {

    companion object {
        const val USER_NAV_ARGUMENT: String = "USER_NAV_ARGUMENT"
        const val USER_FIELD = "userName"
        const val EMAIL_FIELD = "email"
        const val STATUS_FIELD = "status"
        const val INSTRUMENT_FIELD = "instrument"
        const val LEVEL_FIELD = "level"
        const val REHEARSAL_FIELD = "rehearsalSpace"
        const val IGNORE = "Dowolny"
    }

    private lateinit var _searchButton: Button
    private lateinit var _bottomNavigationView: BottomNavigationView
    private lateinit var _searchResultsRecyclerView: RecyclerView
    private lateinit var _userNameEditText: EditText
    private lateinit var _emailEditText: EditText
    private lateinit var _instrumentEditText: EditText
    private lateinit var _levelSpinner: Spinner
    private lateinit var _statusSpinner: Spinner
    private lateinit var _rehearsalSpaceCheckBox: CheckBox

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(LOG_TAG, "SearchProfileFragment")
        findViews(view)
        setOnClickListeners()
        setUpRecyclerView()
        setUpUi()
    }

    private fun setUpRecyclerView() {
        _searchResultsRecyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val adapter = SearchResultsAdapter(object: SearchResultsAdapter.OnUserSelectedListener{
                override fun onUserSelected(user: User) {
                    findNavController().navigate(R.id.action_search_to_view_profile, bundleOf(USER_NAV_ARGUMENT to user)
                )
            }
        })
        _searchResultsRecyclerView.adapter = adapter
    }

    private fun findViews(view: View) {
        _bottomNavigationView = view.findViewById(R.id.bottom_navigation)
        _searchButton = view.findViewById(R.id.search_button)
        _searchResultsRecyclerView = view.findViewById(R.id.searchResultsRecyclerView)
        _userNameEditText = view.findViewById(R.id.userNameEditText)
        _emailEditText = view.findViewById(R.id.emailEditText)
        _instrumentEditText = view.findViewById(R.id.instrumentEditText)
        _levelSpinner = view.findViewById(R.id.levelSpinner)
        _statusSpinner = view.findViewById(R.id.statusSpinner)
        _rehearsalSpaceCheckBox = view.findViewById(R.id.rehearsalSpaceCheckBox)
    }

    private fun setOnClickListeners() {
        _searchButton.setOnClickListener {
            getAuth().currentUser?.uid?.let { uid ->
                getStore().collection(USERS_COLLECTION)
                    .addStringQueryParameter(USER_FIELD, _userNameEditText.text.toString())
                    .addStringQueryParameter(EMAIL_FIELD, _emailEditText.text.toString())
                    .addStringQueryParameter(INSTRUMENT_FIELD, _instrumentEditText.text.toString())
                    .addEnumQueryParameter(LEVEL_FIELD, IGNORE, _levelSpinner.selectedItem.toString())
                    .addEnumQueryParameter(STATUS_FIELD, IGNORE, _statusSpinner.selectedItem.toString())
                    .addBooleanQueryParameter(REHEARSAL_FIELD, _rehearsalSpaceCheckBox.isChecked)
                    .get()
                    .addOnCompleteListener {task ->
                        if(task.isSuccessful) {
                            val searchResults = task.result?.toObjects(User::class.java)?.toMutableList()
                            val resultsExcludingMyself = searchResults?.filter { uid != it.uid }
                            val searchResultAdapter = _searchResultsRecyclerView.adapter as SearchResultsAdapter

                            saveUserSearch(UserSearch(
                                username = _userNameEditText.text.toString(),
                                email = _emailEditText.text.toString(),
                                instrument = _instrumentEditText.text.toString(),
                                level = _levelSpinner.selectedItemPosition,
                                status = _statusSpinner.selectedItemPosition,
                                rehearsalSpace = _rehearsalSpaceCheckBox.isChecked,
                                result =  resultsExcludingMyself
                            ))

                            searchResultAdapter.usersList = resultsExcludingMyself?.toMutableList() ?: mutableListOf()
                            searchResultAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(context, getString(R.string.search_error_text), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }

        _bottomNavigationView.setOnItemSelectedListener {
             item ->
                when(item.itemId) {
                    R.id.search -> {
                        true
                    }
                    R.id.edit_profile -> {
                        findNavController().navigate(R.id.action_search_to_edit_profile)
                        true
                    }
                    else -> false
                }
        }
    }

    private fun setUpUi() {
        _bottomNavigationView.selectedItemId = R.id.search

        getUserSearch()?.let { parameters ->
            _userNameEditText.setText(parameters.username)
            _emailEditText.setText(parameters.email)
            _instrumentEditText.setText(parameters.instrument)
            _levelSpinner.setSelection(parameters.level)
            _statusSpinner.setSelection(parameters.status)
            _rehearsalSpaceCheckBox.isChecked = parameters.rehearsalSpace
            val adapter = (_searchResultsRecyclerView.adapter as SearchResultsAdapter)
            adapter.usersList = parameters.result?.toMutableList() ?: mutableListOf()
            adapter.notifyDataSetChanged()
        }
    }
}