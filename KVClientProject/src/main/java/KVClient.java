import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Random;

public class KVClient {
    static int maxThread;
    static int mode;
    public static void main(String[] args) throws IOException {
        int i=0;
       // System.out.println("Which mode do you want to run the program in? \n 1.interactive mode 2. Test case mode ");
        mode=Integer.parseInt(args[0]);
        if(mode==1){
            Client.commands.clear();
            Client.values.clear();
            new Client(1).interactiveMode();
        }else if( mode==2){
          //  System.out.println("Enter number of threads");
            maxThread=Integer.parseInt(args[1]);
            loadData();
            while(i<maxThread) {
                new Client(2).clientInvokeTestCommand();
                i++;
            }
        }else{
            System.out.println("Please enter valid mode");
        }

    }

    public static void loadData(){
        Client.commands.addAll(Arrays.asList("set name 1024","get name","set name 1024","set age 4","set place 1024","get age"));
        Client.values.addAll(Arrays.asList("Aditya","","Shivali","25", "Bloomington",""));
    }
}
