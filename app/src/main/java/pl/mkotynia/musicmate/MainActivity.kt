package pl.mkotynia.musicmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mkotynia.musicmate.R
import pl.mkotynia.musicmate.MusicMateApplication.Companion.LOG_TAG
import pl.mkotynia.musicmate.prelogin.NewPasswordFragment.Companion.OOB_CODE_NAV_ARGUMENT
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object{
        const val RESET_PASSWORD_SCHEME = "https"
        const val HOST = "musicmate-22cb3.firebaseapp.com"
        const val OOB_CODE_QUERY_PARAMETER = "oobCode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MusicMate)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpNavigation(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
            if(isResetPasswordIntent(intent)) {
                val navController = getNavController()
                // clear backstack
                while (navController.popBackStack()) {
                    Log.d(LOG_TAG, "popped backstack")
                }
                //navigate to login
                navController.navigate(R.id.login)

                //navigate to new_password
                val oobCode = intent?.data?.getQueryParameter(OOB_CODE_QUERY_PARAMETER)
                navController.navigate(
                    R.id.action_login_to_new_password,
                    bundleOf(OOB_CODE_NAV_ARGUMENT to oobCode)
                )
            }
    }

    private fun setUpNavigation(intent: Intent?) {
        val musicMateApplication = application as MusicMateApplication

        when {
            isResetPasswordIntent(intent) -> {
                val oobCode = intent?.data?.getQueryParameter(OOB_CODE_QUERY_PARAMETER)
                val navController = setNavGraph()
                navController.navigate(R.id.action_login_to_new_password, bundleOf(OOB_CODE_NAV_ARGUMENT to oobCode))
            }
            musicMateApplication.userLoggedIn() -> {
                musicMateApplication.refreshMyProfileInfo().addOnCompleteListener { refreshProfileTask ->
                    if(refreshProfileTask.isSuccessful) {
                        val navController = setNavGraph()
                        navController.navigate(R.id.action_login_to_search)
                    } else {
                        setNavGraph()
                    }
                }
            }
            else -> {
                setNavGraph()
            }
        }
    }

    private fun getNavController(): NavController {
        val myNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return myNavHostFragment.navController
    }

    private fun setNavGraph(): NavController {
        val myNavHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = myNavHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.navigation_graph)
        val navController = myNavHostFragment.navController
        navController.graph = graph
        return navController
    }

    private fun isResetPasswordIntent(intent: Intent?): Boolean {
        try {
            val data = intent?.data
            val scheme = data?.scheme
            val host = data?.host
            val oobCode = data?.getQueryParameter(OOB_CODE_QUERY_PARAMETER)

            return TextUtils.equals(scheme, RESET_PASSWORD_SCHEME) && TextUtils.equals(host, HOST) && !TextUtils.isEmpty(oobCode)
        }catch (e: Exception){
            e.printStackTrace()
        }
        return false
    }
}