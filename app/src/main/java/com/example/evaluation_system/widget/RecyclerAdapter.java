package com.example.evaluation_system.widget;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.evaluation_system.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<Data>>
        implements View.OnClickListener, View.OnLongClickListener,
        AdapterCallback<Data> {

    private final List<Data> mDataList;
    private AdapterListener<Data> mListener;

    public RecyclerAdapter() {
        this(null);
    }

    public RecyclerAdapter(AdapterListener<Data> listener) {
        this(new ArrayList<Data>(), listener);
    }

    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 覆写默认的布局类型返回
     * @param position 坐标
     * @return 类型，在这里是约定好的xml布局文件id
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     * @param position 坐标
     * @param data 当前的数据
     * @return xml文件的id，用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型，在这里约定为xml布局文件的id
     * @return ViewHolder
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        //通过子类实现来得到一个ViewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);
        //设置View的tag为ViewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        //设置点击事件
        root.setOnClickListener(this);
        //设置长桉事件
        root.setOnLongClickListener(this);
        //进行界面的注解绑定
        holder.mUnbinder = ButterKnife.bind(holder, root);
        //绑定holder的mCallback
        holder.mCallback = this;
        return holder;
    }

    /**
     * 绑定布局，实际工作交由holder的bind方法去完成
     *
     * @param holder   继承自ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);
    }

    /**
     * 得到一个ViewHolder，由子类来完成
     * @param root 根布局View
     * @param viewType 布局类型，这里就是约定的xml布局文件id
     * @return ViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据并进行通知
     * @param data 数据
     */
    public void add(Data data) {
        this.mDataList.add(data);
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一组数据并通知更新
     * @param dataList 数据集合
     */
    public void add(Data... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPosition = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPosition, dataList.length);

        }
    }

    /**
     * 插入一组数据并更新
     * @param dataList 数据集合
     */
    public void add(Collection<Data> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPosition = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPosition, dataList.size());
        }
    }

    /**
     * 删除全部并更新
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 对数据全部替换
     * @param dataList 数据集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     *item点击事件，将处理转移到自定义的监听器
     * @param view
     */
    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            //得到ViewHolder当前适配器中的坐标
            int position = holder.getAdapterPosition();
            this.mListener.onItemClick(holder, mDataList.get(position));
        }
    }

    /**
     * item长桉事件，将处理转移到自定义的监听器
     * @param view
     * @return
     */
    @Override
    public boolean onLongClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            int position = holder.getAdapterPosition();
            this.mListener.onItemLongClick(holder, mDataList.get(position));
        }
        return true;
    }

    /**
     * 设置适配器的监听接口
     * @param listener 自定义的监听接口
     */
    public void setListener(AdapterListener<Data> listener) {
        this.mListener = listener;
    }

    /**
     * 自定义监听器
     * @param <Data> 范型类型
     */
    public interface AdapterListener<Data> {
        void onItemClick(ViewHolder<Data> holder, Data data);
        void onItemLongClick(ViewHolder<Data> holder, Data data);
    }

    @Override
    public void update(Data data, ViewHolder<Data> holder) {
        int position = holder.getAdapterPosition();
        if (position >= 0) {
            mDataList.remove(position);
            mDataList.add(position, data);
            notifyItemChanged(position);
        }
    }

    /**
     * 自定义的ViewHolder
     * @param <Data> 范型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {

        private Unbinder mUnbinder;
        private Data mData;
        private AdapterCallback<Data> mCallback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(mData);
        }

        /**
         * 当触发绑定的数据时的回调，子类必须完成
         *
         * @param data
         */
        protected abstract void onBind(Data data);

        /**
         * Holder自己对自己Data进行更新操作
         * @param data 数据
         */
        public void updateData(Data data) {
            if (this.mCallback != null) {
                this.mCallback.update(data, this);
            }
        }
    }
}
