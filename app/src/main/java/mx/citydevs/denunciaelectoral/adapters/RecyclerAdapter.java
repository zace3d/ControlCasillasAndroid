package mx.citydevs.denunciaelectoral.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import mx.citydevs.denunciaelectoral.R;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/27/15.
 */
public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private List<ComplaintType> items;
    private OnItemClickListener onItemClickListener;

    protected class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CustomTextView description;

        public RecyclerViewHolder(View view) {
            super(view);

            description = (CustomTextView) view.findViewById(R.id.recycler_tv_description);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getPosition());
        }
    }

    public RecyclerAdapter(List<ComplaintType> items) {
        this.items = items;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int id) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.description.setText(items.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null)
            this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}