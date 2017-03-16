package diamond.com.comp.sam.diamondmanager.coordinator;

import android.util.Log;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Calendar;
import java.util.Date;

import diamond.com.comp.sam.diamondmanager.models.Orders;

/**
 * Created by shubh on 22-02-2017.
 */

public class ParseDatabase {

    private static final String USER = "user";
    private static OnDatabaseChanged mOnDataBaseChanged;

    public static void addOnDatabaseChanged(OnDatabaseChanged onDatabaseChanged) {
        mOnDataBaseChanged = onDatabaseChanged;
    }

    public static <T extends ParseObject> ParseQuery<T> getParseQuery(Class<T> tableClass, String orderBy, boolean isAscending, int offset, int max, FindCallback<T> findCallback) {
        ParseQuery<T> parseQuery = new ParseQuery<T>(tableClass);
        if (ParseUser.getCurrentUser() != null)
            parseQuery.whereEqualTo(USER, ParseUser.getCurrentUser());
        if (max != 0)
            parseQuery.setLimit(max);
        if (offset != 0)
            parseQuery.setSkip(offset);
        if (orderBy != null) {
            if (isAscending)
                parseQuery.orderByAscending(orderBy);
            else
                parseQuery.orderByDescending(orderBy);
        }
        parseQuery
                .findInBackground(findCallback);
        return parseQuery;
    }

    public static ParseQuery<Orders> getParseOrders(ParseQuery<Orders> parseQuery, String orderBy, boolean isAscending, int offset, int max, FindCallback<Orders> findCallback) {

        if (max != 0)
            parseQuery.setLimit(max);
        if (offset != 0)
            parseQuery.setSkip(offset);
        if (orderBy != null) {
            if (isAscending)
                parseQuery.orderByAscending(orderBy);
            else
                parseQuery.orderByDescending(orderBy);
        }
        parseQuery
                .findInBackground(findCallback);
        return parseQuery;
    }

    public static <T extends ParseObject> ParseQuery<T> getParseQuery(Class<T> tableClass, int offset, int max, FindCallback<T> findCallback) {
        ParseQuery<T> parseQuery = new ParseQuery<T>(tableClass);
        if (ParseUser.getCurrentUser() != null)
            parseQuery.whereEqualTo(USER, ParseUser.getCurrentUser());
        if (max != 0)
            parseQuery.setLimit(max);
        if (offset != 0)
            parseQuery.setSkip(offset);
        parseQuery
                .findInBackground(findCallback);
        return parseQuery;
    }

    public static <T extends ParseObject> void save(T parseObject, SaveCallback saveCallback) {
        if (ParseUser.getCurrentUser() != null) {
            parseObject.put(USER, ParseUser.getCurrentUser());
        } else {
            Log.e("BAD USER", "No user is set");
        }
        parseObject
                .saveEventually(new ParseSaveCallBack(saveCallback));

    }

    private static class ParseSaveCallBack implements SaveCallback {
        private SaveCallback mSaveCallback;

        public ParseSaveCallBack(SaveCallback saveCallback) {
            mSaveCallback = saveCallback;
        }

        @Override
        public void done(ParseException e) {
            if (e != null)
                Log.d(getClass().getName(), e.getMessage());
            mSaveCallback.done(e);
            if (mOnDataBaseChanged != null)
                mOnDataBaseChanged.dataInserted(0);
        }
    }

    public static ParseQuery<Orders> getOrdersCount(String status, Date beforeDate, Timeline timeline, CountCallback countCallback) {
        ParseQuery<Orders> parseQuery = new ParseQuery<Orders>(Orders.class);
        if (ParseUser.getCurrentUser() != null) {
            inflateParseQuery(parseQuery, status, beforeDate, timeline);
            parseQuery.countInBackground(countCallback);
            return parseQuery;
        } else {
            Log.d("BAD USER", "No user is set");
        }
        return null;
    }

    private static void inflateParseQuery(ParseQuery<Orders> parseQuery, String status, Date beforeDate, Timeline timeline) {
        if (ParseUser.getCurrentUser() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(beforeDate);
            parseQuery.whereEqualTo(Orders.STATUS, status);
            switch (timeline) {
                case ON:
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    parseQuery.whereGreaterThan(Orders.ORDER_DUE_DATE, cal.getTime());
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    parseQuery.whereLessThan(Orders.ORDER_DUE_DATE, cal.getTime());
                    break;
                case BEFORE:

                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    parseQuery.whereLessThan(Orders.ORDER_DUE_DATE, cal.getTime());
                    break;
                case AFTER:
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    cal.set(Calendar.MILLISECOND, 999);
                    parseQuery.whereGreaterThan(Orders.ORDER_DUE_DATE, cal.getTime());
                    break;
                case ALL:
                    break;
            }
            parseQuery.whereEqualTo(Orders.USER, ParseUser.getCurrentUser());
        } else {
            Log.d("BAD USER", "No user is set");
        }
    }

    public static ParseQuery<Orders> getOrders(String status, Date beforeDate, int max, int offset, Timeline timeLine, FindCallback<Orders> findCallback) {
        ParseQuery<Orders> parseQuery = new ParseQuery<Orders>(Orders.class);
        if (ParseUser.getCurrentUser() != null) {
            if (max != 0)
                parseQuery.setLimit(max);
            if (offset != 0)
                parseQuery.setSkip(offset);
            inflateParseQuery(parseQuery, status, beforeDate, timeLine);
            parseQuery.findInBackground(findCallback);
            return parseQuery;
        } else {
            Log.d("BAD USER", "No user is set");
        }
        return null;
    }

    public static void update(Orders parseObject) {
        if (ParseUser.getCurrentUser() != null) {
            parseObject.put(USER, ParseUser.getCurrentUser());
        } else {
            Log.e("BAD USER", "No user is set");
        }
        parseObject
                .saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null)
                            Log.d(getClass().getName(), e.getMessage());
                        mOnDataBaseChanged.dataUpdated(0);
                    }
                });
    }


    public static enum Timeline {
        BEFORE,
        ON,
        AFTER,
        ALL
    }
}
