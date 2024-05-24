package com.dear.ui.build;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ViewBuild {
    private View view;

    public ViewBuild(Context context) {
        view = new View(context);
    }

    public ViewBuild setId(int id) {
        view.setId(id);
        return this;
    }

    public ViewBuild setLayoutParams(int width, int height, float weight) {
        int cwidth = dp2px(width);
        int cheight = dp2px(height);
        view.setLayoutParams(new LinearLayout.LayoutParams(cwidth, cheight, weight));
        return this;
    }

    public ViewBuild setLayoutParams(int width, int height, boolean isdp) {
        int cwidth = isdp ? dp2px(width) : width;
        int cheight = isdp ? dp2px(height) : height;
        view.setLayoutParams(new LinearLayout.LayoutParams(cwidth, cheight));
        return this;
    }

    public ViewBuild setLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom, boolean isdp) {
        int cwidth = isdp ? dp2px(width) : width;
        int cheight = isdp ? dp2px(height) : height;
        int cmarginLeft = isdp ? dp2px(marginLeft) : marginLeft;
        int cmarginTop = isdp ? dp2px(marginTop) : marginTop;
        int cmarginRight = isdp ? dp2px(marginRight) : marginRight;
        int cmarginBottom = isdp ? dp2px(marginBottom) : marginBottom;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(cwidth, cheight);
        params.setMargins(cmarginLeft, cmarginTop, cmarginRight, cmarginBottom);
        view.setLayoutParams(params);
        return this;
    }

    public ViewBuild setConstraintLayoutParams(int width, int height) {
        view.setLayoutParams(new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height)));
        return this;
    }

    public ViewBuild setConstraintLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom, boolean isdp) {
        int cwidth = isdp ? dp2px(width) : width;
        int cheight = isdp ? dp2px(height) : height;
        int cmarginLeft = isdp ? dp2px(marginLeft) : marginLeft;
        int cmarginTop = isdp ? dp2px(marginTop) : marginTop;
        int cmarginRight = isdp ? dp2px(marginRight) : marginRight;
        int cmarginBottom = isdp ? dp2px(marginBottom) : marginBottom;

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(cwidth, cheight);
        params.setMargins(cmarginLeft, cmarginTop, cmarginRight, cmarginBottom);
        view.setLayoutParams(params);
        return this;
    }

    public ViewBuild setBackground(Drawable drawable) {
        view.setBackground(drawable);
        return this;
    }

    public View build() {
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
