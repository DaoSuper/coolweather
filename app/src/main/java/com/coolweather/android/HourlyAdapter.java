package com.coolweather.android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coolweather.android.gson.Hourly;

import java.util.ArrayList;
import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.ViewHolder>{

    private List<Hourly> mHourlyList = new ArrayList<Hourly>();

    public HourlyAdapter(List<Hourly> hourlyList){
        mHourlyList = hourlyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(),R.layout.hourly_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Hourly hourly = mHourlyList.get(position);
        holder.mCond.setText(hourly.getInfo());
        holder.mTmp.setText(hourly.getTemperature());
        String hourlyTime = hourly.getHourly_time().split(" ")[1];
        char str = hourlyTime.charAt(0);
        if (str == '0') {
            holder.mTime.setText(hourlyTime.substring(1,hourlyTime.length()));
        }else{
            holder.mTime.setText(hourlyTime);
        }
    }

    @Override
    public int getItemCount() {
        return mHourlyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mCond;
        public TextView mTmp;
        public TextView mTime;
        public ViewHolder(View view) {
            super(view);
            mCond = (TextView) view.findViewById(R.id.cond_txt);
            mTmp = (TextView) view.findViewById(R.id.tmp_txt);
            mTime = (TextView) view.findViewById(R.id.time_txt);
        }
    }
}
