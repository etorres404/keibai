package io.github.keibai.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.keibai.R;
import io.github.keibai.auction.Transaction;
import io.github.keibai.auction.TransactionAdapter;

public class ActivityBidFragment extends Fragment{

    public ActivityBidFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activities_bid, container, false);
        ListView listView = view.findViewById(R.id.activities_bid_list);

        // TODO: Change in next sprint, transactions will be retrieved using the API
        List<Transaction> transactions = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        transactions.add(new Transaction("You", 10.31f,
                calendar, "Auction1", Transaction.BID_MESSAGE));
        transactions.add(new Transaction("You", 10.45f,
                calendar, "Auction 2", Transaction.BID_MESSAGE));
        transactions.add(new Transaction("You", 12.31f,
                calendar, "Auction 3", Transaction.BID_MESSAGE));
        transactions.add(new Transaction("You", 15.31f,
                calendar, "Auction 4", Transaction.BID_MESSAGE));

        TransactionAdapter transactionAdapter = new TransactionAdapter(getContext(), transactions);
        listView.setAdapter(transactionAdapter);

        return view;
    }

}