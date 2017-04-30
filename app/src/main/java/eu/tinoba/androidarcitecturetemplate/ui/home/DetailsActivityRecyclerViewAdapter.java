package eu.tinoba.androidarcitecturetemplate.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.data.api.models.response.HistoryApiResponse;

public class DetailsActivityRecyclerViewAdapter extends RecyclerView.Adapter<DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder> {

    List<HistoryApiResponse.Products> productsList = new ArrayList<>();

    Context context;
    public DetailsActivityRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_details_item, parent, false);
        return new DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder holder, final int position) {
        holder.productName.setText(productsList.get(position).name);
        holder.productCount.setText(String.valueOf(productsList.get(position).quantity));
        holder.productDescription.setText(productsList.get(position).description);
        //holder.productPrice.setText(String.valueOf(productsList.get(position).getPrice() + " kn"));
        Picasso.with(context)
               .load(productsList.get(position).url)
               .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public void setData(List<HistoryApiResponse.Products> data) {
        productsList.clear();
        productsList.addAll(data);
        notifyDataSetChanged();
    }

    public class DetailsHistoryPlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.product_name)
        protected TextView productName;

        @BindView(R.id.quantity_edit)
        protected TextView productCount;

        @BindView(R.id.product_description)
        TextView productDescription;

        @BindView(R.id.product_image)
        ImageView productImage;

        @BindView(R.id.product_price)
        TextView productPrice;

        public DetailsHistoryPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
