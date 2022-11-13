package com.burhanrashid52.imageeditor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.TransitionManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.burhanrashid52.imageeditor.backgrounds.BackgroundViewAdapter;
import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.border.BorderAdapter;
import com.burhanrashid52.imageeditor.crop.CropImageActivity;
import com.burhanrashid52.imageeditor.filters.FilterListener;
import com.burhanrashid52.imageeditor.filters.FilterViewAdapter;
import com.burhanrashid52.imageeditor.fonts.FontAdapter;
import com.burhanrashid52.imageeditor.tools.EditingToolsAdapter;
import com.burhanrashid52.imageeditor.tools.ToolType;
import com.burhanrashid52.imageeditor.utils.FileUtils;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.burhanrashid52.imageeditor.utils.PermissionUtils;
import com.burhanrashid52.imageeditor.utils.PreferenceUtils;
import com.desmond.squarecamera.CameraActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ja.burhanrashid52.photoeditor.KerningTextView;
import ja.burhanrashid52.photoeditor.OnPhotoEditorListener;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.SaveSettings;
import ja.burhanrashid52.photoeditor.ViewType;
import ja.burhanrashid52.photoeditor.PhotoFilter;
import top.defaults.drawabletoolbox.DrawableBuilder;

public class EditImageActivity extends BaseActivity implements OnPhotoEditorListener,
        View.OnClickListener,
        PropertiesBSFragment.Properties,
        EmojiBSFragment.EmojiListener,
        StickerBSFragment.StickerListener, EditingToolsAdapter.OnItemSelected, FilterListener, FontAdapter.OnFontSelected,
        BackgroundViewAdapter.OnBackgroundSelectListener,
        BorderAdapter.OnBorderSelected/*, ColorSeekBar.OnColorChangeListener*/ {

    private static String DEFAULT_FONT_PATH = "13";
    private Map<String, Map<String, String>> configMap = new HashMap();

    private static final String TAG = EditImageActivity.class.getSimpleName();
    public static final String EXTRA_IMAGE_PATHS = "extra_image_paths";
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private static final int SELECT_IMAGE_REQUEST = 54;
    private PhotoEditor mPhotoEditor;
    private PhotoEditorView mPhotoEditorView;
    private PropertiesBSFragment mPropertiesBSFragment;
    private EmojiBSFragment mEmojiBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private TextView mTxtCurrentTool;
    private Typeface mWonderFont;
    private RecyclerView mRvTools, mRvFilters, mRvBackgrounds, mRvFont, mRvBorder;

    private EditingToolsAdapter mEditingToolsAdapter = new EditingToolsAdapter(this);
    private FilterViewAdapter mFilterViewAdapter = new FilterViewAdapter(this);
    private FontAdapter mFontAdapter;
    private BorderAdapter mBorderAdapter;

    private ConstraintLayout llColor;
    private LinearLayout llBorder;
    private LinearLayout llTextSetting;
    private Switch swEnableBorder;
    //private ColorSeekBar colorSlider;
    private View btAlightLeft, btAlightCenter, btAlightRight;
    private BackgroundViewAdapter mBackgroundViewAdapter;
    private ConstraintLayout mRootView;
    private ConstraintSet mConstraintSet = new ConstraintSet();
    private boolean mIsFilterVisible;

    private View mrlParentBold, mrlParentItalic, mrlParentUnderline, mrlParentLeft, mrlParentCenter, mrlParentRight;
    private boolean saved = false;


    InterstitialAd popup;
    /*AdView banner;
    NativeAdView nativeAdView;*/

    String preText = "\"Double tap to edit\nPin to zoom and rotate\"";
    int preSourceRef = R.drawable.bg_01;

    LinearLayout rlAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeFullScreen();
        setContentView(R.layout.activity_edit_image);

        initViews();
        initAds();
        Toast.makeText(this, "Tip: Adjust the overlay to increase or decrease brightness", Toast.LENGTH_LONG).show();

        if (getIntent().getExtras() != null) {
            preText = getIntent().getExtras().getString(Constant.KEY_PRE_TEXT, "Double tap to edit");
            preSourceRef = getIntent().getIntExtra(Constant.KEY_PRE_SOURCE, R.drawable.bg_01);
        }

        mWonderFont = Typeface.createFromAsset(getAssets(), "beyond_wonderland.ttf");

        mPropertiesBSFragment = new PropertiesBSFragment();
        mEmojiBSFragment = new EmojiBSFragment();
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        mEmojiBSFragment.setEmojiListener(this);
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        LinearLayoutManager llmTools = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvTools.setLayoutManager(llmTools);
        mRvTools.setAdapter(mEditingToolsAdapter);

        LinearLayoutManager llmFilters = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFilters.setLayoutManager(llmFilters);
        mRvFilters.setAdapter(mFilterViewAdapter);

        LinearLayoutManager llmBackgrounds = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvBackgrounds.setLayoutManager(llmBackgrounds);
        mBackgroundViewAdapter = new BackgroundViewAdapter(this);

        mRvBackgrounds.setAdapter(mBackgroundViewAdapter);

        LinearLayoutManager llmFonts = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvFont.setLayoutManager(llmFonts);
        mFontAdapter = new FontAdapter(this);
        mFontAdapter.setOnFontSelected(this);
        mRvFont.setAdapter(mFontAdapter);

        LinearLayoutManager llmBorders = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRvBorder.setLayoutManager(llmBorders);
        mBorderAdapter = new BorderAdapter();
        mBorderAdapter.setOnFontSelected(this);
        mRvBorder.setAdapter(mBorderAdapter);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);
        mPhotoEditorView.setOnClickListener(this);

        //mPhotoEditorView.getSource().setImageResource(preSource);
        Glide.with(this).load(preSourceRef).apply(Constant.glideRequestOption).into(mPhotoEditorView.getSource());
        selectedTextView = mPhotoEditor.addText(ResourcesCompat.getFont(this, Constant.getResourceFontId("13")), preText, Color.WHITE);
        configMap.put(selectedTextView.getTag().toString(), generateDefaultFontMap());
    }

    private void initAds() {
        boolean isCanShowAds = PreferenceUtils.isCanShowAds(this);
        if(!isCanShowAds) {
            return;
        }
        popup = null;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, BuildConfig.popup_ads_id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        popup = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        LogUtils.d("onAdFailedToLoad: "+loadAdError.toString());
                        popup = null;
                    }
                });



    }

    /*private void initNonPopupAds() {
        boolean isCanShowAds = PreferenceUtils.isCanShowAds(this);
        rlAds = findViewById(R.id.rlAds);
        if (!isCanShowAds || !PreferenceUtils.canShowBannerNativeAdsInEditingScreen(this)) {
            rlAds.setVisibility(View.GONE);
            return;
        }


        nativeAdView = findViewById(R.id.nativeAdView);
        banner = findViewById(R.id.adView);


        String adsTypePriority = PreferenceUtils.getFirstAdsTypePriority(this);
        LogUtils.d("adsTypePriority = " + adsTypePriority);
        //use banner
        if (adsTypePriority.equalsIgnoreCase("banner")) {
            boolean showNativeIfLoadBannerFailed = true;
            showBannerAds(showNativeIfLoadBannerFailed);
        }
        //use native ads
        else if (adsTypePriority.equalsIgnoreCase("native")) {
            LogUtils.d("NativeAdSize = " + Constant.listNativeAds.size());
            if (Constant.listNativeAds.size() > 0) {
                showNativeAds();
            } else {
                boolean showNativeIfLoadBannerFailed = false;
                showBannerAds(showNativeIfLoadBannerFailed);
            }
        } else {
            rlAds.setVisibility(View.GONE);
        }
    }*/

    /*private void showNativeAds() {

        banner.setVisibility(View.GONE);

        if (Constant.listNativeAds.size() == 0) {
            return;
        }


        ImageView ivIcon = findViewById(R.id.ivIcon);
        TextView tvHeadline = findViewById(R.id.tvHeadline);
        TextView tvBody = findViewById(R.id.tvBody);
        TextView tvCallToAction = findViewById(R.id.tvCallToAction);
        TextView tvAd = findViewById(R.id.tvAdv);
        MediaView adMedia = findViewById(R.id.adMedia);
        nativeAdView.setVisibility(View.VISIBLE);

        nativeAdView.setIconView(ivIcon);
        nativeAdView.setHeadlineView(tvHeadline);
        nativeAdView.setBodyView(tvBody);
        nativeAdView.setCallToActionView(tvCallToAction);
        nativeAdView.setAdvertiserView(tvAd);

        com.google.android.gms.ads.nativead.NativeAd unifiedNativeAd = Constant.listNativeAds.get(new Random().nextInt(Constant.listNativeAds.size()));
        NativeAd.Image icon = unifiedNativeAd.getIcon();
        if (icon != null) {
            ivIcon.setImageDrawable(icon.getDrawable());
        } else {
            ivIcon.setVisibility(View.INVISIBLE);
            adMedia.setVisibility(View.VISIBLE);
            nativeAdView.setMediaView(adMedia);
        }
        if (unifiedNativeAd.getHeadline() != null) {
            tvHeadline.setText(unifiedNativeAd.getHeadline());
        }
        if (unifiedNativeAd.getAdvertiser() != null) {
            tvAd.setText(unifiedNativeAd.getAdvertiser());
        }
        if (unifiedNativeAd.getBody() != null) {
            tvBody.setText(unifiedNativeAd.getBody());
        }
        if (unifiedNativeAd.getCallToAction() != null) {
            tvCallToAction.setText(unifiedNativeAd.getCallToAction());
        }

        nativeAdView.setNativeAd(unifiedNativeAd);


        llColor.post(new Runnable() {
            @Override
            public void run() {
                int llColorHeight = llColor.getHeight();
                int adsHeight = rlAds.getHeight();
                int settingBarHeight = llTextSetting.getHeight();
                LogUtils.d("llColorHeight = " + llColorHeight + " ; adsHeight = " + adsHeight + " ;settingBarHeight = " + settingBarHeight);
                if (llColorHeight - adsHeight - settingBarHeight < getResources().getDimensionPixelSize(R.dimen._20sdp)) {
                    rlAds.setVisibility(View.GONE);
                }
            }
        });
    }

    private void showBannerAds(final boolean showNativeIfBannerFailed) {

        nativeAdView.setVisibility(View.GONE);

        boolean canShowBanner = PreferenceUtils.isEnableShowBannerAds(this);
        LogUtils.d("canShowBanner = " + canShowBanner);
        if (!canShowBanner) {
            rlAds.setVisibility(View.GONE);
            return;
        }

        banner.setVisibility(View.VISIBLE);
        banner.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                LogUtils.d("Banner load failed");
                if (showNativeIfBannerFailed) {
                    showNativeAds();
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                LogUtils.d("Banner loaded");
                llColor.post(new Runnable() {
                    @Override
                    public void run() {
                        int llColorHeight = llColor.getHeight();
                        int adsHeight = rlAds.getHeight();
                        int settingBarHeight = llTextSetting.getHeight();
                        LogUtils.d("llColorHeight = " + llColorHeight + " ; adsHeight = " + adsHeight + " ;settingBarHeight = " + settingBarHeight);
                        if (llColorHeight - adsHeight - settingBarHeight < getResources().getDimensionPixelSize(R.dimen._20sdp)) {
                            rlAds.setVisibility(View.GONE);
                            PreferenceUtils.saveEnableShowBannerAds(EditImageActivity.this, false);
                        }
                    }
                });
            }
        });
        LogUtils.d("MBanner loading...");
        banner.loadAd(getAdRequest());
    }*/

    private void initViews() {

        llTextSetting = findViewById(R.id.llTextSetting);

        ImageView imgUndo;
        ImageView imgRedo;
        ImageView imgCamera;
        ImageView imgGallery;
        ImageView imgSave;
        ImageView imgClose;

        mPhotoEditorView = findViewById(R.id.photoEditorView);
        mTxtCurrentTool = findViewById(R.id.txtCurrentTool);
        mRvTools = findViewById(R.id.rvConstraintTools);
        mRvFilters = findViewById(R.id.rvFilterView);
        mRvBackgrounds = findViewById(R.id.rvBackgroundsView);
        mRvFont = findViewById(R.id.rvFont);
        mRvBorder = findViewById(R.id.rvBorder);
        mRootView = findViewById(R.id.rootView);


        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgGallery = findViewById(R.id.imgGallery);
        imgGallery.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        llColor = findViewById(R.id.llColor);
        llColor.setOnClickListener(this);
        /*colorSlider = findViewById(R.id.colorSlider);
        colorSlider.setOnColorChangeListener(this);*/
        btAlightLeft = findViewById(R.id.btAlignLeft);
        btAlightLeft.setOnClickListener(this);
        btAlightCenter = findViewById(R.id.btAlignCenter);
        btAlightCenter.setOnClickListener(this);
        btAlightRight = findViewById(R.id.btAlignRight);
        btAlightRight.setOnClickListener(this);

        mrlParentBold = findViewById(R.id.mrlParentBold);
        mrlParentItalic = findViewById(R.id.mrlParentItalic);
        mrlParentUnderline = findViewById(R.id.mrlParentUnderline);
        mrlParentLeft = findViewById(R.id.mrlParentLeft);
        mrlParentCenter = findViewById(R.id.mrlParentCenter);
        mrlParentRight = findViewById(R.id.mrlParentRigth);


        llBorder = findViewById(R.id.llBorder);
        findViewById(R.id.btPickColorForText).setOnClickListener(this);
        findViewById(R.id.btColorPickerBorder).setOnClickListener(this);
        findViewById(R.id.btBold).setOnClickListener(this);
        findViewById(R.id.btItalic).setOnClickListener(this);
        findViewById(R.id.btUnderline).setOnClickListener(this);

        swEnableBorder = findViewById(R.id.sEnableBorder);
        swEnableBorder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                View border = mPhotoEditorView.findBorderView();
                if (border != null) {
                    if (isChecked) {
                        border.setVisibility(View.VISIBLE);
                    } else {
                        border.setVisibility(View.GONE);
                    }
                }
            }
        });

        // mRootView.setOnClickListener(this);
        findViewById(R.id.llColor).setOnClickListener(this);
        findViewById(R.id.mrlBorder).setOnClickListener(this);
        findViewById(R.id.mrlSpacing).setOnClickListener(this);
        findViewById(R.id.mrlTextSize).setOnClickListener(this);
        findViewById(R.id.mrlTextRotation).setOnClickListener(this);
    }


    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode) {
                mPhotoEditor.editText(rootView, inputText, colorCode);
                mTxtCurrentTool.setText(R.string.label_text);
            }
        });
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        saved = false;
        Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
        Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    KerningTextView selectedTextView;
    ImageView selectedBorder;

    @Override
    public void onStartViewChangeListener(View view, ViewType viewType) {
        Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
        saved = false;
        if (viewType != null && viewType.equals(ViewType.TEXT)) {
            if (!mIsFontVisible) {
                showFonts(true);
            }
            selectedTextView = view.findViewById(R.id.tvPhotoEditorText);
            showFontSetting(selectedTextView);
            if (selectedTextView != null) {
                Log.d(TAG, "onStartViewChangeListener() " + selectedTextView.getText().toString());
            } else {
                Log.d(TAG, "onStartViewChangeListener() tvnull");
            }

            /*if (llColor.getVisibility() != View.VISIBLE) {
                llColor.setVisibility(View.VISIBLE);
                //rlAds.setVisibility(View.GONE);
                Log.d(TAG, "ShowFontColor");
            }*/


            if (llTextSetting.getVisibility() != View.VISIBLE) {
                llTextSetting.setVisibility(View.VISIBLE);
                //rlAds.setVisibility(View.GONE);
                Log.d(TAG, "ShowFontColor");
            }

            if (llBorder.getVisibility() == View.VISIBLE) {
                llBorder.setVisibility(View.GONE);
            }

            showBorder(false);
        } else if (view.getTag() != null && view.getTag() instanceof String && ((String) view.getTag()).startsWith(ja.burhanrashid52.photoeditor.Constant.TAG_BORDER)) {
            if (llBorder.getVisibility() != View.VISIBLE) {
                llBorder.setVisibility(View.VISIBLE);
                //rlAds.setVisibility(View.GONE);
                Log.d(TAG, "ShowBorderColor");
            }

            /*if (llColor.getVisibility() == View.VISIBLE) {
                llColor.setVisibility(View.GONE);

            }*/
            if (llTextSetting.getVisibility() == View.VISIBLE) {
                llTextSetting.setVisibility(View.INVISIBLE);

            }
            showFonts(false);
            showBorder(true);
            if (view.getTag() != null && view.getTag() instanceof String && ((String) view.getTag()).startsWith(ja.burhanrashid52.photoeditor.Constant.TAG_BORDER)) {
                selectedBorder = view.findViewById(R.id.imgPhotoEditorImage);
            }
        }
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
        Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mrlTextSize:
                showDialogTextSizeRotationSetting(TYPE_CHANGE_TEXT_SIZE);
                break;
            case R.id.mrlTextRotation:
                showDialogTextSizeRotationSetting(TYPE_CHANGE_TEXT_ROTATION);
                break;
            case R.id.mrlBorder:
                showDialogBorderSetting();
                break;
            case R.id.mrlSpacing:
                showDialogSpaceSetting();
                break;
            case R.id.bt_border_color:
                showColorPicker(TYPE_CHANGE_TEXT_BORDER_COLOR);
                break;
            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                saveImage();
                break;

            case R.id.imgClose:

                onBackPressed();
                break;

            case R.id.imgCamera:
                showCamera();
                break;

            case R.id.imgGallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                break;


            case R.id.btBold:
                if (selectedTextView != null) {
                    Map<String, String> configMap = getConfigMap(selectedTextView);
                    if (Boolean.parseBoolean(configMap.get(B))) {
                        mrlParentBold.setBackgroundResource(0);
                        if (Boolean.parseBoolean(configMap.get(I))) {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.ITALIC);
                            selectedTextView.setTypeface(tfBold);
                        } else {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.NORMAL);
                            selectedTextView.setTypeface(tfBold);
                        }
                        getConfigMap(selectedTextView).put(B, "false");
                    } else {
                        mrlParentBold.setBackgroundResource(R.drawable.bg_align);
                        if (Boolean.parseBoolean(configMap.get(I))) {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.BOLD_ITALIC);
                            selectedTextView.setTypeface(tfBold);
                        } else {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.BOLD);
                            selectedTextView.setTypeface(tfBold);
                        }
                        getConfigMap(selectedTextView).put(B, "true");
                    }

                }
                break;
            case R.id.btItalic:
                if (selectedTextView != null) {
                    Map<String, String> configMap = getConfigMap(selectedTextView);
                    if (Boolean.parseBoolean(configMap.get(I))) {
                        mrlParentItalic.setBackgroundResource(0);
                        if (Boolean.parseBoolean(configMap.get(B))) {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.BOLD);
                            selectedTextView.setTypeface(tfBold);
                        } else {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.NORMAL);
                            selectedTextView.setTypeface(tfBold);
                        }
                        getConfigMap(selectedTextView).put(I, "false");
                    } else {
                        mrlParentItalic.setBackgroundResource(R.drawable.bg_align);
                        if (Boolean.parseBoolean(configMap.get(B))) {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.BOLD_ITALIC);
                            selectedTextView.setTypeface(tfBold);
                        } else {
                            Typeface tfBold = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId(configMap.get(FONT))), Typeface.ITALIC);
                            selectedTextView.setTypeface(tfBold);
                        }
                        getConfigMap(selectedTextView).put(I, "true");
                    }

                }
                break;
            case R.id.btUnderline:
                if (selectedTextView != null) {
                    Map<String, String> configMap = getConfigMap(selectedTextView);
                    if (Boolean.parseBoolean(configMap.get(U))) {
                        selectedTextView.setPaintFlags(selectedTextView.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
                        mrlParentUnderline.setBackgroundResource(0);
                        getConfigMap(selectedTextView).put(U, "false");
                    } else {
                        selectedTextView.setPaintFlags(selectedTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                        mrlParentUnderline.setBackgroundResource(R.drawable.bg_align);
                        getConfigMap(selectedTextView).put(U, "true");
                    }


                }
                break;
            case R.id.btAlignLeft:
                if (selectedTextView != null) {
                    Map<String, String> configMap = getConfigMap(selectedTextView);
                    if (Boolean.parseBoolean(configMap.get(LEFT))) {
                        selectedTextView.setGravity(Gravity.LEFT);
                        mrlParentLeft.setBackgroundResource(0);
                        configMap.put(LEFT, "false");
                    } else {
                        selectedTextView.setGravity(Gravity.LEFT);
                        mrlParentLeft.setBackgroundResource(R.drawable.bg_align);
                        configMap.put(CENTER, "false");
                        configMap.put(LEFT, "true");
                        configMap.put(RIGHT, "false");

                        mrlParentRight.setBackgroundResource(0);
                        mrlParentCenter.setBackgroundResource(0);
                    }
                }
                break;
            case R.id.btAlignRight:
                if (selectedTextView != null) {
                    Map<String, String> configMap = getConfigMap(selectedTextView);
                    if (Boolean.parseBoolean(configMap.get(RIGHT))) {
                        selectedTextView.setGravity(Gravity.LEFT);
                        mrlParentRight.setBackgroundResource(0);
                        configMap.put(RIGHT, "false");
                    } else {
                        selectedTextView.setGravity(Gravity.RIGHT);
                        mrlParentRight.setBackgroundResource(R.drawable.bg_align);
                        configMap.put(CENTER, "false");
                        configMap.put(LEFT, "false");
                        configMap.put(RIGHT, "true");

                        mrlParentCenter.setBackgroundResource(0);
                        mrlParentLeft.setBackgroundResource(0);
                    }
                }
                break;
            case R.id.btAlignCenter:
                if (selectedTextView != null) {
                    Map<String, String> configMap = getConfigMap(selectedTextView);
                    if (Boolean.parseBoolean(configMap.get(CENTER))) {
                        selectedTextView.setGravity(Gravity.LEFT);
                        mrlParentCenter.setBackgroundResource(0);
                        configMap.put(CENTER, "false");
                    } else {
                        selectedTextView.setGravity(Gravity.CENTER);
                        mrlParentCenter.setBackgroundResource(R.drawable.bg_align);
                        configMap.put(CENTER, "true");
                        configMap.put(LEFT, "false");
                        configMap.put(RIGHT, "false");

                        mrlParentLeft.setBackgroundResource(0);
                        mrlParentRight.setBackgroundResource(0);
                    }

                }
                break;
            case R.id.btPickColorForText:
                showColorPicker(TYPE_CHANGE_TEXT_COLOR);
                break;
            case R.id.btColorPickerBorder:
                showColorPicker(TYPE_CHANGE_BORDER_COLOR);
                break;
            case R.id.photoEditorView:

                if (llTextSetting.getVisibility() == View.VISIBLE) {
                    llTextSetting.setVisibility(View.INVISIBLE);
                    //rlAds.setVisibility(View.VISIBLE);
                }

                if (llBorder.getVisibility() == View.VISIBLE) {
                    llBorder.setVisibility(View.GONE);
                    //rlAds.setVisibility(View.VISIBLE);
                }

                if (mIsFilterVisible) {
                    showFilter(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                } else if (mIsBackgroundsVisible) {
                    showBackgrounds(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                } else if (mIsFontVisible) {
                    showFonts(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                } else if (mIsBorderVisible) {
                    showBorder(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                }
                break;
            case R.id.llColor:
                /*if (llColor.getVisibility() == View.VISIBLE) {
                    llColor.setVisibility(View.GONE);
                    //rlAds.setVisibility(View.VISIBLE);
                }*/

                if (llTextSetting.getVisibility() == View.VISIBLE) {
                    llTextSetting.setVisibility(View.INVISIBLE);
                    //rlAds.setVisibility(View.VISIBLE);
                }

                if (llBorder.getVisibility() == View.VISIBLE) {
                    llBorder.setVisibility(View.GONE);
                    //rlAds.setVisibility(View.VISIBLE);
                }

                if (mIsFilterVisible) {
                    showFilter(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                } else if (mIsBackgroundsVisible) {
                    showBackgrounds(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                } else if (mIsFontVisible) {
                    showFonts(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                } else if (mIsBorderVisible) {
                    showBorder(false);
                    mTxtCurrentTool.setText(R.string.app_name);
                }
                break;
        }
    }

    Dialog colorPickerDialog;
    private static final int TYPE_CHANGE_OVERLAY_COLOR = 1;
    private static final int TYPE_CHANGE_TEXT_COLOR = 2;
    private static final int TYPE_CHANGE_BORDER_COLOR = 3;
    private static final int TYPE_CHANGE_TEXT_BORDER_COLOR = 4;
    int colorType = 0;

    private void showColorPicker(final int type) {
        colorType = type;
        Toast.makeText(this, "Tip: Drag bottom horizontal bar to adjust opacity", Toast.LENGTH_LONG).show();
        Log.d("ColorType", colorType + "");
        //log eventd
        if (colorType == TYPE_CHANGE_OVERLAY_COLOR) {
            logEvent("Action_Change_Overlay_Color");
        } else if (colorType == TYPE_CHANGE_TEXT_COLOR) {
            logEvent("Action_Change_Text_Color");
        } else if (colorType == TYPE_CHANGE_BORDER_COLOR) {
            logEvent("Action_Change_Border_Color");
        } else if (colorType == TYPE_CHANGE_TEXT_BORDER_COLOR) {
            logEvent("Action_Change_Text_Border_Color");
        }

        if (colorPickerDialog == null) {
            colorPickerDialog = new Dialog(this, R.style.WideDialog);
            colorPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            colorPickerDialog.setContentView(R.layout.dialog_color_picker);

            Window window = colorPickerDialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wlp.gravity = Gravity.BOTTOM;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            LinearLayout llTextColorOption = colorPickerDialog.findViewById(R.id.llTextColorOption);
            final RadioButton rbTextColor = colorPickerDialog.findViewById(R.id.rbTextColor);
            final RadioButton rbTextBackground = colorPickerDialog.findViewById(R.id.rbTextBackground);
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.getId() == R.id.rbTextColor && buttonView.isChecked()) {
                        rbTextBackground.setChecked(false);
                    } else if (buttonView.getId() == R.id.rbTextBackground && buttonView.isChecked()) {
                        rbTextColor.setChecked(false);
                    }
                }
            };
            rbTextBackground.setOnCheckedChangeListener(onCheckedChangeListener);
            rbTextColor.setOnCheckedChangeListener(onCheckedChangeListener);


            ColorPickerView colorPickerView = colorPickerDialog.findViewById(R.id.colorPickerView);
            colorPickerView.setOnColorChangedListener(new ColorPickerView.OnColorChangedListener() {

                @Override
                public void onColorChanged(int newColor) {
                    if (colorType == TYPE_CHANGE_TEXT_COLOR) {
                        if (selectedTextView != null) {
                            if (rbTextColor.isChecked()) {
                                selectedTextView.setTextColor(newColor);
                            } else if (rbTextBackground.isChecked()) {
                                if (selectedTextView != null) {
                                    Map<String, String> configMap = getConfigMap(selectedTextView);
                                    if (configMap != null) {
                                        String[] borderInfo = configMap.get(BORDER).split(",");
                                        int padding = Integer.parseInt(borderInfo[0]);
                                        int width = Integer.parseInt(borderInfo[1]);
                                        int radius = Integer.parseInt(borderInfo[2]);
                                        int color = Integer.parseInt(borderInfo[3]);
                                        int solidColor = newColor;
                                        //configMap.put(BORDER, padding + "," + width + "," + radius + "," + color);
                                        configMap.put(SOLID, String.format("%d", solidColor));
                                        Drawable shape = getShape(width, radius, color, solidColor);
                                        selectedTextView.setBackgroundDrawable(shape);
                                        selectedTextView.setPadding(padding, padding, padding, padding);
                                    }
                                }
                            }
                        }
                    } else if (colorType == TYPE_CHANGE_OVERLAY_COLOR) {
                        View overlay = mPhotoEditorView.findViewWithTag("overlay");
                        if (overlay != null) {
                            overlay.setBackgroundColor(newColor);
                        }

                    } else if (colorType == TYPE_CHANGE_BORDER_COLOR) {
                        if (selectedBorder != null) {


                            int alpha = Color.alpha(newColor);
                            int baseColor = newColor | 0xFF000000;
                            selectedBorder.setColorFilter(baseColor);
                            selectedBorder.setAlpha(alpha);

                            Log.d("COLOR BORDER", "get: " + newColor + " ; base: " + baseColor + " ; alpha: " + alpha);
                        }
                    } else if (colorType == TYPE_CHANGE_TEXT_BORDER_COLOR) {
                        /*if (selectedTextView != null) {
                            Map<String, String> configMap = getConfigMap(selectedTextView);
                            String[] borderSetting = configMap.get(BORDER).split(",");
                            int padding = Integer.parseInt(borderSetting[0]);
                            int width = Integer.parseInt(borderSetting[1]);
                            int radius = Integer.parseInt(borderSetting[2]);
                            int color = newColor;
                            selectedTextView.setBackgroundDrawable(getShape(width, radius, color));
                            selectedTextView.setPadding(padding, padding, padding, padding);
                            configMap.put(BORDER, padding + "," + width + "," + radius + "," + color);
                        }*/

                        if (selectedTextView != null) {
                            Map<String, String> configMap = getConfigMap(selectedTextView);
                            if (configMap != null) {
                                String[] borderInfo = configMap.get(BORDER).split(",");
                                int padding = Integer.parseInt(borderInfo[0]);
                                int width = Integer.parseInt(borderInfo[1]);
                                int radius = Integer.parseInt(borderInfo[2]);
                                int color = newColor;
                                int solidColor = Integer.parseInt(configMap.get(SOLID));
                                configMap.put(BORDER, padding + "," + width + "," + radius + "," + color);
                                //configMap.put(SOLID, String.format("%d", solidColor));
                                Drawable shape = getShape(width, radius, color, solidColor);
                                selectedTextView.setBackgroundDrawable(shape);
                                selectedTextView.setPadding(padding, padding, padding, padding);
                            }
                        }
                    }
                }
            });


        }
        if (colorPickerDialog != null && !colorPickerDialog.isShowing()) {

            if (type == TYPE_CHANGE_TEXT_COLOR) {
                colorPickerDialog.findViewById(R.id.llTextColorOption).setVisibility(View.VISIBLE);
            } else {
                colorPickerDialog.findViewById(R.id.llTextColorOption).setVisibility(View.GONE);
            }

            colorPickerDialog.show();
        }
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    @SuppressLint("MissingPermission")
    private void showCamera() {
        /*if (requestPermission(Manifest.permission.CAMERA)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }*/
        askCompactPermissions(new String[]{PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE},
                new PermissionUtils.PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        /*Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                        Intent startCustomCameraIntent = new Intent(EditImageActivity.this, CameraActivity.class);
                        startActivityForResult(startCustomCameraIntent, CAMERA_REQUEST);
                    }

                    @Override
                    public void permissionDenied() {

                    }

                    @Override
                    public void permissionForeverDienid() {

                    }
                });
    }

    private final int KEY_PERMISSION = 200;
    private PermissionUtils.PermissionResult permissionResult;
    private String permissionsAsk[];

    public void askCompactPermissions(String permissions[], PermissionUtils.PermissionResult permissionResult) {
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);

    }

    public void openSettingsApp(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            startActivity(intent);
        }
    }

    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();
        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(EditImageActivity.this, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }
        if (permissionsNotGranted.isEmpty()) {
            if (permissionResult != null)
                permissionResult.permissionGranted();
        } else {
            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions(this, arrayPermissionNotGranted, KEY_PERMISSION);
        }

        String s = "<script>\n" +
                "\t\t\tvar video_contents = {\n" +
                "\t\t\t\t\t\t\t'0': {embed:'<iframe src=\"https://streamable.com/e/0ftaa?autoplay=1&hd=1\" width=\"630\" height=\"390\" frameborder=\"0\" allowfullscreen webkitallowfullscreen mozallowfullscreen scrolling=\"no\"></iframe>', lang:'English', 'type':'Highlights', quality:'HQ', source:'streamable.com'},\n" +
                "\t\t\t\t\n" +
                "\t\t\t}\t\n" +
                "\t\t</script>";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != KEY_PERMISSION) {
            return;
        }
        List<String> permissionDienid = new LinkedList<>();
        boolean granted = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                granted = false;
                permissionDienid.add(permissions[i]);
            }
        }
        if (permissionResult != null) {
            if (granted) {
                permissionResult.permissionGranted();
            } else {
                for (String s : permissionDienid) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, s)) {
                        permissionResult.permissionForeverDienid();
                        return;
                    }
                }
                permissionResult.permissionDenied();
            }
        }

    }


    @SuppressLint("MissingPermission")
    private void saveImage() {
        int saveCount = PreferenceUtils.getNumberSaveQuote(EditImageActivity.this);

        boolean showAdsEdit = Constant.showAdsEdit(saveCount);
        boolean adsLoaded = popup!=null;
        boolean canShowAds = PreferenceUtils.isCanShowAds(EditImageActivity.this);
        LogUtils.d("showAdsEdit = " + showAdsEdit + " ; adsLoaded = " + adsLoaded + " ; canShowAds = " + canShowAds);

        if (showAdsEdit && adsLoaded && canShowAds) {
            popup.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    PreferenceUtils.saveNumberSaveQuote(EditImageActivity.this, PreferenceUtils.getNumberSaveQuote(EditImageActivity.this) + 1);
                    logEvent("Action_Save_Quote");
                    showLoading("Saving...");
//                                    File imageFolder = new File(Environment.getExternalStorageDirectory()
//                                            + File.separator + "QuoterGallery");
//                                    LogUtils.d("ImageFolderExist: " + imageFolder.exists());
//                                    if (!imageFolder.exists()) {
//                                        boolean createResult = imageFolder.mkdirs();
//                                        LogUtils.d("ImageFolderCreate: " + createResult);
//                                    }
//                                    File file = new File(imageFolder, System.currentTimeMillis() + ".png");
                    File file = FileUtils.generateNewImageFile(EditImageActivity.this);
                    try {
                        file.createNewFile();
                        SaveSettings saveSettings = new SaveSettings.Builder()
                                .setClearViewsEnabled(false)
                                .setTransparencyEnabled(false)
                                .build();

                        mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                            @Override
                            public void onSuccess(@NonNull String imagePath) {
                                hideLoading();
                                showSnackbar("Image Saved Successfully");
                                //mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                                saved = true;

                                Intent intent = new Intent(EditImageActivity.this, ShareQuoteActivity.class);
                                intent.putExtra(Constant.KEY_IMAGE_PATH, imagePath);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                hideLoading();
                                showSnackbar("Failed to save Image");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        hideLoading();
                        showSnackbar(e.getMessage());
                    }
                }
            });



            popup.show(EditImageActivity.this);
        }
        else {
            PreferenceUtils.saveNumberSaveQuote(EditImageActivity.this, saveCount + 1);
            logEvent("Action_Save_Quote");
            showLoading("Saving...");
//                            File imageFolder = new File(Environment.getExternalStorageDirectory()
//                                    + File.separator + "QuoterGallery");
//                            LogUtils.d("ImageFolderExist: " + imageFolder.exists());
//                            if (!imageFolder.exists()) {
//                                boolean createResult = imageFolder.mkdirs();
//                                LogUtils.d("ImageFolderCreate: " + createResult);
//                            }
//                            File file = new File(imageFolder, System.currentTimeMillis() + ".png");
            File file = FileUtils.generateNewImageFile(EditImageActivity.this);
            try {
                file.createNewFile();

                SaveSettings saveSettings = new SaveSettings.Builder()
                        .setClearViewsEnabled(false)
                        .setTransparencyEnabled(false)
                        .build();

                mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                    @Override
                    public void onSuccess(@NonNull String imagePath) {
                        hideLoading();
                        showSnackbar("Image Saved Successfully");
                        //mPhotoEditorView.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                        saved = true;

                        Intent intent = new Intent(EditImageActivity.this, ShareQuoteActivity.class);
                        intent.putExtra(Constant.KEY_IMAGE_PATH, imagePath);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        hideLoading();
                        showSnackbar("Failed to save Image");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                hideLoading();
                showSnackbar(e.getMessage());
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    //mPhotoEditor.clearAllViews();
                    //Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Uri photoUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case PICK_REQUEST:
                    try {
                        // mPhotoEditor.clearAllViews();
                        Uri uri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        mPhotoEditorView.getSource().setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case Config.RC_PICK_IMAGES:
                    if (data != null) {
                        ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
                        if (images != null && !images.isEmpty()) {
                            Image image = images.get(0);
                            String path = image.getPath();
                            Glide.with(this).load(path).apply(Constant.glideRequestOption).into(mPhotoEditorView.getSource());
                        }
                    }
                    break;
                case SELECT_IMAGE_REQUEST:
                    String path = data.getStringExtra(Constant.KEY_CROP_PATH);
                    if (path != null && !path.isEmpty()) {
                        Glide.with(this).load(path).apply(Constant.glideRequestOption).into(mPhotoEditorView.getSource());
                    }
                    break;
            }
        }
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
        mTxtCurrentTool.setText(R.string.label_brush);
    }

    @Override
    public void onEmojiClick(String emojiUnicode) {
        mPhotoEditor.addEmoji(emojiUnicode);
        mTxtCurrentTool.setText(R.string.label_emoji);

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.addImage(bitmap);
        mTxtCurrentTool.setText(R.string.label_sticker);
    }

    @Override
    public void onFontSelected(String path) {
        if (selectedTextView != null) {
            Bundle bundle = new Bundle();
            bundle.putString("font", path);
            logEvent("Action_Select_Font", bundle);
            try {
                //selectedTextView.setTypeface(Typeface.createFromAsset(getAssets(), path));

                String fontCode = path.replace("fonts/", "").replace(".TTF", "");
                int id = Constant.getResourceFontId(fontCode);
                //makeToast(fontCode + " = " + id + " - test: " + R.font.f_03);
                selectedTextView.setTypeface(ResourcesCompat.getFont(this, id));

            } catch (RuntimeException e) {

            }
            getConfigMap(selectedTextView).put(FONT, path.replace("fonts/", "").replace(".TTF", ""));
        }
    }


    public boolean isPermissionGranted(Context context, String permission) {
        boolean granted = (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        return granted;
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you want to exit without saving image ?");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveImage();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logEvent("Action_Discard_Quote");
                finish();
            }
        });
        builder.create().show();

    }

    @Override
    public void onFilterSelected(PhotoFilter photoFilter) {
        logEvent("Action_Select_Filter");
        mPhotoEditor.setFilterEffect(photoFilter);
    }

    @Override
    public void onToolSelected(ToolType toolType) {
        switch (toolType) {
            case BRUSH:
                mPhotoEditor.setBrushDrawingMode(true);
                mTxtCurrentTool.setText(R.string.label_brush);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;
            case TEXT:
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
                    @Override
                    public void onDone(String inputText, int colorCode) {
                        Typeface tfDefault = Typeface.create(ResourcesCompat.getFont(EditImageActivity.this, Constant.getResourceFontId("13")), Typeface.NORMAL);
                        TextView textView = mPhotoEditor.addText(tfDefault, inputText, colorCode);
                        configMap.put(textView.getTag().toString(), generateDefaultFontMap());
                        mTxtCurrentTool.setText(R.string.label_text);
                    }
                });
                break;
            case ERASER:
                mPhotoEditor.brushEraser();
                mTxtCurrentTool.setText(R.string.label_eraser);
                break;
            case FILTER:
                mTxtCurrentTool.setText(R.string.label_filter);
                showFilter(true);
                break;
            case EMOJI:
                mEmojiBSFragment.show(getSupportFragmentManager(), mEmojiBSFragment.getTag());
                break;
            case STICKER:
                //mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                mTxtCurrentTool.setText(R.string.label_border);
                showBorder(true);
                if (llBorder.getVisibility() != View.VISIBLE) {
                    llBorder.setVisibility(View.VISIBLE);
                    //rlAds.setVisibility(View.VISIBLE);
                }
                break;
            case CAMERA:
                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);*/
                showCamera();
                break;
            case GALLERY:
                showBackgrounds(true);
                /*Intent intent = new Intent();
                intent.setType("image*//*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);*/
                break;
            case OVERLAY:
                //showFonts(true);
                showColorPicker(TYPE_CHANGE_OVERLAY_COLOR);
                break;
            case SAVE:
                saveImage();
                break;
        }
    }

    boolean mIsBackgroundsVisible = false;
    boolean mIsFontVisible = false;
    boolean mIsBorderVisible = false;

    void showBorder(boolean isVisible) {
        mIsBorderVisible = isVisible;
        mConstraintSet.clone(mRootView);
        if (isVisible) {
            mConstraintSet.clear(mRvBorder.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvBorder.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvBorder.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvBorder.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvBorder.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    void showFonts(boolean isVisible) {
        mIsFontVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFont.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFont.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFont.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFont.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFont.getId(), ConstraintSet.END);
            selectedTextView = null;
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    void showBackgrounds(boolean isVisible) {
        mIsBackgroundsVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvBackgrounds.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvBackgrounds.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvBackgrounds.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvBackgrounds.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvBackgrounds.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    void showFilter(boolean isVisible) {
        mIsFilterVisible = isVisible;
        mConstraintSet.clone(mRootView);

        if (isVisible) {
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.START);
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.END,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
        } else {
            mConstraintSet.connect(mRvFilters.getId(), ConstraintSet.START,
                    ConstraintSet.PARENT_ID, ConstraintSet.END);
            mConstraintSet.clear(mRvFilters.getId(), ConstraintSet.END);
        }

        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(350);
        changeBounds.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        TransitionManager.beginDelayedTransition(mRootView, changeBounds);

        mConstraintSet.applyTo(mRootView);
    }

    @Override
    public void onBackPressed() {
        /*if (llColor.getVisibility() == View.VISIBLE) {
            llColor.setVisibility(View.GONE);
            //rlAds.setVisibility(View.VISIBLE);
        }*/

        if (llTextSetting.getVisibility() == View.VISIBLE) {
            llTextSetting.setVisibility(View.INVISIBLE);
            //rlAds.setVisibility(View.VISIBLE);
        }

        if (llBorder.getVisibility() == View.VISIBLE) {
            llBorder.setVisibility(View.GONE);
            //rlAds.setVisibility(View.VISIBLE);
        }

        if (mIsFilterVisible) {
            showFilter(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (mIsBackgroundsVisible) {
            showBackgrounds(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (mIsFontVisible) {
            showFonts(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (mIsBorderVisible) {
            showBorder(false);
            mTxtCurrentTool.setText(R.string.app_name);
        } else if (/*!mPhotoEditor.isCacheEmpty() && */!saved) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackgroundSelect(Integer source) {
        Glide.with(this).load(source).apply(Constant.glideRequestOption).into(mPhotoEditorView.getSource());
    }

    @Override
    public void addFromGallery() {
        /*Intent intent = new Intent();
        intent.setType("image*//**//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);*/
        selectImageFromGallery();
    }

    @Override
    public void onBorderSelected(int resource) {
        if (!swEnableBorder.isChecked()) {
            Toast.makeText(this, R.string.have_to_enable_border, Toast.LENGTH_LONG).show();
            return;
        }
        Glide.with(this)
                .asBitmap().load(resource)
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  //Toast.makeText(cxt,getResources().getString(R.string.unexpected_error_occurred_try_again),Toast.LENGTH_SHORT).show();
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  View borderView = mPhotoEditorView.findBorderView();
                                  if (borderView == null) {
                                      mPhotoEditor.addImage(bitmap);
                                  } else {
                                      ImageView ivBorder = borderView.findViewById(R.id.imgPhotoEditorImage);
                                      ivBorder.setImageBitmap(bitmap);
                                  }
                                  return false;
                              }
                          }
                ).submit();

    }

    private void selectImageFromGallery() {
        /*ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setMultipleMode(false)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Select")               //  Done button title
                .setLimitMessage("You have reached selection limit")    // Selection limit message
                .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                .setKeepScreenOn(true)              //  Keep screen on when selecting images
                .start();*/
        askCompactPermissions(new String[]{PermissionUtils.Manifest_READ_EXTERNAL_STORAGE, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE},
                new PermissionUtils.PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        Intent intent = new Intent(EditImageActivity.this, CropImageActivity.class);
                        startActivityForResult(intent, SELECT_IMAGE_REQUEST);
                    }

                    @Override
                    public void permissionDenied() {

                    }

                    @Override
                    public void permissionForeverDienid() {

                    }
                });

    }

    private Map<String, String> generateDefaultFontMap() {
        Map<String, String> defaultConfigMap = new HashMap<>();
        defaultConfigMap.put(B, "false");
        defaultConfigMap.put(I, "false");
        defaultConfigMap.put(U, "false");
        defaultConfigMap.put(LEFT, "false");
        defaultConfigMap.put(RIGHT, "false");
        defaultConfigMap.put(CENTER, "false");
        defaultConfigMap.put(FONT, DEFAULT_FONT_PATH);
        defaultConfigMap.put(BORDER, PhotoEditor.DEFAULT_TEXT_PADDING + ",0,0," + Color.WHITE);
        defaultConfigMap.put(SPACING, "0,0");
        defaultConfigMap.put(SOLID, String.format("%d", Color.TRANSPARENT));
        return defaultConfigMap;
    }

    private Map<String, String> getConfigMap(TextView textView) {
        String tag = textView.getTag().toString();
        Log.d("TAGTEXTVIEW", textView.getTag().toString());
        return configMap.get(tag);
    }

    private void showFontSetting(TextView textView) {
        Map<String, String> configMap = getConfigMap(textView);
        if (Boolean.parseBoolean(configMap.get(B))) {
            mrlParentBold.setBackgroundResource(R.drawable.bg_align);
        } else {
            mrlParentBold.setBackgroundResource(0);
        }
        if (Boolean.parseBoolean(configMap.get(I))) {
            mrlParentItalic.setBackgroundResource(R.drawable.bg_align);
        } else {
            mrlParentItalic.setBackgroundResource(0);
        }
        if (Boolean.parseBoolean(configMap.get(U))) {
            mrlParentUnderline.setBackgroundResource(R.drawable.bg_align);
        } else {
            mrlParentUnderline.setBackgroundResource(0);
        }
        if (Boolean.parseBoolean(configMap.get(LEFT))) {
            mrlParentLeft.setBackgroundResource(R.drawable.bg_align);
        } else {
            mrlParentLeft.setBackgroundResource(0);
        }
        if (Boolean.parseBoolean(configMap.get(RIGHT))) {
            mrlParentRight.setBackgroundResource(R.drawable.bg_align);
        } else {
            mrlParentRight.setBackgroundResource(0);
        }
        if (Boolean.parseBoolean(configMap.get(CENTER))) {
            mrlParentCenter.setBackgroundResource(R.drawable.bg_align);
        } else {
            mrlParentCenter.setBackgroundResource(0);
        }
    }

    private Drawable getShape(int strokeWidth, int corner, int strokeColor, int solidColor) {
        return new DrawableBuilder().rectangle()
                .strokeColor(strokeColor)
                .strokeWidth(strokeWidth)
                .cornerRadius(corner)
                .solidColor(solidColor)
                .build();
    }

    private static final int TYPE_CHANGE_TEXT_SIZE = 1;
    private static final int TYPE_CHANGE_TEXT_ROTATION = 2;

    private void showDialogTextSizeRotationSetting(final int type) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_text_size_rotation, null);
        final SeekBar sbTextSize = view.findViewById(R.id.sbTextSize);
        TextView tvDescription = view.findViewById(R.id.tvDescription);
        if (type == TYPE_CHANGE_TEXT_SIZE) {
            tvDescription.setText("Text size");
        } else {
            tvDescription.setText("Rotation");
        }
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (selectedTextView != null) {
                    if (type == TYPE_CHANGE_TEXT_SIZE) {
                        selectedTextView.setTextSize((float) progress);
                    } else if (type == TYPE_CHANGE_TEXT_ROTATION) {
                        if (selectedTextView.getParent() != null && selectedTextView.getParent().getParent() != null) {
                            ((View) selectedTextView.getParent().getParent()).setRotation((float) progress);
                        }

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        sbTextSize.setOnSeekBarChangeListener(onSeekBarChangeListener);

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(type == TYPE_CHANGE_TEXT_SIZE ? "Text size setting" : "Text rotation setting")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        wmlp.y = Constant.screenWidth;
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
    }

    private void showDialogBorderSetting() {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_border_setting, null);
        final SeekBar sbBorderWidth = ((SeekBar) view.findViewById(R.id.sbBorderWidth));
        final SeekBar sbBorderCornerRadius = ((SeekBar) view.findViewById(R.id.sbBorderRadius));
        final SeekBar sbBorderPadding = ((SeekBar) view.findViewById(R.id.sbBorderPadding));

        Map<String, String> configMap = getConfigMap(selectedTextView);
        if (configMap != null) {
            String[] borderSetting = configMap.get(BORDER).split(",");
            int padding = Integer.parseInt(borderSetting[0]);
            int width = Integer.parseInt(borderSetting[1]);
            int radius = Integer.parseInt(borderSetting[2]);

            sbBorderPadding.setProgress(padding);
            sbBorderWidth.setProgress(width);
            sbBorderCornerRadius.setProgress(radius);
        }

        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getId() == R.id.sbBorderWidth || seekBar.getId() == R.id.sbBorderRadius || seekBar.getId() == R.id.sbBorderPadding) {
                    if (selectedTextView != null) {
                        Map<String, String> configMap = getConfigMap(selectedTextView);
                        int padding = sbBorderPadding.getProgress();
                        int width = sbBorderWidth.getProgress();
                        int radius = sbBorderCornerRadius.getProgress();
                        int color = Integer.parseInt(configMap.get(BORDER).split(",")[3]);
                        int solidColor = Integer.parseInt(configMap.get(SOLID));
                        configMap.put(BORDER, padding + "," + width + "," + radius + "," + color);
                        Drawable shape = getShape(width, radius, color, solidColor);
                        selectedTextView.setBackgroundDrawable(shape);
                        selectedTextView.setPadding(padding, padding, padding, padding);
                    }
                    if (seekBar.getId() == R.id.sbBorderWidth) {
                        LogUtils.d("sbBorderWidth progress = " + progress);
                    } else if (seekBar.getId() == R.id.sbBorderRadius) {
                        LogUtils.d("sbBorderRadius progress = " + progress);
                    } else if (seekBar.getId() == R.id.sbBorderPadding) {
                        LogUtils.d("sbBorderPadding progress = " + progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        sbBorderWidth.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBorderPadding.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbBorderCornerRadius.setOnSeekBarChangeListener(onSeekBarChangeListener);
        view.findViewById(R.id.bt_border_color).setOnClickListener(this);

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Border settings")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        wmlp.y = Constant.screenWidth;
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
    }

    private void showDialogSpaceSetting() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_space_setting, null);
        final SeekBar sbHorizontalSpacing = ((SeekBar) view.findViewById(R.id.sbHorizontalSpacing));
        final SeekBar sbVerticalSpacing = ((SeekBar) view.findViewById(R.id.sbVerticalSpacing));
        SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //horizontal spacing
                if (seekBar.getId() == R.id.sbHorizontalSpacing) {
                    if (selectedTextView != null) {
                        Map<String, String> configMap = getConfigMap(selectedTextView);
                        selectedTextView.setKerningFactor(progress);
                        int verticalSpacing = Integer.parseInt(configMap.get(SPACING).split(",")[1]);
                        configMap.put(SPACING, progress + "," + verticalSpacing);
                    }
                }
                //vertical spacing
                else if (seekBar.getId() == R.id.sbVerticalSpacing) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        if (selectedTextView != null) {
                            Map<String, String> configMap = getConfigMap(selectedTextView);
                            selectedTextView.setLineSpacing(0, /*selectedTextView.getLineSpacingMultiplier() + */progress / 10f);
                            int horizontalSpacing = Integer.parseInt(configMap.get(SPACING).split(",")[0]);
                            configMap.put(SPACING, horizontalSpacing + "," + progress);
                        }
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        sbHorizontalSpacing.setOnSeekBarChangeListener(onSeekBarChangeListener);
        sbVerticalSpacing.setOnSeekBarChangeListener(onSeekBarChangeListener);

        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Spacing settings")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        wmlp.y = Constant.screenWidth;
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
    }

    private static final String B = "B";
    private static final String I = "I";
    private static final String U = "U";
    private static final String LEFT = "LEFT";
    private static final String RIGHT = "RIGHT";
    private static final String CENTER = "CENTER";
    private static final String FONT = "FONT";
    private static final String BORDER = "BORDER";
    private static final String SPACING = "SPACING";
    private static final String SOLID = "SOLID";
}
