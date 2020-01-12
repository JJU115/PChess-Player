# PChess-Player
<h2>Details on Using PChess</h2>
<p>
PChess is chess playing program that constructs its game trees and searches them using the alpha-beta pruning algorithm. <br>
PChess takes a parallel approach to executing these algorithms using Java's ForkJoin Pools:<br> https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ForkJoinPool.html    <br><br>
  
<b>To use:</b> This program executes through the Java-Chess framework, included in this repository.<br>
Assuming a JRE is installed, when executing this program on your own computer place all .CLASS files in the same directory. Within that directory place the Supplemental directory included in this repository. Execute the PlayChess file -> java PlayChess.<br> As of now any human user wanting to play against the computer must play the white pieces, the program will play black.<br>

Depending on the Java version the CLASS files may be out of date in which case recompiling the source files is necessary.<br>
Compile PlayChess.java and ensure all generated CLASS files are in the same directory as the Supplemental directory.<br>
Execute PlayChess.<br>

The files making up the core of the PChess playing program are:
<ul>
  <li>GameTree.java</li>
  <li>GameState.java</li>
  <li>BitMap.java</li>
  <li>Evaluate.java</li>
</ul>

<br>
<b>Important:</b> At this point in time the evaluation function is still very simple and prone to future alteration. As such the playing ability of PChess is not strong but somewhat decent.<br>

<br>
Additional future work in improving the program such as extending the evaluation function is planned. 
</p>  


<h2>Notes on Display Resolution</h2>
<p>
  The largest screen of this program is 1180 x 1050 pixels. It assumes 1920*1080 resolution with 100% scaling, anything else may display components incorrectly.
</p>
  
