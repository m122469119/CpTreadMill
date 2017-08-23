package com.liking.treadmill.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.aaron.android.codelibrary.utils.ListUtils
import com.aaron.android.framework.library.imageloader.HImageLoaderSingleton
import com.aaron.android.framework.library.imageloader.HImageView
import com.liking.treadmill.R
import com.liking.treadmill.widget.autoviewpager.InfinitePagerAdapter
import com.liking.treadmill.widget.autoviewpager.indicator.IconPagerAdapter
import org.jetbrains.anko.layoutInflater

/**
 * Created on 2017/6/9
 * Created by sanfen
 *
 * @version 1.0.0
 */
class BannerPagerAdapter(var context: Context): InfinitePagerAdapter(), IconPagerAdapter {

    private var mBannerList: MutableList<String> = mutableListOf()

    fun setData(banners: MutableList<String>) {
         mBannerList.clear()
        if (!ListUtils.isEmpty(banners)) {
            mBannerList.addAll(banners)
        }
    }

    override fun getIconResId(index: Int): Int {
        return R.drawable.banner_indicator
    }

    override fun getItemCount(): Int {
        return mBannerList.size
    }

    override fun getView(position: Int, convertView: View?, container: ViewGroup?): View {
        val holder: ViewHolder
        var rootView = convertView
        if (rootView == null) {
            rootView = context.layoutInflater.inflate(R.layout.item_banner_img, container, false)
            holder = ViewHolder(rootView)
            rootView.tag = holder
        } else {
            holder = rootView.tag as ViewHolder
        }
        holder.bindView(mBannerList[position])
        return rootView!!

    }

    class ViewHolder(var view: View) {
        var imageView: HImageView = view.findViewById(R.id.imageview_banner) as HImageView

        fun bindView(banner : String){
            HImageLoaderSingleton.getInstance().loadImage(imageView, banner)
        }
    }

}