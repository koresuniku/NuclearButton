package com.koresuniku.nuclearbutton

import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by koresuniku on 2/26/18.
 */

interface GithubApi {

    @GET("/repos/koresuniku/wishmaster/releases")
    fun getRealeaseList(): Observable<List<Release>>
}