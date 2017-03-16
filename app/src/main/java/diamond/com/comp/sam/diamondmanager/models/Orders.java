package diamond.com.comp.sam.diamondmanager.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shubh on 22-02-2017.
 */
@ParseClassName("Orders")
public class Orders extends ParseObject {
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String REMARK = "remark";
    public static final String STATUS = "status";
    public static final String ORDER_DATE = "order_date";
    public static final String ORDER_DUE_DATE = "order_due_date";
    public static final String ORDER_ID = "order_id";
    public static final String WORKER_NAME = "karigar_name";
    private static final String IMAGES = "images";
    public static final String USER = "user";

    public Orders() {
    }

    public void setOrderId(String orderId) {
        put(ORDER_ID, orderId);
    }

    public String getOrderId() {
        return get(ORDER_ID).toString();
    }


    public void setCustomerName(String customerName) {
        put(CUSTOMER_NAME, customerName);
    }

    public String getCustomerName() {
        return get(CUSTOMER_NAME).toString();
    }

    public void setRemark(String remark) {
        put(REMARK, remark);
    }

    public String getRemark() {
        return get(REMARK).toString();
    }

    public void setStatus(String status) {
        put(STATUS, status.toString());
    }

    public Status getStatus() {
        String status = get(STATUS).toString();
        if (status.equals(Status.WORKER_ASSIGNED.toString())) {
            return Status.WORKER_ASSIGNED;
        } else if (status.equals(Status.WORKER_RECEIVED.toString())) {
            return Status.WORKER_RECEIVED;
        } else if (status.equals(Status.ORDER_RECEIVED.toString())) {
            return Status.ORDER_RECEIVED;
        } else if (status.equals(Status.CLIENT_DELIVERED)) {
            return Status.CLIENT_DELIVERED;
        } else {
            return Status.READY;
        }
    }

    public void setOrderDate(Date date) {
        put(ORDER_DATE, date);
    }

    public Date getOrderDate() {
        return getDate(ORDER_DATE);
    }

    public void setOrderDueDate(Date orderDueDate) {
        put(ORDER_DUE_DATE, orderDueDate);
    }

    public Date getOrderDueDate() {
        return getDate(ORDER_DUE_DATE);
    }

    public void setWorkerName(String name) {
        put(WORKER_NAME, name);
    }

    public String getWorkerName() {
        return getString(WORKER_NAME);
    }

    public void setImages(ArrayList<ParseFile> images) {
        put(IMAGES, images);
    }

    public ArrayList<ParseFile> getImages() {
        return (ArrayList<ParseFile>) get(IMAGES);
    }
}
