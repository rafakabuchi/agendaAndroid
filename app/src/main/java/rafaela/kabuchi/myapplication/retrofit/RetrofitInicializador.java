package rafaela.kabuchi.myapplication.retrofit;

import rafaela.kabuchi.myapplication.servicos.ServicoContatos;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitInicializador {
    Retrofit retrofit;

    public RetrofitInicializador() {
        retrofit = new Retrofit.Builder().baseUrl("http://www.cronny.somee.com/api/").addConverterFactory(JacksonConverterFactory.create()).build();
    }

    public ServicoContatos servicoContatos()
    {
        return retrofit.create(ServicoContatos.class);
    }
}
