package com.liking.treadmill.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder
import com.liking.treadmill.R
import com.liking.treadmill.adapter.IqiyiVideoListAdapter.VideoListViewHolder
import kotlinx.android.synthetic.main.viewholder_video_list_item.view.*
import liking.com.iqiyimedia.http.result.TopListResult
import org.jetbrains.anko.layoutInflater

class IqiyiVideoListAdapter(context: Context) :BaseRecycleViewAdapter<VideoListViewHolder, TopListResult.DataBean>(context) {

    override fun createViewHolder(parent: ViewGroup?): VideoListViewHolder {
        val inflate = context.layoutInflater.inflate(R.layout.viewholder_video_list_item, parent, false)
        return VideoListViewHolder(inflate)
    }

    inner class VideoListViewHolder(itemView: View) : BaseRecycleViewHolder<TopListResult.DataBean>(itemView) {

        override fun bindViews(data: TopListResult.DataBean?) {
            itemView.video_list_item_name.text = data?.albumName
        }
    }
}
