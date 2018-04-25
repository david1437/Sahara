package sahara.sahara;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProducerMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producer_main);
        setTitle("Add Product");

        final Spinner spinner = (Spinner)findViewById(R.id.categoryDropDown);
        ArrayList<String> items = new ArrayList<>();
        addItems(items);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(ProducerMainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addItems(final ArrayList<String> items) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Constants.URL_GETCATEGORIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")){
                                items.clear();
                                JSONArray ja = new JSONArray(jsonObject.getString("data"));
                                for(int i = 0; i < ja.length(); i++)
                                {
                                    JSONObject jo = ja.getJSONObject(i);
                                    items.add(jo.getString("c_name"));
                                }
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
                });
        // This request handler keeps the internet connection until logout... Instead of attempt everytime.
        RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
