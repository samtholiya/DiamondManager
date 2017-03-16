package diamond.com.comp.sam.diamondmanager.listeners;

import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.view.View;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by shubh on 24-02-2017.
 */

public class DatePickerListener implements View.OnClickListener {

    Calendar mNow = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private FragmentManager mFragmentManager;
    public DatePickerListener(@NonNull FragmentManager fragmentManager){
        mFragmentManager = fragmentManager;
    }

    public void addOnDateChangedListener(DatePickerDialog.OnDateSetListener onDateSetListener){
        mOnDateSetListener = onDateSetListener;
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                mOnDateSetListener,
                mNow.get(Calendar.YEAR),
                mNow.get(Calendar.MONTH),
                mNow.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(mFragmentManager, "Datepickerdialog");
    }
}
