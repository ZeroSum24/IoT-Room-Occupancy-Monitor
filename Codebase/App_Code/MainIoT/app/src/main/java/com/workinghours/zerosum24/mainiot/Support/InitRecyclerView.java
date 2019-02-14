package com.workinghours.zerosum24.mainiot.Support;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.workinghours.zerosum24.mainiot.R;

public class InitRecyclerView {

    private RecyclerView foundDeviceContainer;

    public InitRecyclerView(Device[] devicesList, RecyclerView deviceContainer, final Context appContext) {

        foundDeviceContainer = deviceContainer;

        DevicesRecyclerAdapter mAdapter = new DevicesRecyclerAdapter(devicesList);
        mAdapter.setHasStableIds(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        foundDeviceContainer.setLayoutManager(mLayoutManager);
        foundDeviceContainer.setItemAnimator(new DefaultItemAnimator());
        foundDeviceContainer.addItemDecoration(
                new MyDividerItemDecoration(appContext, R.drawable.res_divider,
                        LinearLayoutManager.VERTICAL, 10));

        foundDeviceContainer.setAdapter(mAdapter);

        foundDeviceContainer.addOnItemTouchListener(new RecyclerTouchListener(appContext,
        foundDeviceContainer, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Toast.makeText(appContext,
                    "Normal Click", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

                Toast.makeText(appContext,
                    "You have made a long click", Toast.LENGTH_SHORT).show();
            }

            }));
        }

    public RecyclerView getFoundDeviceContainer() {
        return foundDeviceContainer;
    }
}

/**
 * Class is used to create a custom Completed Songs Adapter to display them correctly in the
 * RecyclerView.
 */
class DevicesRecyclerAdapter extends RecyclerView.Adapter<DevicesRecyclerAdapter.MyViewHolder> {

    private Device[] devicesList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView blueToothImage;
        TextView deviceName;

        MyViewHolder(View view) {
            super(view);
            blueToothImage = (ImageView) view.findViewById(R.id.layBluetoothImage);
            deviceName = (TextView) view.findViewById(R.id.layDeviceName);
        }
    }


    DevicesRecyclerAdapter(Device[] devicesList) {
        this.devicesList = devicesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.device_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Device current = devicesList[position];

        holder.blueToothImage.setImageResource(current.connectionStatusToImageID());
        holder.deviceName.setText(current.getName());
    }

    @Override
    public int getItemCount() {
        return devicesList.length;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

/**
 * Class is used to create a custom Divider for the RecyclerView.
 */
class MyDividerItemDecoration extends RecyclerView.ItemDecoration {

    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    private int mOrientation;
    private int margin;
    private Context context;

    MyDividerItemDecoration(Context context, int resId, int orientation, int margin) {
        this.margin = margin;
        this.context = context;
        mDivider = ContextCompat.getDrawable(context, resId);
        setOrientation(orientation);
    }

    private void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left + dpToPx(margin), top, right - dpToPx(margin), bottom);
            mDivider.draw(c);
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top + dpToPx(margin), right, bottom - dpToPx(margin));
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }

    private int dpToPx(int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

/**
 * Class is used to implement custom touch events for each individual list item in the RecyclerView.
 * These events allow the user to view access a Youtube link if they long click on the collected
 * list item.
 *
 * This class is also used in the Lyrics activity to allow the user to click each individual line
 * to be informed about which has been selected.
 *
 */
class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
