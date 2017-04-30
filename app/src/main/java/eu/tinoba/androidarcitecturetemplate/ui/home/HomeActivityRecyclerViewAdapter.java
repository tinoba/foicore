package eu.tinoba.androidarcitecturetemplate.ui.home;

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
import eu.tinoba.androidarcitecturetemplate.domain.models.HistoryPlan;

public class HomeActivityRecyclerViewAdapter extends RecyclerView.Adapter<HomeActivityRecyclerViewAdapter.HistoryPlanViewHolder> {

    public interface Listener {

        Listener EMPTY = new EmptyListener();

        void getHistoryId(String id);
    }

    private Listener listener = Listener.EMPTY;

    List<HistoryPlan> historyPlanList = new ArrayList<>();

    @Override
    public HomeActivityRecyclerViewAdapter.HistoryPlanViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_plan_item, parent, false);
        return new HistoryPlanViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final HomeActivityRecyclerViewAdapter.HistoryPlanViewHolder holder, final int position) {
        holder.shopName.setText(historyPlanList.get(position).getShopName());
        holder.shopAddress.setText(historyPlanList.get(position).getShopAddress());
        holder.dateOfShopping.setText(historyPlanList.get(position).getDateOfShopping());
        holder.priceOfPurhcase.setText(historyPlanList.get(position).getPriceOfPurchase());

    }

    @Override
    public int getItemCount() {
        return historyPlanList.size();
    }

    public void setData(List<HistoryPlan> data) {
        historyPlanList.clear();
        historyPlanList.addAll(data);
        notifyDataSetChanged();
    }

    public void setListener(final Listener listener) {
        this.listener = listener != null ? listener : Listener.EMPTY;
    }

    public class HistoryPlanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.shop_name)
        protected TextView shopName;

        @BindView(R.id.shop_address)
        protected TextView shopAddress;

        @BindView(R.id.date_of_shopping)
        protected TextView dateOfShopping;

        @BindView(R.id.price_of_purchase)
        protected TextView priceOfPurhcase;

        @OnClick(R.id.history_item_layout)
        public void onItemClicked() {
            listener.getHistoryId(historyPlanList.get(getAdapterPosition()).getHistoryId());
        }

        public HistoryPlanViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static final class EmptyListener implements Listener {

        @Override
        public void getHistoryId(String id) {
            //NO OP
        }
    }
}
