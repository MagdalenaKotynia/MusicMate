package pl.mkotynia.musicmate.util

import android.util.Log
import com.google.firebase.firestore.Query

const val TAG = "QUERY"

fun Query.addStringQueryParameter(parameter: String, value: String): Query {
    return if(android.text.TextUtils.isEmpty(value)){
        Log.d(TAG, "Ignore $parameter")
        this
    } else {
        Log.d(TAG, "Filter by $parameter: $value")
        this.whereEqualTo(parameter, value)
    }
}

fun Query.addBooleanQueryParameter(parameter: String, value: Boolean): Query {
    return if(value) {
        Log.d(TAG, "Filter by $parameter: $value")
        this.whereEqualTo(parameter, value).whereEqualTo(parameter, value)
    } else {
        Log.d(TAG, "Ignore $parameter")
        this
    }
}

fun Query.addEnumQueryParameter(parameter: String, ignore: String, value: String): Query {
    return if (android.text.TextUtils.equals(ignore, value)) {
        Log.d(TAG, "Ignore $parameter")
        this
    } else {
        Log.d(TAG, "Filter by $parameter: $value")
        this.whereEqualTo(parameter, value)
    }
}
