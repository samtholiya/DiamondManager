package diamond.com.comp.sam.diamondmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Calendar;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.coordinator.ParseDatabase;
import diamond.com.comp.sam.diamondmanager.listeners.OnSummaryButtonClick;
import diamond.com.comp.sam.diamondmanager.listeners.OrdersCountCallback;
import diamond.com.comp.sam.diamondmanager.models.Orders;
import diamond.com.comp.sam.diamondmanager.models.Status;
import diamond.com.comp.sam.diamondmanager.view.SummaryButton;

public class Dashboard extends AppCompatActivity {

    private Calendar mNow = Calendar.getInstance();
    private Calendar mTomorrow = Calendar.getInstance();
    private SummaryButton mBSOR;  //Behind Schedule Order received
    private SummaryButton mBSWA;
    private SummaryButton mBSWR;
    private SummaryButton mSTOR;
    private SummaryButton mSTWA;
    private SummaryButton mSTWR;
    private SummaryButton mSTR;
    private SummaryButton mSROR;
    private SummaryButton mSRWA;
    private SummaryButton mSRWR;
    private SummaryButton mSRR;


    ParseQuery<Orders> parseQuery;
    private SummaryButton mAR;
    private SummaryButton mAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTomorrow.add(Calendar.DATE,1);

        mBSOR = (SummaryButton) findViewById(R.id.b_s_o_r);
        mBSOR.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.ORDER_RECEIVED,
                ParseDatabase.Timeline.BEFORE));

        mBSWA = (SummaryButton) findViewById(R.id.b_s_w_a);
        mBSWA.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.WORKER_ASSIGNED,
                ParseDatabase.Timeline.BEFORE));

        mBSWR = (SummaryButton) findViewById(R.id.b_s_w_r);
        mBSWR.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.WORKER_RECEIVED,
                ParseDatabase.Timeline.BEFORE));

        mSTOR = (SummaryButton) findViewById(R.id.s_t_o_r);
        mSTOR.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.ORDER_RECEIVED,
                ParseDatabase.Timeline.ON));

        mSTWA = (SummaryButton) findViewById(R.id.s_t_w_a);
        mSTWA.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.WORKER_ASSIGNED,
                ParseDatabase.Timeline.ON));

        mSTWR = (SummaryButton) findViewById(R.id.s_t_w_r);
        mSTWR.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.WORKER_RECEIVED,
                ParseDatabase.Timeline.ON));

        mSTR = (SummaryButton) findViewById(R.id.s_t_r);
        mSTR.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.READY,
                ParseDatabase.Timeline.ON));

        mSROR = (SummaryButton) findViewById(R.id.s_r_o_r);
        mSROR.setOnClickListener(new OnSummaryButtonClick(this,
                mTomorrow,
                Status.ORDER_RECEIVED,
                ParseDatabase.Timeline.ON));

        mSRWA = (SummaryButton) findViewById(R.id.s_r_w_a);
        mSRWA.setOnClickListener(new OnSummaryButtonClick(this,
                mTomorrow,
                Status.WORKER_ASSIGNED,
                ParseDatabase.Timeline.ON));

        mSRR = (SummaryButton) findViewById(R.id.s_r_r);
        mSRR.setOnClickListener(new OnSummaryButtonClick(this,
                mTomorrow,
                Status.READY,
                ParseDatabase.Timeline.ON));

        mSRWR = (SummaryButton) findViewById(R.id.s_r_w_r);
        mSRWR.setOnClickListener(new OnSummaryButtonClick(this,
                mTomorrow,
                Status.WORKER_RECEIVED,
                ParseDatabase.Timeline.ON));

        mAR =(SummaryButton) findViewById(R.id.a_r);
        mAR.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.READY,
                ParseDatabase.Timeline.ALL));

        mAD = (SummaryButton) findViewById(R.id.a_d);
        mAD.setOnClickListener(new OnSummaryButtonClick(this,
                mNow,
                Status.CLIENT_DELIVERED,
                ParseDatabase.Timeline.ALL));


        FloatingActionButton mfab = (FloatingActionButton) findViewById(R.id.fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddOrder.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ParseDatabase.getOrdersCount(Status.ORDER_RECEIVED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.BEFORE,
                new OrdersCountCallback(mBSOR));
        ParseDatabase.getOrdersCount(Status.WORKER_ASSIGNED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.BEFORE,
                new OrdersCountCallback(mBSWA));
        ParseDatabase.getOrdersCount(Status.WORKER_RECEIVED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.BEFORE,
                new OrdersCountCallback(mBSWR));
        ParseDatabase.getOrdersCount(Status.ORDER_RECEIVED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSTOR));
        ParseDatabase.getOrdersCount(Status.WORKER_ASSIGNED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSTWA));
        ParseDatabase.getOrdersCount(Status.WORKER_RECEIVED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSTWR));
        ParseDatabase.getOrdersCount(Status.READY.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSTR));

        ParseDatabase.getOrdersCount(Status.ORDER_RECEIVED.toString(),
                mTomorrow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSROR));
        ParseDatabase.getOrdersCount(Status.WORKER_ASSIGNED.toString(),
                mTomorrow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSRWA));
        ParseDatabase.getOrdersCount(Status.WORKER_RECEIVED.toString(),
                mTomorrow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSRWR));
        ParseDatabase.getOrdersCount(Status.READY.toString(),
                mTomorrow.getTime(),
                ParseDatabase.Timeline.ON,
                new OrdersCountCallback(mSRR));
        ParseDatabase.getOrdersCount(Status.READY.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.ALL,
                new OrdersCountCallback(mAR));
        ParseDatabase.getOrdersCount(Status.CLIENT_DELIVERED.toString(),
                mNow.getTime(),
                ParseDatabase.Timeline.ALL,
                new OrdersCountCallback(mAD));

    }
}
