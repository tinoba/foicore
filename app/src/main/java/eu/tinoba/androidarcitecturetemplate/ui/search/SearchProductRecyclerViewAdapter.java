package eu.tinoba.androidarcitecturetemplate.ui.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Product;

public class SearchProductRecyclerViewAdapter extends RecyclerView.Adapter<SearchProductRecyclerViewAdapter.SearchProductViewHolder> {

    public interface Listener {

        Listener EMPTY = new EmptyListener();

        void getProduct(Product product);
    }

    private Listener listener = Listener.EMPTY;

    List<Product> productList = new ArrayList<>();

    @Override
    public SearchProductRecyclerViewAdapter.SearchProductViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new SearchProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SearchProductRecyclerViewAdapter.SearchProductViewHolder holder, final int position) {
        holder.searchProductName.setText(productList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setData(List<Product> data) {
        productList.clear();
        productList.addAll(data);
        notifyDataSetChanged();
    }

    public void setListener(final Listener listener) {
        this.listener = listener != null ? listener : Listener.EMPTY;
    }

    public class SearchProductViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.search_product_name)
        protected TextView searchProductName;

        @OnClick(R.id.search_product_name)
        public void onItemClicked() {
            listener.getProduct(productList.get(getAdapterPosition()));
        }

        public SearchProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static final class EmptyListener implements Listener {

        @Override
        public void getProduct(final Product product) {
            //NO OP
        }
    }
}
