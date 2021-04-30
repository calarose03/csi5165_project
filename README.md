Project: Graph Invariants and Graph Isomorphisms
----------------------------------------

The program takes two files, builds two graphs and attempt to compute isomorphism for these two graphs.

To run the algorithm, run this command to compile:

`javac -cp .;../resources/commons-math3-3.6.1/commons-math3-3.6.1.jar; graph/isomorphism/RunGraphIsomorphism.java`

Then, run this command:

`java -cp .;../resources/commons-math3-3.6.1/commons-math3-3.6.1.jar; graph.isomorphism.RunGraphIsomorphism
[graphfilename1] [graphfilename2]`

where `graphfilenameX` refers to the file of the graph you wish to compute the isomorphism for. These files can be found in the `data` folder.

The program will ask how many invariants to use for the partition refinement step, and then to choose these invariants.

The output of the program is the list of the invariants in order, as well as the result of the algorithm.
If a mapping is found, it will be shown as well.

