package com.paras.baseapplication.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.paras.baseapplication.base.utils.api.HttpService
import com.paras.baseapplication.models.ArticleModel
import com.paras.baseapplication.models.BaseResponse
import com.paras.baseapplication.models.ResponseModel
import com.paras.baseapplication.persistance.db.MyDatabase
import com.paras.baseapplication.persistance.entities.NewsEntity
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() :
    ViewModel(), IMainActivityViewModel {

    private val newsLiveData: MutableLiveData<BaseResponse<ResponseModel>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()
    private val mArticles = mutableListOf<ArticleModel>()

    private var mPageNumber: Int = 0

    override fun getPageNumber(): Int = mPageNumber

    override fun fetchNews(pageNumber: Int) {
        if (mPageNumber < pageNumber) {
            mPageNumber = pageNumber
            fetchNewsData(pageNumber)
        }
    }

    override fun getNewsFromDB() {
        MyDatabase.getInstance()?.newsDao()?.getAllResponse()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ responseList ->
                responseList.takeIf { it.isNotEmpty() }?.let {
                    val responseModel =
                        Gson().fromJson(it.first().response, ResponseModel::class.java)
                    responseModel.articles?.let { articleList ->
                        mArticles.addAll(articleList)
                    }
                    Log.e(
                        "PARAS",
                        "Getting DB data : ${responseModel?.totalResults} ${responseModel?.articles?.size}"
                    )
                    newsLiveData.postValue(BaseResponse(false, null, responseModel))
                }
            }, {
                newsLiveData.postValue(BaseResponse(false, it, null))
            })?.let {
                compositeDisposable.add(it)
            }
    }

    override fun getNewsLiveData(): LiveData<BaseResponse<ResponseModel>> = newsLiveData

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private fun fetchNewsData(
        pageNumber: Int
    ) {
        if (pageNumber == 1) {
            newsLiveData.postValue(BaseResponse(true, null, null))
        }
        HttpService.getInstance()?.getNewsData(pageNumber)
            ?.map {
                it.articles?.forEach { article ->
                    article.pageNumber = pageNumber
                }
                it
            }
            ?.subscribeOn(Schedulers.io())?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                it.articles?.let { articleList ->
                    mArticles.addAll(articleList)
                }
                newsLiveData.postValue(BaseResponse(false, null, it))
                val newsEntity = NewsEntity(
                    response = Gson().toJson(it)
                )
                deleteAllRecordsFromDBInsertNewRecords(newsEntity)
            }, {
                newsLiveData.postValue(BaseResponse(false, it, null))
            }, {})?.let {
                compositeDisposable.add(it)
            }
    }

    private fun deleteAllRecordsFromDBInsertNewRecords(newsEntity: NewsEntity) {
        MyDatabase.getInstance()?.newsDao()?.getAllResponse()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ responseList ->
                responseList.takeIf { it.isNotEmpty() }?.let {
                    Log.e("PARAS", "Getting DB data for insert")
                    compositeDisposable.add(
                        Completable.fromAction {
                            MyDatabase.getInstance()?.newsDao()?.deleteNews(it.first())
                        }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                Log.e("PARAS", "Deleted stored news data")
                                compositeDisposable.add(
                                    Completable.fromAction {
                                        val responseModel = Gson().fromJson(
                                            newsEntity.response,
                                            ResponseModel::class.java
                                        )
                                        responseModel.articles = mArticles
                                        newsEntity.response = Gson().toJson(responseModel)
                                        MyDatabase.getInstance()?.newsDao()
                                            ?.insertOrUpdate(newsEntity)
                                    }
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe({
                                            Log.e("PARAS", "Data Inserted")
                                        }, {
                                            Log.e("PARAS", "Data Inserted problem")
                                        })
                                )
                            }, {
                                Log.e("PARAS", "Data Deletion problem")
                            })
                    )
                } ?: compositeDisposable.add(
                    Completable.fromAction {
                        MyDatabase.getInstance()?.newsDao()
                            ?.insertOrUpdate(newsEntity)
                    }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.e("PARAS", "Data Inserted")
                        }, {
                            Log.e("PARAS", "Data Inserted problem")
                        })
                )
            }, {})?.let {
                compositeDisposable.add(it)
            }
    }

}