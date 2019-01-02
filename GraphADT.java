import java.util.Iterator;

public interface GraphADT<T> {
	
	public int numVertices();
	public int numEdges();
	public void addVertex(T vertex);
	public void addEdge(T vertex1, T vertex2, double weight);
	public void removeVertex(T vertex);
	public void removeEdge(T vertex1, T vertex2);
	public boolean isEmpty();
	public boolean existEdge(T vertex1, T vertex2);
	//Iterator<T> DFS(T vertex);
	int numComponents();
	boolean isConnected();
	//to be completed later
	

}

