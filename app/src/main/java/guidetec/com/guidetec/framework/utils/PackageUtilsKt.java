package guidetec.com.guidetec.framework.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import guidetec.com.guidetec.R;
import com.tapadoo.alerter.Alerter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public final class PackageUtilsKt {
    public static final String bitmapToBase64(Bitmap bitmap) {
        String encodeToString = Base64.encodeToString(getBytesFromBitmap(bitmap), 2);
        return encodeToString;
    }
    private static final byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }
    public static final Bitmap bitmapOptimized(Bitmap bitmap) {
        return scaleDown(bitmap, 800.0f, true);
    }
    private static final Bitmap scaleDown(Bitmap bitmap, float f, boolean z) {
        float min = Math.min(f / ((float) bitmap.getWidth()), f / ((float) bitmap.getHeight()));
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(((float) bitmap.getWidth()) * min), Math.round(min * ((float) bitmap.getHeight())), z);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStream outputStream = byteArrayOutputStream;
        String str = "decoded";
        String str2 = " Count ";
        if (createScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)) {
            Bitmap decodeStream = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            StringBuilder sb = new StringBuilder();
            sb.append("Decoded JPG Row ");
            sb.append(decodeStream.getRowBytes());
            sb.append(str2);
            sb.append(decodeStream.getByteCount());
            //ExtensionsKt.log(sb.toString());
            return decodeStream;
        } else if (createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream)) {
            Bitmap decodeStream2 = BitmapFactory.decodeStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Decoded PNG Row ");
            sb2.append(decodeStream2.getRowBytes());
            sb2.append(str2);
            sb2.append(decodeStream2.getByteCount());
            //ExtensionsKt.log(sb2.toString());
            return decodeStream2;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("ScaledBitmap Row ");
            sb3.append(createScaledBitmap.getRowBytes());
            sb3.append(str2);
            sb3.append(createScaledBitmap.getByteCount());
            return createScaledBitmap;
        }
    }
    public static final Bitmap getCroppedBitmap( Bitmap bitmap, int i) {
        if (!(bitmap.getWidth() == i && bitmap.getHeight() == i)) {
            float min = ((float) Math.min(bitmap.getWidth(), bitmap.getHeight())) / ((float) i);
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) (((float) bitmap.getWidth()) / min), (int) (((float) bitmap.getHeight()) / min), false);
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, i, i);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        int i2 = i / 2;
        float f = (float) i2;
        float f2 = 0.7f + f;
        canvas.drawCircle(f2, f2, f + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, new Rect((bitmap.getWidth() / 2) - i2, (bitmap.getHeight() / 2) - i2, (bitmap.getWidth() / 2) + i2, (bitmap.getHeight() / 2) + i2), rect, paint);
        return createBitmap;
    }
    public static final float convertDpToPixel(float f, Context context) {
        Resources resources = context.getResources();
        return f * (((float) resources.getDisplayMetrics().densityDpi) / ((float) DimensionsKt.MDPI));
    }
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void createLoadingAlerter(Activity activity,String title,String text){
        Alerter.create(activity)
            .setTitle(title)
                .setText(text)
                .enableProgress(true)
                .setEnterAnimation(R.anim.abc_slide_in_top)
                .setExitAnimation(R.anim.alerter_exit)
                .setProgressColorRes(R.color.darkGreen)
                .setBackgroundColorRes(R.color.colorBlue)
                .show();
    }
    public static void createOkAlerter(Activity activity,String title,String text){
        Alerter.create(activity)
                .setTitle(title)
                .setText(text)
                .setIcon(R.drawable.ic_complete)
                .setEnterAnimation(R.anim.abc_slide_in_top)
                .setExitAnimation(R.anim.alerter_exit)
                .setIconColorFilter(0) // Optional - Removes white tint
                .setBackgroundColorRes(R.color.colorBlue)
                .show();
    }
    public static void removeAlerter(Activity activity){
        Alerter.hide();
    }
}
