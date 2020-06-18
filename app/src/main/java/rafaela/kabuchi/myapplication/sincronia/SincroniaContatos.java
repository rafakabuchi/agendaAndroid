package rafaela.kabuchi.myapplication.sincronia;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rafaela.kabuchi.myapplication.dao.DaoContatos;
import rafaela.kabuchi.myapplication.modelo.Contato;
import rafaela.kabuchi.myapplication.retrofit.RetrofitInicializador;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SincroniaContatos {
    Context context;
    List<Contato> listaContatosOnline;
    List<Contato> listaContatosOffline;

   public static boolean sucessoInserir = true;

    public SincroniaContatos(Context context)
    {
        this.context = context;
    }

    public void SincronizarContatos()
    {
        RetrofitInicializador retrofit = new RetrofitInicializador();
        retrofit.servicoContatos().todosContatos().enqueue(new Callback<List<Contato>>() {
            @Override
            public void onResponse(Call<List<Contato>> call, Response<List<Contato>> response) {
                listaContatosOnline = response.body();
                DaoContatos dao = new DaoContatos(context);
                listaContatosOffline = dao.TodosContatos();

                InsereContatosOnline();
                InsereContatosOffline();
            }

            @Override
            public void onFailure(Call<List<Contato>> call, Throwable t) {
                Toast.makeText(context,"Não é possível conectar",Toast.LENGTH_LONG).show();
            }
        });


    }

    private void InsereContatosOffline() {
        List<Contato> listaNovosContatos = new ArrayList<>();

        for (Contato contatoOf:listaContatosOffline)
        {
            boolean encontrado = false;
            for (Contato contatoOn:listaContatosOnline)
            {
                if (contatoOn.getId().equals(contatoOf.getId()))
                {
                    encontrado = true;
                }
            }
            if (encontrado==false)
            {
                listaNovosContatos.add(contatoOf);
            }
        }

        RetrofitInicializador retrofit = new RetrofitInicializador();



        for (Contato contatoNovo:listaNovosContatos
             ) {
            retrofit.servicoContatos().CadastraContato(contatoNovo).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    sucessoInserir = false;
                }
            });
        }

        if (sucessoInserir==false)
        {
            Toast.makeText(context,"Falha ao atualizar", Toast.LENGTH_LONG).show();
        }
    }

    private void InsereContatosOnline() {
        List<Contato> listaNovosContatos = new ArrayList<>();

        for (Contato contatoOn:listaContatosOnline)
        {
            boolean encontrado = false;
            for (Contato contatoOff:listaContatosOffline)
            {
                if (contatoOn.getId().equals(contatoOff.getId()))
                {
                encontrado = true;
                }
            }
            if (encontrado==false)
            {
                listaNovosContatos.add(contatoOn);
            }
        }

        DaoContatos dao = new DaoContatos(context);

        for (Contato contatoNovo:listaNovosContatos)
        {
            dao.CadastraContato(contatoNovo);
        }

    }
}
