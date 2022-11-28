package uk.ac.ed.inf;

import uk.ac.ed.inf.enums.OrderOutcome;
import uk.ac.ed.inf.jsons.OrderStructure;
import uk.ac.ed.inf.records.LngLat;

import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * This controls the movement of the drone according to a day's orders
 */
public class Drone {

    private static OrderStructure[] orders;
    private static HashMap<String,OrderOutcome> dayOrderOutcomes;
    private static HashMap<String,LngLat[]> dayOrderMoves;

    private static LngLat[] calculateMoves(OrderStructure order){

        LngLat[] moves = null;

        return moves;

    }

    public static void StartDay(String date) throws MalformedURLException {

        orders = Order.getDayOrders(date);
        OrderStructure currentOrder = null;
        OrderOutcome currentOrderOutcome = null;
        LngLat[] currentMoves = null;

        for (int i = 0; i < orders.length; i++){

            currentOrder = orders[i];
            currentOrderOutcome = Order.validateOrder(currentOrder);

            if (currentOrderOutcome != OrderOutcome.ValidButNotDelivered){

                dayOrderOutcomes.put(currentOrder.orderNo, currentOrderOutcome);
                continue;

            }

            currentMoves = calculateMoves(currentOrder);

            dayOrderMoves.put(currentOrder.orderNo, currentMoves);

        }

    }

}
