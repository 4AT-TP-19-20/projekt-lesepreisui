package sample.backend.data.database;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DatabaseInterface {
    private static final String BASE_PATH = "https://634a67e8.eu-gb.apigw.appdomain.cloud/lpui/";

    public static JSONArray read(String type) {
        boolean successful = false;
        JSONObject content = null;

        while (!successful) {
            successful = true;

            try {
                URL url = new URL(BASE_PATH + type);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();

                content = checkSuccessAndGetContent(connection, "add");
            } catch (IOException ex) {
                System.out.println("Error reading " + type);
                ex.printStackTrace();
                successful = false;
            }
        }

        return content.getJSONArray("entries");
    }

    static void add(DatabaseEntry entry) {
        boolean successful = false;

        while (!successful) {
            successful = true;

            try {
                URL url = new URL(BASE_PATH + entry.getType() + "/add");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String output = "{ \"doc\": " + entry.toJson().toString() + " }";
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(output.getBytes(StandardCharsets.UTF_8));
                }

                connection.connect();

                JSONObject content = checkSuccessAndGetContent(connection, "add");

                entry.setId(content.getString("id"));
                entry.setRev(content.getString("rev"));
            } catch (IOException ex) {
                System.out.println("Error adding " + entry.toString());
                ex.printStackTrace();
                successful = false;
            }
        }
    }

    static void update(DatabaseEntry entry) {
        boolean successful = false;

        while (!successful) {
            successful = true;

            try {
                URL url = new URL(BASE_PATH + entry.getType() + "/update");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
                connection.setDoOutput(true);

                JSONObject jsonEntry = entry.toJson();
                jsonEntry.put("_id", entry.getId());
                jsonEntry.put("_rev", entry.getRev());
                String output = "{ \"doc\": " + jsonEntry.toString() + " }";

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(output.getBytes(StandardCharsets.UTF_8));
                }

                connection.connect();

                JSONObject content = checkSuccessAndGetContent(connection, "update");

                entry.setRev(content.getString("_rev"));
            } catch (IOException ex) {
                System.out.println("Error updating " + entry.toString());
                ex.printStackTrace();
                successful = false;
            }
        }
    }

    static void delete(DatabaseEntry entry) {
        boolean successful = false;

        while (!successful) {
            successful = true;

            try {
                URL url = new URL(BASE_PATH + entry.getType() + "/delete?docid="
                        + entry.getId()
                        + "&docrev="
                        + entry.getRev());

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.connect();

                checkSuccessAndGetContent(connection, "delete");
            } catch (IOException ex) {
                System.out.println("Error deleting " + entry.toString());
                ex.printStackTrace();
                successful = false;
            }
        }
    }

    private static JSONObject checkSuccessAndGetContent(HttpURLConnection connection, String type) throws IOException {
        if(connection.getResponseCode() != 200) {
            throw new IOException("Unsuccessful " + type + "-request");
        }

        StringBuilder contentBuilder;
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String currentLine;
            contentBuilder = new StringBuilder();
            while ((currentLine = reader.readLine()) != null) {
                contentBuilder.append(currentLine);
            }
        }

        return new JSONObject(contentBuilder.toString());
    }
}
