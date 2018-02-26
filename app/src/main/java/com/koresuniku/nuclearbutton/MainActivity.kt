package com.koresuniku.nuclearbutton

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import io.reactivex.schedulers.Schedulers
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction

class MainActivity : AppCompatActivity() {

    private lateinit var theButton: Button
    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var githubHelper: GithubHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        theButton = findViewById(R.id.the_button)

        FirebaseAuth.getInstance().signInWithEmailAndPassword("koresuniku@gmail.com", "123rjhFgeP")
        firebaseHelper = FirebaseHelper()
        githubHelper = GithubHelper()

        Single.zip(
                firebaseHelper.getLatestFromFirebaseDB()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                githubHelper.getLastVersionFromGithub()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread()),
                BiFunction { fromFB: String, fromGH: String ->
                    Log.d("MA", "fromFB: $fromFB, fromGH: $fromGH")
                    if (compareVersionNames(fromFB, fromGH)) {
                        Log.d("MA", "Updating FB")
                        firebaseHelper.updateFirebaselastVersion(fromGH)
                    } else  Log.d("MA", "no need to update FB")

                }).subscribe()

    }

    private fun compareVersionNames(fromFB: String, fromGH: String): Boolean {
        val c = fromFB.replace(Regex("[^0-9\\.]+"), "")
        val l = fromGH.replace(Regex("[^0-9\\.]+"), "")
        val fbArr = c.split(Regex("\\."))
        val ghArr = l.split(Regex("\\."))
        fbArr.zip(ghArr).forEach {
            Log.d("MA", "first: ${it.first}, second: ${it.second}")
            if (it.first.toInt() < it.second.toInt()) return true
            else if (it.first.toInt() > it.second.toInt()) return false
        }
        return false
    }
}
