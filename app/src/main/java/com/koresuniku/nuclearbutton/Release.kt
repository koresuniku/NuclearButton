package com.koresuniku.nuclearbutton

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by koresuniku on 2/26/18.
 */

class Release {

    @SerializedName("tag_name")
    @Expose
    lateinit var tagName: String

    @SerializedName("assets")
    @Expose
    lateinit var assetList: List<Asset>
}