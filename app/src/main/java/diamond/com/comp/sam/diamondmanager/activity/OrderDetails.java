package diamond.com.comp.sam.diamondmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.coordinator.OnListFragmentInteractionListener;
import diamond.com.comp.sam.diamondmanager.fragments.OrderFragment;
import diamond.com.comp.sam.diamondmanager.models.Orders;

public class OrderDetails extends AppCompatActivity implements OnListFragmentInteractionListener {


    private OrderFragment mOrderFragment;
    public static ParseQuery<Orders> parseQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOrderFragment = OrderFragment.newInstance(1,getIntent().getExtras());
        getFragmentManager().beginTransaction().add(R.id.data_layout, mOrderFragment).commit();
        if(getIntent().getExtras()==null)
            Log.d("Here","Null");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddOrder.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Orders item) {
        Intent intent = new Intent(getApplicationContext(),ViewOrder.class);
        ViewOrder.order = item;
        startActivity(intent);
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
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

}
