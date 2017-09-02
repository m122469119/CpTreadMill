package com.liking.treadmill.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup

import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewAdapter
import com.aaron.android.framework.base.widget.recycleview.BaseRecycleViewHolder
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton
import com.liking.treadmill.R
import com.liking.treadmill.socket.result.NotifyUserResult
import kotlinx.android.synthetic.main.layout_notify_user_item.view.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.layoutInflater

class NotifyUserAdapter(context: Context) : BaseRecycleViewAdapter<NotifyUserAdapter.NotifyUserViewHolder, NotifyUserResult.DataBean>(context) {

    override fun createViewHolder(parent: ViewGroup?): NotifyUserViewHolder {
        val inflate = context.layoutInflater.inflate(R.layout.layout_notify_user_item, parent, false)
        return NotifyUserViewHolder(inflate)
    }

    inner class NotifyUserViewHolder(itemView: View) : BaseRecycleViewHolder<NotifyUserResult.DataBean>(itemView) {

        override fun bindViews(data: NotifyUserResult.DataBean?) {
            HImageLoaderSingleton.getInstance().loadImage(itemView.notify_user_head_imageView, data?.avatar)
            itemView.notify_user_name.text = data?.name
            if (data?.gender == 1) {
                itemView.notify_user_name_sex.imageResource = R.drawable.run_icon_man
            } else if (data?.gender == 0) {
                itemView.notify_user_name_sex.imageResource = R.drawable.run_icon_woman
            }
            itemView.notify_user_name_meetcount.text = data?.encounter.toString()
        }
    }
}
