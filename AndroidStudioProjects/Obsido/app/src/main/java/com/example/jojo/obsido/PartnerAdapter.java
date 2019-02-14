package com.example.jojo.obsido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class PartnerAdapter extends ListAdapter<Partner, PartnerAdapter.PartnerHolder> {
    private OnItemClickListener listener;
    public PartnerAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Partner> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Partner>() {
        @Override
        public boolean areItemsTheSame(@NonNull Partner oldItem, @NonNull Partner newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Partner oldItem, @NonNull Partner newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public PartnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partners_list_item, parent, false);
        return new PartnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerHolder holder, int position) {
        Partner currentPartner = getItem(position);
        holder.textViewName.setText(currentPartner.getName());
        holder.textViewDescription.setText(currentPartner.getDescription());
    }

    public Partner getPartnerAt(int position) {
        return getItem(position);
    }

    class PartnerHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDescription;

        public PartnerHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.partner_name);
            textViewDescription = itemView.findViewById(R.id.partner_status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Partner partner);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
