package com.paras.baseapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paras.baseapplication.R
import com.paras.baseapplication.base.utils.GenericImageLoader
import com.paras.baseapplication.base.utils.Utility
import com.paras.baseapplication.interfaces.NewsAdapterListener
import com.paras.baseapplication.models.ArticleModel
import kotlinx.android.synthetic.main.item_view_news.view.*
import kotlinx.android.synthetic.main.item_view_no_item_or_loading.view.*

class NewsAdapter(
    private val mNewsAdapterListener: NewsAdapterListener,
    private var mArticleList: MutableList<ArticleModel> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // region variables

    private var mTotalResults = 0

    val itemTypeLoadingOrNoItem = 0
    val itemTypeNews = 1

    // endregion

    // region override methods of RecyclerView.Adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == itemTypeLoadingOrNoItem) {
            return LoadingOrNoMoreItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_view_no_item_or_loading,
                    parent,
                    false
                )
            )
        }
        return AddressHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_news,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = mArticleList.size

    override fun getItemViewType(position: Int): Int {
        if (position == (itemCount - 1)) {
            return itemTypeLoadingOrNoItem
        }
        return itemTypeNews
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddressHolder) {
            holder.setData(mArticleList[position])
            if (position + 3 == itemCount) {
                mNewsAdapterListener.fetchNews((mArticleList[position].pageNumber ?: 0) + 1)
            }
        } else {
            if (mTotalResults == (position + 1)) {
                holder.itemView.progress.visibility = View.GONE
                holder.itemView.tvNoMoreItems.visibility = View.VISIBLE
            } else {
                holder.itemView.progress.visibility = View.VISIBLE
                holder.itemView.tvNoMoreItems.visibility = View.GONE
            }
        }
    }

    // endregion

    // region method for update dataset

    fun updateData(articleList: List<ArticleModel>, totalResult: Int) {
        mTotalResults = totalResult
        mArticleList.addAll(articleList)
        notifyDataSetChanged()
    }

    // endregion

    // Address Holder inner class
    inner class AddressHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(articleModel: ArticleModel) {
            itemView.tvSource.text = articleModel.source?.name
            itemView.tvNewsTitle.text = articleModel.title
            GenericImageLoader.setImageView(itemView.iv_news_image, articleModel.urlToImage)
            itemView.tvPublishDate.text = Utility.getFormattedDate(articleModel.publishedAt ?: "")
        }

    }

    class LoadingOrNoMoreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}