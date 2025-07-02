package com.mmsstudio.htmldesignsample;


import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
    @Override public void transformPage(@NonNull View page, float pos) {
        page.setAlpha(0f);
        page.setVisibility(View.VISIBLE);
        if (pos >= -1 && pos <= 1) {
            float scale = 1 - Math.abs(pos);
            page.setScaleX(scale);
            page.setScaleY(scale);
            page.setAlpha(scale);
        } else {
            page.setVisibility(View.INVISIBLE);
        }
    }
}
