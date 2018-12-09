# PChess-Player
<h2>Details on Using PChess</h2>
<p>
PChess is chess playing program that constructs its game trees and searches them using the alpha-beta pruning algorithm. <br>
PChess takes a parallel approach to executing these algorithms using Java's ForkJoin Pools:<br> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ForkJoinPool.html    <br><br>
  
<b>To use:</b> This program executes through the Java-Chess framework, included in this repository.<br>
When executing this program on your own computer place all .CLASS files in the same directory. Within that directory place the Supplementary directory included in this repository. Execute the PlayChess file -> java PlayChess.<br> As of now any human user must play the white pieces, the program will play black.<br>

The files making up the core of the PChess playing program are:
<ul>
  <li>GameTree.java</li>
  <li>GameState.java</li>
  <li>BitBoard.java</li>
  <li>Evaluate.java</li>
</ul>

<br>
Additional future work in improving and documenting the program is planned. 
</p>  
