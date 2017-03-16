package diamond.com.comp.sam.diamondmanager.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.Date;
import java.util.List;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.activity.OrderDetails;
import diamond.com.comp.sam.diamondmanager.adapters.OrderRecyclerViewAdapter;
import diamond.com.comp.sam.diamondmanager.base.EndlessRecyclerViewScrollListener;
import diamond.com.comp.sam.diamondmanager.coordinator.OnDatabaseChanged;
import diamond.com.comp.sam.diamondmanager.coordinator.OnListFragmentInteractionListener;
import diamond.com.comp.sam.diamondmanager.coordinator.ParseDatabase;
import diamond.com.comp.sam.diamondmanager.models.Orders;
import diamond.com.comp.sam.diamondmanager.models.Status;

import static diamond.com.comp.sam.diamondmanager.models.Orders.ORDER_DATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrderFragment extends Fragment implements OnDatabaseChanged {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int PAGE_ITEMS = 10;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private OrderRecyclerViewAdapter mOrderRecyclerViewAdapter;
    private Status mStatus;
    private Date mDate;
    private ParseDatabase.Timeline mTimeline;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OrderFragment newInstance(int columnCount, Bundle args) {
        OrderFragment fragment = new OrderFragment();
        if (args == null)
            args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            if (getArguments().containsKey(Orders.ORDER_DUE_DATE)) {
                mDate = new Date();
                mDate.setTime(getArguments().getLong(Orders.ORDER_DUE_DATE, -1));
                mStatus = (Status) getArguments().getSerializable(Orders.STATUS);
                mTimeline = (ParseDatabase.Timeline) getArguments().getSerializable("Timeline");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            ParseDatabase.addOnDatabaseChanged(this);
            mOrderRecyclerViewAdapter = new OrderRecyclerViewAdapter(mListener);
            recyclerView.setAdapter(mOrderRecyclerViewAdapter);
            if (mColumnCount <= 1) {
                Bundle bundle = getActivity().getIntent().getExtras();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        loadDataFromParse(page);
                    }
                });
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, mColumnCount);
                recyclerView.setLayoutManager(gridLayoutManager);
            }
            loadDataFromParse(0);
        }
        return view;
    }

    private void loadDataFromParse(int page) {
        int offset = PAGE_ITEMS * page;
        Log.d(getClass().getName(), (OrderDetails.parseQuery == null) + "loaded data from parse " + page);
        if (mStatus == null)
            ParseDatabase.getParseQuery(Orders.class, ORDER_DATE, false, offset, PAGE_ITEMS, new FindCallback<Orders>() {
                @Override
                public void done(List<Orders> objects, ParseException e) {
                    if (e == null)
                        mOrderRecyclerViewAdapter.addOrders(objects);
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "exception " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        else
            ParseDatabase.getOrders(mStatus.toString(), mDate, PAGE_ITEMS, offset, mTimeline, new FindCallback<Orders>() {
                @Override
                public void done(List<Orders> objects, ParseException e) {
                    Log.d(getClass().getName()," orders count "+ objects.size());
                    if (e == null)
                        mOrderRecyclerViewAdapter.addOrders(objects);
                    else
                        Toast.makeText(getActivity().getApplicationContext(), "exception " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void dataInserted(int position) {
        /*ParseDatabase.getParseQuery(Orders.class, ORDER_DATE, false, 0, PAGE_ITEMS, new FindCallback<Orders>() {
            @Override
            public void done(List<Orders> objects, ParseException e) {
                mOrderRecyclerViewAdapter.updateDataSet(objects);
            }
        });*/

    }

    @Override
    public void dataDeleted(int position) {

    }

    @Override
    public void dataUpdated(int position) {
        Log.d(getClass().getName(), "Data Updated");
        Log.d(getClass().getName(), mDate +"" + mStatus.toString());
        ParseDatabase.getOrders(mStatus.toString(), mDate, PAGE_ITEMS, 0, mTimeline, new FindCallback<Orders>() {
            @Override
            public void done(List<Orders> objects, ParseException e) {
                if (e == null) {
                    Log.d(getClass().getName(),"Here its added");
                    mOrderRecyclerViewAdapter.updateDataSet(objects);
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "exception " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
