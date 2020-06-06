package com.paras.baseapplication.viewModels

import androidx.lifecycle.LiveData
import com.paras.baseapplication.models.BaseResponse
import com.paras.baseapplication.models.ResponseModel
import com.paras.baseapplication.persistance.entities.NewsEntity

interface IMainActivityViewModel {

    fun fetchNews(pageNumber: Int)

    fun getNewsFromDB()

    fun getPageNumber() : Int

    fun getNewsLiveData(): LiveData<BaseResponse<ResponseModel>>

}