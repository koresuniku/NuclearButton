package com.koresuniku.nuclearbutton

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by koresuniku on 2/26/18.
 */

class GithubHelper {

    fun getLastVersionFromGithub(): Single<Release> {
        return Single.create { e ->

            val githubApi = Http.retrofit.create(GithubApi::class.java)

            githubApi.getRealeaseList().subscribeOn(Schedulers.newThread()).subscribe {
                e.onSuccess(it[0])
            }
        }

    }
}