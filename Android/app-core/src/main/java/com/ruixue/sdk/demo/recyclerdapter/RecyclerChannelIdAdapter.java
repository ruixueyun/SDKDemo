package com.ruixue.sdk.demo.recyclerdapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter4.BaseQuickAdapter;
import com.chad.library.adapter4.viewholder.QuickViewHolder;
import com.ruixue.sdk.demo.main.R;
import com.ruixue.sdk.demo.model.ProDuctIDItem;

public class RecyclerChannelIdAdapter
        extends BaseQuickAdapter<ProDuctIDItem.DataDTO.ChannelsDTO, QuickViewHolder> {

    @NonNull
    @Override
    protected QuickViewHolder onCreateViewHolder(@NonNull Context context, @NonNull ViewGroup viewGroup, int i) {
        return new QuickViewHolder(R.layout.layout_select_cpid_item, viewGroup);
    }

    @Override
    protected void onBindViewHolder(@NonNull QuickViewHolder holder, int i,
                                    @Nullable ProDuctIDItem.DataDTO.ChannelsDTO channelsDTO) {
        TextView textView = holder.getView(R.id.cpid_name);
        if (channelsDTO != null) {
            textView.setText(channelsDTO.getId());
        }
    }
}
