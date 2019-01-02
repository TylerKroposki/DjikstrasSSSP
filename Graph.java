
public class Graph<T> extends WGraph<T> {
	

	public Graph() {
		super();
	}
	

	public void addEdge(T vertex1, T vertex2) {
		if(isValidVertex(vertex1)  && isValidVertex(vertex2) 
				&& vertex1 != vertex2){
			super.addEdge(vertex1, vertex2, 1 );
			
		}
	}
	
	
	@Override
	public void addEdge(T vertex1, T vertex2, double weight) {
		if(isValidVertex(vertex1)  && isValidVertex(vertex2) && vertex1 != vertex2 && weight >= 0){
			super.addEdge(vertex1, vertex2,1 );

			
		}
	}
	
}
