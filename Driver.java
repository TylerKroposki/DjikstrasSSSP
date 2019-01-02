import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

//JavaFX Imports
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

//import java.util.Arrays; used for Arrays.toString() for debugging purposes.


/**
 * @author Tyler Kroposki
 * This class is the main Driver of the application.
 * File reading, processing, and final output is done within this class.
 * Although I thoroughly recognize the monstrosity that is this class, it got the job done for the assignment and earned me a 200/200.
 */
public class Driver extends Application {

    private final int WINDOW_WIDTH = 500;
    private final int WINDOW_HEIGHT = 500;

    //Create a new Weighted Graph. This is held outside of the main method so it can be used by other areas of the class.
    private static WGraph graph = new WGraph();

    //String containing the name of the file that is used to add information to the graph.
    private static String FILENAME = "graph.dat";

    //Stores all of the Vertices.
    private static List<Vertex> vertexList = new ArrayList<>();

    //These are initialized here because they are accessed through other methods other than the JavaFX methods.
    private Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
    private GraphicsContext gc = canvas.getGraphicsContext2D();

    //Populate the graph with edges and vertices from an input file. Return array containing solution.
    private static List<Vertex> solutions;
    {
        try {
            solutions = populateGraph(graph, FILENAME, gc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        launch(args);
    }

    /**
     *
     * @param graph - Pass a WGraph that will be manipulated.
     * @param FILENAME - an argument that provides the file path to be used.
     * @throws IOException
     * @return
     */
    public static List<Vertex> populateGraph(WGraph graph, String FILENAME, GraphicsContext gc) throws IOException {

        //Constants retrieved from specific lines in the file.
        int numVertices;
        int numEdges;

        BufferedReader bufferedReader = new BufferedReader(new FileReader(FILENAME));

        //Assuming that the first line of the file is an integer representing the number of vertices.
        numVertices = Integer.parseInt(bufferedReader.readLine());

        //A temporary storage array containing the vertices.
        ArrayList<Vertex> vertexArr = new ArrayList<>(numVertices);

        //Iterate numVertices amount of times adding each line to the graph.
        for(int i = 0; i < numVertices; i++) {

            //Since the x-coordinate, y-coordinate,
            // and vertex name are separated by a space on a single line,
            // they must be broken apart and handled.
            String[] tempVertArr = bufferedReader.readLine().trim().split("\\s+");

            //tempVertArr[0] = x-coordinate
            //tempvertArr[1] = y-coordinate
            //tempVertArr[2] = vertex name
            //A new vertex on the graph is created.
            Vertex vertex = new Vertex(Integer.parseInt(tempVertArr[0]), Integer.parseInt(tempVertArr[1]), tempVertArr[2]);

            //Add the new vertex to the graph.
            vertexArr.add(vertex);
            graph.addVertex(vertex);

            //Used for drawing the final screen.
            vertexList.add(vertex);

        }//end vertice loop


        //Assuming after the vertices that a number representing the number edges is present.
        numEdges = Integer.parseInt(bufferedReader.readLine());

        //Iterate numEdges amount of times adding each edge to the graph. Weights are as well calculated within this stage.
        for(int i = 0; i < numEdges; i++) {

            //Separates the two points into separate indices of a String array.
            String[] tempEdgeArr = bufferedReader.readLine().trim().split("\\s+");

            //tempEdgeArr[0] = Vertex 1
            //tempEdgeArr[1] = Vertex 2
            String point1 = tempEdgeArr[0];
            String point2 = tempEdgeArr[1];

            //Temporary Vertex objects
            Vertex v1 = null, v2 = null;

            //Searches through the temporary vertex's array to assign the corresponding vertex object to the temporary object.
            for (Vertex vertex : vertexArr) {
                if (vertex.getVertexName().equals(point1)) {
                    v1 = vertex;
                }
                if (vertex.getVertexName().equals(point2)) {
                    v2 = vertex;
                }
            }

            //Calculates the weight between the two vertices of the edge.
            double weight = v1.distance(v2);

            //The edge is finally able to be added to the graph.
            graph.addEdge(v1, v2, weight);

            drawLine(gc, v1, v2);

        } //end edge loop


        //Skips the blank line in the file
        bufferedReader.readLine();

        //Separates the two points into separate indices of a String array.
        String[] sourceDestinationArray = bufferedReader.readLine().trim().split("\\s+");

        //sourceDestinationArray[0] = Vertex 1
        //sourceDestinationArray[1] = Vertex 2
        String point1 = sourceDestinationArray[0];
        String point2 = sourceDestinationArray[1];

        //Temporary Vertex objects
        Vertex v1 = null, v2 = null;

        //Searches through the temporary vertex's array to assign the corresponding vertex object to the temporary object.
        for (Vertex vertex: vertexArr) {
            if (vertex.getVertexName().equals(point1)) {
                v1 = vertex;
            }
            if (vertex.getVertexName().equals(point2)) {
                v2 = vertex;
            }
        }

        //Find the singleShortestPath
        //graph.singleSourceShortestPath(v1);

        //Return the list that contains the path between Vertex 1 and Vertex 2.
        return graph.shortestPath(v1, v2);

    }


    /**
     * This portion of the class handles the GUI used for the application.
     */


    @Override
    public void start(Stage stage) {

        stage.setTitle("| Djikstra's Shortest Path - By Tyler Kroposki |");
        Group root = new Group();
        drawImage(gc);
        root.getChildren().add(canvas);
        stage.setScene(new Scene(root));
        stage.show();
    }




    private void drawImage(GraphicsContext gc) {
        drawPoints(gc, solutions);
        drawPath(gc, solutions);

    }


    private void drawPoints(GraphicsContext gc, List<Vertex> list) {

        //Draw every Vertex.
        for(Vertex v : vertexList) {
            drawPoint(gc, v);
        }

        //Highlight each Vertex in the list that is part of the Solution.
        for(Vertex v : list) {
            //This will recolor the solution Vertices.
            drawSolutionPoint(gc, v);
        }
    }

    private void drawPoint(GraphicsContext gc, Vertex v) {
        int width = 20;   //point diameter
        gc.setFill(Color.GREY);
        gc.fillOval(v.getX() - width/2, v.getY()- width/2, width, width);
        gc.setFill(Color.BLACK);
        //Prints the Vertex's name
        gc.fillText(v.getVertexName(), v.getX(), v.getY());

    }

    //Highlights Vertexes that are part of the solution.
    private void drawSolutionPoint(GraphicsContext gc, Vertex v) {
        int width = 20;   //point diameter
        gc.setFill(Color.BLUE);
        gc.fillOval(v.getX() - width/2, v.getY()- width/2, width, width);
        gc.setFill(Color.BLACK);
        gc.fillText(v.getVertexName(), v.getX(), v.getY());
    }


    //Draws the solution.
    private void drawPath(GraphicsContext gc, List<Vertex> list) {

        Vertex v1, v2;

        //Adjusted by + 1 to avoid list.get(-1);
        for(int i = 1; i < list.size(); i++) {
            v2 = list.get(i);
            v1 = list.get(i - 1);
            drawSolutionLine(gc, v1, v2);

        }
    }



    private static void drawLine(GraphicsContext gc, Vertex v1, Vertex v2) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(v1.getX(), v1.getY(), v2.getX(), v2.getY());


    }

    //Recolors the solution line so it is visible.
    public void drawSolutionLine(GraphicsContext gc, Vertex p1, Vertex p2) {
        gc.setStroke(Color.MAGENTA);
        gc.setLineWidth(5);
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }



}
