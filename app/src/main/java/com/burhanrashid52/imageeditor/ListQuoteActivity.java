package com.burhanrashid52.imageeditor;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.custom.CarouselEffectTransformer;
import com.burhanrashid52.imageeditor.custom.CircleIndicator;
import com.burhanrashid52.imageeditor.models.Category;

import java.util.ArrayList;


public class ListQuoteActivity extends BaseActivity {

    ViewPager vpQuote, viewPagerBackground;
    CircleIndicator indicator;
    ArrayList<Category> listCategory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_list_quote);
        prepareListCate();
        vpQuote = findViewById(R.id.vp_quote);
        viewPagerBackground = findViewById(R.id.viewPagerbackground);
        vpQuote.setClipChildren(false);
        vpQuote.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.pager_margin));
        vpQuote.setOffscreenPageLimit(3);
        vpQuote.setPageTransformer(false, new CarouselEffectTransformer(this));
        vpQuote.setAdapter(new QuotePagerAdapter(listCategory, this, vpQuote, QuotePagerAdapter.TYPE_TOP));
        viewPagerBackground.setAdapter(new QuotePagerAdapter(listCategory, this, viewPagerBackground, QuotePagerAdapter.TYPE_BOTTOM));
        indicator = findViewById(R.id.indicator);

        indicator.setViewPager(viewPagerBackground);
        vpQuote.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            private int index = 0;

            @Override
            public void onPageSelected(int position) {
                index = position;

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int width = viewPagerBackground.getWidth();
                viewPagerBackground.scrollTo((int) (width * position + width * positionOffset), 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    viewPagerBackground.setCurrentItem(index);
                }

            }
        });

        viewPagerBackground.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (vpQuote.getCurrentItem() != position) {
                    vpQuote.setCurrentItem(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void prepareListCate() {
        listCategory = new ArrayList<>();
        listCategory.add(new Category(R.drawable.bg_category_love, "Love"));
        listCategory.add(new Category(R.drawable.bg_category_motivation, "Motivation"));
        listCategory.add(new Category(R.drawable.bg_category_friendship, "Friendship"));
        listCategory.add(new Category(R.drawable.bg_category_family, "Family"));
        listCategory.add(new Category(R.drawable.bg_category_life, "Life"));
        listCategory.add(new Category(R.drawable.bg_category_happiness, "Happiness"));
        listCategory.add(new Category(R.drawable.bg_category_woman, "Women"));
        listCategory.add(new Category(R.drawable.bg_category_mother, "Mother"));
        listCategory.add(new Category(R.drawable.bg_category_sad, "Sad"));

    }


}
