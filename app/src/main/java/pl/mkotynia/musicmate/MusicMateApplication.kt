package pl.mkotynia.musicmate

import android.app.Application
import android.text.TextUtils
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.mkotynia.musicmate.model.UserSearch
import pl.mkotynia.musicmate.model.User

class MusicMateApplication : Application() {

    companion object {
        const val USERS_COLLECTION= "users"
        const val UID_FIELD = "uid"
        const val LOG_TAG = "MusicMateDebug"
    }

    lateinit var auth: FirebaseAuth
    lateinit var store: FirebaseFirestore

    var searchParameters: UserSearch? = null

    override fun onCreate() {
        super.onCreate()
        auth = FirebaseAuth.getInstance()
        store = FirebaseFirestore.getInstance()
    }

    fun refreshMyProfileInfo(): Task<User> {
        val uid = auth.currentUser!!.uid
        return store.collection(USERS_COLLECTION)
            .whereEqualTo(UID_FIELD, uid)
            .get()
            .continueWith {
                if(it.isSuccessful && it.result != null && !it.result!!.isEmpty) {
                    it.result?.first()?.toObject(User::class.java)
                } else {
                    User(uid = auth.currentUser!!.uid, email = auth.currentUser!!.email!!)
                }
            }
    }

    fun userLoggedIn(): Boolean {
        return auth.currentUser != null && !TextUtils.isEmpty(auth.currentUser!!.email)  && !TextUtils.isEmpty(auth.currentUser!!.uid)
    }
}