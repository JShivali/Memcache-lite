import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RequestHandler extends  Thread {

    private Socket clientSocket;
    private BufferedReader inputFromClient;
    private PrintWriter outputForClient;
    ConcurrentMap<String, String> concurrentMap = new ConcurrentHashMap<String,String>();


    public RequestHandler(Socket clientSocket) throws IOException {
        this.clientSocket=clientSocket;
        inputFromClient = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
        outputForClient = new PrintWriter(clientSocket.getOutputStream(),true);
    }

    public void run() {
        String key = null;
        String value=null;
        String path="KVStore.txt";
        File file = new File(path);
        try {
            inputFromClient = new BufferedReader(new InputStreamReader((clientSocket.getInputStream())));
            outputForClient = new PrintWriter(clientSocket.getOutputStream(),true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try{

            while(true){
                String request=inputFromClient.readLine();
                if(request==null){
                    break;
                }
                //System.out.println("Command received is: "+ Arrays.asList(request.split("-")).toString());
                String command=request.split("-")[0];
                key=request.split("-")[1];
                if(command.contains("set")){
                    value=request.split("-")[2];
                    setValueInCache(file,key,value);
                }else if(command.equals("get")){
                    getValueFromCache(file,key,value);
                }else{
                    outputForClient.println("Command not supported");
                }
            }
        }catch (IOException e){
            System.out.println("error occurred!");
        }
        }



    public  void   setValueInCache(File file, String key, String value) throws IOException {
        if (!file.exists()) {
            if (!file.createNewFile()) {
                throw new IOException("Cannot create file KVStore.txt");
            }
        }

        try{
            // Read data from file to a concurrent map.
            readFromFile(file);

            outputForClient.flush();

            // Write to the map
            if(concurrentMap.containsKey(key)){
                // update
                concurrentMap.replace(key,concurrentMap.get(key),value);
            }else{
                // add value
                concurrentMap.putIfAbsent(key,value);
            }
            // Write the map back to file
            writeToFile(file,key);
            outputForClient.println("STORED");
        }catch(Exception e){
            outputForClient.println("NOT STORED");
        }


    }


    // Get value from the Cache(hashmap)
    public   void getValueFromCache(File file, String key, String value) throws IOException {
        // Read data from file to a concurrent map.
        readFromFile(file);
        outputForClient.flush();

        // Fetch value from hashmap and sent to client
        if(concurrentMap.keySet().contains(key)){
            outputForClient.println("VALUE "+ key + " "+concurrentMap.get(key).getBytes().length+"\r\n"+concurrentMap.get(key)+"\r\n"+"END");
        }else{
            outputForClient.println("Key not present");
            outputForClient.println("END");
        }
    }

    // Read the values from file
    public void readFromFile(File file){
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getAbsoluteFile()));
            String line;
            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split("-");
                try {
                    concurrentMap.put(splitInput[0], splitInput[1]);
                } catch (Exception e) {
                    System.out.println( "buffered reader exception");
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Write data from hashmap to file
    public void writeToFile(File file, String key){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile(), false));
            BufferedWriter finalBw = bw;
            concurrentMap.forEach((k, v) -> {
                try {
                    bw.write(k + "-" + v + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
