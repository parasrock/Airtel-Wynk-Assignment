package com.paras.baseapplication.base.utils.api

import com.paras.baseapplication.base.utils.Constants
import com.paras.baseapplication.models.ResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by parasj on 30/9/18.
 */
interface APIService {

    @GET("v2/top-headlines?country=${Constants.DEFAULT_COUNTRY}&apiKey=${Constants.API_KEY}&pageSize=${Constants.DEFAULT_PAGE_SIZE}")
    fun getNewsData(@Query("page") pageNumber: Int): Observable<ResponseModel>

}