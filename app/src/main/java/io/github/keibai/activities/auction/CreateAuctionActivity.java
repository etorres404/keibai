package io.github.keibai.activities.auction;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import io.github.keibai.R;
import io.github.keibai.SaveSharedPreference;
import io.github.keibai.activities.event.DetailEventActivity;
import io.github.keibai.form.DefaultAwesomeValidation;
import io.github.keibai.http.Http;
import io.github.keibai.http.HttpCallback;
import io.github.keibai.http.HttpUrl;
import io.github.keibai.models.Auction;
import io.github.keibai.models.Good;
import io.github.keibai.models.meta.Error;
import io.github.keibai.runnable.RunnableToast;
import okhttp3.Call;

public class CreateAuctionActivity extends AppCompatActivity {

    private int eventId;
    private DefaultAwesomeValidation validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_auction);

        Toolbar toolbar = findViewById(R.id.toolbar_create_event);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        eventId = intent.getIntExtra(DetailEventActivity.EXTRA_EVENT_ID, -1);

        validation = new DefaultAwesomeValidation(getApplicationContext());
        validation.addValidation(this, R.id.edit_create_auction_name, "[a-zA-Z0-9\\s]+", R.string.auction_name_invalid);
        validation.addValidation(this, R.id.edit_create_auction_starting_price, "[0-9\\.]+", R.string.starting_price_invalid);
        validation.addValidation(this, R.id.edit_create_auction_good_name, "[a-zA-Z0-9\\s]+", R.string.good_name_invalid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_auction_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_create_auction_save:
                onSave();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Auction auctionFromForm() {
        EditText formName = findViewById(R.id.edit_create_auction_name);
        EditText formStartingPrice = findViewById(R.id.edit_create_auction_starting_price);

        Auction auction = new Auction();
        auction.name = formName.getText().toString();
        auction.startingPrice = Float.parseFloat(formStartingPrice.getText().toString());

        return auction;
    }

    public Good goodFromForm() {
        EditText formName = findViewById(R.id.edit_create_auction_good_name);

        Good good = new Good();
        good.name = formName.getText().toString();

        return good;
    }

    private void onSave() {
        if (eventId == -1) {
            Toast.makeText(getApplicationContext(), R.string.event_id_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validation.validate()) {
            return;
        }

        Toast.makeText(getApplicationContext(), R.string.submitting, Toast.LENGTH_SHORT).show();

        Auction attemptAuction = auctionFromForm();
        attemptAuction.eventId = eventId;
        new Http(getApplicationContext()).post(HttpUrl.newAuctionUrl(), attemptAuction, new HttpCallback<Auction>(Auction.class) {
            @Override
            public void onError(Error error) throws IOException {
                runOnUiThread(new RunnableToast(getApplicationContext(), error.toString()));
            }

            @Override
            public void onSuccess(final Auction response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        saveGood(response.id);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new RunnableToast(getApplicationContext(), e.toString()));
            }
        });
    }

    private void saveGood(int auctionId) {
        if (!validation.validate()) {
            return;
        }

        Toast.makeText(getApplicationContext(), R.string.submitting, Toast.LENGTH_SHORT).show();
        Good attemptGood = goodFromForm();
        attemptGood.auctionId = auctionId;
        attemptGood.image= "1234";
        new Http(getApplicationContext()).post(HttpUrl.newGoodUrl(), attemptGood, new HttpCallback<Good>(Good.class) {
            @Override
            public void onError(Error error) throws IOException {
                runOnUiThread(new RunnableToast(getApplicationContext(), error.toString()));
            }

            @Override
            public void onSuccess(Good response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Auction successfully created", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new RunnableToast(getApplicationContext(), e.toString()));
            }
        });
    }
}
