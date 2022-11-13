package com.burhanrashid52.imageeditor.fonts;

import android.content.Context;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 8/22/18.
 */

public class FontAdapter extends RecyclerView.Adapter<FontAdapter.FontViewHolder> {

    AssetManager assetManager;
    List<String> fontNames;

    public FontAdapter(Context context) {
        fontNames = new ArrayList<>();
        for (int i = 1; i <= 45; i++) {
            fontNames.add(String.format("%02d.TTF", i));
        }
        assetManager = context.getAssets();
    }

    @NonNull
    @Override
    public FontViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FontViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FontViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return fontNames.size();
    }

    class FontViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageFilterView;
        TextView mTxtFilterName;
        TextView tvFont;

        public FontViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            mTxtFilterName.setVisibility(View.GONE);
            tvFont = itemView.findViewById(R.id.tvFont);
            tvFont.setVisibility(View.VISIBLE);
            tvFont.setAlpha(0.85f);
        }

        public void setData(final int position) {
            try {
                String fontName = fontNames.get(position);
                String fontCode = fontName.replace("fonts/", "").replace(".TTF", "");
                int id = Constant.getResourceFontId(fontCode);
                tvFont.setTypeface(ResourcesCompat.getFont(itemView.getContext(), id));
            } catch (RuntimeException e) {

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFontSelected != null) {
                        onFontSelected.onFontSelected(String.format("fonts/%s", fontNames.get(position)));
                    }
                }
            });
        }
    }

    OnFontSelected onFontSelected;

    public interface OnFontSelected {
        void onFontSelected(String path);
    }


    public void setOnFontSelected(OnFontSelected onFontSelected) {
        this.onFontSelected = onFontSelected;
    }
}
