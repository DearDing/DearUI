package com.dear.ui.build;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 代码创建 TextView
 */
public class TextViewBuild {
    private TextView textView;

    public TextViewBuild(Context context) {
        textView = new TextView(context);
    }

    public TextViewBuild setId(int id) {
        textView.setId(id);
        return this;
    }

    public TextViewBuild setTextSize(int sp) {
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
        return this;
    }

    public TextViewBuild setLinearLayoutParams(int width, int height, int gravity) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dp2px(width), dp2px(height));
        layoutParams.gravity = gravity;
        textView.setLayoutParams(layoutParams);
        return this;
    }

    public TextViewBuild setLinearLayoutParams(int width, int height) {
        textView.setLayoutParams(new LinearLayout.LayoutParams(dp2px(width), dp2px(height)));
        return this;
    }

    public TextViewBuild setConstraintLayoutParams(int width, int height) {
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height)));
        return this;
    }

    public TextViewBuild setLayoutParams(ViewGroup.LayoutParams params) {
        textView.setLayoutParams(params);
        return this;
    }


    public TextViewBuild setLinearLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom, int gravity) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(width), dp2px(height));
        params.setMargins(dp2px(marginLeft), dp2px(marginTop), dp2px(marginRight), dp2px(marginBottom));
        params.gravity = gravity;
        textView.setLayoutParams(params);
        return this;
    }



    public TextViewBuild setLinearLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp2px(width), dp2px(height));
        params.setMargins(dp2px(marginLeft), dp2px(marginTop), dp2px(marginRight), dp2px(marginBottom));
        textView.setLayoutParams(params);
        return this;
    }

    public TextViewBuild setConstraintLayoutParams(int width, int height, int marginLeft, int marginTop, int marginRight, int marginBottom) {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(dp2px(width), dp2px(height));
        params.setMargins(dp2px(marginLeft), dp2px(marginTop), dp2px(marginRight), dp2px(marginBottom));
        textView.setLayoutParams(params);
        return this;
    }

    public TextViewBuild setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        textView.setPadding(dp2px(paddingLeft), dp2px(paddingTop), dp2px(paddingRight), dp2px(paddingBottom));
        return this;
    }

    public TextViewBuild setTextColor(@ColorInt int color) {
        textView.setTextColor(color);
        return this;
    }

    public TextViewBuild setEllipsize(TextUtils.TruncateAt where) {
        textView.setEllipsize(where);
        return this;
    }

    public TextViewBuild setLines(int lines) {
        textView.setLines(lines);
        return this;
    }

    public TextViewBuild setMaxWidth(int maxWidth) {
        textView.setMaxWidth(dp2px(maxWidth));
        return this;
    }

    public TextViewBuild setText(String text) {
        textView.setText(text);
        return this;
    }

    public TextViewBuild setMaxLines(int lines) {
        textView.setMaxLines(lines);
        return this;
    }

    public TextViewBuild setGravity(int gravity) {
        textView.setGravity(gravity);
        return this;
    }

    public TextViewBuild setBackground(Drawable drawable) {
        textView.setBackground(drawable);
        return this;
    }

    public TextViewBuild setVisibility(int visible){
        textView.setVisibility(visible);
        return this;
    }

    public TextViewBuild setTextStyle(){
        textView.getPaint().setFakeBoldText(true);
        return this;
    }

    public TextView build() {
        return textView;
    }

    public int dp2px(int dp) {
        if (textView == null) {
            return 0;
        }
        if (dp > 0) {
            DisplayMetrics metrics = textView.getResources().getDisplayMetrics();
            float px = dp * (metrics.densityDpi / 160f);
            return (int) px;
        } else {
            return dp;
        }
    }
}
