
package com.likebamboo.widget.roundbt;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likebamboo.shoot.R;

public class RoundButton extends LinearLayout {

    private LayoutInflater mInflater;

    private LinearLayout mButtonContainer;

    private ClickListener mClickListener;

    private CharSequence mTitle;

    private CharSequence mValue;

    private int mImage;

    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setClickable(true);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mButtonContainer = (LinearLayout)mInflater.inflate(R.layout.round_bt_item_single, null);
        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundButton, 0, 0);
        mTitle = a.getString(R.styleable.RoundButton_title);
        mValue = a.getString(R.styleable.RoundButton_value);
        mImage = a.getResourceId(R.styleable.RoundButton_icon, -1);

        if (mTitle != null) {
            ((TextView)mButtonContainer.findViewById(R.id.round_bt_title)).setText(mTitle);
        } else {
            ((TextView)mButtonContainer.findViewById(R.id.round_bt_title)).setText("subtitle");
        }

        if (mValue != null) {
            ((TextView)mButtonContainer.findViewById(R.id.round_bt_value)).setText(mValue);
        } else {
            ((TextView)mButtonContainer.findViewById(R.id.round_bt_value)).setVisibility(View.GONE);
        }
        if (mImage > -1) {
            ((ImageView)mButtonContainer.findViewById(R.id.round_bt_icon)).setImageResource(mImage);
        }

        mButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mClickListener != null)
                    mClickListener.onClick(RoundButton.this);
            }

        });

        addView(mButtonContainer, params);

        a.recycle();
    }

    public interface ClickListener {
        void onClick(View view);
    }

    /**
     * @param listener
     */
    public void addClickListener(ClickListener listener) {
        this.mClickListener = listener;
    }

    /**
	 * 
	 */
    public void removeClickListener() {
        this.mClickListener = null;
    }

}
