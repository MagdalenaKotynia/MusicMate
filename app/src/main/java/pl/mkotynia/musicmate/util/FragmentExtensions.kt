package pl.mkotynia.musicmate.util

import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.mkotynia.musicmate.MusicMateApplication
import android.text.TextUtils
import pl.mkotynia.musicmate.model.UserSearch

fun Fragment.getAuth(): FirebaseAuth {
    return (activity?.application as MusicMateApplication).auth
}

fun Fragment.getStore(): FirebaseFirestore {
    return (activity?.application as MusicMateApplication).store
}

fun Fragment.refreshMyProfileInfo() = (activity?.application as MusicMateApplication).refreshMyProfileInfo()

fun Fragment.saveUserSearch(userSearch: UserSearch?) {
    (activity?.application as MusicMateApplication).searchParameters = userSearch
}

fun Fragment.getUserSearch(): UserSearch? {
    return (activity?.application as MusicMateApplication).searchParameters
}

