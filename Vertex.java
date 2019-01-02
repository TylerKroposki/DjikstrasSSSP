
/**
 * This class is a representation of a Vertex.
 */
public class Vertex implements Comparable<Vertex>{
    //The Vertex's coordinates.
    private int x, y;

    //The Vertex's location name.
    private String vertexName;

    public Vertex(int x, int y, String vertexName) {
        this.x = x;
        this.y = y;
        this.vertexName = vertexName;
    }

    //Creates a default Vertex if no parameters are given.
    public Vertex() {
        this.x = -1;
        this.y = -1;
        this.vertexName = null;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getVertexName() {
        return vertexName;
    }

    public void setVertexName(String vertexName) {
        this.vertexName = vertexName;
    }

    public boolean equals(Vertex v) {
        return ((Vertex) v).vertexName.equals(vertexName);
    }

    public int compareTo(Vertex v) {
        return vertexName.compareTo(((Vertex) v).vertexName);
    }

    public double distance(Vertex v2) {
        return Math.sqrt((this.getX() - v2.getX()) * (this.getX() - v2.getX()) + (this.getY() - v2.getY()) * (this.getY() - v2.getY()));
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", vertexName='" + vertexName + '\'' +
                '}';
    }
}