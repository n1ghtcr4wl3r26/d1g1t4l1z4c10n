package anb.general;

import com.google.gson.Gson;

import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


public class Json {
    
    public static void print(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
    
    public static void error(HttpServletResponse response, String message) throws IOException {
        Json.message(response, "ERROR", message);
    }
    
    public static void ok(HttpServletResponse response, String message) throws IOException {
        Json.message(response, "OK", message);
    }
    
    public static void success(HttpServletResponse response, String message) throws IOException {
        Json.message(response, "SUCCESS", message);
    }
    
    public static void warning(HttpServletResponse response, String message) throws IOException {
        Json.message(response, "WARNING", message);
    }
    
    public static void notification(HttpServletResponse response, String message) throws IOException {
        Json.message(response, "NOTIFICATION", message);
    }
    
    public static void ajax(HttpServletResponse response, String message) throws IOException {
        Json.message(response, "AJAX", message);
    }
    
    public static void gson(HttpServletResponse response, Object object) throws IOException {
        Json.print(response, new Gson().toJson(object));
    }
    
    public static void message(HttpServletResponse response, String state, String message) throws IOException {
        Json.print(response, "{ \"state\" : \"" + state + "\", \"data\" : \"" + message + "\"}");
    }
    
    public static String datatable(List lista, int total, String draw) {
        JSONObject datatable = new JSONObject();
        datatable.put("draw", draw);
        datatable.put("recordsTotal", total);
        datatable.put("recordsFiltered", total);
        datatable.put("data", lista);
        
        return datatable.toString();
    }
    
}
