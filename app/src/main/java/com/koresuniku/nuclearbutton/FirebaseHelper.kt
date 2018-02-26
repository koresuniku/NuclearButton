package com.koresuniku.nuclearbutton

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

/**
 * Created by koresuniku on 2/26/18.
 */

class FirebaseHelper {
    private val LOG_TAG = FirebaseHelper::class.java.simpleName

    companion object {
        const val LAST_VERSION_UPDATE = "last_version_update"
    }

    fun getLatestFromFirebaseDB(): Single<String> {
        return Single.create({
            val database = FirebaseDatabase.getInstance().reference


            FirebaseAuth.getInstance().signInWithEmailAndPassword("koresuniku@gmail.com", "123rjhFgeP")
                    .addOnCompleteListener {
                        Log.d("MA", "onSuccess: ${it.result.user.uid}" )
                        Log.d(LOG_TAG, "setting listener")
                        database.child(LAST_VERSION_UPDATE).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError?) {}

                            override fun onDataChange(snapshot: DataSnapshot?) {
                                val value = snapshot?.getValue(String::class.java) ?: "Хуй"
                                Log.d(LOG_TAG, "value: $value")
                            }
                        })
                    }


        })
    }
}