
package com.likebamboo.widget.roundbt;

public class BasicItem implements IListItem {

    private boolean mClickable = true;

    private int mDrawable = -1;

    private String mTitle = "";

    private String mValue;

    public BasicItem(String _title) {
        this.mTitle = _title;
    }

    public BasicItem(String _title, String _value) {
        this.mTitle = _title;
        this.mValue = _value;
    }

    public BasicItem(int _drawable, String _title) {
        this.mDrawable = _drawable;
        this.mTitle = _title;
    }

    public BasicItem(int _drawable, String _title, String _value) {
        this.mDrawable = _drawable;
        this.mTitle = _title;
        this.mValue = _value;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public void setDrawable(int drawable) {
        this.mDrawable = drawable;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getVaule() {
        return mValue;
    }

    public void setValue(String summary) {
        this.mValue = summary;
    }

    @Override
    public boolean isClickable() {
        return mClickable;
    }

    @Override
    public void setClickable(boolean clickable) {
        mClickable = clickable;
    }

    public String getCount() {
        return mValue;
    }

    public void setCount(String mCount) {
        this.mValue = mCount;
    }

}
