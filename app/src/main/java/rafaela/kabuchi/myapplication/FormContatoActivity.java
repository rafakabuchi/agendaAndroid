package rafaela.kabuchi.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

import rafaela.kabuchi.myapplication.modelo.Contato;
import rafaela.kabuchi.myapplication.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormContatoActivity extends AppCompatActivity {
    public static final int CODIGO_FOTO = 456;
    EditText txtNome;
    EditText txtEmail;
    EditText txtTelefone;
    EditText txtEndereco;
    ImageView imgFoto;
    Button btSalvar;

    Contato contato;

    String miniFoto="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_contato);
        txtNome = findViewById(R.id.txtNome);
        txtEmail = findViewById(R.id.txtEmail);
        txtEndereco = findViewById(R.id.txtEndereco);
        txtTelefone = findViewById(R.id.txtTelefone);
        imgFoto = findViewById(R.id.imgFoto);
        btSalvar = findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvaContato();
            }
        });

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(FormContatoActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CODIGO_FOTO);
                } else {
                    String[] permissoes = new String[]{
                            Manifest.permission.CAMERA
                    };
                    ActivityCompat.requestPermissions(FormContatoActivity.this,permissoes,000);
                }
            }
        });

        if(this.getIntent().getSerializableExtra("contato")!=null)
        {
            contato = (Contato) this.getIntent().getSerializableExtra("contato");
            exibirDadosContato();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CODIGO_FOTO)
        {
            Bundle extra = data.getExtras();
            Bitmap miniaturaFoto = (Bitmap) extra.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            miniaturaFoto.compress(Bitmap.CompressFormat.JPEG,92,stream);
            String imageString = Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT);
            miniFoto = imageString;
            imgFoto.setImageBitmap(miniaturaFoto);
        }
    }

    private void exibirDadosContato() {
        txtNome.setText(contato.getNome());
        txtEmail.setText(contato.getEmail());
        txtTelefone.setText(contato.getTelefone());
        txtEndereco.setText(contato.getEndereco());
        if (contato.getFoto() !=null && !contato.getFoto().equals(""))
        {
          byte[] imagemBytes = Base64.decode(contato.getFoto(),Base64.DEFAULT);
          Bitmap bitmap = BitmapFactory.decodeByteArray(imagemBytes,0,imagemBytes.length);
          imgFoto.setImageBitmap(bitmap);
        }
    }

    private void salvaContato() {
        //Novo Contato
        if(contato==null)
        {
          preencheDadosContato ();
            RetrofitInicializador retrofit = new RetrofitInicializador();
            retrofit.servicoContatos().CadastraContato(contato).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(FormContatoActivity.this,"Cadastrado", Toast.LENGTH_LONG).show();
                    FormContatoActivity.this.finish();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(FormContatoActivity.this,"Não cadastrado!", Toast.LENGTH_LONG).show();
                    contato = null;
                }
            });
        }//Atualizar Contato
        else{
            preencheDadosContato();
            RetrofitInicializador retrofit = new RetrofitInicializador();
            retrofit.servicoContatos().AtualizaContato(contato.getId(),contato).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(FormContatoActivity.this,"Atualizado", Toast.LENGTH_LONG).show();
                    FormContatoActivity.this.finish();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(FormContatoActivity.this,"Não Atualizado", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void preencheDadosContato() {
        if(contato == null)
        {
            contato = new Contato();
            contato.setId(UUID.randomUUID().toString());
        }
        contato.setNome(txtNome.getText().toString());
        contato.setEmail(txtEmail.getText().toString());
        contato.setTelefone(txtTelefone.getText().toString());
        contato.setEndereco(txtEndereco.getText().toString());
        if (!miniFoto.equals(""))
        {
            contato.setFoto(miniFoto);
        }
        }
}
