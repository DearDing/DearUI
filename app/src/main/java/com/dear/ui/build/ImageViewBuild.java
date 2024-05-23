package com.dear.ui.build;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 代码创建 ImageView
 */
public class ImageViewBuild {
    private ImageView imageView;

    public ImageViewBuild(Context context) {
        this.imageView = new ImageView(context);
    }

    public ImageViewBuild setId(int id) {
        imageView.setId(id);
        return this;
    }

    public ImageViewBuild setFrameLayoutParams(int width, int height, int gravity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp2px(width), dp2px(height));
        params.gravity = gravity;
        imageView.setLayoutParams(params);
        return this;
    }

    public ImageViewBuild setLinearLayoutParams(int width, int height) {
        imageView.setLayoutParams(new LinearLayout.LayoutParams(dp2px(width), dp2px(height)));
        return this;
    }

    public ImageViewBuild setLinearLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(width), dp2px(height));
        params.setMargins(dp2px(marginLeft), dp2px(marginTop), dp2px(marginRight), dp2px(marginBottom));
        imageView.setLayoutParams(params);
        return this;
    }


    public ImageViewBuild setConstraintLayoutParams(int width, int height) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height));
        imageView.setLayoutParams(params);
        return this;
    }

    public ImageViewBuild setConstraintLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height));
        params.setMargins(dp2px(marginLeft), dp2px(marginTop), dp2px(marginRight), dp2px(marginBottom));
        imageView.setLayoutParams(params);
        return this;
    }

    public ImageViewBuild setSrc(int resourceId) {
        imageView.setImageResource(resourceId);
        return this;
    }

    public ImageViewBuild setScaleType(ImageView.ScaleType scaleType) {
        imageView.setScaleType(scaleType);
        return this;
    }


    public ImageViewBuild setVisibility(int visible){
        imageView.setVisibility(visible);
        return this;
    }

    public ImageView build() {
        return imageView;
    }

    public int dp2px(int dp) {
        if (imageView == null) {
            return 0;
        }
        if (dp > 0) {
            DisplayMetrics metrics = imageView.getResources().getDisplayMetrics();
            float px = dp * (metrics.densityDpi / 160f);
            return (int) px;
        } else {
            return dp;
        }
    }
}
