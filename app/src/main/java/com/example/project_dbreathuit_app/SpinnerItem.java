package com.example.project_dbreathuit_app;

public class SpinnerItem {
    private int iconResId;
    private String text;

    public SpinnerItem(int iconResId, String text) {
        this.iconResId = iconResId;
        this.text = text;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getText() {
        return text;
    }
}
