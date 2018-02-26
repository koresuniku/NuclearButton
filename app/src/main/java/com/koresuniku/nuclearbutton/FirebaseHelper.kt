package com.koresuniku.nuclearbutton

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single

/**
 * Created by koresuniku on 2/26/18.
 */

class FirebaseHelper {
    private val LOG_TAG = FirebaseHelper::class.java.simpleName

    companion object {
        const val LAST_VERSION_UPDATE = "last_version_update"
    }

    fun getLatestFromFirebaseDB(): Single<String> {
        return Single.create({e ->
            val database = FirebaseDatabase.getInstance().reference

            database.child(LAST_VERSION_UPDATE).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError?) {}

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        val value = snapshot?.getValue(String::class.java) ?: String()
                        e.onSuccess(value)
                        database.child(LAST_VERSION_UPDATE).removeEventListener(this)
                    }
                })
        })
    }

    fun updateFirebaselastVersion(newLastVersionFromGH: String) {
        val database = FirebaseDatabase.getInstance().reference
        database.child(LAST_VERSION_UPDATE).setValue(newLastVersionFromGH)
    }

}