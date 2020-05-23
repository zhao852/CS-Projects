import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class KruGraph {
    private Vertex[] vertexArr;
    private ArrayList<MyEdge> edgeArr;
    private int vertexCount;
    private int edgeCount;

    //The constructor for KruGraph
    public KruGraph(String graph_file)throws IOException{
        edgeArr = new ArrayList<>();
        File file = new File(graph_file);
        Scanner sc = new Scanner(file);
        vertexCount = sc.nextInt();
        edgeCount = sc.nextInt();
        vertexArr = new Vertex[(vertexCount + 1)];
        for(int i = 1; i < vertexCount + 1; i++){
            vertexArr[i] = new Vertex(i);
        }
        for(int i = 0; i < edgeCount; i ++){
            int begin = sc.nextInt();
            int end = sc.nextInt();
            int weight = sc.nextInt();
            addEdge(begin, end, weight);
        }
    }

    private void addEdge(int from, int to, int weight){
        MyEdge edge = new MyEdge(from, to, weight);
        edgeArr.add(edge);
    }


    //Kruskal's algorithm with weighted union find algorithm
    public PriorityQueue<MyEdge> kruskalMST(){
        PriorityQueue<MyEdge> pq = new PriorityQueue<>();

        for (int i = 0; i < edgeArr.size(); i++) {
            pq.add(edgeArr.get(i));
        }

        PriorityQueue<MyEdge> mst = new PriorityQueue<>();

        int index = 1;
        while (index < vertexCount) {
            MyEdge edge = pq.remove();
            if (find(vertexArr[edge.getS()]) != find(vertexArr[edge.getD()])) {
                mst.add(edge);
                index++;
                union(vertexArr[edge.getS()], vertexArr[edge.getD()]);
            }
        }
        return mst;
    }

    public static Vertex find(Vertex x){
        Vertex p = x;
        if (x.getParent() != x) {
            x = find(x.getParent());
        }
        p.updateParent(x);
        return x;
    }


    //Function unions two vertices when an edge is added to the MST
    //Return true when the edge can be picked in the MST
    //Otherwise return false
    public static boolean union(Vertex x, Vertex y){
        Vertex xroot = find(x);
        Vertex yroot = find(y);
        int newsize = yroot.getSize() + xroot.getSize();
        if (xroot == yroot) {
            return false;
        } else {
            if (xroot.getSize() <= yroot.getSize()) {
                yroot.updateSize(newsize);
                xroot.updateParent(yroot);
                return true;
            } else {
                xroot.updateSize(newsize);
                yroot.updateParent(xroot);
                return true;
            }
        }
    }

    public static void printGraph(PriorityQueue<MyEdge> edgeList){
        int turn = edgeList.size();
        for (int i = 0; i < turn; i++) {
            MyEdge edge = edgeList.poll();
            int source = edge.getS();
            int dest = edge.getD();
            if(source > dest){
                int temp = source;
                source = dest;
                dest = temp;
            }
            System.out.println("from: " + source + " to: " + dest + " weight: " + edge.getWeight());
        }
    }

    public static void main(String[] args) throws IOException {
        KruGraph graph = new KruGraph("localtestkru1.txt");
        printGraph(graph.kruskalMST());
    }
}
