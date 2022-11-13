package com.burhanrashid52.imageeditor.border;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.imageeditor.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 9/6/18.
 */

public class BorderAdapter extends RecyclerView.Adapter<BorderAdapter.BorderViewHolder> {

    List<Integer> listBorderResource;

    public BorderAdapter() {
        listBorderResource = Arrays.asList(R.drawable.img_borders_1, R.drawable.img_borders_2,
                R.drawable.img_borders_3, R.drawable.img_borders_4,
                R.drawable.img_borders_5, R.drawable.img_borders_6,
                R.drawable.img_borders_7, R.drawable.img_borders_8,
                R.drawable.img_borders_9, R.drawable.img_borders_10,
                R.drawable.img_borders_11);
    }

    @NonNull
    @Override
    public BorderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BorderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BorderViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return listBorderResource.size();
    }


    class BorderViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageFilterView;
        TextView mTxtFilterName;
        TextView tvFont;

        public BorderViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mImageFilterView.setScaleType(ImageView.ScaleType.FIT_XY);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            mTxtFilterName.setVisibility(View.GONE);
            tvFont = itemView.findViewById(R.id.tvFont);
            tvFont.setVisibility(View.GONE);
        }

        public void setData(final int position) {
            //tvFont.setText(Html.fromHtml("<b>Abc</b>"));
            Glide.with(itemView.getContext()).load(listBorderResource.get(position)).into(mImageFilterView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBorderontSelected != null) {
                        onBorderontSelected.onBorderSelected(listBorderResource.get(position));
                    }
                }
            });
        }
    }

    OnBorderSelected onBorderontSelected;

    public interface OnBorderSelected {
        void onBorderSelected(int resource);
    }


    public void setOnFontSelected(OnBorderSelected onBorderontSelected) {
        this.onBorderontSelected = onBorderontSelected;
    }
}
