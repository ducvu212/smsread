package com.example.root.smsread.api;

import android.animation.ValueAnimator;
import android.os.Environment;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import cz.msebera.android.httpclient.HttpHost;

public class AppUtil implements AppConstants {
    public static final String separator = System.getProperty("line.separator");
    private static Exception e2;
    private static int column_index;
    private static Exception e222;
    private static int columnIndex;

    public enum AnimatorType {
        VERTICAL,
        HORIZONTAL
    }

    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String url) {
            super(url);
        }

        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }

    public static void log(String log) {
        Log.e("abc", "" + log);
    }

    public static boolean isUrl(String filePath) {
        return filePath != null && filePath.startsWith(HttpHost.DEFAULT_SCHEME_NAME);
    }

    public static File getAppSaveFolder(String folderName) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/" + "com.tg.oio" + "/" + folderName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private static ValueAnimator slideAnimator(int start, int end, final View view, final AnimatorType type) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (type == AnimatorType.VERTICAL) {
                    layoutParams.height = value;
                } else {
                    layoutParams.width = value;
                }
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }



}
