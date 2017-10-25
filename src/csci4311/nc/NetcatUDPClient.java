package csci4311.nc;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class NetcatUDPClient {

    private static DatagramSocket datagramSocket;

    /**
     * Begin program if host and port number is provided.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Ensure port number is provided
        if (args[0] != null && args[1] != null) {
            run(args[0], Integer.parseInt(args[1]));
        } else {
            System.out.println("Please provide a host and port number.");
        }
    }

    /**
     * Instantiate socket and execute download or upload functionality based on command entered.
     *
     * @param port
     * @throws Exception
     */
    private static void run(String host, int port) throws Exception {
        datagramSocket = new DatagramSocket();
        if (System.in.available() > 0) {
            upload(port, host);
        } else {
            download(port, host);
        }
        datagramSocket.close();
    }

    /**
     * Read in data from input stream and write to standard output.
     *
     * java csci4311.nc.NetcatServer 1234 < original-file
     * java csci4311.nc.NetcatClient hostA 1234 > downloaded-file
     *
     * @throws Exception
     */
    private static void download(int port, String host) throws Exception {
        byte[] buff = new byte[4096];
        // Initiate request to server
        datagramSocket.send(new DatagramPacket(buff, buff.length, InetAddress.getByName(host), port));

        DatagramPacket datagramPacket = new DatagramPacket(buff, buff.length);
        // Get datagram packets from datagram socket
        datagramSocket.receive(datagramPacket);
        // Write to standard output
        System.out.println(new String(datagramPacket.getData(), 0, datagramPacket.getLength()));
    }

    /**
     * Read in standard input and write to output stream.
     *
     * java csci4311.nc.NetcatServer 4321 > uploaded-file
     * java csci4311.nc.NetcatClient hostA 4321 < original-file
     *
     * @throws Exception
     */
    private static void upload(int port, String host) throws Exception {
        byte[] buff;
        Scanner input = new Scanner(System.in);
        // Get standard input
        while (input.hasNextLine()) {
            buff = input.nextLine().getBytes();
            // Output standard input to datagram socket as a datagram
            datagramSocket.send(new DatagramPacket(buff, buff.length, InetAddress.getByName(host), port));
        }
    }
}
