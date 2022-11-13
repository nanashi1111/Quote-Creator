package com.burhanrashid52.imageeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.burhanrashid52.imageeditor.listquote.ListQuoteActivity;
import com.burhanrashid52.imageeditor.models.Category;

import java.util.List;

public class QuotePagerAdapter extends PagerAdapter {

    public static int TYPE_TOP = 1;
    public static int TYPE_BOTTOM = 2;

    List<Category> listCate;
    Context context;
    ViewPager viewPager;
    int type;
    RequestOptions glideRequestOptionTop = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Constant.screenWidth, Constant.screenHeight).centerCrop().dontAnimate().dontTransform().priority(Priority.HIGH);
    RequestOptions glideRequestOptionBottom = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Constant.screenWidth, Constant.screenHeight).centerCrop().priority(Priority.HIGH);

    public QuotePagerAdapter(List<Category> listCate, Context context, ViewPager viewPager, int type) {
        this.context = context;
        this.viewPager = viewPager;
        this.type = type;
        this.listCate = listCate;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View view;
        ImageView imageView;
        TextView textView;
        if (type == TYPE_TOP) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_quote_item, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_cate_background_item, null);
            textView = view.findViewById(R.id.tv_quote);
            textView.setText(listCate.get(position).getName());
        }
        imageView = view.findViewById(R.id.ivCateBackground);
        if (type == TYPE_TOP) {
            Glide.with(context).load(listCate.get(position).getResId()).apply(glideRequestOptionTop).into(imageView);
        } else {
            Glide.with(context)
                    .load(listCate.get(position).getResId())
                    .apply(glideRequestOptionBottom)
                    .transition(GenericTransitionOptions.with(R.anim.alpha))
                    .into(imageView);
        }
        try {
            if (type == TYPE_TOP) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (viewPager.getCurrentItem() != position) {
                            viewPager.setCurrentItem(position);
                        } else {
                            Toast.makeText(context, listCate.get(position).getName(), Toast.LENGTH_LONG).show();
                            Bundle bundleLog = new Bundle();
                            bundleLog.putString("category_name", listCate.get(position).getName());

                            Intent intent = new Intent(context, ListQuoteActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constant.KEY_CATEGORY, listCate.get(position).getName());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                });
            }
            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listCate.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
}
