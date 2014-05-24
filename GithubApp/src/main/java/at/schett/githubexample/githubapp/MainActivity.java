package at.schett.githubexample.githubapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jpardogo.android.googleprogressbar.library.FoldingCirclesDrawable;
import com.quentindommerc.superlistview.SuperListview;

import java.util.ArrayList;
import java.util.List;

import at.schett.githubexample.githubapp.model.User;
import at.schett.githubexample.githubapp.restapi.ApiClient;
import at.schett.githubexample.githubapp.restapi.GitHubApiService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends Activity {

    private SuperListview listView;
    private ArrayAdapter<String> adapter;
    private String selectedUser;
    private ProgressBar mProgressBar;
    private int ProgressId;
    public final static String EXTRA_MESSAGE = "at.schett.githubexample.githubapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (SuperListview) findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_black_text, R.id.list_content);

        ApiClient.getInstance().getApiClient().getUsers(new UserCallBack());

        listView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ApiClient.getInstance().getApiClient().getUsers(new UserCallBack());
                adapter.clear();
                listView.showProgress();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RepositoryActivity.class);
                selectedUser = (String) adapterView.getItemAtPosition(i);
                intent.putExtra(EXTRA_MESSAGE, selectedUser);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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


    private class UserCallBack implements Callback<List<User>> {
        @Override
        public void success(List<User> users, Response response) {
            for (User user : users) {
                adapter.add(user.getLogin());
            }
            listView.setAdapter(adapter);
            listView.hideProgress();
        }

        @Override
        public void failure(RetrofitError error) {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error during request", Toast.LENGTH_SHORT)
                    .show();
            adapter.add("No results were fetched");
        }
    }
}
