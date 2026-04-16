package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.myapplication.dao.GameRecord;

import java.util.List;

public class RankingAdapter extends BaseAdapter {

    private List<GameRecord> records;
    private final LayoutInflater inflater;
    private int selectedPosition = -1;

    public RankingAdapter(Context context, List<GameRecord> records) {
        this.records = records;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public GameRecord getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return records.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_ranking, parent, false);
            holder = new ViewHolder();
            holder.tvRank = convertView.findViewById(R.id.tv_rank);
            holder.tvName = convertView.findViewById(R.id.tv_name);
            holder.tvScore = convertView.findViewById(R.id.tv_score);
            holder.tvTime = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GameRecord record = records.get(position);
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvName.setText(record.getUserName());
        holder.tvScore.setText(String.valueOf(record.getScore()));
        holder.tvTime.setText(record.getTime());

        // 选中高亮
        if (position == selectedPosition) {
            convertView.setBackgroundColor(0x44FFFFFF);
        } else {
            convertView.setBackgroundColor(0x00000000);
        }

        return convertView;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    public void updateData(List<GameRecord> newRecords) {
        this.records = newRecords;
        this.selectedPosition = -1;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvRank;
        TextView tvName;
        TextView tvScore;
        TextView tvTime;
    }
}
