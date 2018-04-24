package sahara.sahara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

public class Checkout extends AppCompatActivity implements ProductAdapter.ItemClickListener {

    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Product> data = new ArrayList<>();
    private TextView subtotal;
    private TextView taxes;
    private TextView shipping;
    private TextView total;
    private Button cancel;
    private Button buy;
    private float sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        addShoppingCart(data);
        mAdapter = new ProductAdapter(this, data);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        subtotal = (TextView) findViewById(R.id.subtotal);
        taxes = (TextView) findViewById(R.id.tax);
        shipping = (TextView) findViewById(R.id.shipping);
        total = (TextView) findViewById(R.id.total);
        cancel = (Button) findViewById(R.id.cancel);
        buy = (Button) findViewById(R.id.placeOrder);
        sum = 0;

        getShipping();
        getPrices();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(), MainActivity.class));
                // finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    public void getShipping() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GETSHIPPING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                shipping.setText(jsonObject.getString("data"));
                                sum += Float.parseFloat(jsonObject.getString("data"));
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

    public void getPrices() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_GETPRICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                taxes.setText(jsonObject.getString("taxes"));
                                subtotal.setText(jsonObject.getString("price"));
                                sum += Float.parseFloat(jsonObject.getString("taxes")) + Float.parseFloat(jsonObject.getString("price"));
                                total.setText(Float.toString(sum));
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

    @Override
    public void onItemClick(View view, final int position) {

    }

    public void addShoppingCart(final ArrayList<Product> p) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SHOPPINGCARTDATA,
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
                                            jo.getString("pr_recid"), jo.getString("p_recid"), jo.getInt("sc_quantity")));
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Failed to fetch shopping cart!",
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
