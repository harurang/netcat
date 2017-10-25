package csci4311.nc;

import java.net.*;
import java.util.Scanner;

public class NetcatUDPServer {

    private static DatagramSocket datagramSocket;

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
        datagramSocket = new DatagramSocket(port);
        // If standard input has been redirected
        if (System.in.available() > 0) {
            download();
        } else {
            upload();
        }
        datagramSocket.close();
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
        byte[] buff = new byte[1024];
        // Receive client request
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        datagramSocket.receive(datagramPacket);

        // Get standard input
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            buff = input.nextLine().getBytes();
            // Send standard input in a datagram packet through a datagram socket
            datagramSocket.send(new DatagramPacket(buff, buff.length, datagramPacket.getAddress(), datagramPacket.getPort()));
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
        byte[] buff = new byte[4096];
        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        // Get datagram packets from datagram socket
        datagramSocket.receive(datagramPacket);
        // Write to standard output
        System.out.println(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
    }
}
