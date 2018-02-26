package com.koresuniku.nuclearbutton

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by koresuniku on 2/26/18.
 */

class RMData {

    @SerializedName("new_version_name")
    @Expose
    lateinit var newVersionName: String
}