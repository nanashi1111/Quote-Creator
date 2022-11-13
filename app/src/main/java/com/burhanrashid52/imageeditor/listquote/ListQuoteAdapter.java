package com.burhanrashid52.imageeditor.listquote;

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
import com.burhanrashid52.imageeditor.models.Quote;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.List;

/**
 * Created by admin on 8/19/18.
 */

public class ListQuoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_QUOTE = 1;
    private static final int TYPE_ADS = 2;

    List<Object> listQuote;
    RequestOptions glideRequestOption = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Constant.screenWidth, Constant.screenWidth).centerCrop().dontAnimate().dontTransform().priority(Priority.HIGH);

    public ListQuoteAdapter(List<Object> listQuote) {
        this.listQuote = listQuote;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_QUOTE) {
            return new QuoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quote, parent, false));
        } else {
            return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_native_ads, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_QUOTE) {
            ((QuoteViewHolder) holder).setData((Quote) listQuote.get(position));
        } else {
            ((AdViewHolder) holder).setData((NativeAd) listQuote.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return listQuote.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listQuote.get(position) instanceof Quote) {
            return TYPE_QUOTE;
        }
        return TYPE_ADS;
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeadline;
        TextView tvBody;
        MediaView mv;
        NativeAdView adView;
        TextView tvCallToAction;

        AdViewHolder(View view) {
            super(view);
            adView = itemView.findViewById(R.id.ad_view);
            tvHeadline = itemView.findViewById(R.id.ad_headline);
            tvBody = itemView.findViewById(R.id.ad_body);
            mv = itemView.findViewById(R.id.ad_media);
            tvCallToAction = itemView.findViewById(R.id.ad_call_to_action);


            adView.setHeadlineView(tvHeadline);
            adView.setMediaView(mv);
            adView.setBodyView(tvBody);
            adView.setCallToActionView(tvCallToAction);

            mv.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
                @Override
                public void onChildViewAdded(View parent, View child) {
                    if (child != null && child instanceof ImageView) {
                        ((ImageView) child).setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }
                }

                @Override
                public void onChildViewRemoved(View parent, View child) {

                }
            });



            /*adView.iconView = itemView.ad_icon
            adView.priceView = itemView.ad_price
            adView.starRatingView = itemView.ad_stars
            adView.storeView = itemView.ad_store
            adView.advertiserView = itemView.ad_advertiser*/
        }

        public void setData(NativeAd ad) {
            tvHeadline.setText(ad.getHeadline());
            tvBody.setText(ad.getBody());
            tvCallToAction.setText(ad.getCallToAction());
            adView.setNativeAd(ad);

        }
    }

    class QuoteViewHolder extends RecyclerView.ViewHolder {

        ImageView ivQuote;
        TextView tvQuote;
        View vLayer;
        View btShare, btCopy, btEdit;

        public QuoteViewHolder(final View itemView) {
            super(itemView);
            ivQuote = itemView.findViewById(R.id.iv_quote);
            tvQuote = itemView.findViewById(R.id.tv_quote);
            int id = Constant.getResourceFontId("13");
            tvQuote.setTypeface(ResourcesCompat.getFont(itemView.getContext(), id));

            btShare = itemView.findViewById(R.id.btShare);
            btCopy = itemView.findViewById(R.id.btCopy);
            vLayer = itemView.findViewById(R.id.v_layer);
            btEdit = itemView.findViewById(R.id.btEdit);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (quoteSelectListener == null) {
                        return;
                    }
                    if (v.getId() == R.id.v_layer || v.getId() == R.id.btEdit) {
                        quoteSelectListener.onQuoteSelect((Quote) listQuote.get(getLayoutPosition()));
                    } else if (v.getId() == R.id.btCopy) {
                        quoteSelectListener.onQuoteCopy((Quote) listQuote.get(getLayoutPosition()));
                    } else if (v.getId() == R.id.btShare) {
                        quoteSelectListener.onQuoutShare((Quote) listQuote.get(getLayoutPosition()));
                    }
                }
            };
            vLayer.setOnClickListener(onClickListener);
            btEdit.setOnClickListener(onClickListener);
            btCopy.setOnClickListener(onClickListener);
            btShare.setOnClickListener(onClickListener);

        }

        public void setData(Quote quote) {
            //Glide.with(itemView.getContext()).load(quote.getResId()).into(ivQuote);
            Glide.with(itemView.getContext()).load(quote.getRef()).apply(glideRequestOption).into(ivQuote);
            tvQuote.setText(quote.getContent());
        }
    }

    QuoteSelectListener quoteSelectListener;

    public List<Object> getListQuote() {
        return listQuote;
    }

    public interface QuoteSelectListener {
        void onQuoteSelect(Quote quote);

        void onQuoteCopy(Quote quote);

        void onQuoutShare(Quote quote);
    }

    public QuoteSelectListener getQuoteSelectListener() {
        return quoteSelectListener;
    }

    public void setQuoteSelectListener(QuoteSelectListener quoteSelectListener) {
        this.quoteSelectListener = quoteSelectListener;
    }
}
