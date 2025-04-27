package org.example.TELEGRAM.translator;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Translator {
    public static String translate(String joke) throws Exception {
        String lang = "ru"; // Целевой язык (русский)

        String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=" + lang +
                "&dt=t&q=" + URLEncoder.encode(joke, StandardCharsets.UTF_8);
        StringBuilder translatedText = new StringBuilder();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = client.execute(request)) {
                String json = EntityUtils.toString(response.getEntity());

                // Разбираем JSON-ответ с помощью Jackson
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(json);

                // Сбор перевода из JSON-массив
                for (JsonNode node : rootNode.get(0)) {
                    translatedText.append(node.get(0).asText()).append(" ");
                }
            }
        }
        return translatedText.toString().trim();
    }
}
