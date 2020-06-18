package rafaela.kabuchi.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import rafaela.kabuchi.myapplication.R;
import rafaela.kabuchi.myapplication.modelo.Contato;

public class AdaptersContatos extends BaseAdapter {
    List<Contato> listaContatos;
    Context context;
    public AdaptersContatos(Context context, List<Contato> listaContatos)
    {
        this.context =  context;
        this.listaContatos = listaContatos;
    }

    @Override
    public int getCount() {
        return listaContatos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaContatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view;

        if (convertView==null){
            view = inflater.inflate(R.layout.linha_contato, parent, false);
        }else {
            view = convertView;
        }
            TextView txtNome = view.findViewById(R.id.txtNome);
            TextView txtTelefone = view.findViewById(R.id.txtTelefone);
            Contato contato = listaContatos.get(position);
            txtNome.setText(contato.getNome());
            txtTelefone.setText(contato.getTelefone());


        return view;
    }
}
