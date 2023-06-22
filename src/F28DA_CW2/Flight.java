package F28DA_CW2;

public class Flight implements IFlight {

    private String flightCode;
    private Airport from;
    private Airport to;
    private String fromGMTime;
    private String toGMTime;
    private int cost;

    public Flight(String flightCode, Airport from, Airport to, String fromGMTime, String toGMTime, int cost) {
        this.flightCode = flightCode;
        this.from = from;
        this.to = to;
        this.fromGMTime = fromGMTime;
        this.toGMTime = toGMTime;
        this.cost = cost;
    }

    @Override
    public String getFlightCode() {
        return this.flightCode;
    }

    @Override
    public Airport getTo() {
        return this.to;
    }

    @Override
    public Airport getFrom() {
        return this.from;
    }

    @Override
    public String getFromGMTime() {
        return this.fromGMTime;
    }

    @Override
    public String getToGMTime() {
        return this.toGMTime;
    }

    @Override
    public int getCost() {
        return this.cost;
    }

    public int getAirTime() {
        int airTime = 0;
        int fromHour = Integer.parseInt(this.fromGMTime.substring(0, 2));
        int fromMin = Integer.parseInt(this.fromGMTime.substring(2, 4));
        int toHour = Integer.parseInt(this.toGMTime.substring(0, 2));
        int toMin = Integer.parseInt(this.toGMTime.substring(2, 4));
        if (toHour < fromHour) {
            toHour += 24;
        }
        airTime = (toHour - fromHour) * 60 + (toMin - fromMin);
        return airTime;
    }
}
