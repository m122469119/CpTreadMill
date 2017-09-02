package com.liking.treadmill.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton
import com.liking.treadmill.R
import com.liking.treadmill.socket.result.MarathonRankHotResult
import com.liking.treadmill.utils.TypefaceHelper
import kotlinx.android.synthetic.main.viewholder_ranking_list_item.view.*
import org.jetbrains.anko.layoutInflater

/**
 * Created by aaa on 17/9/2.
 * 全国排名的选手
 */
class RankingAdapter(context: Context) : BaseRecycleViewAdapter<RankingAdapter.RankingViewHolder, MarathonRankHotResult.DataBean.ListBean>(context) {

    lateinit var mTypeface: Typeface
    override fun createViewHolder(parent: ViewGroup?): RankingViewHolder {
        val inflate = context.layoutInflater.inflate(R.layout.viewholder_ranking_list_item, parent, false)
        return RankingViewHolder(inflate)
    }


    inner class RankingViewHolder(itemView: View) : BaseRecycleViewHolder<MarathonRankHotResult.DataBean.ListBean>(itemView) {
        override fun bindViews(data: MarathonRankHotResult.DataBean.ListBean?) {
            mTypeface = TypefaceHelper.getImpactTypeface(context)
            itemView.ranking_number.typeface = mTypeface
            itemView.ranking_distance.typeface = mTypeface
            itemView.ranking_time.typeface = mTypeface

            itemView.ranking_number.text = data?.ranking_number
            itemView.ranking_name.text = data?.username
            itemView.ranking_distance.text = data?.distance
            val time = data?.useTime
            val hour: Int = time!! / 3600
            val minute: Int = time % 3600 / 60
            val second: Int = time % 60
            val str = "$hour:$minute:$second"
            itemView.ranking_time.text = str
            HImageLoaderSingleton.getInstance().loadImage(itemView.ranking_head_HImageView, data.avatar)
        }


    }


}