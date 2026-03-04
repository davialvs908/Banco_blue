package dev._x.piramede;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ServicoViaCep {
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final Gson gson = new Gson();

    public static String buscarRuaPorCep(String cep) {
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) return null;

        try {
            String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject json = gson.fromJson(response.body(), JsonObject.class);
                if (!json.has("erro")) {
                    return json.get("logradouro").getAsString();
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar CEP: " + e.getMessage());
        }
        return null;
    }
}