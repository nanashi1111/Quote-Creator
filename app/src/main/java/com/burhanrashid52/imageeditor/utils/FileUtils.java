package com.burhanrashid52.imageeditor.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class FileUtils {

    public static File getImageFolder(Context context) {
        File imageFolder = new File(context.getFilesDir()
                + File.separator + "QuoterGallery");
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
        }
        return imageFolder;
    }


    public static File generateNewImageFile(Context context) {
        File file = new File(getImageFolder(context), System.currentTimeMillis() + ".png");
        return file;
    }

}
