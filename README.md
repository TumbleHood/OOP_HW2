# OOP_HW2
Object Oriented Programming Homework 2

# DirectedWeightedGraph:

We save the vertices as a HashMap<Integer, NodeData>, the Integer is the key (NodeData.getKey()) of the NodeData.

We save the edges as a HashMap<Integer, HashMap<Integer, EdgeData>>, the first Integer is the source, and the second is the destination (of the EdgeData).

The function getNode(int key) will simply return "[NodeDataMap].get(key)" (if it exists).

The function getEdge(int src, int dest) will return "[EdgeDataMap].get(src).get(dest)", assuming they both exist.

Adding a vertex (addNode(NodeData n)) is simple - "[NodeDataMap].put(n.getKey(), n)".

Connecting between 2 vertices: "[EdgeDataMap].get(src).put(dest)", if the src exists.

Removing a vertex: remove the node with [NodeDataMap].remove(key), then remove every instance of the key in [EdgeDataMap] - if it is the source or the dest.

Removing an edge: [EdgeDataMap].get(src).remove(dest).



# DirectedWeightedGraphAlgorithms:

Copying a graph is just adding all existing nodes and then connecting them based on the existing edges.


isConnected:

go over all vertices.

each time, go over all vertices again (except the current one)

is there a path (use the function shortestPath) from the current vertex to the one we are checking? if there is not, we break the function and return false.

if we arrived at the end of the funciton, it means the graph is connected, return true.


shortestPathDist:

calculate the shortest path (using shortestPath function)

add the weight of all the edges (if the path is 1->2->3->4->5, we add the weight of 1->2, and then 2->3 etc.).

add the weight of all nodes (there is a weight value to the nodes, all the examples had it be "0" but we guess it could be different.

return the sum.


center:

check if the graph is connected (if not, return null)

go over all vertices.

each time, go over all vertices again (except the current one)

add the shortest path distance from the current vertex to the observed one to the sum.

check which vertex has the lowest sum, return it.


shortestPath:

if the source and dest are equal, return just this node

go over all edges connected to the source

for each dest in edge, repeat the process for the dest in the edge and the initial dest, while pushing the source to a stack and popping it later, so we won't go in a loop.

add the path to the paths list

calculate the shortest path

return this path


# testing with larger graphs:

1,000 - load: 0.118s, save: 0.033s

10,000 - load: 0.064s, save: 0.055s
100000
0.483
0.4
1000000
5.417
2.533
