package rafaela.kabuchi.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import rafaela.kabuchi.myapplication.adapters.AdaptersContatos;
import rafaela.kabuchi.myapplication.modelo.Contato;
import rafaela.kabuchi.myapplication.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ListView lstContatos;
    List<Contato> listaContatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstContatos = findViewById(R.id.lstContatos);
        lstContatos.setOnCreateContextMenuListener(this);
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista_contatos, menu);
    }

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Contato contato = listaContatos.get(info.position);
        if (item.getItemId() == R.id.menu_item_mapa) {
            Uri uri = Uri.parse("geo:0,0?q=" + contato.getEndereco() + "&z=18");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_item_email) {
            Uri uri = Uri.parse("mailto:" + contato.getEmail());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_item_sms) {
            Uri uri = Uri.parse("sms:" + contato.getTelefone());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.menu_item_licacao) {
            Uri uri = Uri.parse("tel:" + contato.getTelefone());
            Intent intent = new Intent(Intent.ACTION_CALL, uri);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                String[] permissoes = new String[]{
                        Manifest.permission.CALL_PHONE
                };

                ActivityCompat.requestPermissions(this, permissoes,123);
            }else {
                startActivity(intent);
            }

        }
        if (item.getItemId()==R.id.menu_item_excluir)
        {
            RetrofitInicializador retrofit = new RetrofitInicializador();
            retrofit.servicoContatos().RemoveContato(contato.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(MainActivity.this, "Excluido", Toast.LENGTH_LONG).show();
                    carregaContatos();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Falha ao excluir", Toast.LENGTH_LONG).show();
                }
            });
        }

        if (item.getItemId()==R.id.menu_item_atualizar)
        {
            Intent intent = new Intent(this,FormContatoActivity.class);
            intent.putExtra("contato",contato);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaContatos();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() ==R.id.menu_item_novo)
        {
            Intent intent = new Intent(this, FormContatoActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void carregaContatos() {
        RetrofitInicializador retrofit = new RetrofitInicializador();
        retrofit.servicoContatos().todosContatos().enqueue(
                new Callback<List<Contato>>() {
                    @Override
                    public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                    listaContatos = response.body();
                        AdaptersContatos adapter = new AdaptersContatos(MainActivity.this, listaContatos);
                        lstContatos.setAdapter(adapter);
                        //AlertDialog alerta = new AlertDialog.Builder(MainActivity.this).setMessage("Usuários carregados").setPositiveButton("OK", null).show();
//                        final AlertDialog alerta = new AlertDialog.Builder(MainActivity.this)
//                                .setMessage("Tem certeza?")
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).setCancelable(false).show();
                    }

                    @Override
                    public void onFailure(Call<List<Contato>> call, Throwable t) {
                        AlertDialog alerta = new AlertDialog.Builder(MainActivity.this).setView(R.layout.alerta).show();
                    }
                }
        );
    }
}
