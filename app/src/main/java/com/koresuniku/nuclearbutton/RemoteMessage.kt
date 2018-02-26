package com.koresuniku.nuclearbutton

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by koresuniku on 2/26/18.
 */

class RemoteMessage {

    @SerializedName("to")
    @Expose
    lateinit var to: String

    @SerializedName("data")
    @Expose
    lateinit var rmData: RMData
}