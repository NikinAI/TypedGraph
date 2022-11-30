# TypedGraph

A library to describe **Typed Condensed Oriented Directed Acyclic Planar Multigraphs**.

## Definitions

| Keyword                   | Definition                                                                                                                                        |
|---------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| **Vertex** / **Vertices** | Sometimes called a *Node(s)*, they are the components of the graph                                                                                |
| **Edge**                  | Joins two vertices. They are the second component of the graph                                                                                    |
| **Directed Edge**         | An edge that has a one way traversal direction. Only from *start vertex* to *end vertex* but not from end *vertex back* to *start vertex*.        |
| **Graph**                 | Collection of edges and vertices                                                                                                                  |
| **Path**                  | List of vertices and edges with no repeated vertices                                                                                              |
| **Predecessor**           | The vertex before a given vertex on a path                                                                                                        |
| **Successor**             | The vertex after a given vertex on a path                                                                                                         |
| **Cycle**                 | A List of vertices and edges looping back to itself. The start vertex is the same as the end vertex                                               |
| **Loop**                  | An edge that connect a vertex to itself                                                                                                           |
| **Directed Graph**        | A graph where the edges have a direction *from* and *to* a vertex                                                                                 |
| **Acyclic Graph**         | A graph that does not contain any cycles                                                                                                          |
| **Planar Graph**          | A graph that can be drawn in such a way that no edges cross each other                                                                            |
| **Condensed Graph**       | A graph where every pair of vertices are connected by either zero or one edge                                                                     |
| **Multigraphs**           | A graph without loops                                                                                                                             |
| **Oriented Graph**        | A graph without any symmetric pair of directed edges                                                                                              |
| **Typed Graph**           | Not a term from Graph Theory but from Computer Science. A typed graph is a graph where each one of the edges and the nodes has a given data type. |

Sources:
* https://www.statisticshowto.com/graph-theory
* https://en.wikipedia.org/wiki/Planar_graph

## Potential applications

Potential applications for this library would be:

* UML Class diagram of calls and dependencies
    * Each vertex can be of the type of the Class 
    * Each edge can be of type: Dependency, Method return, Method argument, etc...
* Social Network exploration
* Blockchain currency flow
    * Transaction graph
    * Ownership flow
* Knowledge graph
* Data pipeline in data engineering applications
    * Each vertex can be a step in data processing
    * Each edge can be a type of data transfer
* Definition of neural network / machine learning
* Civil engineering - city, road network

## Objectives

### Graph

The output of the library should be a `Graph` that allow graph operations such as traversal, cycle detection, path finding, etc...

Because it is a directed graph, we can also find predecessor of a given vertex.

### Vertices Typed-constraints

The creation of the graph must be Vertex-typed-constraint. 

For given vertex types: A, B and C 
For given edge types: F and G

The user should be able to define authorized condition:

For instance, `A` connected to `B` through `F` can be allowed, but not `B` to `A`.

Let's define a set of rules using the following syntax:

`VERTEX_FROM >EDGE_TYPE> VERTEX_TO`

Rules: 

* `A >F> B`
* `A >G> C`
* `B >F> C`

Based on those rules, the library should behave in the following way:

```scala
// Connect two vertices
def >>> = ???

def a(): A = ???
def b(): B = ???
def c(): C = ???

// val e1 = a() >>> a() // Should not compile
val e2: F[A, B] = a() >>> b()
val e3: G[A, C] = a() >>> c()

// val e4 = b() >>> a() // Should not compile
// val e5 = b() >>> b() // Should not compile
val e6: F[B, C] = b() >>> c()

// val e7 = c() >>> a() // Should not compile
// val e8 = c() >>> b() // Should not compile
// val e9 = c() >>> c() // Should not compile

// val e10: F[A, B] = a() >>> b() >>> a() // Should not compile
// val e11: F[A, B] = a() >>> b() >>> b() // Should not compile
val e12: F[A, F[B, C]] = a() >>> b() >>> c()

// val e3: G[A, C] = a() >>> c() >>> a() // Should not compile
// val e3: G[A, C] = a() >>> c() >>> b() // Should not compile
// val e3: G[A, C] = a() >>> c() >>> c() // Should not compile

// val e6: F[B, C] = b() >>> c() >>> a() // Should not compile
// val e6: F[B, C] = b() >>> c() >>> b() // Should not compile
// val e6: F[B, C] = b() >>> c() >>> c() // Should not compile 
```


### Content typed-constraints

Now imagine that each vertex could carry information about what they do in a business application. Maybe which content they transmit or which class they are transforming, or which type of business it is, or which cryptocurrency it is, etc...

In this implementation of a graph, we would need a new type of constraint: a *content flow contraint*.

Based on the previous section related to *Node constraint*, let's use this new syntax:

`VERTEX_FROM[IN, TRANSIT] >EDGE_TYPE> VERTEX_TO[TRANSIT, OUT]`

Example of rules:

Vertices:

* `Storage`
* `Pan` 
* `Pot`
* `Knives`
* `Plates`

Edges:

* `Cut`
* `Cook`
* `Serve`

Content:

* `Steak`
* `Potatoes`
* `Peeled_potatoes`
* `Cooked_steak`
* `Cooked_potatoes`
* `Meal`

Graph example:

```scala

val potato_cooking = Storage[Potatoes] >>> Knives[Potatoes, Peeled_potatoes] >>> Pot[Peeled_potatoes, Cooked_potatoes] >>> Plates[Cooked_potatoes && Cooked_steak]

val steak_cooking = Storage[Steak] >>> Pan[Steak, Cooked_steak] >>> Plates[Cooked_potatoes && Cooked_steak]

val graph = potato_cooking ++ steak_cooking

```

Rules should be able to be written to allow this type of graph structure and no other. For instance, we do not want to allow `Knives[Peeled_potatoes, Potatoes] >>> Pot[Peeled_potatoes, Cooked_potatoes]` because the content flow would be inconsistent. Following the previous section, we can also write rules to forbid edge construction like `Pot >>> Storage`.
