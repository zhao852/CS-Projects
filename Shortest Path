import java.io.File;
import java.io.IOException;
import java.util.*;

public class DijGraph {
    static int MAXWEIGHT = 10000000;//The weight of edge will not exceed this number
    private Node[] nodeArr;//The vertices set in the graph
    private int nodeCount;//number of total vertices
    private int edgeCount;//number of total edges

    public DijGraph(String graph_file)throws IOException{
        File file = new File(graph_file);
        Scanner sc = new Scanner(file);
        nodeCount = sc.nextInt();
        edgeCount = sc.nextInt();
        nodeArr = new Node[nodeCount + 1];
        for(int i =0; i < nodeCount + 1; i ++){
            nodeArr[i]= new Node(i);
        }
        for(int i = 0;i < edgeCount; i ++){
            int begin = sc.nextInt();
            int end = sc.nextInt();
            int weight = sc.nextInt();
            nodeArr[begin].addEdge(end, weight);
            nodeArr[end].addEdge(begin,weight);
        }
    }

    //Finding the single source shortest distances by implementing Dijkstra's algorithm
    //Using min heap to find the next smallest target
    public  Dist[] dijkstra(int source){
        Dist[] result = new Dist[nodeCount +1];
        Dist[] dists = new Dist[nodeCount + 1];
        int[] a = new int[nodeCount + 1];
        Dist dist;
        int size = 0;
        for (int i = 1; i < nodeArr.length; i++) {
            if (nodeArr[i].getNodeNumber() != source) {
                dist = new Dist(i, MAXWEIGHT);
            } else {
                dist = new Dist(i, 0);
            }
            insert(dists, dist, size);
            result[i] = dist;
            size++;
        }
        while(size != 0) {
            Dist current = extractMin(dists, size);
            size--;
            a[current.getNodeNumber()] = -1;
            Integer adj[] = nodeArr[current.getNodeNumber()].getEdges().keySet().toArray(new Integer[0]);
            for (int i = 0; i < adj.length; i++ ){
                if (a[adj[i]] == -1) {
                    continue;
                }
                int b = adj[i];
                int temp = result[current.getNodeNumber()].getDist() +
                        nodeArr[b].getEdges().get(current.getNodeNumber());
                if (temp < result[b].getDist()) {
                    result[b].updateDist(temp);
                    for (int j = 0; j < size; j++) {
                        if (dists[j].getNodeNumber() == b) {
                            dists[j].updateDist(temp);
                        }
                    }
                }
            }
            Dist[] e = new Dist[nodeCount + 1];
            for (int k = 0; k < size; k++) {
                insert(e, dists[k], k);
            }
            dists = e;
        }
        return result;
    }

    //Find the vertex by the location name
    public Node findByName(String name){
        for (int x =1; x < nodeCount + 1; x++){
            if(nodeArr[x].getLocation().equals(name)){
                return nodeArr[x];
            }
        }
        return null;
    }

    //Implement insertion in min heap
    //first insert the element to the end of the heap
    //then swim up the element if necessary
    public static void insert(Dist [] arr, Dist value, int index){
        if (index >= arr.length) {
            return;
        } else {
            arr[index] = value;
            int current = index;
            while (arr[current].compareTo(arr[(current - 1)/ 2]) < 0)  {
                swap(arr, current, (current - 1) / 2);
                current = (current - 1)/ 2;
            }
        }
    }

    public static void swap(Dist []arr, int index1, int index2){
        Dist temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    //Extract the minimum element in the min heap
    //replace the last element with the root and then do minheapify
    public static Dist extractMin (Dist[] arr, int size){
        Dist popped = arr[0];
        arr[0] = arr[size - 1];
        int pos = 0;
        size--;
        if (size > 1) {
            if (arr[(pos * 2) + 1] != null && arr[(pos * 2) + 2] != null) {
                while (arr[pos].compareTo(arr[(pos * 2) + 1]) > 0 ||
                        arr[pos].compareTo(arr[(pos * 2) + 2]) > 0) {
                    if (arr[(pos * 2) + 1] != null && arr[(pos * 2) + 2] != null) {
                        if ((arr[(2 * pos) + 1].compareTo(arr[(2 * pos) + 2])) <= 0) {
                            swap(arr, pos, (2 * pos) + 1);
                            pos = 2 * pos + 1;
                        } else {
                            swap(arr, pos, (2 * pos) + 2);
                            pos = (2 * pos) + 2;
                        }
                        if (pos >= (size / 2) && pos <= size) {
                            break;
                        }
                    } else {
                        if (arr[(pos * 2) + 1] != null && arr[(pos * 2) + 2] == null) {
                            if (arr[pos].compareTo(arr[(pos * 2) + 1]) > 0) {
                                swap(arr, pos, (2 * pos) + 1);
                            }
                        }
                        break;
                    }
                }
            } else {
                if (arr[(pos * 2) + 1] != null && arr[(pos * 2) + 2] == null) {
                    if (arr[pos].compareTo(arr[(pos * 2) + 1]) > 0) {
                        swap(arr, pos, (2 * pos) + 1);
                    }
                }
            }
        }
        return popped;
    }

    //This will print the shortest distance result
    public static void printResult(Dist[] result, int source){
        for(int x = 1;  x < result.length; x++){
            if(x != source){
                System.out.println(result[x].getNodeNumber() + " " +result[x].getDist());
            }
        }
    }

    public static void main(String[] args)throws IOException {
        DijGraph graph = new DijGraph("localtest1.txt");
        Dist[] result  = graph.dijkstra(7);
        printResult(result, 7);
    }
}
