package pl.mkotynia.musicmate.util

import android.text.TextUtils

class TextUtils {
    companion object {
        fun validatePassword(password: String, confirmPassword: String): Boolean {
            if(TextUtils.isEmpty(password))
                return false
            return TextUtils.equals(password ,confirmPassword)
        }
    }
}