package com.uu.mahjong_analyse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uu.mahjong_analyse.R;

/**
 * @auther Nagisa.
 * @date 2016/7/1.
 */
public class PlayerScoreAdapter extends RecyclerView.Adapter<PlayerScoreAdapter.MyViewHolder> {
    private Context mContext;
    private int[] mDatas;

    public PlayerScoreAdapter(Context context, int[] datas) {
        mContext = context;
        mDatas = datas;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

    }
    private OnItemClickListener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(View.inflate(mContext, R.layout.item_recycler_view, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(mDatas[position]);
        if(mOnItemClickLitener != null) {
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int pos = holder.getLayoutPosition();
//                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                    mOnItemClickLitener.onItemClick(holder.tv,position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDatas.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);

        }
    }
}
