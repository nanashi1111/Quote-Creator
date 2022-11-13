package com.burhanrashid52.imageeditor.mygallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.burhanrashid52.imageeditor.Constant;
import com.burhanrashid52.imageeditor.R;
import com.burhanrashid52.imageeditor.ShareQuoteActivity;
import com.burhanrashid52.imageeditor.utils.FileUtils;
import com.burhanrashid52.imageeditor.utils.LogUtils;
import com.burhanrashid52.imageeditor.utils.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by admin on 9/21/18.
 */

public class MyGalleryActivity extends AppCompatActivity {

    private final int KEY_PERMISSION = 200;
    private PermissionUtils.PermissionResult permissionResult;
    private String permissionsAsk[];

    RecyclerView rvMyGallery;
    ProgressBar pbLoading;
    List<String> listImage = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gallery);
        rvMyGallery = findViewById(R.id.rv_my_gallery);
        rvMyGallery.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pbLoading = findViewById(R.id.pb_loading);
        getListGallery();
    }

    void getListGallery() {
        File imageFolder = FileUtils.getImageFolder(MyGalleryActivity.this);
        boolean folderExist = imageFolder.exists();
        LogUtils.d("MyGalleryFolderExist: " + folderExist);
        if (!imageFolder.exists()) {
            showMessageNoData();
        } else {
            File[] photos = imageFolder.listFiles();
            LogUtils.d("MyGalleryFolderFileCount: " + photos.length);
            if (photos.length > 0) {
                for (File f : photos) {
                    LogUtils.d("MyGalleryFolderFile: " + f.getAbsolutePath());
                    String extension = getFileExtension(f);
                    LogUtils.d("Extension:" + extension);
                    if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
                            || extension.equalsIgnoreCase(".jpeg")) {
                        listImage.add(f.getAbsolutePath());
                    }

                }
                LogUtils.d("MyGallerySize: " + listImage.size());
                if (listImage.size() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final MyGalleryAdapter adapter = new MyGalleryAdapter(listImage, getScreenWidth());
                            adapter.setOnImageSelect(new MyGalleryAdapter.OnImageSelect() {
                                @Override
                                public void select(String imagePath) {
                                    Intent intent = new Intent(MyGalleryActivity.this, ShareQuoteActivity.class);
                                    intent.putExtra(Constant.KEY_IMAGE_PATH, imagePath);
                                    startActivity(intent);
                                }

                                @Override
                                public void share(String imagePath) {
                                    Intent intent = new Intent(MyGalleryActivity.this, ShareQuoteActivity.class);
                                    intent.putExtra(Constant.KEY_IMAGE_PATH, imagePath);
                                    intent.putExtra(Constant.KEY_SHARE_NOW, true);
                                    startActivity(intent);
                                }

                                @Override
                                public void delete(final String imagePath) {
                                    new AlertDialog.Builder(MyGalleryActivity.this)
                                            .setTitle("Confirm")
                                            .setMessage("Do you want to delete this image?")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    File file = new File(imagePath);
                                                    if (file.exists()) {
                                                        file.delete();
                                                        int index = adapter.getListPath().indexOf(imagePath);
                                                        adapter.getListPath().remove(index);
                                                        adapter.notifyItemRemoved(index);
                                                    }
                                                }
                                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            }).show();
                                }
                            });
                            rvMyGallery.setAdapter(adapter);
                            pbLoading.setVisibility(View.GONE);
                        }
                    });
                } else {
                    showMessageNoData();
                }
            } else {
                showMessageNoData();
            }
        }
    }

    private void showMessageNoData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyGalleryActivity.this, "You don't have any photo yet", Toast.LENGTH_LONG).show();
                pbLoading.setVisibility(View.GONE);
            }
        });
    }

    public boolean isPermissionGranted(Context context, String permission) {
        boolean granted = (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
        return granted;
    }

    public boolean isPermissionsGranted(Context context, String permissions[]) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        boolean granted = true;
        for (String permission : permissions) {
            if (!(ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED))
                granted = false;
        }
        return granted;
    }

    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();
        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(this, permissionAsk[i])) {
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


    public void askCompactPermission(String permission, PermissionUtils.PermissionResult permissionResult) {
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }

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

    private String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            extension = "";
        }

        return extension;

    }

    protected int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
}
