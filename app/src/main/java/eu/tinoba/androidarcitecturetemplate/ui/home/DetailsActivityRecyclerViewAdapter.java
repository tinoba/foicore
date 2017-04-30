package eu.tinoba.androidarcitecturetemplate.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Discount;

public class DetailsActivityRecyclerViewAdapter extends RecyclerView.Adapter<DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder> {

    List<Discount> discountList = new ArrayList<>();

    @Override
    public DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_details_item, parent, false);
        return new DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DetailsActivityRecyclerViewAdapter.DetailsHistoryPlanViewHolder holder, final int position) {
       // holder.productName.setText(discountList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public void setData(List<Discount> data) {
        discountList.clear();
        discountList.addAll(data);
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
