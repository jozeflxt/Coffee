package com.lexot.cenicafe.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.lexot.cenicafe.Views.ViewWrapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class RecyclerViewAdapterBase<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {
    /**
     * Lista de datos del adaptador base
     */
    public List<T> items = new ArrayList<T>();
    /**
     * Lista de datos del adaptador base, la lista original cuando se instancio el adaptador
     */
    public List<T> origItems = new ArrayList<T>();
    protected Context context;
    @Override
    /**
     * Obtiene el total de datos del adaptador
     */
    public int getItemCount() {
        return items.size();
    }

    @Override
    /**
     * Vista base
     */
    public final ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }

    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    /**
     * Establece la lista del adaptador
     * @param list Lista de datos
     */
    public void setList(List<T> list) {
        if(list == null)
            list = new ArrayList<T>();
        this.items = list;
        this.origItems =  new ArrayList<T>(list);
    }
    /**
     * Establece la lista del adaptador
     * @param array Arreglo de datos
     */
    public void setList(T[] array) {
        this.items = new ArrayList<T>(Arrays.asList(array));
        this.origItems = new ArrayList<T>(Arrays.asList(array));
    }

    /**
     * Obtiene un dato de la lista en una posicion especifica
     * @param index Posicion
     * @return Item
     */
    public T getItem(int index) {
        return items.get(index);
    }

    /**
     * Filtra el adaptador a una nueva lista, y notifica los cambios
     * @param models Datos filtrados
     */
    public void filterTo(List<T> models) {
        this.items = models;
        notifyDataSetChanged();
    }

    /**
     * Establece el contexto del adaptador
     * @param ctx Contexto
     */
    public void setContext(Context ctx) {
        context = ctx;
    }
}