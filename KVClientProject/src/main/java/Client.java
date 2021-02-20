import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.net.*;


public class Client {

    // List to store test commands when running test
    static List<String> commands=new ArrayList();
    static List<String> values=new ArrayList();

    // Mode of operation of client - interactive mode or test mode
    int mode;

    // Socket config
    Socket socket = new Socket(InetAddress.getLocalHost().getHostAddress(), 9090);
    BufferedReader clientKeyboard = new BufferedReader(new InputStreamReader(System.in));
    BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    PrintWriter pr = new PrintWriter(socket.getOutputStream(), true);
    String command=null;
    String value=null;


    public Client(int mode) throws IOException {
        this.mode=mode;
    }

    // Method invoked on client object when using interactive mode
    public void interactiveMode() {
        try {
            while(true) {
                System.out.print(">>");
                if (mode==1){
                    command= clientKeyboard.readLine();
                    if(command.contains("set")){
                        value=clientKeyboard.readLine();
                    }
                }
                String params[] = command.split(" ");
                if (params[0].contains("set")) {
                    setCommand(params[0],params[1],params[2],value);
                } else if (params[0].contains("get")) {
                    getCommand(params[0],params[1]);
                } else if (params[0].equals("quit")) {
                    break;
                } else {
                    System.out.println("Invalid command");
                }
            }
        }
        catch(IOException e){
            System.out.println("IOException exception"+ e);
        }
        catch(Exception e){
            System.out.println("Exception occurred");
        }
    }

    // Set command- sends the send command
    public void setCommand(String command,String key, String size, String value){
        try{
            //String setkey = key;
            if(key.getBytes().length>250 || value.getBytes().length>250){
                System.out.println("Key/value must not be greater than 250 bytes");
                return;
            }
            int setsize = Integer.parseInt(size);
            if (value.getBytes().length <= setsize) {
                //System.out.println(command + "-" + key + "-" + value);
                pr.println(command + "-" + key + "-" + value);
                System.out.println(inputFromServer.readLine());
            } else {
                System.out.println(" The message is longer that the size");
            }
        }   catch(NumberFormatException e){
            System.out.println("Size should be numeric value");
        }
        catch(SocketException e){
            System.out.println("Socket exception"+ e +" Make sure server is running");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //send the get command to server
    public  void getCommand(String command,String key){
        String getkey = key;
        //System.out.println(command + "-" + key);
        if(command==null || key == null){
           // System.out.println("command proble"+command);
        }
        pr.println(command + "-" + key);
        String line;
        while (true) {
            try {
                if (!((line = inputFromServer.readLine()) != null)) break;
                System.out.println(line);
                if (line.equals("END") || line.equals("Key not present"))
                    break;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    // method to invoke test commands on clients for testing- runs the get/set operations random number of times for a thread
    public void clientInvokeTestCommand()  {
        Random random = new Random();
        int n = random.nextInt(commands.size());
        command = commands.get(n);
        value = values.get(n);
        int opCount=random.nextInt(commands.size());
        if(mode==2) {
            while (opCount > 0) {
                String params[] = command.split(" ");

                if (params[0].contains("set")) {
                    setCommand(params[0], params[1], params[2], value);
                } else if (params[0].contains("get")) {
                    getCommand(params[0], params[1]);
                } else if (params[0].equals("quit")) {

                } else {
                    System.out.println("Invalid command");
                }
                opCount--;
            }
        }

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
