package com.test.packages;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Адаптер
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {


    // В этом методе мы создаем новую ячейку
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    // В этом методе мы привязываем данные к ячейке
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    // В этом методе мы возвращаем количество элементов списка
    @Override
    public int getItemCount() {
        return 0;
    }

    /**
     * View holder
     * Хранит информацию о ячейке
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
