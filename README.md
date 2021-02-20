# Memcache-lite
Implementation of Memcache-lite


### Design details:

The server java program accepts connections and creates a separate thread for the client. The server program creates a ServerSocket and accepts connections. The socket is passed to a Request Handler class that extends Thread class and is used for thread creation. The threads are added to Executors pool (I have used fixed size thread pool). The executor service provides a pool of threads along with API for assigning tasks to it. The run method in the Request handler class, decomposes the message from client and invokes a method to perform the operation. The method setValueInCache() is used to store data and getValueFromCache() to get data. These methods read values from the file and put them into a concurrent hash map before performing the set/get operation. The data is then written back to the file from the hash map with updated values.The client java program can run in two modes 1.) Interactive and 2.) Test case mode. The user can use the interactive mode the interact with the server in real-time. The user can fire get and set commands using the console that opens when the client programs run. The test mode is used to fire random commands on the server from a fixed set of commands list in the client class. While running the client program, mention the mode number and maximum number of threads through command line arguments. The setCommand() and getCommand() method composed the command in proper format from the console inputs and send command to the server.

### Instructions to run the program:
1.    Open two terminals (one for client and one for server).
2.    On the server terminal, go to the KVServerProject project folder and run the following command:
      a.mvn clean install
      b.    java -cp target/KVServer-1.0-SNAPSHOT.jar KVServer executeThis should start the server and you should see message “Server waiting for connections”
3.    On the client terminal, go to the KVClientProject project folder and run the following command:
      a.mvn clean install
      b.    java -cp target/KVClient-1.0-SNAPSHOT.jar KVClient <mode> <threads> 
  executeMode 1- interactive mode and mode 2- test case modeIf you run the program in mode 1, you will see “>>” appear on the screen. You can now write your command.If you run the program in mode 2, the testcase execution will start automatically and you will see bunch of commands printed on the screen (these are outputs of the commands being fired.)Note: To switch modes, you will have to kill the program and run in again with different command line arguments.
