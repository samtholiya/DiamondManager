package diamond.com.comp.sam.diamondmanager.coordinator;

/**
 * Created by shubh on 25-02-2017.
 */
public interface OnDatabaseChanged {
    void dataInserted(int position);
    void dataDeleted(int position);
    void dataUpdated(int position);
}
