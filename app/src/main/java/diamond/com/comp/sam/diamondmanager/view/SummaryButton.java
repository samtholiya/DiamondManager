package diamond.com.comp.sam.diamondmanager.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import diamond.com.comp.sam.diamondmanager.R;

/**
 * Created by shubh on 13-03-2017.
 */

public class SummaryButton extends LinearLayout {
    private View mRootView;
    private TextView mSummary;
    private TextView mLabel;
    private View mUnderlineView;

    public String getSummary() {
        return mSummary.getText().toString();
    }

    public void setSummary(String summary) {
        this.mSummary.setText(summary);
    }

    public String getLabel() {
        return mLabel.getText().toString();
    }

    public void setLabel(String label) {
        this.mLabel.setText(label);
    }

    public SummaryButton(Context context) {
        super(context);
        init(context, null);
    }

    public SummaryButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.ValueBar, 0, 0);
        init(context, ta);
    }

    protected void init(Context context, TypedArray typedArray) {
        mRootView = inflate(context, R.layout.summary_button, this);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true);
        setClickable(true);
        mSummary = (TextView) mRootView.findViewById(R.id.summary_value);
        mLabel = (TextView) mRootView.findViewById(R.id.label_value);
        mUnderlineView = mRootView.findViewById(R.id.underline_view);
        if (typedArray != null) {
            setSummary(typedArray.getString(R.styleable.ValueBar_summary));
            setLabel(typedArray.getString(R.styleable.ValueBar_labelText));
            int color = 0;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
                color = context.getResources().getColor(android.R.color.holo_red_light);
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                color = context.getResources().getColor(android.R.color.holo_red_light, context.getTheme());
            setBaseColor(typedArray.getColor(R.styleable.ValueBar_baseColor, color));
        }
        setBackgroundResource(outValue.resourceId);
    }

    public void setBaseColor(int baseColor) {
        if (baseColor != 0) {
            mUnderlineView.setBackgroundColor(baseColor);
            mLabel.setTextColor(baseColor);
        }
    }
}
