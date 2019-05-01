package com.lexot.cenicafe.Adapters;

import android.view.View;
import android.view.ViewGroup;

import com.lexot.cenicafe.Models.CoffeeFrame;
import com.lexot.cenicafe.Views.ViewFrame;
import com.lexot.cenicafe.Views.ViewWrapper;

public class FrameAdapter extends RecyclerViewAdapterBase<CoffeeFrame, ViewFrame> implements View.OnClickListener {
    private View.OnClickListener listener;
    public FrameAdapter()
    {
    }
    @Override
    /**Inicializador del adaptador de sucursales
     *
     * @param parent Vista principal
     * @param viewType Tipo de vista
     * @return Vista individual de sucursal para lista
     */
    protected ViewFrame onCreateItemView(ViewGroup parent, int viewType) {
        ViewFrame v = new ViewFrame(context);
        v.setOnClickListener(this);
        return v;

    }

    /**
     * Enlazar la vista con el adaptador
     * @param viewHolder Vista
     * @param position Posicion en la lista
     */
    @Override
    public void onBindViewHolder(ViewWrapper<ViewFrame> viewHolder, int position) {
        ViewFrame view = viewHolder.getView();
        CoffeeFrame item = items.get(position);

        view.bind(item);
    }

    /**
     * Establece el Listener para cuando se hace clic en un item de la lista
     * @param listener Listener para el metodo
     */
    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * Metodo para cuando se da clic en un item de la lista
     * @param view Vista que hace referencia al item de la lista
     */
    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

}