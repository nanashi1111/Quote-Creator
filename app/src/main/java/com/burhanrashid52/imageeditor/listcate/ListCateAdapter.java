package com.burhanrashid52.imageeditor.listcate;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.models.Category;

import java.util.List;

/**
 * Created by admin on 8/18/18.
 */

public class ListCateAdapter extends RecyclerView.Adapter<ListCateAdapter.CateViewHolder> {

    List<Category> listCategory;
    int itemSize;

    public ListCateAdapter(List<Category> listCategory, int itemSize) {
        this.listCategory = listCategory;
        this.itemSize = itemSize;
    }

    @NonNull
    @Override
    public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CateViewHolder holder, int position) {
        holder.setData(listCategory.get(position));
    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    class CateViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCate;
        TextView tvCate;
        View parentView;
        View vItem;

        public CateViewHolder(View itemView) {
            super(itemView);
            ivCate = itemView.findViewById(R.id.iv_category_image);
            tvCate = itemView.findViewById(R.id.tv_category_name);
            parentView = itemView.findViewById(R.id.rl_parent);
            vItem = itemView.findViewById(R.id.v_item);
        }

        public void setData(final Category category) {
            parentView.getLayoutParams().width = itemSize;
            parentView.getLayoutParams().height = itemSize;
            Glide.with(itemView.getContext()).load(category.getResId()).into(ivCate);
            tvCate.setText(category.getName());
            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent intent = new Intent(itemView.getContext(), ListQuoteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.KEY_CATEGORY, category.getName());
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);*/
                    if (onCateSelectListener != null) {
                        onCateSelectListener.onCateSelected(category);
                    }
                }
            });
        }
    }

    OnCateSelectListener onCateSelectListener;

    public interface OnCateSelectListener {
        void onCateSelected(Category category);
    }

    public OnCateSelectListener getOnCateSelectListener() {
        return onCateSelectListener;
    }

    public void setOnCateSelectListener(OnCateSelectListener onCateSelectListener) {
        this.onCateSelectListener = onCateSelectListener;
    }
}
