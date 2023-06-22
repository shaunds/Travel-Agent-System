package F28DA_CW2;

import java.util.Scanner;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

public class FlyingPlannerMainPartA {

	public static void main(String[] args) {
		
        Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        String vH = "Heathrow";
        String vE = "Edinburgh";
        String vD = "Dubai";
        String vS = "Sydney";
        String vK = "Kuala Lumpur";


        // add the vertices
        g.addVertex(vH);
        g.addVertex(vE);
        g.addVertex(vD);
        g.addVertex(vS);
        g.addVertex(vK);


        // add edges to create a circuit
        g.setEdgeWeight(g.addEdge(vH, vE), 80);
        g.setEdgeWeight(g.addEdge(vE, vH), 80);

        g.setEdgeWeight(g.addEdge(vH, vD), 130);
        g.setEdgeWeight(g.addEdge(vD, vH), 130);
        
        g.setEdgeWeight(g.addEdge(vH, vS), 570);
        g.setEdgeWeight(g.addEdge(vS, vH), 570);

        g.setEdgeWeight(g.addEdge(vE, vD), 150);
        g.setEdgeWeight(g.addEdge(vD, vE), 150);

        g.setEdgeWeight(g.addEdge(vD, vK), 170);
        g.setEdgeWeight(g.addEdge(vK, vD), 170);

        g.setEdgeWeight(g.addEdge(vK, vS), 150);
        g.setEdgeWeight(g.addEdge(vS, vK), 150);

        
       System.out.println("The following airports are used:");
       System.out.println(vH);
       System.out.println(vE);
       System.out.println(vD);
       System.out.println(vS);
       System.out.println(vK);
       
       Scanner input = new Scanner(System.in);
       
       System.out.println("Please enter the start airport: ");
       String start = input.nextLine();
       
       System.out.println("Please enter the end airport: ");
       String end = input.nextLine();
       
       input.close();
       
       DijkstraShortestPath<String, DefaultWeightedEdge> path = new DijkstraShortestPath<>(g);
       
       GraphPath<String, DefaultWeightedEdge> cheapestPath = path.getPath(start, end);
       
       System.out.println("Cheapest Path:");
       
       int sum = 0;
       int index = 0;
       
       for (DefaultWeightedEdge edge: cheapestPath.getEdgeList()) {
    	   System.out.println((++index)+". "+g.getEdgeSource(edge)+" -> "+g.getEdgeTarget(edge));
    	   int weight = (int) g.getEdgeWeight(edge);
    	   sum += weight;
       }
       
       System.out.println("Number of Stops: "+(index-1));
       System.out.println("Total Cost: "+sum);

        
	}

}
