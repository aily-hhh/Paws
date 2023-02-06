package com.hhh.paws.ui.petProfile.menu.procedures;

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
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProceduresAdapter extends RecyclerView.Adapter<ProceduresAdapter.ProceduresViewHolder> {

    static class ProceduresViewHolder extends RecyclerView.ViewHolder {

        public ProceduresViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        CardView surgicalProceduresContainer = itemView.findViewById(R.id.surgicalProceduresContainer);
        TextView typeSurgicalProcedureItem = itemView.findViewById(R.id.typeSurgicalProcedureItem);
        TextView nameSurgicalProcedureItem = itemView.findViewById(R.id.nameSurgicalProcedureItem);
        TextView dateSurgicalProcedureItem = itemView.findViewById(R.id.dateSurgicalProcedureItem);
        TextView anesthesiaSurgicalProcedureItem = itemView.findViewById(R.id.anesthesiaSurgicalProcedureItem);
        TextView veterinarianSurgicalProcedureItem = itemView.findViewById(R.id.veterinarianSurgicalProcedureItem);

    }

    private List<SurgicalProcedure> oldList = new ArrayList<>();
    private final AsyncListDiffer<SurgicalProcedure> differ = new AsyncListDiffer<SurgicalProcedure>(
            this, callback
    );
    private static final DiffUtil.ItemCallback<SurgicalProcedure> callback = new DiffUtil.ItemCallback<SurgicalProcedure>() {
        @Override
        public boolean areItemsTheSame(@NonNull SurgicalProcedure oldItem, @NonNull SurgicalProcedure newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SurgicalProcedure oldItem, @NonNull SurgicalProcedure newItem) {
            if (!Objects.equals(oldItem.getId(), newItem.getId())) {
                return false;
            } else if (!Objects.equals(oldItem.getAnesthesia(), newItem.getAnesthesia())) {
                return false;
            } else if (!Objects.equals(oldItem.getName(), newItem.getName())) {
                return false;
            } else if (!Objects.equals(oldItem.getDate(), newItem.getDate())) {
                return false;
            } else if (!Objects.equals(oldItem.getDescription(), newItem.getDescription())) {
                return false;
            } else if (!Objects.equals(oldItem.getType(), newItem.getType())) {
                return false;
            } else {
                return Objects.equals(oldItem.getVeterinarian(), newItem.getVeterinarian());
            }
        }
    };

    public void setDiffer(List<SurgicalProcedure> procedureList) {
        this.oldList.clear();
        this.oldList = procedureList;
        this.differ.submitList(oldList);
    }

    public void removeItemFromDiffer(SurgicalProcedure procedure) {
        this.oldList.remove(procedure);
        differ.submitList(oldList);
    }

    @NonNull
    @Override
    public ProceduresViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProceduresViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.procedure_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProceduresViewHolder holder, int position) {
        SurgicalProcedure procedure = differ.getCurrentList().get(position);
        holder.typeSurgicalProcedureItem.setText(procedure.getType());
        holder.anesthesiaSurgicalProcedureItem.setText(procedure.getAnesthesia());
        holder.nameSurgicalProcedureItem.setText(procedure.getName());
        holder.dateSurgicalProcedureItem.setText(procedure.getDate());
        holder.veterinarianSurgicalProcedureItem.setText(procedure.getVeterinarian());

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
                        holder.surgicalProceduresContainer
                );
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    private ItemClickListener clickListener;
    public void setClickListener(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
