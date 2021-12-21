import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import java.io.File;
import java.io.*;
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
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        List<ApiNASA> apiNASAList = mapper.readValue(response.getEntity().getContent(), new TypeReference<List<ApiNASA>>() {});
        String imageUrl = apiNASAList.get(0).getUrl();
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());
        request = new HttpGet(imageUrl);

        saveFile(httpClient.execute(request), imageName);
    }

    public static void saveFile(CloseableHttpResponse response, String imageName) throws IOException {
        byte[] imgBytes = response.getEntity().getContent().readAllBytes();
        try (FileOutputStream fos = new FileOutputStream(new File(imageName))) {
            fos.write(imgBytes, 0, imgBytes.length);
        } catch (Exception exception) {
            exception.getMessage();
        }
    }
}
