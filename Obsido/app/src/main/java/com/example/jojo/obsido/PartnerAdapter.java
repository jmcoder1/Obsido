package com.example.jojo.obsido;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jojo.obsido.db.Partner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

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
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getAge() == newItem.getAge() &&
                    oldItem.getGender() == newItem.getGender();
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
        String currentPartnerName = currentPartner.getName();

        holder.textViewName.setText(currentPartnerName);
        holder.textViewDescription.setText(currentPartner.getDescription());

        // Handles what drawable to show
        holder.circleImageViewImg.setVisibility(View.INVISIBLE);
        holder.textViewImg.setVisibility(View.VISIBLE);
        holder.textViewImg.setText(currentPartnerName.substring(0, 1).toUpperCase());
    }

    public Partner getPartnerAt(int position) {
        return getItem(position);
    }

    class PartnerHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDescription;
        private TextView textViewImg;
        private CircleImageView circleImageViewImg;

        public PartnerHolder(View itemView) {
            super(itemView);
            textViewImg = itemView.findViewById(R.id.partner_main_img_text_view);
            textViewName = itemView.findViewById(R.id.partner_name);
            textViewDescription = itemView.findViewById(R.id.partner_status);
            circleImageViewImg = itemView.findViewById(R.id.partner_profile_img);

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
