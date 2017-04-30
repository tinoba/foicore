package eu.tinoba.androidarcitecturetemplate.ui.discount;

import android.graphics.Paint;
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

public class DiscountActivityRecyclerViewAdapter extends RecyclerView.Adapter<DiscountActivityRecyclerViewAdapter.DiscountPlanViewHolder> {

    List<Discount> discountList = new ArrayList<>();

    @Override
    public DiscountActivityRecyclerViewAdapter.DiscountPlanViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discount_item, parent, false);
        return new DiscountActivityRecyclerViewAdapter.DiscountPlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DiscountActivityRecyclerViewAdapter.DiscountPlanViewHolder holder, final int position) {
        holder.productName.setText(discountList.get(position).getName());
        holder.productDescription.setText(discountList.get(position).getDescription());
        holder.discount.setText(discountList.get(position).getDiscountPercentage());
        holder.productPrice.setText(discountList.get(position).getOldPrice());
        holder.productPriceNew.setText(discountList.get(position).getNewPrice());
        holder.discountTillDate.setText(discountList.get(position).getDiscountTillDate());
        holder.productImage.setImageResource(discountList.get(position).getImageId());
        holder.productPrice.setPaintFlags(holder.productPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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

    public class DiscountPlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.discount_product_name)
        protected TextView productName;

        @BindView(R.id.discount_product_price)
        protected TextView productPrice;

        @BindView(R.id.discount_product_price_new)
        protected TextView productPriceNew;

        @BindView(R.id.discount_discount_till_date)
        protected TextView discountTillDate;

        @BindView(R.id.discount_product_description)
        protected TextView productDescription;

        @BindView(R.id.discount_product_image)
        protected ImageView productImage;

        @BindView(R.id.discount_discount)
        protected TextView discount;

        public DiscountPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
