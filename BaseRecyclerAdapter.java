package com.xyb.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by xybcoder
 * RecyclerView的通用适配器
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerHolder> {

    protected List<T> mDatas;
    protected final int mLayoutId;
    protected boolean isScrolling;
    protected Context mContext;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;


    public BaseRecyclerAdapter(RecyclerView v, Collection<T> datas, int layoutId) {
        if (datas == null) {
            mDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            mDatas = (List<T>) datas;
        } else {
            mDatas = new ArrayList<>(datas);
        }
        mLayoutId = layoutId;
        mContext = v.getContext();

        v.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = !(newState == RecyclerView.SCROLL_STATE_IDLE);
                if (!isScrolling) {
                    notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Recycler适配器填充方法
     *
     * @param holder      viewholder
     * @param item
     * @param isScrolling RecyclerView是否正在滚动
     */
    public abstract void convert(RecyclerHolder holder, T item, int position, boolean isScrolling);

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerHolder holder = RecyclerHolder.createViewHolder(mContext, parent, mLayoutId);
        setListener(parent, holder, viewType);
        return holder;
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }


    protected void setListener(ViewGroup parent, final RecyclerHolder holder, int viewType) {
        if (!isEnabled(viewType)) return;
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = holder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, holder, position);
                }
            }
        });

        holder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int position = holder.getAdapterPosition();
                    return mOnItemLongClickListener.onItemLongClick(v, holder, position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        convert(holder, mDatas.get(position), position, isScrolling);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public BaseRecyclerAdapter<T> refreshData(Collection<T> datas) {
        if (datas == null) {
            mDatas = new ArrayList<>();
        } else if (datas instanceof List) {
            mDatas = (List<T>) datas;
        } else {
            mDatas = new ArrayList<>(datas);
        }
        return this;
    }


    public List<T> getDatas() {
        return mDatas;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);
    }


    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }
}
