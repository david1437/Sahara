package sahara.sahara;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements ProductAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> data = new ArrayList<>();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.shopping_cart:
                startActivity(new Intent(getApplicationContext(), ShoppingCart.class));
                return true;
            case R.id.shopping_history:
                return true;
            case R.id.update_information:
                Intent i = new Intent(getApplicationContext(), UserInfo.class);
                i.putExtra("EMAIL", PreferenceManager.getInstance(getApplicationContext()).getLogin());
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        getMenuInflater().inflate(R.menu.options, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if( ! searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                searchProducts(query, data);
                mAdapter.notifyDataSetChanged();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                searchProducts(s, data);
                mAdapter.notifyDataSetChanged();
                return false;
            }
            public void searchProducts(final String q, final ArrayList<Product> p) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_SEARCHPRODUCT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")){
                                        p.clear();
                                        JSONArray ja = new JSONArray(jsonObject.getString("products"));
                                        for(int i = 0; i < ja.length(); i++)
                                        {
                                            JSONObject jo = ja.getJSONObject(i);
                                            p.add(new Product(jo.getString("p_name"), Float.parseFloat(jo.getString("p_price")), jo.getString("c_name"),
                                                    jo.getString("pr_recid"), jo.getString("p_recid"), jo.getInt("pri_quantity")));
                                        }
                                        mAdapter.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Failed to fetch new products!",
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
                        params.put("query", q);
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addAllProducts(data);
        mAdapter = new ProductAdapter(this, data);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Would you like to add " + mAdapter.getItem(position).title + " to your shopping cart?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToCart(mAdapter.getItem(position));
                dialog.cancel();
            }
            public void addToCart(final Product p) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_ADDTOCART,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")){
                                        Toast.makeText(
                                                getApplicationContext(),
                                                p.title + " added to cart!",
                                                Toast.LENGTH_LONG
                                        ).show();
                                        addAllProducts(data);
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
                        params.put("p_recid", p.productId);
                        params.put("pr_recid", p.producerId);
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addAllProducts(final ArrayList<Product> p) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.URL_ALLPRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                p.clear();
                                JSONArray ja = new JSONArray(jsonObject.getString("products"));
                                for(int i = 0; i < ja.length(); i++)
                                {
                                    JSONObject jo = ja.getJSONObject(i);
                                    p.add(new Product(jo.getString("p_name"), Float.parseFloat(jo.getString("p_price")), jo.getString("c_name"),
                                            jo.getString("pr_recid"), jo.getString("p_recid"), jo.getInt("pri_quantity")));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Failed to fetch new products!",
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
                });
        // This request handler keeps the internet connection until logout... Instead of attempt everytime.
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
}