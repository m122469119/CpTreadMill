package com.liking.treadmill.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton
import com.liking.treadmill.R
import com.liking.treadmill.socket.result.MarathonRankHotResult
import kotlinx.android.synthetic.main.viewholder_before_my_ranking_list_item.view.*
import org.jetbrains.anko.layoutInflater

/**
 * Created by aaa on 17/9/2.
 * 在我前面选手
 */

class BeforeMyRankingAdapter(context: Context) : BaseRecycleViewAdapter<BeforeMyRankingAdapter.BeforeMyRankingViewHolder, MarathonRankHotResult.DataBean.FrontBean>(context) {
    override fun createViewHolder(parent: ViewGroup?): BeforeMyRankingViewHolder {
        val inflate = context.layoutInflater.inflate(R.layout.viewholder_before_my_ranking_list_item, parent, false)
        return BeforeMyRankingViewHolder(inflate)
    }


    inner class BeforeMyRankingViewHolder(itemView: View) : BaseRecycleViewHolder<MarathonRankHotResult.DataBean.FrontBean>(itemView) {
        override fun bindViews(data: MarathonRankHotResult.DataBean.FrontBean?) {
            HImageLoaderSingleton.getInstance().loadImage(itemView.before_my_ranking_head_HImageView, data?.avatar)
            itemView.before_my_ranking_name.text = data?.username
        }

    }

}
