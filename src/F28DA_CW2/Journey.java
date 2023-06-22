package F28DA_CW2;

import java.util.LinkedList;
import java.util.List;

import org.jgrapht.GraphPath;

public class Journey implements IJourneyPartB<Airport, Flight>, IJourneyPartC<Airport, Flight> {

	private GraphPath<Airport,Flight> graphPath;
	private int totalCost, airTime, connectingTime;

	public Journey(GraphPath<Airport,Flight> graphPath) {
		this.graphPath = graphPath;
	}

	@Override
	public List<String> getStops() {
		List<String> stopList = new LinkedList<String>();
		for (Airport airport: graphPath.getVertexList()) {
			stopList.add(airport.getCode());
		}
		return stopList;
	}

	@Override
	public List<String> getFlights() {
		List<String> flighList = new LinkedList<String>();
		for (Flight flight: graphPath.getEdgeList()) {
			flighList.add(flight.getFlightCode());
		}
		return flighList;
	}

	@Override
	public int totalHop() {
		return this.graphPath.getEdgeList().size();
	}

	@Override
	public int totalCost() {
		totalCost = 0;
		for (Flight flight: graphPath.getEdgeList()) {
			totalCost += flight.getCost();
		}
		return totalCost;
	}

	@Override
	public int airTime() {
		airTime = 0;
		for (Flight flight: graphPath.getEdgeList()) {
			airTime += flight.getAirTime();
		}
		return airTime;
	}

	@Override
	public int connectingTime() {
		// The connecting time is the time between the arrival of a flight and the departure of the next flight.
		connectingTime = 0;
		for (int i = 0; i < graphPath.getEdgeList().size() - 1; i++) {
			Flight currentFlight = graphPath.getEdgeList().get(i);
			Flight nextFlight = graphPath.getEdgeList().get(i + 1);
			int fromHourNext = Integer.parseInt(nextFlight.getFromGMTime().substring(0, 2));
        	int fromMinNext = Integer.parseInt(nextFlight.getFromGMTime().substring(2, 4));
        	int toHourCurrent = Integer.parseInt(currentFlight.getToGMTime().substring(0, 2));
        	int toMinCurrent = Integer.parseInt(currentFlight.getToGMTime().substring(2, 4));
			if (fromHourNext < toHourCurrent) {
				fromHourNext += 24;
			}
			connectingTime += (fromHourNext - toHourCurrent) * 60 + (fromMinNext - toMinCurrent);
		}
		return connectingTime;
}

	@Override
	public int totalTime() {
    // The total time is the sum of the air time and the connecting time.
		return airTime() + connectingTime();
	}

	@Override
	public int totalAirmiles() {
		// The total airmiles is calculated by multiplying the flight time (time on air only) by 3% of the total cost of a journey.
		return (int) (airTime() * totalCost() * 0.03);
	}

}
