package sample.backend.data.database;

import org.json.JSONObject;

public interface JsonSerializable {
    JSONObject toJson();
    void fromJson(JSONObject json);
}
