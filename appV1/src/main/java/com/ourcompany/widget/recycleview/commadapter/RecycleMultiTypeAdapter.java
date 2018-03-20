package com.ourcompany.widget.recycleview.commadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author : 唐家森
 * Version: 1.0
 * On     : 2017/8/30 20:50
 * Des    : RecycleCommonAdapter 通用Adapter
 */

public abstract class RecycleMultiTypeAdapter<D> extends RecyclerView.Adapter<SViewHolder> {

    private List<D> mData;
    private final LayoutInflater mLayoutInflater;

    public RecycleMultiTypeAdapter(Context context, List<D> data) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public SViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(getLayoutId(viewType),parent,false);
        return new SViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return setViewType(position);
    }

    protected abstract int setViewType(int position);


    protected abstract int getLayoutId(int viewType);

    private OnItemOnclickLinstener linstener;
    public void setOnItemClickLinstener(OnItemOnclickLinstener linstener){
        this.linstener = linstener;
    }



    @Override
    public void onBindViewHolder(SViewHolder holder, final int position) {
        bindItemData(holder,mData.get(position),position);
        if (linstener !=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (linstener!=null){
                        linstener.itemOnclickLinstener(position);
                    }
                }
            });
        }
    }

    public abstract void bindItemData(SViewHolder holder, D itemData, int position) ;

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
