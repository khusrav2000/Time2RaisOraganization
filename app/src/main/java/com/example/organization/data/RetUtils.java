package com.example.organization.data;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RetUtils {

    public static MultipartBody.Part prepareFilePart(Context con , String partName, Uri fileUri) {
        String filePath = com.example.organization.data.DocumentHelper.getPath(con, fileUri);

        System.out.println(" filePath "+ filePath);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return null;
        File file = new File(filePath);
        ContentResolver cr = con.getContentResolver();
        String type = getMimeType(filePath);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(type),
                        file
                );
        System.out.println("type = "+type+" file "+file.getName() );
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
