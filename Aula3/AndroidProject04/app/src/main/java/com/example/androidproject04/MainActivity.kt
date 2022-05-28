package com.example.androidproject04

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {

    /* Do login by Firebase Authentication */
    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
            this.onSignInResult(res)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Initiate firebase */
        FirebaseApp.initializeApp(this)

        /* Get authenticated user */
        val user = FirebaseAuth.getInstance().currentUser

        /* Verify if user is authenticate */
        if (user != null) {

            /* Get user data */
            val name = user.displayName
            val email = user.email

            /* Initiate the remote config */
            setFirebaseRemoteConfig()

            /* If user is authenticated show main page */
            setContentView(R.layout.activity_main)
        }
        /* If user is not authenticator show login page */
        else {

            /* Providers that users can do the login (Google, Microsoft...) */
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

            /* Create and show a login page by Providers (Google) */
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()

            /* Realize auth */
            signInLauncher.launch(signInIntent)
        }
    }

    /* Remote config to enable or disable features by conditional */
    private fun setFirebaseRemoteConfig() {
        /* Declare remoteConfig */
        val remoteConfig = Firebase.remoteConfig

        /* Remote config options */
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }

        /* Configuration */
        remoteConfig.setConfigSettingsAsync(configSettings)

        /* Default values */
        val defaultConfigMap: MutableMap<String, Any> = HashMap()
        defaultConfigMap["delete_detail_view"] = true
        defaultConfigMap["delete_list_view"] = false

        /* Set default value */
        remoteConfig.setDefaultsAsync(defaultConfigMap)

        /* Get remote config */
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("MainActivity", "Remote config updated: $updated")
                } else {
                    Log.d("MainActivity", "Failed to load remote config")
                }
            }
    }


        /* Specific the menu that will be used */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    /* Get the event when the user selects an item on the menu */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /* When user click on nav_sign_out - Logout */
            R.id.nav_sign_out -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        /* Recreate component request login  */
                        this.recreate()
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /**
     * Verify is sign in result
     */
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        /* Get the result */
        val response = result.idpResponse

        /* Verify the result */
        if (result.resultCode == RESULT_OK) {
            /* Initiate the remote config */
            setFirebaseRemoteConfig()

            /* Open the main page */
            setContentView(R.layout.activity_main)
        } else {
            /* If fails login - Show a message */
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
        }
    }
}