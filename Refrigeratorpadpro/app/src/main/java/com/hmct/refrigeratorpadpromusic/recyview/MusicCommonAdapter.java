package com.hmct.refrigeratorpadpromusic.recyview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.hmct.refrigeratorpadpromusic.R;
import com.hmct.refrigeratorpadpromusic.units.MusicEventMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;


public abstract class MusicCommonAdapter<T> extends RecyclerView.Adapter<MusicViewHolder>
{
    protected Context mContext;
    protected int mLayoutId,thisPosition=-1,mtype;
    public List<T> mDatas;
    protected LayoutInflater mInflater;
    private boolean isSelected=true;
    private int tag=-1;
    protected OnItemClickListener mOnItemClickListener;


    public MusicCommonAdapter(Context context, int layoutId, List<T> datas, int type)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
        mtype = type;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        final MusicViewHolder musicViewHolder = MusicViewHolder.get(mContext, parent, mLayoutId);

        setListener(parent, musicViewHolder, viewType);
        return musicViewHolder;
    }


    @Override
    public void onBindViewHolder(final MusicViewHolder holder, int position)
    {
        convert(holder, mDatas.get(position));

        if ((position+1)%2==0){
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.listq));

        }else {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.listw));

        }


        if (mtype==1) {
            if (position == getthisPosition()) {
                Log.e("isselect",tag+"");
                    if (tag==position){

                        holder.getView(R.id.img_play).setSelected(false);
                        tag=-1;
                        EventBus.getDefault().post(new MusicEventMessage<String>(1,String.valueOf(position)));
                    }else {
                        holder.getView(R.id.img_play).setSelected(true);
                        holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.colorPrimary));
                        tag=position;

                        EventBus.getDefault().post(new MusicEventMessage<String>(2,String.valueOf(position)));

                    }

            }
            else {

            }

        }

        if (mtype==2){

            if (position == getthisPosition()) {
                Log.e("isselect","wwwwwwww");
                holder.getView(R.id.tex_menu).setSelected(true);
            }else {
                holder.getView(R.id.tex_menu).setSelected(false);
            }
        }





    }

    public abstract void convert(MusicViewHolder holder, T t);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }


    protected void setListener(final ViewGroup parent, final MusicViewHolder musicViewHolder, int viewType) {
        musicViewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = musicViewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, musicViewHolder, position);
                }
            }
        });

        musicViewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = musicViewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, musicViewHolder, position);
                }
                return false;
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public int getthisPosition() {
        return thisPosition;
    }
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }

}
