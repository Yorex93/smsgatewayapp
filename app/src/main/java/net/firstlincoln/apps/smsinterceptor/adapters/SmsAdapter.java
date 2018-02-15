package net.firstlincoln.apps.smsinterceptor.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.firstlincoln.apps.smsinterceptor.*;
import net.firstlincoln.apps.smsinterceptor.db.entity.SmsEntity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by webmaster on 09/02/2018.
 */

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(SmsEntity item);
    }

    private final List<SmsEntity> sms;
    private final OnItemClickListener listener;



    public SmsAdapter(List<SmsEntity> sms, OnItemClickListener listener){
        this.sms = sms;
        this.listener = listener;
    }

    @Override
    public SmsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SmsAdapter.ViewHolder holder, int position) {
        holder.bind(sms.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return sms.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDate;
        public TextView tvContent;
        public TextView tvPhone;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }

        public void bind(final SmsEntity smsEntity, final OnItemClickListener listener) {
            tvPhone.setText(smsEntity.getPhone());
            tvContent.setText(smsEntity.getContent());
            tvDate.setText(new SimpleDateFormat("MM-dd-yyyy HH:mm:ss").format(smsEntity.getCreatedAt()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(smsEntity);
                }
            });
        }
    }
}
