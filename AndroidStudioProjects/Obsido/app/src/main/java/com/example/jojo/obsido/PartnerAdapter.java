package com.example.jojo.obsido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.PartnerHolder> {

    private List<Partner> partners = new ArrayList<>();

    @NonNull
    @Override
    public PartnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partners_list_item, parent, false);
        return new PartnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PartnerHolder holder, int position) {
        Partner currentPartner = partners.get(position);
        holder.textViewName.setText(currentPartner.getName());
        holder.textViewDescription.setText(currentPartner.getDescription());
    }

    @Override
    public int getItemCount() {
        return partners.size();
    }

    public void setPartners(List<Partner> partners) {
        this.partners = partners;
        notifyDataSetChanged();
    }

    class PartnerHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDescription;

        public PartnerHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.partner_name);
            textViewDescription = itemView.findViewById(R.id.partner_status);
        }
    }
}
