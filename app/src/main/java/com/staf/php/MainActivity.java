package com.staf.php;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //Button Enviar= (Button) findViewById(R.id.Enviar);
    }

    public void listadoOnClick(View view){
        // inicia la nueva activy listado al apretar boton listado (lista datos q envia servidor)
        startActivity(new Intent(this, ListadoActivity.class));


    }



    // boton enviar (dependiendo modo get o put seleccionado)
    public void EnviarOnClik(View view) {
        Thread nt = new Thread() {
            @Override
            public void run() {
                EditText nombre = (EditText) findViewById(R.id.et_nombre);
                EditText telefono = (EditText) findViewById(R.id.et_telefono);
                EditText email = (EditText) findViewById(R.id.et_email);
                //EditText observaciones = (EditText) findViewById(R.id.et_edad);
               // EditText observaciones = (EditText) findViewById(R.id.et_edad);
                CheckBox modo = (CheckBox) findViewById(R.id.ck_modo);

                try {
                    final String res;

                    // elige el modo de envio a php (getphp o putphp)
                    if (modo.isChecked()) {
                        res = enviarGet(nombre.getText().toString(), telefono
                                .getText().toString(), email.getText().toString(),"");

                    } else {
                        res = enviarPost(nombre.getText().toString(), telefono
                                .getText().toString(), email.getText().toString(),"");
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, res,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        };
        nt.start();
    }

    // funcion que arma los datos nombre, apellido, edad para enviarlos por modo put al servidor php (se le tiene que indicar ip,puerto donde esta servidor php)
    public String enviarPost(String nombre, String telefono, String email, String observaciones) {

        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(
                "http://gymjdena.esy.es/gym/PutData.php");
        HttpResponse response = null;
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("nombre", nombre));
            params.add(new BasicNameValuePair("telefono", telefono));
            params.add(new BasicNameValuePair("email", email));

            params.add(new BasicNameValuePair("observaciones", observaciones));
            //params.add(new BasicNameValuePair("modo", "POST"));
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = httpClient.execute(httpPost, localContext);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return response.toString();

    }
    // funcion que arma los datos nombre, apellido, edad para enviarlos por modo get al servidor php (se le tiene que indicar ip,puerto donde esta servidor php)
    public String enviarGet(String nombre, String telefono, String email, String observaciones) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpResponse response = null;
        String parametros = "?nombre=" + nombre + "&telefono=" + telefono
                + "&email=" + email + "&observaciones="+observaciones;

        //Toast.makeText(MainActivity.this, res,Toast.LENGTH_LONG).show();
        HttpGet httpget = new HttpGet(
                "http://gymjdena.esy.es/gym/PutData.php" + parametros);
        try {
            response = httpClient.execute(httpget, localContext);

        } catch (Exception e) {

        }
        return response.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}

