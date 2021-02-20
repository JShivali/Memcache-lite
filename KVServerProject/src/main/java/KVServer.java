import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KVServer {

    private static ExecutorService pool = Executors.newFixedThreadPool(100);
    private static ArrayList<RequestHandler> clients= new ArrayList<RequestHandler>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);

        while(true){
            System.out.println("Server waiting for connection");
            Socket socket=serverSocket.accept();
            System.out.println("Client connected");

            RequestHandler requestHandler= new RequestHandler((socket));
            requestHandler.start();

            clients.add(requestHandler);
            pool.execute(requestHandler);

            //System.out.println("Size is "+ clients.size());
        }
    }

}
