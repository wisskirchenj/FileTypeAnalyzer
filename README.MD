# IDEA EDU Course ...

Implemented in the Java Core Track of hyperskill.org's JetBrain Academy.

Purpose of doing this project, is to further practise core java topics as multi-threading, file handling, applying
new algorithms as Knuth-Morris-Pratt algorithm and Rabin-Karp algorithm for data hashing, searching and sorting - and just some more POJO java.

Also: This is the graduate project of the Java Core Track, which covers the most yet undone learning topics and thus
will serve to finish this last unfinished Jetbrains java track.

## Technology / External Libraries

- POJO Java 18,
- multi-threading with Java Executor-Service (Java-core .util.concurrent)
- speed-streaming searches of arbitrarily large files - e.g >65 GB docker files... (in <60 sec.)
- prefix, hashing and search algorithms as Knuth-Morris-Prath (best performance!) and Rabin-Karp.
- with Lombok annotation processors,
- Apache Log4j SLF4J API binding to Log4j 2 logging and
- Junit 5 with
- Mockito (mockito-inline) testing.

## Repository Contents

The sources of main project tasks (5 stages) and unit, mockito testing.

## Program description

An application / tool a tool that will extract info from unknown (also binary) files
to determine the type of the file. Different algorithms are used to solve this problem, and will
be compared regarding the speed (of search and sorting).

CL-Command overview:

> usage: java fileanalyzer.Main "<directory-path>" "<patterns-csv-path>"

all files in the directory path given are "file-type-analyzed" using the patterns-csv-file given as
a dataset for prioritized search patterns to detect file types... 
Priorization means, that e.g. when we find the identifying character sequences for a zip-File AND a
MS PowerPoint-file in one file, then we know it is a PPT, because all PPT are stored as Zips.
this is realized, by giving the identifying PPT-sequence a higher priority than the zip-sequence.

Have fun!

## Project completion

Project was completed on 20.09.22.

## Progress

30.08.22 Project started - just git repo and gradle setup.

02.09.22 Stage 1 completed - analyze arbitrary (also binary) file given as argument for occurrence of a (type defining)
string - also given as argument. Implemented in two versions with Files.readAllBytes and FileInputStream for huge files.

08.09.22 Stage 2 completed - Knuth-Morris-Pratt (KMP) algorithm implemented accelerating the string search about a factor 4-5.
Used strategy pattern in controller class to dynamically choose a SearchAlgorithm (naive vs KMP)

12.09.22 Stage 3 completed - parallelize the search on all files of a directory for a search pattern by use of an executor
service with KMP-algorithm - use of DirectoryStream.

15.09.22 Stage 4 completed - Read in a CL-argument specified CSV-file with prioritized search patterns to identify the file
type of an arbitrary (binary) file. Perform parallelized multi-threaded search on all files of a directory for all the patterns.

20.09.22 Final Stage 5 completed - Implemented a multi-pattern search with variable length patterns (not ideal) using 
Rabin-Karp algorithm and polynomial rolling hash functions. Though carefully implemented - optimized for speed, the performance
is about a factor 3 to 5 worse, than with the Knuth-Morris-Pratt algorithm.