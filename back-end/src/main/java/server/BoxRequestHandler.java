package server;

import box.Box;
import box.BoxandData;
import box.MikeBoxGetter;
import box.TDBoxGetter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.Main;
import project.Canvas;
import project.View;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoxRequestHandler  implements HttpHandler {

    // gson builder
    private final Gson gson;
    private TDBoxGetter boxGetter;
    private List<Double> fetchTimes;

    public BoxRequestHandler() {

        gson = new GsonBuilder().create();
        // boxGetter = new MikeBoxGetter();
        boxGetter = new TDBoxGetter();
        fetchTimes = new ArrayList<>();
    }
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        // TODO: this method should be thread safe, allowing concurrent requests
        System.out.println("\nServing /dynamic Box");

        // get data of the current request
        // variable definitions
        String response;
        String canvasId, viewId;
        double minx, miny;
        BoxandData data = null;

        // check if this is a POST request
        if (! httpExchange.getRequestMethod().equalsIgnoreCase("GET")) {
            Server.sendResponse(httpExchange, HttpsURLConnection.HTTP_BAD_METHOD, "");
            return;
        }

        // get data of the current request
        String query = httpExchange.getRequestURI().getQuery();
        Map<String, String> queryMap = Server.queryToMap(query);
        // print
        for (String s : queryMap.keySet())
            System.out.println(s + " : " + queryMap.get(s));

        // check parameters, if not pass, send a bad request response
        response = checkParameters(queryMap);
        if (response.length() > 0) {
            Server.sendResponse(httpExchange, HttpsURLConnection.HTTP_BAD_REQUEST, response);
            return;
        }
        // get parameters
        canvasId = queryMap.get("id");
        viewId = queryMap.get("viewId");
        minx = Double.valueOf(queryMap.get("x"));
        miny = Double.valueOf(queryMap.get("y"));
        Canvas c = null;
        try {
            c = Main.getProject().getCanvas(canvasId).deepCopy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        View v = Main.getProject().getView(viewId);
        if (queryMap.containsKey("canvasw"))
            c.setW(Integer.valueOf(queryMap.get("canvasw")));
        if (queryMap.containsKey("canvash"))
            c.setH(Integer.valueOf(queryMap.get("canvash")));
        ArrayList<String> predicates = new ArrayList<>();
        for (int i = 0; i < c.getLayers().size(); i ++)
            predicates.add(queryMap.get("predicate" + i));
        double oMinX = Double.valueOf(queryMap.get("oboxx"));
        double oMinY = Double.valueOf(queryMap.get("oboxy"));
        double oMaxX = oMinX + Double.valueOf(queryMap.get("oboxw"));
        double oMaxY = oMinY + Double.valueOf(queryMap.get("oboxh"));
        Box oldBox = new Box(oMinX, oMinY, oMaxX, oMaxY);

        //get box data
        long st = System.currentTimeMillis();
        try {
            data = boxGetter.getBox(c, v, minx, miny, oldBox, predicates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double fetchTime = System.currentTimeMillis() - st;
        System.out.println("Fetch data time: " + fetchTime + "ms.");
        fetchTimes.add(fetchTime);
        if (fetchTimes.size() % 5 == 0 && fetchTimes.size() > 0) {
            System.out.println("writing fetch data");
            JsonWriter.writeJSON("fetchTimes", fetchTimes);
        }

        //send data and box back
        Map<String, Object> respMap = new HashMap<>();
        respMap.put("renderData", BoxandData.getDictionaryFromData(data.data, c));
        respMap.put("minx", data.box.getMinx());
        respMap.put("miny", data.box.getMiny());
        respMap.put("boxH", data.box.getHight());
        respMap.put("boxW", data.box.getWidth());
        respMap.put("canvasId", canvasId);
        response = gson.toJson(respMap);

        // send back response
        st = System.currentTimeMillis();
        Server.sendResponse(httpExchange, HttpsURLConnection.HTTP_OK, response);
        System.out.println("Send response time: " + (System.currentTimeMillis() - st) + "ms.");
        System.out.println();
    }

    private String checkParameters(Map<String, String> queryMap) {

        // check fields
        if (! queryMap.containsKey("id"))
            return "canvas id missing.";
        if (! queryMap.containsKey("x") || ! queryMap.containsKey("y"))
            return "x or y missing.";

        String canvasId = queryMap.get("id");

        // check whether this canvas exists
        if (Main.getProject().getCanvas(canvasId) == null)
            return "Canvas " + canvasId + " does not exist!";

        // check passed
        return "";
    }
}
