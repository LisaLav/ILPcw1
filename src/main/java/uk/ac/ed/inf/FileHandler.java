package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.ac.ed.inf.enums.CompassDirection;
import uk.ac.ed.inf.records.LngLat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {

    public static void writeFlightpathJSONs(String orderNo, LngLat from, LngLat to, CompassDirection angleCompass, int ticksSinceStart) throws JsonProcessingException {

        ArrayList<String> moves = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode move = mapper.createObjectNode();
        double angle = 0;

        if (angleCompass != null){
            angle = angleCompass.getDegree();
        } else if (to == null){
            to = from;
        }

        move.put("orderNo", orderNo);
        move.put("fromLongitude", from.longitude());
        move.put("fromLatitude", from.latitude());
        move.put("angle", angle);
        move.put("toLongitude", to.longitude());
        move.put("toLatitude", to.latitude());
        move.put("ticksSinceStartOfCalculation", ticksSinceStart);

        String json = mapper.writeValueAsString(move);
        moves.add(json);

    }

    public static void writeFlightPath(String date, String moves){

        String filename = "flightpath" + date + ".json";

    }

    public static void writeDeliveries(String date, String deliveries) throws IOException {

        String filename = "deliveries-" + date + ".json";

        FileWriter fileWriter = new FileWriter(filename);

        fileWriter.write(deliveries);
        fileWriter.close();

    }

    public static void writeDronePath(String date){

    }

}
