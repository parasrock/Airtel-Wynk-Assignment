package com.paras.baseapplication.activities

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.paras.baseapplication.R
import com.paras.baseapplication.adapters.NewsAdapter
import com.paras.baseapplication.base.activities.BaseActivity
import com.paras.baseapplication.base.activities.getViewModel
import com.paras.baseapplication.interfaces.NewsAdapterListener
import com.paras.baseapplication.models.ArticleModel
import com.paras.baseapplication.models.BaseResponse
import com.paras.baseapplication.models.ResponseModel
import com.paras.baseapplication.persistance.entities.NewsEntity
import com.paras.baseapplication.viewModels.IMainActivityViewModel
import com.paras.baseapplication.viewModels.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity(), NewsAdapterListener {

    // region view model

    private val mMainActivityViewModel: IMainActivityViewModel by lazy {
        getViewModel<MainActivityViewModel>()
    }

    // endregion

    // region lifecycle methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initializing views
        initView()

        // Registering livedata ovserver
        registerLiveData()

        fetchNewsData(1)
    }

    // endregion

    // region private methods

    /**
     * Initializing views
     */
    private fun initView() {
        rv_news.adapter = NewsAdapter(this)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rv_news.layoutManager = LinearLayoutManager(this)
        } else if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rv_news.layoutManager = GridLayoutManager(this, 2)
        }
        (rv_news.layoutManager as? GridLayoutManager)?.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if ((position + 1) == rv_news.adapter?.itemCount) {
                    return 2
                }
                return 1
            }
        }
    }

    /**
     * fetching address through API call using ViewModel
     */
    private fun fetchNewsData(pageNumber: Int) {
        if (isNetworkConnected()) {
            mMainActivityViewModel.fetchNews(pageNumber)
        } else if (pageNumber == 1) {
            mMainActivityViewModel.getNewsFromDB()
//            showError(getString(R.string.text_no_network))
        }
    }

    private fun showError(errorText: String) {
        rv_news.visibility = View.GONE
        progress.visibility = View.GONE
        tv_progress.visibility = View.GONE
        tvError.visibility = View.VISIBLE
        tvError.text = errorText
    }

    private fun showProgress() {
        rv_news.visibility = View.GONE
        progress.visibility = View.VISIBLE
        tv_progress.visibility = View.VISIBLE
        tvError.visibility = View.GONE
    }

    /**
     * Observing liveData changes
     */
    private fun registerLiveData() {
        mMainActivityViewModel.getNewsLiveData().observe(
            this, Observer {
                Log.e("PARAS", "API data : ${it.anyObject?.totalResults} ${it.anyObject?.articles?.size}")
                setNewsListUI(it)
            }
        )
    }

    private fun addAdapterItems(articleList: List<ArticleModel>, totalResult: Int) {
        rv_news.visibility = View.VISIBLE
        progress.visibility = View.GONE
        tv_progress.visibility = View.GONE
        tvError.visibility = View.GONE
        (rv_news.adapter as? NewsAdapter)?.updateData(articleList, totalResult)
    }

    /**
     * This method will be called when any change occurs in baseResponse.
     */
    private fun setNewsListUI(baseResponse: BaseResponse<ResponseModel>) {
        when {
            baseResponse.loading -> {
                showProgress()
            }
            baseResponse.anyObject?.articles?.isEmpty() == true -> {
                if ((rv_news.adapter?.itemCount ?: 0) == 0) {
                    showError(getString(R.string.text_no_news_found))
                }
            }
            else -> {
                addAdapterItems(baseResponse.anyObject?.articles ?: arrayListOf(), baseResponse.anyObject?.totalResults ?: 0)
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        connectivityManager?.let {
            it.activeNetworkInfo?.let { networkInfo ->
                if (networkInfo.isConnected) return true
            }
        }
        return false
    }

    // endregion

    // region override method of @NewsAdapterListener

    override fun fetchNews(pageNumber: Int) {
        fetchNewsData(pageNumber)
    }

    // endregion

}