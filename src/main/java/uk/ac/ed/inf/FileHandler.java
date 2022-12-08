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

    private static ArrayList<String> flightpath = new ArrayList<String>();

    public static void writeFlightpathJSONs(String orderNo, LngLat from, LngLat to, Double angle, int ticksSinceStart) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode move = mapper.createObjectNode();

        move.put("orderNo", orderNo);

        //if angle is null then LngLat to is null
        if (angle == null){

            move.put("fromLongitude", from.longitude());
            move.put("fromLatitude", from.latitude());
            move.put("angle", angle);
            move.put("toLongitude", from.longitude());
            move.put("toLatitude", from.latitude());

        } else if (from == null){

            move.put("fromLongitude", to.longitude());
            move.put("fromLatitude", to.latitude());
            move.put("angle", angle);
            move.put("toLongitude", to.longitude());
            move.put("toLatitude", to.latitude());

        } else {

            move.put("fromLongitude", from.longitude());
            move.put("fromLatitude", from.latitude());
            move.put("angle", angle);
            move.put("toLongitude", to.longitude());
            move.put("toLatitude", to.latitude());

        }

        move.put("ticksSinceStartOfCalculation", ticksSinceStart);

        String json = mapper.writeValueAsString(move);
        flightpath.add(json);

    }

    public static void writeFlightPath(String date) throws IOException {

        String filename = "flightpath-" + date + ".json";

        FileWriter fileWriter = new FileWriter(filename);

        String flightpathArray = flightpath.toString();

        fileWriter.write(flightpathArray);
        fileWriter.close();


    }

    public static void writeDeliveries(String date, String deliveries) throws IOException {

        String filename = "deliveries-" + date + ".json";

        FileWriter fileWriter = new FileWriter(filename);

        fileWriter.write(deliveries);
        fileWriter.close();

    }

    public static void writeDronePath(String date){

        String filename = "drone-" + date

    }

}
