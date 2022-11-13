package com.burhanrashid52.imageeditor.backgrounds;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.R;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 8/19/18.
 */

public class BackgroundViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ADD = 0;
    private static final int TYPE_SET = 1;

    //List<Ref> listBgReference;

    /*public BackgroundViewAdapter(List<Ref> listBgReference, OnBackgroundSelectListener onBackgroundSelectListener) {
        this.listBgReference = listBgReference;
        this.onBackgroundSelectListener = onBackgroundSelectListener;
    }*/

    List<Integer> listRes;

    public BackgroundViewAdapter(OnBackgroundSelectListener onBackgroundSelectListener) {
        this.onBackgroundSelectListener = onBackgroundSelectListener;
        listRes = new ArrayList<>();

        listRes.add(R.drawable.bg_44);
        listRes.add(R.drawable.bg_45);
        listRes.add(R.drawable.bg_46);
        listRes.add(R.drawable.bg_47);
        listRes.add(R.drawable.bg_48);
        listRes.add(R.drawable.bg_49);
        listRes.add(R.drawable.bg_50);
        listRes.add(R.drawable.bg_51);
        listRes.add(R.drawable.bg_52);
        listRes.add(R.drawable.bg_53);
        listRes.add(R.drawable.bg_54);
        listRes.add(R.drawable.bg_55);
        listRes.add(R.drawable.bg_56);
        listRes.add(R.drawable.bg_57);
        listRes.add(R.drawable.bg_58);
        listRes.add(R.drawable.bg_59);
        listRes.add(R.drawable.bg_60);
        listRes.add(R.drawable.bg_61);
        listRes.add(R.drawable.bg_62);
        listRes.add(R.drawable.bg_63);
        listRes.add(R.drawable.bg_64);
        listRes.add(R.drawable.bg_65);
        listRes.add(R.drawable.bg_66);
        listRes.add(R.drawable.bg_67);
        listRes.add(R.drawable.bg_68);
        listRes.add(R.drawable.bg_69);
        listRes.add(R.drawable.bg_70);
        listRes.add(R.drawable.bg_71);
        listRes.add(R.drawable.bg_72);
        listRes.add(R.drawable.bg_73);
        listRes.add(R.drawable.bg_74);
        listRes.add(R.drawable.bg_75);
        listRes.add(R.drawable.bg_76);
        listRes.add(R.drawable.bg_77);
        listRes.add(R.drawable.bg_78);
        listRes.add(R.drawable.bg_79);
        listRes.add(R.drawable.bg_80);
        listRes.add(R.drawable.bg_81);
        listRes.add(R.drawable.bg_82);
        listRes.add(R.drawable.bg_83);
        listRes.add(R.drawable.bg_84);
        listRes.add(R.drawable.bg_85);
        listRes.add(R.drawable.bg_86);

        listRes.add(R.drawable.bg_01);
        listRes.add(R.drawable.bg_02);
        listRes.add(R.drawable.bg_03);
        listRes.add(R.drawable.bg_04);
        listRes.add(R.drawable.bg_05);
        listRes.add(R.drawable.bg_06);
        listRes.add(R.drawable.bg_07);
        listRes.add(R.drawable.bg_08);
        listRes.add(R.drawable.bg_09);
        listRes.add(R.drawable.bg_10);
        listRes.add(R.drawable.bg_11);
        listRes.add(R.drawable.bg_12);
        listRes.add(R.drawable.bg_13);
        listRes.add(R.drawable.bg_14);
        listRes.add(R.drawable.bg_15);
        listRes.add(R.drawable.bg_16);
        listRes.add(R.drawable.bg_17);
        listRes.add(R.drawable.bg_18);
        listRes.add(R.drawable.bg_19);
        listRes.add(R.drawable.bg_20);
        listRes.add(R.drawable.bg_21);
        listRes.add(R.drawable.bg_22);
        listRes.add(R.drawable.bg_23);
        listRes.add(R.drawable.bg_24);
        listRes.add(R.drawable.bg_25);
        listRes.add(R.drawable.bg_26);
        listRes.add(R.drawable.bg_27);
        listRes.add(R.drawable.bg_28);
        listRes.add(R.drawable.bg_29);
        listRes.add(R.drawable.bg_30);
        listRes.add(R.drawable.bg_31);
        listRes.add(R.drawable.bg_32);
        listRes.add(R.drawable.bg_33);
        listRes.add(R.drawable.bg_34);
        listRes.add(R.drawable.bg_35);
        listRes.add(R.drawable.bg_36);
        listRes.add(R.drawable.bg_37);
        listRes.add(R.drawable.bg_38);
        listRes.add(R.drawable.bg_39);
        listRes.add(R.drawable.bg_40);
        listRes.add(R.drawable.bg_41);
        listRes.add(R.drawable.bg_42);
        listRes.add(R.drawable.bg_43);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SET) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false));
        } else {
            return new AddViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SET) {
            ((ViewHolder) holder).setData(listRes.get(position));
        } else {
            ((AddViewHolder) holder).setData();
        }
    }

    @Override
    public int getItemCount() {
        return listRes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ADD;
        }
        return TYPE_SET;
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        AddViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            mTxtFilterName.setVisibility(View.GONE);
            //itemView.setBackgroundResource(R.drawable.bg_add_photo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBackgroundSelectListener != null) {
                        onBackgroundSelectListener.addFromGallery();
                    }
                }
            });
        }

        void setData() {
            mImageFilterView.setImageResource(R.drawable.ic_select_from_gallery);
            mImageFilterView.setPadding(15, 15, 15, 15);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            mTxtFilterName.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBackgroundSelectListener != null) {
                        onBackgroundSelectListener.onBackgroundSelect(listRes.get(getLayoutPosition()));
                    }
                }
            });

        }

        void setData(Integer source) {
            Glide.with(itemView.getContext()).load(source).apply(Constant.glideRequestOption).into(mImageFilterView);

        }
    }

    OnBackgroundSelectListener onBackgroundSelectListener;

    public interface OnBackgroundSelectListener {
        void onBackgroundSelect(Integer source);

        void addFromGallery();
    }

    public OnBackgroundSelectListener getOnBackgroundSelectListener() {
        return onBackgroundSelectListener;
    }

    public void setOnBackgroundSelectListener(OnBackgroundSelectListener onBackgroundSelectListener) {
        this.onBackgroundSelectListener = onBackgroundSelectListener;
    }
}
