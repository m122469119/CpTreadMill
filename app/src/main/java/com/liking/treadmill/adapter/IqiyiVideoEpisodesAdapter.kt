package com.liking.treadmill.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder
import com.liking.treadmill.R
import kotlinx.android.synthetic.main.viewholder_video_list_item.view.*
import org.jetbrains.anko.layoutInflater

class IqiyiVideoEpisodesAdapter(videoName: String, context: Context) : BaseRecycleViewAdapter<IqiyiVideoEpisodesAdapter.VideoInfoViewHolder, String>(context) {

    val name = videoName
    var season:Int = 0
    override fun createViewHolder(parent: ViewGroup?): VideoInfoViewHolder {
        val inflate = context.layoutInflater.inflate(R.layout.viewholder_video_list_item, parent, false)
        return VideoInfoViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        season = position + 1
        super.onBindViewHolder(holder, position)
    }

    inner class VideoInfoViewHolder(itemView: View) : BaseRecycleViewHolder<String>(itemView) {

        override fun bindViews(tvQipuId: String?) {
            itemView.video_list_item_name.tag = tvQipuId
            itemView.video_list_item_name.text = "$name 剧集$season "
        }
    }
}
