package com.example.milos.creitive.utils;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by milos on 06/02/2018.
 */

public class HtmlUtils {

    @SuppressWarnings("deprecation")
    public static String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return String.valueOf(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY));
        } else {
            return String.valueOf(Html.fromHtml(html));
        }
    }


}
