package uk.ac.ed.inf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import uk.ac.ed.inf.records.LngLat;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * FileHandler writes all the output files required (flightpath-YYYY-MM-DD.json, drone-YYYY-MM-DD.geojson, deliveries-YYYY-MM-DD.json)
 */
public class FileHandler {

    //this stores all the moves the drone makes over the course of the day in JSON format
    final private static ArrayList<String> flightpath = new ArrayList<>();

    /**
     * writeFlightpathJSONs takes a single move and converts it into a JSON according to what type of move it is
     * @param orderNo the order the move belongs to
     * @param from the LngLat the move starts from
     * @param to the LngLat the move ends up
     * @param angle the degree of the movement, always one of the CompassDirection values, or null if hovering
     * @param ticksSinceStart how long it took to calculate this move
     * @throws JsonProcessingException if there's an error converting the JSON into a string
     */
    public static void writeFlightpathJSONs(String orderNo, LngLat from, LngLat to, Double angle, int ticksSinceStart) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();

        //this helps create a JSON
        ObjectNode move = mapper.createObjectNode();

        move.put("orderNo", orderNo);

        //if angle is null then we are hovering for 1 move
        if (angle == null){

            move.put("fromLongitude", from.longitude());
            move.put("fromLatitude", from.latitude());
            move.put("angle", angle);
            move.put("toLongitude", from.longitude());
            move.put("toLatitude", from.latitude());

        } else if (from == null){ //else if from is null, then we are hovering for a move

            move.put("fromLongitude", to.longitude());
            move.put("fromLatitude", to.latitude());
            move.put("angle", angle);
            move.put("toLongitude", to.longitude());
            move.put("toLatitude", to.latitude());

        } else { //else it is a normal move

            move.put("fromLongitude", from.longitude());
            move.put("fromLatitude", from.latitude());
            move.put("angle", angle);
            move.put("toLongitude", to.longitude());
            move.put("toLatitude", to.latitude());

        }

        move.put("ticksSinceStartOfCalculation", ticksSinceStart);

        //convert to string and add to flightpath
        String json = mapper.writeValueAsString(move);
        flightpath.add(json);

    }

    /**
     * writeFlightPath writes out the flightpath file
     * @param date the date for the name of the file
     * @throws IOException if there's an error writing the file
     */
    public static void writeFlightPath(String date) throws IOException {

        String filename = "flightpath-" + date + ".json";

        FileWriter fileWriter = new FileWriter(filename);

        String flightpathArray = flightpath.toString();

        fileWriter.write(flightpathArray);
        fileWriter.close();


    }

    /**
     * This writes the deliveries output file
     * @param date the date for the filename
     * @param deliveries the deliveries that should be written to the file
     * @throws IOException if there's an error writing the file
     */
    public static void writeDeliveries(String date, String deliveries) throws IOException {

        String filename = "deliveries-" + date + ".json";

        FileWriter fileWriter = new FileWriter(filename);

        fileWriter.write(deliveries);
        fileWriter.close();

    }

    /**
     * This writes the drone output file
     * @param date the date for the filename
     * @param geojson the vector of LngLats to be converted into a geojson
     * @throws IOException if there's an error writing the file
     */
    public static void writeDronePath(String date, Vector<LngLat> geojson) throws IOException {

        String filename = "drone-" + date + ".geojson";

        FileWriter fileWriter = new FileWriter(filename);

        String content = GeoJSON.generateGeoJson(geojson);

        fileWriter.write(content);
        fileWriter.close();

    }

}
