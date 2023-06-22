package F28DA_CW2;

import java.util.Set;

public class Airport implements IAirportPartB, IAirportPartC {

    private String code;
    private String airportName;
    private String location;
    private Set<Airport> directlyConnected;
    private int directlyConnectedOrder;

    /**
     * Constructor that initializes airport details based on the provided code and
     * FlightsReader object.
     */
    public Airport(String code, String location, String airportName) {
		
		this.code = code;
		this.location = location;
		this.airportName = airportName;
	}

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.airportName;
    }

    @Override
    public void setDicrectlyConnected(Set<Airport> directlyConnected) {
        this.directlyConnected = directlyConnected;
    }

    @Override
    public Set<Airport> getDicrectlyConnected() {
        return this.directlyConnected;
    }

    @Override
    public void setDicrectlyConnectedOrder(int order) {
        this.directlyConnectedOrder = order;
    }

    @Override
    public int getDirectlyConnectedOrder() {
        return this.directlyConnectedOrder;
    }
}
