package com.koresuniku.nuclearbutton

import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by koresuniku on 2/26/18.
 */

interface FirebasePosting {

    @POST("https://fcm.googleapis.com/fcm/send")
    fun sendMessage(@HeaderMap map: Map<String, String>, @Body message: RemoteMessage): Observable<RemoteMessage>
}