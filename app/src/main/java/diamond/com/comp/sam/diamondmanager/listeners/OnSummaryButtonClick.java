package diamond.com.comp.sam.diamondmanager.listeners;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

import diamond.com.comp.sam.diamondmanager.activity.OrderDetails;
import diamond.com.comp.sam.diamondmanager.coordinator.ParseDatabase;
import diamond.com.comp.sam.diamondmanager.models.Orders;
import diamond.com.comp.sam.diamondmanager.models.Status;

/**
 * Created by shubh on 15-03-2017.
 */

public class OnSummaryButtonClick implements View.OnClickListener {

    private Calendar mNow;
    private Activity mActivity;
    private Status mStatus;
    private ParseDatabase.Timeline mTimeline;

    public OnSummaryButtonClick(Activity activity, Calendar calender, Status status, ParseDatabase.Timeline timeline) {
        mActivity = activity;
        mStatus = status;
        mTimeline = timeline;
        mNow = calender;
    }

    @Override
    public void onClick(View v) {
        startActivity(mStatus, mTimeline);
    }

    private Bundle getBundle(Status status, Date date, ParseDatabase.Timeline timeLine) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Orders.STATUS, status);
        bundle.putLong(Orders.ORDER_DUE_DATE, date.getTime());
        bundle.putSerializable("Timeline", timeLine);
        return bundle;
    }

    private void startActivity(Status status, ParseDatabase.Timeline timeline) {
        Intent intent = new Intent(mActivity, OrderDetails.class);
        intent.putExtras(
                getBundle(status,
                        mNow.getTime(),
                        timeline));
        mActivity.startActivity(intent);
    }
}
