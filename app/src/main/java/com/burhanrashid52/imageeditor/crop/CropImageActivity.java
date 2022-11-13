package com.burhanrashid52.imageeditor.crop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.base.BaseActivity;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by admin on 9/24/18.
 */

public class CropImageActivity extends BaseActivity implements View.OnClickListener {

    CropImageView cropImageView;
    Thread threadProcessImage;

    boolean mustStop = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        cropImageView = findViewById(R.id.iv_crop);
        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.bt_done).setOnClickListener(this);
        selectImageFromGallery();
    }

    private void selectImageFromGallery() {
        ImagePicker.with(this)                         //  Initialize ImagePicker with activity or fragment context
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
                .setShowCamera(false)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == Config.RC_PICK_IMAGES && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            if (images != null && !images.isEmpty()) {
                Image image = images.get(0);
                String path = image.getPath();
                //Glide.with(this).load(path).apply(Constant.glideRequestOption).into(mPhotoEditorView.getSource());
                Glide.with(this).asBitmap().load(new File(path)).into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        cropImageView.setImageBitmap(resource);
                        cropImageView.setCustomRatio(1, 1);
                    }
                });
            }
        } else {
            onBackPressed();
        }
    }

    private void cropImage() {
        final long time = System.currentTimeMillis();
        cropImageView.startCrop(null, new CropCallback() {
            @Override
            public void onSuccess(final Bitmap cropped) {
                LogUtils.d("CropTime = " + (System.currentTimeMillis() - time));
                final long timeStartProcess = System.currentTimeMillis();

                if (threadProcessImage != null) {
                    threadProcessImage.interrupt();
                    threadProcessImage = null;
                }
                threadProcessImage = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mustStop = false;
                        File imageFolder = new File(Environment.getExternalStorageDirectory()
                                + File.separator + "QuoterGallery" + File.separator + "CropResult");
                        if (!imageFolder.exists()) {
                            imageFolder.mkdirs();
                        }
                        try {
                            String fileName = imageFolder.getAbsoluteFile() + File.separator + System.currentTimeMillis() + ".PNG";
                            FileOutputStream fos = new FileOutputStream(fileName);
                            cropped.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            Intent intent = new Intent();
                            intent.putExtra(Constant.KEY_CROP_PATH, fileName);
                            setResult(Activity.RESULT_OK, intent);
                            if (!mustStop) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (dialogLoading != null && dialogLoading.isShowing()) {
                                            dialogLoading.dismiss();
                                        }
                                        onBackPressed();
                                    }
                                });
                            }

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            LogUtils.d("ProcessTime = " + (System.currentTimeMillis() - timeStartProcess));
                        }
                    }
                });
                threadProcessImage.start();
                showDialogLoading();
            }

            @Override
            public void onError(Throwable e) {

            }
        }, new SaveCallback() {
            @Override
            public void onSuccess(Uri uri) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_back) {
            onBackPressed();
        } else if (v.getId() == R.id.bt_done) {
            cropImage();
        }
    }

    AlertDialog dialogLoading;

    void showDialogLoading() {
        if (dialogLoading != null) {
            if (dialogLoading.isShowing()) {
                dialogLoading.dismiss();
            }
            dialogLoading = null;
        }
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
        dialogLoading = new AlertDialog.Builder(this).setTitle("Processing")
                .setView(view)
                .setPositiveButton("Stop", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (threadProcessImage != null) {
                            mustStop = true;
                            threadProcessImage.interrupt();
                            threadProcessImage = null;
                        }
                    }
                }).setCancelable(false)
                .show();
    }
}
