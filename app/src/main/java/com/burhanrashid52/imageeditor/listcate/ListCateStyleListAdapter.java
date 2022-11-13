package com.burhanrashid52.imageeditor.listcate;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.models.Category;

import java.util.List;

/**
 * Created by admin on 9/30/18.
 */

public class ListCateStyleListAdapter extends RecyclerView.Adapter<ListCateStyleListAdapter.CateViewHolder> {

    List<Category> listCategory;
    List<String> listQuoteOfCategory;
    RequestOptions glideRequestOption = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Constant.screenWidth, Constant.screenWidth * 180 / 300).centerCrop().dontAnimate().dontTransform().priority(Priority.HIGH);

    public ListCateStyleListAdapter(List<Category> listCategory, List<String> listQuoteOfCategory) {
        this.listCategory = listCategory;
        this.listQuoteOfCategory = listQuoteOfCategory;
    }

    @NonNull
    @Override
    public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_cate_style_list, parent, false));
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
        //View parentView;
        View vItem;
        TextView tvQuoteOfCategory;

        public CateViewHolder(View itemView) {
            super(itemView);
            ivCate = itemView.findViewById(R.id.iv_category_image);
            tvCate = itemView.findViewById(R.id.tv_category_name);
            tvQuoteOfCategory = itemView.findViewById(R.id.tv_quote_of_category);
            vItem = itemView.findViewById(R.id.v_item);

            int id = Constant.getResourceFontId("13");
            tvCate.setTypeface(ResourcesCompat.getFont(itemView.getContext(), id));
        }

        public void setData(final Category category) {
            Glide.with(itemView.getContext()).load(category.getResId()).apply(glideRequestOption).into(ivCate);
            if (category.getName().equalsIgnoreCase("motivation")) {
                tvCate.setText("Motivational");
            } else {
                tvCate.setText(category.getName());
            }

            tvQuoteOfCategory.setText("\"" + listQuoteOfCategory.get(listCategory.indexOf(category)) + "\"");
            vItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCateSelectListener != null) {
                        onCateSelectListener.onCateSelected(category);
                    }
                }
            });
        }
    }

    ListCateAdapter.OnCateSelectListener onCateSelectListener;

    public interface OnCateSelectListener {
        void onCateSelected(Category category);
    }

    public ListCateAdapter.OnCateSelectListener getOnCateSelectListener() {
        return onCateSelectListener;
    }

    public void setOnCateSelectListener(ListCateAdapter.OnCateSelectListener onCateSelectListener) {
        this.onCateSelectListener = onCateSelectListener;
    }
}
