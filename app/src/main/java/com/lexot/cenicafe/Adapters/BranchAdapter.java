package com.lexot.cenicafe.Adapters;

import android.view.View;
import android.view.ViewGroup;

import com.lexot.cenicafe.Listeners.BranchListener;
import com.lexot.cenicafe.Models.CoffeeBranch;
import com.lexot.cenicafe.Views.ViewBranch;
import com.lexot.cenicafe.Views.ViewWrapper;

public class BranchAdapter extends RecyclerViewAdapterBase<CoffeeBranch, ViewBranch> {

    private BranchListener listener;

    public BranchAdapter(BranchListener listener) {
        this.listener = listener;
    }

    @Override
    /**Inicializador del adaptador de sucursales
     *
     * @param parent Vista principal
     * @param viewType Tipo de vista
     * @return Vista individual de sucursal para lista
     */
    protected ViewBranch onCreateItemView(ViewGroup parent, int viewType) {
        ViewBranch v = new ViewBranch(this.context, listener);
        return v;

    }

    /**
     * Enlazar la vista con el adaptador
     *
     * @param viewHolder Vista
     * @param position   Posicion en la lista
     */
    @Override
    public void onBindViewHolder(ViewWrapper<ViewBranch> viewHolder, int position) {
        ViewBranch view = viewHolder.getView();
        CoffeeBranch item = items.get(position);

        view.bind(item);
    }


}