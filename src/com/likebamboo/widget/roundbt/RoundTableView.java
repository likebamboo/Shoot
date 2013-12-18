
package com.likebamboo.widget.roundbt;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likebamboo.shoot.R;

import java.util.ArrayList;
import java.util.List;

public class RoundTableView extends LinearLayout {

    private int mIndexController = 0;

    private LayoutInflater mInflater;

    private LinearLayout mMainContainer;

    private LinearLayout mListContainer;

    private List<IListItem> mItemList;

    private ClickListener mClickListener;

    public RoundTableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItemList = new ArrayList<IListItem>();
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMainContainer = (LinearLayout)mInflater.inflate(R.layout.round_bt_container, null);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mMainContainer, params);
        mListContainer = (LinearLayout)mMainContainer.findViewById(R.id.buttonsContainer);
    }

    /**
     * @param title
     * @param summary
     */
    public void addBasicItem(String title) {
        mItemList.add(new BasicItem(title));
    }

    /**
     * @param title
     * @param summary
     */
    public void addBasicItem(String title, String summary) {
        mItemList.add(new BasicItem(title, summary));
    }

    /**
     * @param drawable
     * @param title
     * @param summary
     */
    public void addBasicItem(int drawable, String title) {
        mItemList.add(new BasicItem(drawable, title));
    }

    /**
     * @param drawable
     * @param title
     * @param summary
     */
    public void addBasicItem(int drawable, String title, String summary) {
        mItemList.add(new BasicItem(drawable, title, summary));
    }

    /**
     * @param item
     */
    public void addBasicItem(BasicItem item) {
        mItemList.add(item);
    }

    /**
     * @param itemView
     */
    public void addViewItem(ViewItem itemView) {
        mItemList.add(itemView);
    }

    /**
     * 新增setValue方法( 右边显示的文字)
     * 
     * @param value
     * @param index
     */
    public void setValue(CharSequence value, int index) {
        try {
            TextView tempTv = (TextView)(mListContainer.findViewWithTag(index)
                    .findViewById(R.id.round_bt_value));
            if (tempTv != null) {
                tempTv.setVisibility(View.VISIBLE);
                tempTv.setText(value);
            }
        } catch (NullPointerException e) {
            System.out.println("not found the view ----->");
            e.printStackTrace();
        }
    }

    public void commit() {
        mIndexController = 0;
        if (mItemList.size() > 1) {
            // when the list has more than one item
            for (IListItem obj : mItemList) {
                View tempItemView;
                if (mIndexController == 0) {
                    tempItemView = mInflater.inflate(R.layout.round_bt_item_top, null);
                } else if (mIndexController == mItemList.size() - 1) {
                    tempItemView = mInflater.inflate(R.layout.round_bt_item_bottom, null);
                } else {
                    tempItemView = mInflater.inflate(R.layout.round_bt_item_middle, null);
                }
                setupItem(tempItemView, obj, mIndexController);
                tempItemView.setClickable(obj.isClickable());
                // OEM++
                tempItemView.setTag(mIndexController);
                // OEM--
                mListContainer.addView(tempItemView);
                mIndexController++;
            }
        } else if (mItemList.size() == 1) {
            // when the list has only one item
            View tempItemView = mInflater.inflate(R.layout.round_bt_item_single, null);
            IListItem obj = mItemList.get(0);
            setupItem(tempItemView, obj, mIndexController);
            tempItemView.setClickable(obj.isClickable());
            // OEM++
            tempItemView.setTag(mIndexController);
            // OEM--
            mListContainer.addView(tempItemView);
        }
    }

    private void setupItem(View view, IListItem item, int index) {
        if (item instanceof BasicItem) {
            BasicItem tempItem = (BasicItem)item;
            setupBasicItem(view, tempItem, mIndexController);
        } else if (item instanceof ViewItem) {
            ViewItem tempItem = (ViewItem)item;
            setupViewItem(view, tempItem, mIndexController);
        }
    }

    /**
     * @param view
     * @param item
     * @param index
     */
    private void setupBasicItem(View view, BasicItem item, int index) {
        if (item.getDrawable() > -1) {
            ((ImageView)view.findViewById(R.id.round_bt_icon)).setBackgroundResource(item
                    .getDrawable());
        }
        if (!TextUtils.isEmpty(item.getVaule())) {
            ((TextView)view.findViewById(R.id.round_bt_value)).setText(item.getVaule());
        } else {
            ((TextView)view.findViewById(R.id.round_bt_value)).setVisibility(View.GONE);
        }
        TextView titleTv = (TextView)view.findViewById(R.id.round_bt_title);
        titleTv.setText(item.getTitle());
        // view.setTag(index);
        if (item.isClickable()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mClickListener != null)
                        mClickListener.onClick((Integer)view.getTag());
                }
            });
        } else {
            ((ImageView)view.findViewById(R.id.round_bt_icon)).setVisibility(View.GONE);
        }
    }

    /**
     * @param view
     * @param itemView
     * @param index
     */
    private void setupViewItem(View view, ViewItem itemView, int index) {
        if (itemView.getView() != null) {
            LinearLayout itemContainer = (LinearLayout)view.findViewById(R.id.itemContainer);
            itemContainer.removeAllViews();
            // itemContainer.removeAllViewsInLayout();
            itemContainer.addView(itemView.getView());

            if (itemView.isClickable()) {
                itemContainer.setTag(index);
                itemContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mClickListener != null)
                            mClickListener.onClick((Integer)view.getTag());
                    }
                });
            }
        }
    }

    public interface ClickListener {
        void onClick(int index);
    }

    /**
     * @return
     */
    public int getCount() {
        return mItemList.size();
    }

    /**
	 * 
	 */
    public void clear() {
        mItemList.clear();
        mListContainer.removeAllViews();
    }

    /**
     * @param listener
     */
    public void setClickListener(ClickListener listener) {
        this.mClickListener = listener;
    }

    /**
	 * 
	 */
    public void removeClickListener() {
        this.mClickListener = null;
    }

}
