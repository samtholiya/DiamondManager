package diamond.com.comp.sam.diamondmanager.listeners;

import com.parse.CountCallback;
import com.parse.ParseException;

import diamond.com.comp.sam.diamondmanager.view.SummaryButton;

/**
 * Created by shubh on 15-03-2017.
 */

public class OrdersCountCallback implements CountCallback {
    SummaryButton mSummaryButton;

    public OrdersCountCallback(SummaryButton summaryButton){
        mSummaryButton = summaryButton;
    }

    @Override
    public void done(int count, ParseException e) {
        if (e == null)
            mSummaryButton.setSummary(String.valueOf(count));
        if (count > 0) {

        }
    }
}
