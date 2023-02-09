package com.hhh.paws.ui.petProfile.menu.treatment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hhh.paws.R;
import com.hhh.paws.database.model.Treatment;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;

import java.util.List;
import java.util.Objects;

public class TreatmentAdapter extends RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder> {

    static class TreatmentViewHolder extends RecyclerView.ViewHolder {

        public TreatmentViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        CardView treatmentContainer= itemView.findViewById(R.id.treatmentContainer);
        TextView nameTreatmentItem = itemView.findViewById(R.id.nameTreatmentItem);
        TextView manufacturerTreatmentItem = itemView.findViewById(R.id.manufacturerTreatmentItem);
        TextView dateTreatmentItem = itemView.findViewById(R.id.dateTreatmentItem);
        TextView veterinarianTreatmentItem = itemView.findViewById(R.id.veterinarianTreatmentItem);
    }

    private final AsyncListDiffer<Treatment> differ = new AsyncListDiffer<Treatment>(this, callback);

    private static final DiffUtil.ItemCallback<Treatment> callback = new DiffUtil.ItemCallback<Treatment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Treatment oldItem, @NonNull Treatment newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Treatment oldItem, @NonNull Treatment newItem) {
            if (!Objects.equals(oldItem.getId(), newItem.getId())) {
                return false;
            } else if (!Objects.equals(oldItem.getDate(), newItem.getDate())) {
                return false;
            } else if (!Objects.equals(oldItem.getName(), newItem.getName())) {
                return false;
            } else if (!Objects.equals(oldItem.getVeterinarian(), newItem.getVeterinarian())) {
                return false;
            } else {
                return Objects.equals(oldItem.getManufacturer(), newItem.getManufacturer());
            }
        }
    };

    public void setDiffer(List<Treatment> treatmentList) {
        this.differ.submitList(null);
        this.differ.submitList(treatmentList);
    }

    @NonNull
    @Override
    public TreatmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TreatmentViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.treatment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TreatmentViewHolder holder, int position) {
        Treatment currentTreatment = differ.getCurrentList().get(position);

        holder.dateTreatmentItem.setText(currentTreatment.getDate());
        holder.nameTreatmentItem.setText(currentTreatment.getName());
        holder.manufacturerTreatmentItem.setText(currentTreatment.getManufacturer());
        holder.veterinarianTreatmentItem.setText(currentTreatment.getVeterinarian());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClickListener(differ.getCurrentList().get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onItemLongClickListener(
                        differ.getCurrentList().get(holder.getAdapterPosition()),
                        holder.treatmentContainer
                );
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.differ.getCurrentList().size();
    }

    private ItemClickListener clickListener;
    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
