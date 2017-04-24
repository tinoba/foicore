package eu.tinoba.androidarcitecturetemplate.ui.cart;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

        void countChanged();
    }

    private final Context context;

    public CartListAdapter(final Context context) {
        this.context = context;
    }

    private Listener listener = Listener.EMPTY;

    final List<Product> products = new ArrayList<>();

    @Override
    public CartListAdapter.PlanViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new PlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CartListAdapter.PlanViewHolder holder, final int position) {
        holder.productName.setText(products.get(position).getName());
        holder.productCount.setText(String.valueOf(products.get(position).getCount()));
        holder.productDescription.setText(products.get(position).getDescription());
        holder.productPrice.setText(String.valueOf(products.get(position).getPrice() + " kn"));
        Picasso.with(context)
               .load(products.get(position).getImageUrl())
               .into(holder.productImage);

        holder.buttonAdd.setOnClickListener(v -> {
            products.get(position).increaseCount();
            notifyDataSetChanged();
            listener.countChanged();
        });

        holder.buttonRemove.setOnClickListener(v -> {
            if (products.get(position).getCount() == 1) {
                listener.openRemoveDialog(position);
            } else {
                products.get(position).decreaseCount();
                listener.countChanged();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setData(final List<Product> data) {
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

        @BindView(R.id.quantity_edit)
        protected TextView productCount;

        @BindView(R.id.button_add)
        ImageButton buttonAdd;

        @BindView(R.id.button_remove)
        ImageButton buttonRemove;

        @BindView(R.id.product_description)
        TextView productDescription;

        @BindView(R.id.product_image)
        ImageView productImage;

        @BindView(R.id.product_price)
        TextView productPrice;

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

        @Override
        public void countChanged() {
            //NO OP
        }
    }
}
