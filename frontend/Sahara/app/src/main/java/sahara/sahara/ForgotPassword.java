package sahara.sahara;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Forgotten Password");

        final EditText email = (EditText) findViewById(R.id.r_email);
        final EditText oldPass = (EditText) findViewById(R.id.r_password2);
        final EditText newPass = (EditText) findViewById(R.id.r_password);
        final Button changePass = (Button) findViewById(R.id.register);
        changePass.setText("CHANGE PASSWORD");

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(email.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Email field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Email must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (email.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Email must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEmpty(newPass.getText().toString().trim()) || isEmpty(oldPass.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password field can not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (newPass.getText().toString().trim().length() < 8 || oldPass.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must be more than 8 characters!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (newPass.getText().toString().trim().length() > 50 || oldPass.getText().toString().trim().length() > 50) {
                    Toast.makeText(getApplicationContext(), "Password must be less than 50 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ChangePassword(email.getText().toString().trim(), oldPass.getText().toString().trim(), newPass.getText().toString().trim());
            }
            public void ChangePassword(final String userEmail, final String oldPassword, final String newPassword) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST,
                        Constants.URL_FORGOTPASSWORD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (!jsonObject.getBoolean("error")) {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Password Changed Successfully!",
                                                Toast.LENGTH_SHORT
                                        ).show();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                        finish();
                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Password Changed Unsucessfully!",
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
                                if (error.getMessage() != null) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Check your connection or contact the administrator.",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {  // Script returned a message, now we make does variables.
                        Map<String, String> params = new HashMap<>();
                        params.put("u_email", userEmail);
                        params.put("old_upword", oldPassword);
                        params.put("new_upword", newPassword);
                        return params;
                    }
                };
                // This request handler keeps the internet connection until logout... Instead of attempt everytime.
                RequestHandler.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        });
    }
}
