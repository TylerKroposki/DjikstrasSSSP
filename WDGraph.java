import javafx.application.Application;

import java.util.*;
public class WDGraph<T> implements GraphADT<T> {


	private int CAPACITY = 2;
	private final double INFINITY = Double.POSITIVE_INFINITY;
	private final int UNDEFINED = -1;
	private int numVertices;
	private int numEdges;
	private double[][] adjMatrix;
	private T[] vertices;

	public WDGraph() {
		numVertices = 0;
		numEdges = 0;

		//Stores the edges within the graph. Acts as a coordinate system for finding an edge between two Vertices.
		adjMatrix = new double[CAPACITY][CAPACITY];

		//Stores the actual Vertex objects. It is assumed each input is a Vertex object for this assignment's sake.
		vertices = (T[]) new Object[CAPACITY];

		//Make every single weight in the graph equal to infinity.
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				adjMatrix[i][j] = INFINITY;
			}
		}

	}





	public String toString(){
		int GAP = 5;
		if(numVertices == 0)
			return "Graph is empty";
		String result = "";

		result += String.format("%7s", "");
		for (int i = 0; i < numVertices; i++)
			result += String.format("%7s", vertices[i]);
		result += "\n";

		for (int i = 0; i < numVertices; i++) {
			result += String.format("%7s", vertices[i]);

			for (int j = 0; j < numVertices; j++) {
				if(adjMatrix[i][j] == INFINITY)
					//result += String.format("%7s", "inf");
					result += String.format("%7c", '\u221e');
				else
					result += String.format("%7.2f", adjMatrix[i][j]);
			}
			result += "\n";
		}


		return result;

	}

	@Override
	public int numVertices() {
		return numVertices;
	}

	@Override
	public int numEdges() {
		return numEdges;
	}

	@Override
	public void addVertex(T vertex) {
		if(!isValidVertex(vertex)) {
			if(numVertices == CAPACITY)
				expand();
			vertices[numVertices] = vertex;    //check to see if expansion is needed
			numVertices++;
		}
		//need to throw exception or assertion if vertex already exists

	}

	/**
	 *
	 * @param vertex
	 * @return  the index of the vertex.  return -1 if not found
	 */
	protected int vertexIndex(T vertex){
		for(int i = 0; i < numVertices; i++)
			if(vertices[i].equals(vertex))
				return i;
		return -1;
	}


	protected boolean isValidVertex(T vertex){
		for(int i = 0; i < numVertices; i++)
			if(vertices[i].equals(vertex))
				return true;
		return false;
	}


	/**
	 * add or update an edge
	 */
	@Override
	public void addEdge(T vertex1, T vertex2, double weight) {
		if(isValidVertex(vertex1)  && isValidVertex(vertex2) && vertex1 != vertex2 && weight >= 0){
			if(!this.existEdge(vertex1, vertex2))
				numEdges++;
			adjMatrix[vertexIndex(vertex1)][vertexIndex(vertex2)] = weight;

		}
	}

	@Override
	public void removeVertex(T vertex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEdge(T vertex1, T vertex2) {
		if(existEdge(vertex1, vertex2)){
			adjMatrix[vertexIndex(vertex1)][vertexIndex(vertex2)] = INFINITY;
			numEdges--;
		}
	}

	@Override
	public boolean isEmpty() {
		return numVertices == 0;
	}

	@Override
	public boolean existEdge(T vertex1, T vertex2) {
		return isValidVertex(vertex1) &&
				isValidVertex(vertex2) &&
				(adjMatrix[vertexIndex(vertex1)][vertexIndex(vertex2)] != INFINITY);
	}

	@Override
	public int numComponents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}


	private void expand() {
		int newCapacity = CAPACITY * 2;

		double[][] newAdjMatrix = new double[newCapacity][newCapacity];
		T[] newVertices = (T[]) new Object[newCapacity];

		for(int i = 0; i < numVertices; i++)
			newVertices[i] = vertices[i];

		for (int i = 0; i < newAdjMatrix.length; i++)
			for(int j = 0; j < newAdjMatrix.length; j++)
				newAdjMatrix[i][j] = INFINITY;

		for (int i = 0; i < numVertices; i++)
			for(int j = 0; j < numVertices; j++)
				newAdjMatrix[i][j] = adjMatrix[i][j];

		adjMatrix = newAdjMatrix;
		vertices = newVertices;
		CAPACITY = newCapacity;


	}

	/**
	 * lists all the beighbors of the gioven vertex
	 * @param vertex
	 * @return
	 */
	public List<T> neighbors(T vertex) {
		if(!isValidVertex(vertex))
			return null;
		int row = vertexIndex(vertex);
		ArrayList<T> list = new ArrayList();
		for(int i = 0; i < numVertices; i++)
			if(adjMatrix[row][i] != INFINITY)
				list.add(vertices[i]);
		return list;

	}


	/**
	 * returns the next neighbor after the current neighbor, if exists
	 * @param vertex
	 * @param currNeighbor
	 * @return
	 */
	public T nextNeighbor(T vertex, T currNeighbor) {
		if(!isValidVertex(vertex) || !isValidVertex(currNeighbor))
			return null;
		int row = vertexIndex(vertex);
		int col = vertexIndex(currNeighbor);
		for(int i = col + 1; i < numVertices; i++)
			if(adjMatrix[row][i] != INFINITY)
				return vertices[i];
		return null;

	}

	public double getWeight(T v1, T v2) {
			return adjMatrix[vertexIndex(v1)][vertexIndex(v2)];
	}

	/**
	 * method uses Dijksra's single source shortest path algorithm
	 * It returns the previous array containing the shortest previous
	 * vertices from the given source vertex to all other
	 * vertices.
	 *
	 * @param start
	 * @return previous array
	 */
	public T singleSourceShortestPath(T start) {

		//This checks to determine if the input is a Vertex or not.
		if(start instanceof Vertex) {

			//Cast the input to a Vertex.
			int sourceIndex = vertexIndex(start);

			//Stores each Vertex's parent; Example: previous[1] = Each index represents a Vertex, and the value within that Index of the array,
			int[] previous = new int[numVertices];

			double[] distance = new double[numVertices];

			boolean[] visited = new boolean[numVertices];

			//Iterate through each Vertex.
			for (int i = 0; i < numVertices; i++) {

				//Make each Vertex's previous undefined.
				previous[i] = UNDEFINED;
				distance[i] = INFINITY;
				visited[i] = false;
			}


			distance[sourceIndex] = 0;

			//Runs until there aren't any more unprocessed Vertexes. This is done because it ensures every Vertex has been processed.
			while (containsFalse(visited)) {

				//Retrieve the minimum, unvisited Vertex.
				T v = vertices[shortest(distance, visited)];

				//Retrieve neighbors of v and add to a List.
				List<T> neighbors = neighbors(v);

				//Traverse through each neighbor within the list.
				for(T N : neighbors) {

					//The distance to v.
					double tempVDist = distance[vertexIndex(v)];

					//The distance to N.
					double tempNDist = distance[vertexIndex(N)];

					//Distance of V + weight of edge from V to N.
					double distVAndEdgeFromVToN = tempVDist + getWeight(v, N);

					//Check if the calculated distance is less than what is current.
					if(distVAndEdgeFromVToN < tempNDist) {

						//Update distance of N to be the distance of V + weight of edge from v to N.
						distance[vertexIndex(N)] = distVAndEdgeFromVToN;

						//Set v as previous to N.
						previous[vertexIndex(N)] = vertexIndex(v);

					}
					//Flag that the Vertex has been processed.
					visited[vertexIndex(v)] = true;

				}
			}

			//This is our solution.
			return (T) previous;
		}

		//return if the input isn't a Vertex object.
		return null;
	}

	//Iterates through a boolean array to determine if it contains any false values.
	private boolean containsFalse(boolean[] visited) {
		if (visited.length > 0) {
			for (int i = 0; i < visited.length; i++) {
				if (!visited[i]) {
					return true;
				}
			}
		}
		return false;
	}

	//Finds the minimum distance from the source Vertex.
	private int shortest(double[] distance, boolean[] visited) {

		//Create an undefined index to begin.
		int minIndex = UNDEFINED;

		//Set minDistance = a number that will be grater than any other.
		double minDist = INFINITY;

		//Loop until a shortest is found.
		for (int i = 0; i < visited.length; i++ ) {

			//Check if i is visited, and if its distance is less than the current min.
			if (!visited[i] && distance[i] < minDist) {

				//If so, make the minIndex = the current index
				minIndex = i;

				//The minimum distance is = the current distance of index i.
				minDist = distance[i];
			}
		}

		//return the index of the Vertex with the minimum distance from the source.
		return minIndex;
	}

	/**
	 *
	 * @param sourceVertex - Beginning Vertex
	 * @param destinationVertex - End Vertex
	 *
	 * @return List<T> of the shortest path from sourceVertex to destinationVertex.
	 *
	 * Solution: 15 -> 14 -> 18 -> 7 -> 2 -> 3 -> 4 -> 6
	 */
	public List<T> shortestPath(T sourceVertex, T destinationVertex) {

		//These two if statements determine if each input is actually a Vertex.
		if(sourceVertex instanceof Vertex) {
			if (destinationVertex instanceof Vertex) {

				//Since an abstract is retrirved from Dijkstra's SSSP, it must be casted to an int.
				int[] pathBetweenVertices = (int[]) singleSourceShortestPath(sourceVertex);

				//Stores the previous
				int previousVertex = vertexIndex(destinationVertex);

				//List storing the solution.
				List<Vertex> solutionList = new ArrayList();

				//The destination Vertex must be found and added first.

				int tempVert = previousVertex;

				solutionList.add((Vertex) vertices[tempVert]);

				//Since the source will have no predecessor and be = -1, we run until this condition is found.
				while(!(pathBetweenVertices[previousVertex] == UNDEFINED)) {

					//Assign new tempVert.
					tempVert = pathBetweenVertices[previousVertex];

					//Assign new previous.
					previousVertex = pathBetweenVertices[previousVertex];

					//Add the Vertex to the solutionList.
					solutionList.add((Vertex) vertices[tempVert]);

				}

				//Since the output goes from Destination to Solution, reversing it puts it in order from Source to Destination.
				Collections.reverse(solutionList);

				//solutionList will contain the path of Vertexes from sourceVertex to destinationVertex.
				return (List<T>) solutionList;

			}
		}

		return null;
	}


}
 