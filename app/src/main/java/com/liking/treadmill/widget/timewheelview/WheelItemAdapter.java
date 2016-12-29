/*
*****************************************************************************************
* @file WheelItemAdapter.java
*
* @brief 
*
* Code History:
*       2015-10-13  下午4:11:47  Teemo , initial version
*
* Code Review:
*
********************************************************************************************
*/

package com.liking.treadmill.widget.timewheelview;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liking.treadmill.R;

public class WheelItemAdapter extends AbstractWheelAdapter{
    private List<String> mItemData; 
    private Context mContext;
    private Typeface mTypeFace;

    public WheelItemAdapter(Context mContext, List<String> listData) {
        this.mItemData = listData;
        this.mContext = mContext;
        mTypeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/Impact.ttf");
    }

    @Override
    public int getItemsCount() {
        
        return mItemData.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_wheel, null);
        }
        String rate = String.format(mItemData.get(index).toString());
        TextView taxRate = (TextView) view.findViewById(R.id.item_text);
        taxRate.setTypeface(mTypeFace);
        taxRate.setText(rate);
        return view;
    }

}
