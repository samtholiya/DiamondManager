package diamond.com.comp.sam.diamondmanager.models;

/**
 * Created by shubh on 22-02-2017.
 */

public enum Status {
    ORDER_RECEIVED("Order Received"),
    WORKER_ASSIGNED("Karigar Assigned"),
    WORKER_RECEIVED("Karigar Received"),
    READY("Ready"),
    CLIENT_DELIVERED("Client Delivered");

    private String status;

    private Status(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }
}
