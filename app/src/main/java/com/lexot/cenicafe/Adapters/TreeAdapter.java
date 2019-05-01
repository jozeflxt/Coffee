package com.lexot.cenicafe.Adapters;

import android.view.ViewGroup;

import com.lexot.cenicafe.Listeners.TreeListener;
import com.lexot.cenicafe.Models.CoffeeTree;
import com.lexot.cenicafe.Views.ViewTree;
import com.lexot.cenicafe.Views.ViewWrapper;

public class TreeAdapter extends RecyclerViewAdapterBase<CoffeeTree, ViewTree> {

    private TreeListener treeListener;
    public TreeAdapter(TreeListener treeListener) {
        this.treeListener = treeListener;
    }

    @Override
    protected ViewTree onCreateItemView(ViewGroup parent, int viewType) {
        ViewTree v = new ViewTree(this.context, treeListener);
        return v;

    }

    /**
     * Enlazar la vista con el adaptador
     *
     * @param viewHolder Vista
     * @param position   Posicion en la lista
     */
    @Override
    public void onBindViewHolder(ViewWrapper<ViewTree> viewHolder, int position) {
        ViewTree view = viewHolder.getView();
        CoffeeTree item = items.get(position);

        view.bind(item);
    }


}