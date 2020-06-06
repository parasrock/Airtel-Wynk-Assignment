package com.paras.baseapplication.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "response")
class ResponseModel {

    @SerializedName("status")
    var status: String? = null

    @SerializedName("totalResults")
    var totalResults : Int? = null

    @SerializedName("articles")
    var articles: List<ArticleModel>? = null

    @SerializedName("articlesInString")
    var articalsInString: String? = null

    @SerializedName("created_at")
    var created_at : Long = Calendar.getInstance().timeInMillis

}