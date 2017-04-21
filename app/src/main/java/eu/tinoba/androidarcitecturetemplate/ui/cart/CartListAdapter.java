package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;

public final class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.PlanViewHolder> {

    public interface Listener {

        Listener EMPTY = new EmptyListener();

         void openRemoveDialog(int position);
    }

    private Listener listener = Listener.EMPTY;

    List<Product> products = new ArrayList<>();

    @Override
    public CartListAdapter.PlanViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
        return new PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CartListAdapter.PlanViewHolder holder, final int position) {
        holder.productName.setText(products.get(position).getName());
        holder.productCount.setText(String.valueOf(products.get(position).getCount()));

        holder.buttonAdd.setOnClickListener(v -> {
            products.get(position).increaseCount();
            notifyDataSetChanged();
        });

        holder.buttonRemove.setOnClickListener(v -> {
            if (products.get(position).getCount() == 1){
                listener.openRemoveDialog(position);
            }else {
                products.get(position).decreaseCount();
                notifyDataSetChanged();
            }

        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setData(List<Product> data) {
        products.clear();
        products.addAll(data);
        notifyDataSetChanged();
    }

    public void setListener(final Listener listener) {
        this.listener = listener != null ? listener : Listener.EMPTY;
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_name)
        protected TextView productName;

        @BindView(R.id.product_count)
        protected TextView productCount;

        @BindView(R.id.button_add)
        ImageButton buttonAdd;

        @BindView(R.id.button_remove)
        ImageButton buttonRemove;


        public PlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static final class EmptyListener implements Listener {

        @Override
        public void openRemoveDialog(int position) {
            //NO OP
        }
    }
}
