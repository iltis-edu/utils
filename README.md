# The Iltis project - utils

This library provides representations for graphs, trees, terms, and (tree and term) patterns. It also contains data containers and operators such as iterators that extend the functionality of the standard Java library and a customizable parsing framework based on ANTLR. This is the base library for all other Iltis repositories.

## Dependencies

Some computations in this library are outsourced to dedicated programs ("web libs"). To be able to use these functionalities, these programs have to be separately installed and accessibly by this library. The URLs for these programs have to be configured at `src/main/resources/config.xml`. This applies to the following functionalities:

* computing isomorphisms (bliss)
* computing linguistic similarities (an NLP model)
* solving (Presburger) formulas (Princess)

## Contributors

Iltis is a collaborative project between Ruhr University Bochum and TU Dortmund University under the leadership of Thomas Zeume. If you would like to contribute code or if you have any questions, please [drop us a message](mailto:iltis-feedback@ls1.cs.uni-dortmund.de).
We are grateful for all the contributions to the Iltis project. The following people have contributed to this repository:

* Sven Argo
* Jill Berg
* Lukas Dienst
* Gaetano Geck
* Jonas Haldimann
* Tristan Elias Kneisel
* Alexandra Latys
* Johannes May
* Lukas Paul Pradel
* Lars Richter
* Marius Rößler
* Patrick Roy
* Marko Schmellenkamp
* Jonas Schmidt
* Daniel Sonnabend
* Fynn Stebel
* Fabian Vehlken
* Jan Zumbrink
