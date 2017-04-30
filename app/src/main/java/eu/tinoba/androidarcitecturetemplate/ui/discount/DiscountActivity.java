package eu.tinoba.androidarcitecturetemplate.ui.discount;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.tinoba.androidarcitecturetemplate.R;
import eu.tinoba.androidarcitecturetemplate.domain.models.Discount;

public class DiscountActivity extends AppCompatActivity {

    @BindView(R.id.activity_discount_list_of_products)
    RecyclerView recyclerViewDiscounts;


    DiscountActivityRecyclerViewAdapter recyclerViewAdapter;

    List<Discount> discountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);

        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Akcije");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        recyclerViewAdapter = new DiscountActivityRecyclerViewAdapter();

        recyclerViewDiscounts.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewDiscounts.setAdapter(recyclerViewAdapter);

        //TODO REMOVE TEST DATA

        discountList.add(new Discount("Staročeško 0,5l", "6,69kn", "4,99kn", "Najfinije pivo hehe", "Akcija vrijedi do 09.05.2017", R.drawable.ic_starocesko, "-25%"));
        discountList.add(new Discount("Pšenično glatko prašno 5kg", "18.99kn", "12,99kn", "Kvalitetno brašno", "Akcija vrijedi do 09.05.2017", R.drawable.ic_brasno, "-31%"));
        discountList.add(new Discount("Faks natural sensitive deterdžent 1,4 kg", "39,99kn", "29,99kn", "Ma pere sve živo", "Akcija vrijedi do 15.05.2017", R.drawable.ic_faks, "-25%"));
        discountList.add(new Discount("Jacobs Cronat Gold kava 200 g", "64,99kn", "60,49kn", "Zlatna kavica", "Akcija vrijedi do 10.06.2017", R.drawable.ic_kava, "-6%"));
        discountList.add(new Discount("Iso Sport limun grejp 1,75 l", "11,99kn", "8,99kn", "Negazirani napitak", "Akcija vrijedi do 09.05.2017", R.drawable.ic_sok, "-25%"));
        discountList.add(new Discount("Z bregov trajno mlijeko 2,8% m.m. 1 l", "6,39kn", "5,39kn", "Mlijeko ko mlijeko", "Akcija vrijedi do 04.05.2017", R.drawable.ic_mlijeko, "-15%"));

        recyclerViewAdapter.setData(discountList);

    }
}
