package diamond.com.comp.sam.diamondmanager.listeners;

import android.view.View;

import com.parse.ParseQuery;

import java.util.Date;

import diamond.com.comp.sam.diamondmanager.models.Orders;

/**
 * Created by shubh on 13-03-2017.
 */

public class SummaryClick implements View.OnClickListener {
    ParseQuery<Orders> mParseQuery;
    public SummaryClick(String status,Date date){
        mParseQuery.cancel();
    }

    @Override
    public void onClick(View v) {

    }
}
