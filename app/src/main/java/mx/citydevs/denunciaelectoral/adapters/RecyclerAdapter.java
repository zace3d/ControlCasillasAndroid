package mx.citydevs.denunciaelectoral.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import mx.citydevs.denunciaelectoral.R;
import mx.citydevs.denunciaelectoral.beans.CategoriesType;
import mx.citydevs.denunciaelectoral.beans.ComplaintType;
import mx.citydevs.denunciaelectoral.views.CustomTextView;

/**
 * Created by zace3d on 5/27/15.
 */
public class RecyclerAdapter  extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private final Context context;
    private final int categoryId;
    private List<ComplaintType> items;
    private OnItemClickListener onItemClickListener;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    protected class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public int holderId;

        public CustomTextView description;

        public CustomTextView title;
        public ImageView icon;

        public RecyclerViewHolder(View view, int viewType) {
            super(view);

            if (viewType == TYPE_ITEM) {
                description = (CustomTextView) view.findViewById(R.id.recycler_tv_description);
                view.setOnClickListener(this);
                holderId = 1;
            } else {
                title = (CustomTextView) view.findViewById(R.id.header_title);
                icon = (ImageView) view.findViewById(R.id.header_icon);
                holderId = 0;
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, getAdapterPosition() - 1);
        }
    }

    public RecyclerAdapter(Context context, List<ComplaintType> items, int categoryId) {
        this.context = context;
        this.items = items;
        this.categoryId = categoryId;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
            return new RecyclerViewHolder(v, viewType);

        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_recycler, parent, false);
            return new RecyclerViewHolder(v, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (holder.holderId == 1) {
            holder.description.setText(items.get(position - 1).getDescription());

        } else if (holder.holderId == TYPE_HEADER) {
            if (items.get(position).getCategory() != null) {
                if (categoryId == CategoriesType.CIUDADANO_ID) {
                    holder.title.setText(context.getString(R.string.label_un_ciudadano));
                    holder.icon.setImageResource(R.drawable.ic_ciudadano);

                } else if (categoryId == CategoriesType.FUNCIONARIO_ID) {
                    holder.title.setText(context.getString(R.string.label_un_funcionario));
                    holder.icon.setImageResource(R.drawable.ic_funcionario);

                } else if (categoryId == CategoriesType.CANDIDATO_ID) {
                    holder.title.setText(context.getString(R.string.label_un_candidato));
                    holder.icon.setImageResource(R.drawable.ic_candidato);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null)
            this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}