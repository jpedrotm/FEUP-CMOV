package org.feup.cmov.acmecoffee.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONCreater {

    public enum FormType {
        REGISTER_FORM, LOGIN_FORM
    }

    private static String[] REGISTER_FORM_FIELD = {"email", "name", "password", "nif"};
    private static String[] LOGIN_FORM_FIELD = {"email", "password"};

    public static JSONObject convertToJSON(FormType type, List<String> values) {
        String[] fields = getForm(type);
        JSONObject result = new JSONObject();
        for(int i=0;i < fields.length; i++) {
            try {
                result.put(fields[i],values.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    private static String[] getForm(FormType type) {
        switch (type) {
            case REGISTER_FORM:
                return REGISTER_FORM_FIELD;
            case LOGIN_FORM:
                return LOGIN_FORM_FIELD;
            default:
                return null;
        }
    }
}
