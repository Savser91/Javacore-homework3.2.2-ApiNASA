import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

import java.io.IOException;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URL =
            "https://api.nasa.gov/planetary/apod?api_key=sE4OUjK6yHqdlt1904NzxJB3EXFhbhBH9E4ChIbd";
    public static final ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        List<ApiNASA> apiNASAList = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<ApiNASA>>() {});

    }
}
