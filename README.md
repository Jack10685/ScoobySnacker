# ScoobySnacker
an open source zip encryption breaker featuringt a dictionary attack, brute-force attack, and scooby doo
Uses Zip4j (available here: https://github.com/srikanth-lingala/zip4j), which is packaged in with the source code

HOW TO USE:
1. Click the "Open Zip File" button and select the zip file you wish to decrypt
2. If using a dictionary attack, click the "Open Dictionary" button and select the dictionary file you want to use
3. Set how much ground the brute-force attack should cover in the next box, it will attempt all password lengths between the lower and upper bound, inclusive, with the char sets sets selected with the check boxes
4. Click the "Gangway!" button to begin
5. You can select or deselect the bottom most checkbox at any time to see each password as it is tested. Using this however, may cause lag and eat up more resources
6. The password, if one is found, will be printed in the bottom right text box upon completion

Dictionary file:
The dictionary file must be a .txt file, with each password to be tested written one-per-line
