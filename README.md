# OOP_HW2
Object Oriented Programming Homework 2

DirectedWeightedGraph:
We save the vertices as a HashMap<Integer, NodeData>, the Integer is the key (NodeData.getKey()) of the NodeData.
We save the edges as a HashMap<Integer, HashMap<Integer, EdgeData>>, the first Integer is the source, and the second is the destination (of the EdgeData).
The function getNode(int key) will simply return "[NodeDataMap].get(key)" (if it exists).
The function getEdge(int src, int dest) will return "[EdgeDataMap].get(src).get(dest)", assuming they both exist.
Adding a vertex (addNode(NodeData n)) is simple - "[NodeDataMap].put(n.getKey(), n)".
Connecting between 2 vertices: "[EdgeDataMap].get(src).put(dest)", if the src exists.
Removing a vertex: remove the node with [NodeDataMap].remove(key), then remove every instance of the key in [EdgeDataMap] - if it is the source or the dest.
Removing an edge: [EdgeDataMap].get(src).remove(dest).

DirectedWeightedGraphAlgorithms:
Copying a graph is just adding all existing nodes and then connecting them based on the existing edges.
isConnected:
1. go over all vertices.
2. each time, go over all vertices again (except the current one)
3. is there a path (use the function shortestPath) from the current vertex to the one we are checking? if there is not, we break the function and return false.
4. if we arrived at the end of the funciton, it means the graph is connected, return true.
shortestPathDist:
1. calculate the shortest path (using shortestPath function)
2. add the weight of all the edges (if the path is 1->2->3->4->5, we add the weight of 1->2, and then 2->3 etc.).
3. add the weight of all nodes (there is a weight value to the nodes, all the examples had it be "0" but we guess it could be different.
4. return the sum.
center:
1. check if the graph is connected (if not, return null)
2. go over all vertices.
3. each time, go over all vertices again (except the current one)
4. add the shortest path distance from the current vertex to the observed one to the sum.
5. check which vertex has the lowest sum, return it.
shortestPath: we looked on the internet and didn't find a suiting algorithm. we currently use a stack and recursion. same for tsp.
