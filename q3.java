import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class q3 {
    public static class Edge {
        int source;
        int destination;
        int weight;

        public Edge(int source, int destination, int weight) {
            this.destination = destination - 1;
            this.source = source - 1;
            this.weight = weight;
        }

    }

    private static void shortestPath(int vertices, int source, int destination, int edges, Edge[] edgeList) {
        int[] distance = new int[vertices];
        int[] parent = new int[vertices];
        for (int i = 0; i < vertices; i++) {
            distance[i] = Integer.MAX_VALUE;
            parent[i] = -1;
        }
        distance[source] = 0;
        for (int i = 0; i < vertices - 1; i++) {
            for (int j = 0; j < edges; j++) {
                if (distance[edgeList[j].source] != Integer.MAX_VALUE
                        && distance[edgeList[j].source] + edgeList[j].weight < distance[edgeList[j].destination]) {
                    distance[edgeList[j].destination] = distance[edgeList[j].source] + edgeList[j].weight;
                    parent[edgeList[j].destination] = edgeList[j].source;
                }
            }

        }
        for (int j = 0; j < edges; j++) {
            if (distance[edgeList[j].source] != Integer.MAX_VALUE
                    && distance[edgeList[j].source] + edgeList[j].weight < distance[edgeList[j].destination]) {
                System.out.println("Negative Cycles Exist");
                return;
            }
        }
        Stack<Integer> tempStack = new Stack<Integer>();
        int node = destination;
        while (node != -1) {
            tempStack.push(node + 1);
            node = parent[node];
        }
        System.out.print("Path: ");
        while (!tempStack.empty()) {
            System.out.print(tempStack.pop() + " ");
        }
        System.out.println("\nDistance From Source: " + distance[destination]);

    }

    private static void allPaths(int vertices, int source, int destination, boolean[][] graph, ArrayList<Integer> v,
            int distance, boolean[] visited) {
        v.add(source);
        visited[source] = true;
        if (source == destination) {
            System.out.print("\nPath: ");
            for (Integer integer : v) {
                System.out.print(integer + 1 + " ");
            }
            System.out.println("\nDistance From Source: " + distance);
            visited[source] = false;
            v.remove(v.size() - 1);
            return;
        }

        for (int i = 0; i < vertices; i++) {
            if (visited[i] == false && graph[source][i]) {
                allPaths(vertices, i, destination, graph, v, distance + 1, visited);
            }
        }
        visited[source] = false;
        v.remove(v.size() - 1);
    }

    public static void main(String args[]) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter number of Vertices: ");
        int vertices = input.nextInt();
        System.out.print("Enter number of Edges: ");
        int edges = input.nextInt();
        Edge[] edgeList = new Edge[edges];
        for (int i = 0; i < edges; i++) {
            System.out.print("Enter Edge: ");
            int source = input.nextInt(), destination = input.nextInt(), weight = input.nextInt();
            edgeList[i] = new q3.Edge(source, destination, weight);
        }
        System.out.println("Enter Source and Destination");
        int source = input.nextInt(), destination = input.nextInt();
        input.close();

        System.out.println("\nShortest Path:");
        shortestPath(vertices, source - 1, destination - 1, edges, edgeList);

        System.out.println("\nAll Paths: ");
        boolean[][] graph = new boolean[vertices][vertices];
        boolean[] visited = new boolean[vertices];
        for (Edge edge : edgeList) {
            graph[edge.source][edge.destination] = true;
        }
        ArrayList<Integer> v = new ArrayList<Integer>();
        allPaths(vertices, source - 1, destination - 1, graph, v, 0, visited);
    }
}