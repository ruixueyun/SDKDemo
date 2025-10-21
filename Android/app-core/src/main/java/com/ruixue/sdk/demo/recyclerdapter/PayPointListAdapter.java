package com.ruixue.sdk.demo.recyclerdapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.ruixue.sdk.demo.main.R;

public class PayPointListAdapter extends BaseQuickAdapter<String, QuickViewHolder> {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.layout_select_cpid_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int i, @Nullable String tag) {
        TextView textView = holder.getView(R.id.cpid_name);
        textView.setText(tag);
    }
}
