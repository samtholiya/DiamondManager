package diamond.com.comp.sam.diamondmanager.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import diamond.com.comp.sam.diamondmanager.R;
import diamond.com.comp.sam.diamondmanager.coordinator.OnListFragmentInteractionListener;
import diamond.com.comp.sam.diamondmanager.models.Orders;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Orders} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {

    public List<Orders> mValues;
    private final OnListFragmentInteractionListener mListener;
    public OrderRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<>();
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mOrderIdView.setText(holder.mItem.getOrderId());
        holder.mCustomerNameView.setText(holder.mItem.getCustomerName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void addOrders(List<Orders> objects) {
        int start = mValues.size() - 1;
        mValues.addAll(objects);
        notifyItemRangeInserted(start,objects.size());
    }

    public void updateDataSet(List<Orders> objects){
        mValues.clear();
        Log.d(getClass().getName(),"Updated count"+objects.size());
        mValues.addAll(objects);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mOrderIdView;
        public final TextView mCustomerNameView;

        public Orders mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mOrderIdView = (TextView) view.findViewById(R.id.order_id_value);
            mCustomerNameView = (TextView) view.findViewById(R.id.customer_name_value);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCustomerNameView.getText() + "'";
        }
    }
}
