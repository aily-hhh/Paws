package com.hhh.paws.ui.petProfile.menu.dehelmintization;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hhh.paws.R;
import com.hhh.paws.database.model.Dehelmintization;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;

import java.util.Objects;

public class DehelmintizationAdapter extends RecyclerView.Adapter<DehelmintizationAdapter.DehelmintizationViewHolder>{

    static class DehelmintizationViewHolder extends RecyclerView.ViewHolder {

        public DehelmintizationViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        TextView nameDehelmintizationItem = itemView.findViewById(R.id.nameDehelmintizationItem);
        TextView timeDehelmintizationItem = itemView.findViewById(R.id.timeDehelmintizationItem);
        TextView manufacturerDehelmintizationItem = itemView.findViewById(R.id.manufacturerDehelmintizationItem);
        TextView doseDehelmintizationItem = itemView.findViewById(R.id.doseDehelmintizationItem);
        TextView veterinarianDehelmintizationItem = itemView.findViewById(R.id.veterinarianDehelmintizationItem);

    }

    public final AsyncListDiffer<Dehelmintization> differ = new AsyncListDiffer<Dehelmintization>(this, callback);
    private static final DiffUtil.ItemCallback<Dehelmintization> callback = new DiffUtil.ItemCallback<Dehelmintization>() {
        @Override
        public boolean areItemsTheSame(@NonNull Dehelmintization oldItem, @NonNull Dehelmintization newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Dehelmintization oldItem, @NonNull Dehelmintization newItem) {
            if (!Objects.equals(oldItem.getId(), newItem.getId()))
                return false;
            else if (!Objects.equals(oldItem.getDate(), newItem.getDate()))
                return false;
            else if (!Objects.equals(oldItem.getDescription(), newItem.getDescription()))
                return false;
            else if (!Objects.equals(oldItem.getDose(), newItem.getDose()))
                return false;
            else if (!Objects.equals(oldItem.getManufacturer(), newItem.getManufacturer()))
                return false;
            else if (!Objects.equals(oldItem.getName(), newItem.getName()))
                return false;
            else if (!Objects.equals(oldItem.getTime(), newItem.getTime()))
                return false;
            else return Objects.equals(oldItem.getVeterinarian(), newItem.getVeterinarian());
        }
    };

    @NonNull
    @Override
    public DehelmintizationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DehelmintizationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dehelmintization_item, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DehelmintizationViewHolder holder, int position) {
        Dehelmintization newDehelmintization = differ.getCurrentList().get(position);
        holder.nameDehelmintizationItem.setText(newDehelmintization.getName());
        holder.doseDehelmintizationItem.setText(newDehelmintization.getDose());
        holder.timeDehelmintizationItem.setText(newDehelmintization.getDate() + ", " + newDehelmintization.getTime());
        holder.manufacturerDehelmintizationItem.setText(newDehelmintization.getManufacturer());
        holder.veterinarianDehelmintizationItem.setText(newDehelmintization.getVeterinarian());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClickListener(v);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onItemLongClickListener(v);
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
