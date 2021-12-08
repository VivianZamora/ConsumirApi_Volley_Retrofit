package com.example.consumirapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.IconMarginSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consumirapi.Interfaces.UsuarioApi;
import com.example.consumirapi.Models.ListaUsuario;
import com.example.consumirapi.Models.Usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.file.attribute.AclEntry;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView tvInformacion;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInformacion = findViewById(R.id.TvLista);
        requestQueue = Volley.newRequestQueue(this);
    }

    public void btEnviar(View view) {
        String Api = "";
        RadioButton Retro = (RadioButton) findViewById(R.id.RbRetrofil);

        if (Retro.isChecked()) {
            CasoRetro();
        } else {
            CasoVolley();

        }
    }


    private void CasoRetro() {
        tvInformacion.setText("");
        tvInformacion.append("RETROFIT \n");
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://gorest.co.in/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        UsuarioApi usuarioApi = retrofit.create(UsuarioApi.class);
        Call<ListaUsuario> call = usuarioApi.Listar();
        call.enqueue(new Callback<ListaUsuario>() {
            @Override
            public void onResponse(Call<ListaUsuario> call, Response<ListaUsuario> response) {
                try {
                    if (response.isSuccessful()) {

                        ListaUsuario R = response.body();
                        List<Usuarios> UsuList = R.getData();
                        //tvInformacion.setText("codigo: "+ response.code());
                        for (Usuarios usu : UsuList) {
                            String contentido = "";
                            contentido += ("id:" + usu.getId() + "; ");
                            contentido += ("Nombre:" + usu.getName() + "; ");
                            contentido += ("Email:" + usu.getEmail() + "; ");
                            contentido += ("Gender:" + usu.getGender() + "; ");
                            contentido += ("Estado:" + usu.getStatus() + ";\n");
                            tvInformacion.append(contentido);
                        }
                        return;


                    }
                } catch (Exception ex) {
                    // Toast.makeText(MainActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).

                }

            }

            @Override
            public void onFailure(Call<ListaUsuario> call, Throwable t) {

            }
        });


    }

    private void CasoVolley() {
        tvInformacion.setText("");
        tvInformacion.append("Google Volley \n");
        String url = "https://gorest.co.in/public/v1/users";
        JsonObjectRequest jsonR = new JsonObjectRequest(Request.Method.GET,
                url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               try {

                    JSONArray U = response.getJSONArray("data");
                    for (int i = 0; i < U.length(); i++) {

                        JSONObject info= new JSONObject(U.get(i).toString());
                        Usuarios usuario = new Usuarios(info.getString("id"),
                                info.getString("name"), info.getString("email"),
                                info.getString("gender"), info.getString("status"));
                        String contentido = "";
                        contentido += ("id:" + usuario.getId() + "; ");
                        contentido += ("Nombre:" + usuario.getName() + "; ");
                        contentido += ("Email:" + usuario.getEmail() + "; ");
                        contentido += ("Gender:" + usuario.getGender() + "; ");
                        contentido += ("Estado:" + usuario.getStatus() + "; \n");
                        tvInformacion.append(contentido);
                    }
                    return;
                } catch (Exception e) {
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonR);
    }
}
