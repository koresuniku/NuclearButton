package com.koresuniku.nuclearbutton

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.FirebaseApp
import io.reactivex.schedulers.Schedulers
import com.firebase.ui.auth.AuthUI
import java.util.*
import java.util.Arrays.asList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.firebase.ui.auth.IdpResponse
import android.content.Intent
import android.util.Log


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 123

    private lateinit var theButton: Button
    private lateinit var firebaseHelper: FirebaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        theButton = findViewById(R.id.the_button)


        firebaseHelper = FirebaseHelper()

        firebaseHelper.getLatestFromFirebaseDB().subscribeOn(Schedulers.newThread()).subscribe()


//        val providers = Arrays.asList(AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())
//
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser


                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
                Log.d("MA", "fuckup!" )
            }
        }
    }
}
