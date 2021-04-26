package com.testequalificacao.cadastroDeLocalizacao.controller.integracaoexternageocoding;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testequalificacao.cadastroDeLocalizacao.controller.integracaoexternageocoding.ws.AddressResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Classe responsável por acessar o WebService da API Geocoding.
 *
 * @author Mateus
 */
public class IntegracaoExternaGeocodingWS {

    /**
     * Retorna os dados de Geolocalização através da API do Google.
     * 
     * @param endereco
     *   A linha de endereço a ser utilizada.
     * 
     * @return 
     *   Os dados de Geolocaliação.
     */
    public AddressResponse getDadosGeolocalicacao(String endereco) {
        if (endereco == null) {
            throw new IllegalStateException("O endereço é inválido");
        }
        
        endereco = endereco.replace(" ", "+");

        RestTemplate rt = new RestTemplate();
        UriComponents uri = UriComponentsBuilder.newInstance()
                .scheme("https")
                .host("maps.googleapis.com")
                .path("maps/api/geocode/json")
                .queryParam("address", endereco)
                .queryParam("key",
                        IntegracaoExternaGeocodingConstants.KEY_GEOCODING)
                .build();

        ResponseEntity<AddressResponse> entity
                = rt.getForEntity(uri.toUriString(), AddressResponse.class);

        int responseCode = entity.getStatusCodeValue();
        String responseMessage = entity.getStatusCode().toString();
        System.out.println("\n#########################################");
        System.out.println("\nEnviando Requisição a URL: " + uri.toUriString());
        System.out.println("\nCódigo de Resposta: " + responseCode
                + "\n\nMensagem: " + responseMessage);

        System.out.println("\n#########################################\n");

        if (responseCode != 200) {
            return null;
        }else{
            return entity.getBody();
        }
    }
}
