package diamond.com.comp.sam.diamondmanager.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseFile;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.coordinator.OnImageListFragmentInteractionListener;
import diamond.com.comp.sam.diamondmanager.coordinator.ParseDatabase;
import diamond.com.comp.sam.diamondmanager.fragments.ImageFragment;
import diamond.com.comp.sam.diamondmanager.library.DateLibrary;
import diamond.com.comp.sam.diamondmanager.listeners.DatePickerListener;
import diamond.com.comp.sam.diamondmanager.models.Orders;
import diamond.com.comp.sam.diamondmanager.models.Status;

public class ViewOrder extends AppCompatActivity implements OnImageListFragmentInteractionListener {

    private TextView mDateValue;
    private View mDatePickerView;
    private TextView mDueDateValue;
    private View mDueDatePickerView;
    private Orders mOrder;
    private TextView mCustomerName;
    private TextView mRemark;
    private Spinner mStatus;
    private EditText mWorker;
    private ImageFragment mImageFragment;

    public static Orders order;
    private boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        isFirstTime = true;
        mOrder = order;

        setTitle(getString(R.string.order_id) + ": " + mOrder.getOrderId());

        mCustomerName = (TextView) findViewById(R.id.customer_name_value);
        mCustomerName.setText(mOrder.getCustomerName());

        mWorker = (EditText) findViewById(R.id.worker_value);
        mWorker.setText(mOrder.getWorkerName());
        if(!mOrder.getStatus().toString().equals(Status.ORDER_RECEIVED.toString())) {
            mWorker.setEnabled(false);
        }
        mDateValue = (TextView) findViewById(R.id.date_value);
        mDateValue.setText(new SimpleDateFormat("dd-MM-yyyy").format(mOrder.getOrderDate()));

        mDueDateValue = (TextView) findViewById(R.id.due_date_value);
        mDueDateValue.setText(new SimpleDateFormat("dd-MM-yyyy").format(mOrder.getOrderDueDate()));


        mRemark = (EditText) findViewById(R.id.remark_value);
        mRemark.setText(mOrder.getRemark());

        mStatus = (Spinner) findViewById(R.id.status_value);
        mStatus.setSelection(((ArrayAdapter<String>) mStatus.getAdapter()).getPosition(mOrder.getStatus().toString()));

        mDatePickerView = findViewById(R.id.due_date_picker);
        DatePickerListener dueDatePickerListener = new DatePickerListener(getFragmentManager());
        dueDatePickerListener.addOnDateChangedListener(mDueDateListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrder.setCustomerName(mCustomerName.getText().toString());
                mOrder.setWorkerName(mWorker.getText().toString());
                mOrder.setRemark(mRemark.getText().toString());
                mOrder.setStatus(mStatus.getSelectedItem().toString());
                ParseDatabase.update(mOrder);
                finish();
            }
        });

        mImageFragment = ImageFragment.newInstance(3);
        getFragmentManager().beginTransaction().add(R.id.image_layer, mImageFragment).commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    DatePickerDialog.OnDateSetListener mDueDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            mDueDateValue.setText(DateLibrary.getStringDate(dayOfMonth, monthOfYear, year));
            mOrder.setOrderDueDate(DateLibrary.getDate(dayOfMonth, monthOfYear, year));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstTime) {
            for (ParseFile file : mOrder.getImages()) {
                mImageFragment.addUri(file.getUrl());
            }
            isFirstTime = false;
        }
    }

    @Override
    public void onListFragmentInteraction(Uri item) {
        Intent intent = new Intent(getApplicationContext(), ImageViewerActivity.class);
        intent.putExtra(ImageViewerActivity.IMAGE_URI, item);
        startActivity(intent);
    }
}
