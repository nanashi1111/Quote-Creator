package com.burhanrashid52.imageeditor.mygallery;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.burhanrashid52.imageeditor.R;

import java.io.File;
import java.util.List;

/**
 * Created by admin on 9/22/18.
 */

public class MyGalleryAdapter extends RecyclerView.Adapter<MyGalleryAdapter.GalleryViewHolder> {

    List<String> listPath;
    int itemSize;
    RequestOptions loadImageOption;

    public MyGalleryAdapter(List<String> listPath, int itemSize) {
        this.listPath = listPath;
        this.itemSize = itemSize;
        loadImageOption = new RequestOptions().centerCrop().override(itemSize, itemSize);
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        holder.setData(listPath.get(position));
    }

    @Override
    public int getItemCount() {
        return listPath.size();
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivGallery;
        View vLayer, vShare, vDelete;

        public GalleryViewHolder(View itemView) {
            super(itemView);
            ivGallery = itemView.findViewById(R.id.iv_gallery);
            vLayer = itemView.findViewById(R.id.v_layer);
            vShare = itemView.findViewById(R.id.btShare);
            vDelete = itemView.findViewById(R.id.btDelete);
        }

        public void setData(final String path) {
            Glide.with(itemView.getContext()).load(new File(path)).apply(loadImageOption).into(ivGallery);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btShare) {
                        if (onImageSelect != null) {
                            onImageSelect.share(path);
                        }
                    } else if (v.getId() == R.id.btDelete) {
                        if (onImageSelect != null) {
                            onImageSelect.delete(path);
                        }
                    } else if (v.getId() == R.id.v_layer) {
                        if (onImageSelect != null) {
                            onImageSelect.select(path);
                        }
                    }
                }
            };
            vLayer.setOnClickListener(onClickListener);
            vDelete.setOnClickListener(onClickListener);
            vShare.setOnClickListener(onClickListener);
        }
    }

    public void setOnImageSelect(OnImageSelect onImageSelect) {
        this.onImageSelect = onImageSelect;
    }

    OnImageSelect onImageSelect;

    public interface OnImageSelect {
        void select(String imagePath);

        void delete(String imagePath);

        void share(String imagePath);
    }

    public List<String> getListPath() {
        return listPath;
    }

    public void setListPath(List<String> listPath) {
        this.listPath = listPath;
    }
}
