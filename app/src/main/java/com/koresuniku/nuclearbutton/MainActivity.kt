package com.koresuniku.nuclearbutton

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import io.reactivex.schedulers.Schedulers
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import android.view.KeyEvent.KEYCODE_ENTER
import android.view.inputmethod.EditorInfo
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.*


class MainActivity : AppCompatActivity() {

    private lateinit var theButton: ImageView
    private lateinit var thePass: EditText
    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var githubHelper: GithubHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        theButton = findViewById(R.id.the_button)
        thePass = findViewById(R.id.pass)

        thePass.setOnEditorActionListener(TextView.OnEditorActionListener { p0, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    event != null &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER) {
                if (event == null || !event.isShiftPressed) {
                    // the user is done typing.
                    Log.d("MA", "typed: ${thePass.text}")
                    this@MainActivity.currentFocus?.let {
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(it.windowToken, 0)
                    }
                    FirebaseAuth.getInstance().signInWithEmailAndPassword("alo_yoba@eto.ti", thePass.text.toString())
                            .addOnSuccessListener {
                                thePass.visibility = View.GONE
                                theButton.visibility = View.VISIBLE
                            }.addOnFailureListener { Toast.makeText(this, "YOU SHALL NOT PASS!", Toast.LENGTH_SHORT).show() }

                    return@OnEditorActionListener true
                }
            }
            false
        })


        firebaseHelper = FirebaseHelper()
        githubHelper = GithubHelper()

        theButton.setOnClickListener {
            Single.zip(
                    firebaseHelper.getLatestFromFirebaseDB()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()),
                    githubHelper.getLastVersionFromGithub()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()),
                    firebaseHelper.getServerKey()
                            .subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread()),
                    Function3( { fromFB: String, fromGH: Release, key: String ->
                        Log.d("MA", "fromFB: $fromFB, fromGH: $fromGH, key: $key")
                        if (compareVersionNames(fromFB, fromGH.tagName)) {
                            Log.d("MA", "Updating FB")
                            firebaseHelper.updateFirebaseLastVersion(fromGH.assetList[0].name)
                            sendDataMessage(fromGH.assetList[0].name, key)
                        } else {
                            Toast.makeText(this, "Already notified last version", Toast.LENGTH_SHORT).show()
                        }

                    })).subscribe()

        }

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

    private fun sendDataMessage(fromGH: String, key: String) {
        val message = RemoteMessage()
        message.to = "/topics/new_version"
        val rmData = RMData()
        rmData.newVersionName = fromGH
        message.rmData = rmData

        val map = HashMap<String, String>()
        map["Content-Type"] = "application/json"
        map["Authorization"] = "key=$key"
        Http.retrofit.create(FirebasePosting::class.java)
                .sendMessage(map, message)
                .subscribeOn(Schedulers.newThread())
                .subscribe()
    }
}
