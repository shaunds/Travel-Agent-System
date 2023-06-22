package F28DA_CW2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlyingPlanner implements IFlyingPlannerPartB<Airport,Flight>, IFlyingPlannerPartC<Airport,Flight> {

	private SimpleDirectedWeightedGraph<Airport, Flight> graph;

	public FlyingPlanner() {
		graph = new SimpleDirectedWeightedGraph<Airport, Flight>(Flight.class);
	}

	@Override
	public boolean populate(FlightsReader fr) {
		HashSet<String[]> airports = fr.getAirports();
		HashSet<String[]> flights = fr.getFlights();
		return populate(airports, flights);
	}

	@Override
	public boolean populate(HashSet<String[]> airports, HashSet<String[]> flights) {
		try {
			// Add airports to graph
			for (String[] airport : airports) {			
				Airport temp = new Airport(airport[0], airport[1], airport[2]);
				graph.addVertex(temp);
			}
			// Add flights to graph
			for (String[] flight : flights) {
				Airport deptAirport = airport(flight[1]);
				Airport arrAirport = airport(flight[3]);
				Flight newFlight = new Flight(flight[0], deptAirport, arrAirport, flight[2], flight[4], Integer.valueOf(flight[5]));
				graph.addEdge(deptAirport, arrAirport, newFlight);
			}
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	@Override
	public Airport airport(String code) {
		for (Airport airport: graph.vertexSet()) {
			if (airport.getCode().equals(code)) {
				return airport;
			}
		}
		return null;
	}

	@Override
	public Flight flight(String code) {
		for (Flight flight: graph.edgeSet()) {
			if (flight.getFlightCode().equals(code)) {
				return flight;
			}
		}
		return null;
	}

	@Override
	public Journey leastCost(String from, String to) throws FlyingPlannerException {
		return leastCost(from, to, null);
	}

	@Override
	public Journey leastHop(String from, String to) throws FlyingPlannerException {
		return leastHop(from, to, null);
	}

	@Override
	public Journey leastCost(String from, String to, List<String> excluding)
			throws FlyingPlannerException {
		validateFromTo(from, to);
		if (excluding != null && excluding.size() >= 0) {
			graph = excludeAirports(excluding);
		}
		for (Flight edge: graph.edgeSet()) {
			graph.setEdgeWeight(edge, ((Flight) edge).getCost());
		}

		GraphPath<Airport, Flight> shortestPath = DijkstraShortestPath.findPathBetween(graph, airport(from), airport(to));
		if (shortestPath == null) {
			throw new FlyingPlannerException("Journey not found!");
		}
		return new Journey(shortestPath);
	}

	@Override
	public Journey leastHop(String from, String to, List<String> excluding)
			throws FlyingPlannerException {
		validateFromTo(from, to);
		SimpleDirectedWeightedGraph<Airport, Flight> temp;
		if (excluding != null && excluding.size() >= 0) {
			temp = excludeAirports(excluding);
		} else {
			temp = graph;
		}

		for (Flight edge: temp.edgeSet()) {
			temp.setEdgeWeight(edge, 1);
		};
		GraphPath<Airport, Flight> shortestPath = DijkstraShortestPath.findPathBetween(temp, airport(from), airport(to));
		if (shortestPath == null) {
			throw new FlyingPlannerException("Journey not found!");
		}
		return new Journey(shortestPath);
	}


	@Override
	public Set<Airport> directlyConnected(Airport airport) {
		Set<Airport> result = new HashSet<Airport>();
		
		for (Airport connectedAirport : graph.vertexSet()) {
			boolean connectedInBothDirections = false;
			for (Flight outgoingFlight : graph.getAllEdges(airport, connectedAirport)) {
				for (Flight incomingFlight : graph.getAllEdges(connectedAirport, airport)) {
					if (outgoingFlight.getTo().equals(incomingFlight.getFrom())) {
						connectedInBothDirections = true;
						break;
					}
				}
				if (connectedInBothDirections) {
					break;
				}
			}
			if (connectedInBothDirections) {
				result.add(connectedAirport);
			}
		}
		return result;		
	}

	@Override
	public int setDirectlyConnected() {
		int result = 0;
		for (Airport airport: graph.vertexSet()) {
			Set<Airport> set = directlyConnected(airport);
			airport.setDicrectlyConnected(set);
			result += set.size();
		}
		return result;
	}

	@Override
	public int setDirectlyConnectedOrder() {
		int result = 0;
		for (Airport airport : graph.vertexSet()) {
			Set<Airport> betterConnected = new HashSet<Airport>();
			Set<Flight> outFlights = graph.outgoingEdgesOf(airport);
			for (Flight f : outFlights) {
				Airport destAirport = graph.getEdgeTarget(f);
				if (destAirport.getDicrectlyConnected().size() > airport.getDicrectlyConnected().size()) {
					betterConnected.add(destAirport);
				}
			}
			airport.setDicrectlyConnected(betterConnected);
			result += betterConnected.size();
		}
		return result;
	}

	@Override
	public Set<Airport> getBetterConnectedInOrder(Airport airport) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String leastCostMeetUp(String at1, String at2) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String leastHopMeetUp(String at1, String at2) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String leastTimeMeetUp(String at1, String at2, String startTime) throws FlyingPlannerException {
		// TODO Auto-generated method stub
		return null;
	}

	public void validateFromTo(String from, String to) throws FlyingPlannerException{
		if (airport(from) == null || airport(to) == null) {
			throw new FlyingPlannerException("Airport code invalid");
		}
		if (from.equals(to)) {
			throw new FlyingPlannerException("Departure and Arrival airports are the same");
		}
	}

	private SimpleDirectedWeightedGraph<Airport, Flight> excludeAirports(List<String> excluding) {
		SimpleDirectedWeightedGraph<Airport, Flight> temp = new SimpleDirectedWeightedGraph<Airport,Flight>(Flight.class);
		Graphs.addAllVertices(temp, graph.vertexSet());
		Graphs.addAllEdges(temp, graph, graph.edgeSet());

		// excluding.forEach(airportCode -> {
		for (String airportCode: excluding){
			// Check if the airport is valid
			if (temp.containsVertex(airport(airportCode))) {
				// Remove the airport and all its edges
				temp.removeAllEdges(temp.edgesOf(airport(airportCode)));
				temp.removeVertex(airport(airportCode));
			}
		}
		return temp;
	}


}
