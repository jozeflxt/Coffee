package com.lexot.cenicafe.Adapters;

import android.view.View;
import android.view.ViewGroup;

import com.lexot.cenicafe.Models.CoffeeBatch;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Views.ViewBatch;
import com.lexot.cenicafe.Views.ViewBranch;
import com.lexot.cenicafe.Views.ViewWrapper;

public class BatchAdapter extends RecyclerViewAdapterBase<CoffeeBatch, ViewBatch> {

    public BatchAdapter() {
    }

    @Override
    protected ViewBatch onCreateItemView(ViewGroup parent, int viewType) {
        ViewBatch v = new ViewBatch(this.context);
        return v;

    }

    /**
     * Enlazar la vista con el adaptador
     *
     * @param viewHolder Vista
     * @param position   Posicion en la lista
     */
    @Override
    public void onBindViewHolder(ViewWrapper<ViewBatch> viewHolder, int position) {
        ViewBatch view = viewHolder.getView();
        CoffeeBatch item = items.get(position);

        view.bind(item);
    }


}