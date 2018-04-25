package sahara.sahara;

import android.app.SearchManager;
import android.provider.MediaStore;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentHistory extends AppCompatActivity implements ProductAdapter.ItemClickListener {

    private RadioButton radioButtonAsc = (RadioButton) findViewById(R.id.sortAsc);
    private RadioButton radioButtonDsc = (RadioButton) findViewById(R.id.sortDsc);
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> data = new ArrayList<>();
    private int ascending;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                if(ascending == 1) {
                    sortData(data, query, 1, "ASC");
                }
                else if (ascending == 0) {
                    sortData(data, query, 1, "DESC");
                }
                else {
                    sortData(data, query, 0, "");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(ascending == 1) {
                    sortData(data, s, 1, "ASC");
                }
                else if (ascending == 0) {
                    sortData(data, s, 1, "DESC");
                }
                else {
                    sortData(data, s, 0, "");
                }
                return false;
            }
        });
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        setTitle("Sahara");
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addHistoryProducts(data);
        mAdapter = new ProductAdapter(this, data);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        ascending = -1;
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch(v.getId()) {
            case R.id.sortAsc:
                if(checked) {
                    ascending = 1;
                }
                break;
            case R.id.sortDsc:
                if(checked) {
                    ascending = 0;
                }
        }
    }

    @Override
    public void onItemClick(View view, final int position) {

    }

    public void sortData(final ArrayList<Product> p, final String query, final int sort, final String sortType) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_HISTORYCARTSEARCH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                p.clear();
                                JSONArray ja = new JSONArray(jsonObject.getString("data"));
                                for(int i = 0; i < ja.length(); i++)
                                {
                                    JSONObject jo = ja.getJSONObject(i);
                                    p.add(new Product(jo.getString("p_name"), Float.parseFloat(jo.getString("p_price")), jo.getString("c_name"),
                                            jo.getString("pr_recid"), jo.getString("p_recid"), jo.getInt("ph_quantity")));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // If listener listens, then we had an error and we will display the msg from db.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.getMessage() != null) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Check your connection or contact the administrator.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("u_email", PreferenceManager.getInstance(getApplicationContext()).getLogin());
                params.put("query", query);
                params.put("sort", Integer.toString(sort));
                params.put("type",sortType);
                return params;
            }
        };
        // This request handler keeps the internet connection until logout... Instead of attempt everytime.
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void addHistoryProducts(final ArrayList<Product> p) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SHOPPINGHISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                p.clear();
                                JSONArray ja = new JSONArray(jsonObject.getString("data"));
                                for(int i = 0; i < ja.length(); i++)
                                {
                                    JSONObject jo = ja.getJSONObject(i);
                                    p.add(new Product(jo.getString("p_name"), Float.parseFloat(jo.getString("p_price")), jo.getString("c_name"),
                                            jo.getString("pr_recid"), jo.getString("p_recid"), jo.getInt("ph_quantity")));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() { // If listener listens, then we had an error and we will display the msg from db.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.getMessage() != null) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Check your connection or contact the administrator.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("u_email", PreferenceManager.getInstance(getApplicationContext()).getLogin());
                return params;
            }
        };
        // This request handler keeps the internet connection until logout... Instead of attempt everytime.
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
