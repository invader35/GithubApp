package at.schett.githubexample.githubapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import at.schett.githubexample.githubapp.model.Repository;
import at.schett.githubexample.githubapp.restapi.ApiClient;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RepositoryActivity extends Activity {

    private List<Card> cardList;
    private CardListView cardListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        cardListView = (CardListView) findViewById(R.id.cardList);
        cardList = new ArrayList<>();
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        ApiClient.getInstance().getApiClient().getRepositoriesForUser(username, new RepositoryCallback());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.repository, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RepositoryCallback implements Callback<List<Repository>> {

        @Override
        public void success(List<Repository> repository, Response response) {
            for(Repository repo : repository) {
                Card card = new Card(getApplicationContext());
                CardHeader header = new CardHeader(getApplicationContext());
                CardExpand expand = new CardExpand(getApplicationContext());
                header.setTitle(repo.getName());
                header.setButtonExpandVisible(true);
                expand.setTitle(repo.getDescription());
                card.addCardHeader(header);
                card.addCardExpand(expand);
                cardList.add(card);
            }
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getApplicationContext(), cardList);
            cardListView.setAdapter(mCardArrayAdapter);
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error during request", Toast.LENGTH_SHORT)
                    .show();
            Card card = new Card(getApplicationContext());
            CardHeader header = new CardHeader(getApplicationContext());
            header.setTitle("no results fetched");
            card.addCardHeader(header);
            cardList.add(card);
            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getApplicationContext(), cardList);

            cardListView.setAdapter(mCardArrayAdapter);
        }
    }
}
