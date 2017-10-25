package csci4311.nc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Hillary Arurang
 * PA1
 * Computer Networks
 * Fall 2017
 */

public class NetcatServer {

    private static Socket socket;
    private static BufferedReader bufferedReader;

    /**
     * Begin program if port number is provided.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Ensure port number is provided
        if (args[0] != null) {
            run(Integer.parseInt(args[0]));
        } else {
            System.out.println("Please provide a port number.");
        }
    }

    /**
     * Instantiate socket and execute download or upload functionality based on command entered.
     *
     * @param port
     * @throws Exception
     */
    private static void run(int port) throws Exception {
        // ServerSocket is used for socket.
        ServerSocket serverSocket = new ServerSocket(port);
        // Listens for a connection to be made to this socket and accepts it
        socket = serverSocket.accept();
        // If standard input has been redirected
        if (System.in.available() > 0) {
            download();
        } else {
            upload();
        }
        socket.close();
    }

    /**
     * Read in standard input and write to output stream.
     *
     * java csci4311.nc.NetcatServer 1234 < original-file
     * java csci4311.nc.NetcatClient hostA 1234 > downloaded-file
     *
     * @throws Exception
     */
    private static void download() throws Exception {
        // Creates a new data output stream to write data to the specified underlying output stream.
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        // Get standard input.
        Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            // Write standard input to output stream.
            dataOutputStream.writeBytes(in.nextLine());
        }
    }

    /**
     * Read in data from input stream and write to standard output.
     *
     * java csci4311.nc.NetcatServer 4321 > uploaded-file
     * java csci4311.nc.NetcatClient hostA 4321 < original-file
     *
     * @throws Exception
     */
    private static void upload() throws Exception {
        // Read in data from stream.
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            // Print to standard output.
            System.out.println(line);
        }
    }
}