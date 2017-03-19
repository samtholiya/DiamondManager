package diamond.com.comp.sam.diamondmanager.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.vlk.multimager.activities.GalleryActivity;
import com.vlk.multimager.utils.Constants;
import com.vlk.multimager.utils.Image;
import com.vlk.multimager.utils.Params;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.coordinator.OnFileUploadListener;
import diamond.com.comp.sam.diamondmanager.coordinator.OnImageListFragmentInteractionListener;
import diamond.com.comp.sam.diamondmanager.coordinator.ParseDatabase;
import diamond.com.comp.sam.diamondmanager.fragments.ImageFragment;
import diamond.com.comp.sam.diamondmanager.library.DateLibrary;
import diamond.com.comp.sam.diamondmanager.library.SaveParseFileList;
import diamond.com.comp.sam.diamondmanager.listeners.DatePickerListener;
import diamond.com.comp.sam.diamondmanager.models.Orders;

public class AddOrder extends AppCompatActivity implements OnImageListFragmentInteractionListener, OnFileUploadListener {
    private static final int INTENT_REQUEST_GET_IMAGES = 543;
    private static final int REQUEST_CODE = 441;
    private Calendar mNow = Calendar.getInstance();
    private TextView mDateValue;
    private View mDatePickerView;
    private TextView mDueDateValue;
    private View mDueDatePickerView;
    private Orders mOrder;
    private EditText mCustomerName;
    private EditText mRemark;
    private Spinner mStatus;
    private EditText mWorker;
    private ImageFragment mImageFragment;
    private boolean isGranted;
    private ParseFile parseFile;
    private ArrayList<ParseFile> mParseFiles;
    private EditText mOrderId;

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE);
            } else {
                isGranted = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean flag = false;
        for (int item : grantResults) {
            if (item == PackageManager.PERMISSION_DENIED) {
                isGranted = false;
                flag = true;
                //resume tasks needing this permission
            }
        }
        if (!flag) {
            isGranted = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mParseFiles = new ArrayList<>();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        mOrder = ParseObject.create(Orders.class);

        mImageFragment = ImageFragment.newInstance(3);
        getFragmentManager().beginTransaction().add(R.id.image_layer, mImageFragment).commit();

        mOrderId = (EditText) findViewById(R.id.order_id_value);
        mCustomerName = (EditText) findViewById(R.id.customer_name_value);
        mWorker = (EditText) findViewById(R.id.worker_value);
        mRemark = (EditText) findViewById(R.id.remark_value);
        mStatus = (Spinner) findViewById(R.id.status_value);


        mDueDateValue = (TextView) findViewById(R.id.due_date_value);
        mDueDateValue.setText(DateLibrary
                .getStringDate(mNow.get(Calendar.DAY_OF_MONTH),
                        mNow.get(Calendar.MONTH),
                        mNow.get(Calendar.YEAR)));

        //TODO: SET UNIQUE CODE
        mOrderId.setText("DM" + mNow.get(Calendar.MILLISECOND));
        mDueDatePickerView = findViewById(R.id.due_date_picker);
        DatePickerListener dueDatePickerListener = new DatePickerListener(getFragmentManager());
        dueDatePickerListener.addOnDateChangedListener(mDueDateListener);
        mDueDatePickerView.setOnClickListener(dueDatePickerListener);
        mDateValue = (TextView) findViewById(R.id.date_value);
        mDateValue.setText(DateLibrary
                .getStringDate(mNow.get(Calendar.DAY_OF_MONTH),
                        mNow.get(Calendar.MONTH),
                        mNow.get(Calendar.YEAR)));

        mOrder.setOrderDate(DateLibrary
                .getDate(mNow.get(Calendar.DAY_OF_MONTH),
                        mNow.get(Calendar.MONTH),
                        mNow.get(Calendar.YEAR)));

        mOrder.setOrderDueDate(DateLibrary
                .getDate(mNow.get(Calendar.DAY_OF_MONTH),
                        mNow.get(Calendar.MONTH),
                        mNow.get(Calendar.YEAR)));

        mDatePickerView = findViewById(R.id.date_picker);
        DatePickerListener datePickerListener = new DatePickerListener(getFragmentManager());
        datePickerListener.addOnDateChangedListener(mDateListener);
        mDatePickerView.setOnClickListener(datePickerListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOrder.setCustomerName(mCustomerName.getText().toString());
                mOrder.setWorkerName(mWorker.getText().toString());
                mOrder.setRemark(mRemark.getText().toString());
                //TODO: How to set Random Order Id
                if (mOrderId.getText().toString().isEmpty()) {
                    Snackbar.make(mCustomerName, "Order Id cannot be Empty", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (mOrderId.getText().toString().length() < 5) {
                    Snackbar.make(mCustomerName, "Order Id cannot be Less than 5 char", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (mOrderId.getText().toString().matches("^\\s+$")) {
                    Snackbar.make(mCustomerName, "Order Id cannot be blank spaces", Snackbar.LENGTH_LONG).show();
                    return;
                }

                mOrder.setOrderId(mOrderId.getText().toString());
                mOrder.setStatus(mStatus.getSelectedItem().toString());
                mOrder.setImages(mParseFiles);
                ParseDatabase.save(mOrder, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                            finish();
                    }
                });
            }
        });

    }

    DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            mDateValue.setText(DateLibrary.getStringDate(dayOfMonth, monthOfYear, year));
            mOrder.setOrderDate(DateLibrary.getDate(dayOfMonth, monthOfYear, year));
        }
    };

    DatePickerDialog.OnDateSetListener mDueDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            mDueDateValue.setText(DateLibrary.getStringDate(dayOfMonth, monthOfYear, year));
            mOrder.setOrderDueDate(DateLibrary.getDate(dayOfMonth, monthOfYear, year));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (isGranted) {
            //noinspection SimplifiableIfStatement
            if (id == R.id.pick_image) {
                Intent intent = new Intent(this, GalleryActivity.class);
                Params params = new Params();
                params.setCaptureLimit(10);
                params.setPickerLimit(10);
                /*params.setToolbarColor(selectedColor);
                params.setActionButtonColor(selectedColor);
                params.setButtonTextColor(selectedColor);*/
                intent.putExtra(Constants.KEY_PARAMS, params);

                startActivityForResult(intent, Constants.TYPE_MULTI_PICKER);
                return true;
            }
        } else {
            Snackbar.make(mCustomerName, R.string.permission_denied, Snackbar.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Uri item) {
        Intent intent = new Intent(getApplicationContext(), ImageViewerActivity.class);
        intent.putExtra(ImageViewerActivity.IMAGE_URI, item);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.TYPE_MULTI_PICKER && resultCode == Activity.RESULT_OK) {


            ArrayList<Image> image_uris = data.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);
            /*for (Uri image : image_uris) {

                parseFile = new ParseFile(new File(image.getPath()));
                parseFile.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Snackbar.make(mCustomerName, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Log.d(getClass().getName(),parseFile.getUrl());
                            mParseFiles.add(parseFile);
                        }
                    }
                });
            }*/
            SaveParseFileList saveParseFileList = new SaveParseFileList(image_uris);
            saveParseFileList.addOnFileUploadFailedListener(this);
            saveParseFileList.upload();
            mImageFragment.addImages(image_uris);

            //do something
        }
    }

    @Override
    public void onFileUploadFailed(Uri uri) {
        Log.d(getClass().getName(), "Here I failed to upload files");
    }

    @Override
    public void onUploadFinished(ArrayList<ParseFile> parseFiles) {
        mParseFiles = parseFiles;
    }
}
