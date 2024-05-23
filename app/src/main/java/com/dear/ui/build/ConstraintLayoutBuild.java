package com.dear.ui.build;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ConstraintLayoutBuild {

    private ConstraintLayout view;

    public ConstraintLayoutBuild(Context context) {
        view = new ConstraintLayout(context);
    }

    public ConstraintLayoutBuild setId(int id) {
        view.setId(id);
        return this;
    }

    public ConstraintLayoutBuild setLayoutParams(int width, int height) {
        view.setLayoutParams(new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height)));
        return this;
    }

    public ConstraintLayoutBuild setLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height));
        params.setMargins(dp2px(marginLeft), dp2px(marginTop), dp2px(marginRight), dp2px(marginBottom));
        view.setLayoutParams(params);
        return this;
    }

    public ConstraintLayoutBuild setBackground(Drawable drawable) {
        view.setBackground(drawable);
        return this;
    }

    public ConstraintLayoutBuild setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        view.setPadding(dp2px(paddingLeft), dp2px(paddingTop), dp2px(paddingRight), dp2px(paddingBottom));
        return this;
    }


    public ConstraintLayout build() {
        return view;
    }

    public int dp2px(int dp) {
        if (view == null) {
            return 0;
        }
        if (dp > 0) {
            DisplayMetrics metrics = view.getResources().getDisplayMetrics();
            float px = dp * (metrics.densityDpi / 160f);
            return (int) px;
        } else {
            return dp;
        }
    }

}
