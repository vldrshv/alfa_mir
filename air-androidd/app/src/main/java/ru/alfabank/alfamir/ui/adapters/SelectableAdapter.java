package ru.alfabank.alfamir.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by U_M0WY5 on 12.10.2017.
 */

public abstract class SelectableAdapter <VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    @SuppressWarnings("unused")
    private static final String TAG = SelectableAdapter.class.getSimpleName();

    private SparseBooleanArray selectedItems;
    private SelectedCounter selectedCounter;

    public SelectableAdapter() {
        selectedItems = new SparseBooleanArray();
    }

    public void setListener(SelectedCounter selectedCounter){
        this.selectedCounter = selectedCounter;
    }


    /**
     * Indicates if the item at position position is selected
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {

        toggleActionMode();

        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);

        selectedCounter.setSelected(selectedItems.size());

    }

    public void selectAll(int listLength) {
        for (int i = 0; i < listLength; i++){
            if(!selectedItems.get(i, false)){
                selectedItems.put(i, true);
            }
        }
        selectedCounter.setSelected(selectedItems.size());
    }

    public void toggleSelectionOff() {

        toggleActionModeOff();

        clearSelection();

    }

    public abstract void toggleActionMode();

    public abstract void toggleActionModeOff();

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
//        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        selectedCounter.setSelected(selectedItems.size());

//        for (int i = 0; i < selectedItems.size(); i++) {
//            notifyItemChanged(i);
//        }

        // old and working
//        for (Integer i : selection) {
//            notifyItemChanged(i);
//        }
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override public void run() {
//                clearFeed();
//            }
//        }, 500);
    }

    /**
     * Count the selected items
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public interface SelectedCounter {
        void setSelected(int i);
    }

}
