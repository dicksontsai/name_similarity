name_similarity
===============

Find words that are similar to your name! 

Setup
----------
1. Clone this repo
2. Download the CMU pronunciation dictionary at svn.code.sf.net/p/cmusphinx/code/trunk/cmudict/
3. The file name for the dictionary should be cmudict.0.7a
4. Compile Arpabet.java using javac Arpabet.java
5. Now, enter the phonemes of your target word. To find words similar to "John" type "java Arpabet JH AA N". You can find the Arpabet symbols at http://en.wikipedia.org/wiki/Arpabet
6. The results will be in results.txt. To sort by number, run the UNIX command "cat results.txt | sort -nr > sorted.txt". Now you can view the results in sorted.txt
7. If you want to run a new query, please delete results.txt (rm results.txt) before running the java command

How to tweak
------------
Not impressed by the results? You can tweak the features in arpabet.txt, as long as the vowel-consonant distinction and binary form are preserved. Or you can modify the source code at your pleasure.

Todo
--------------
A nicer user interface.
