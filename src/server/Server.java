package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by dimaz on 25.05.2017.
 */
public class Server {

    public static void main(String[] args) throws IOException{

        BufferedReader in1 = null, in2 = null;
        PrintWriter out1 = null, out2 = null;

        ServerSocket server1 = null, server2 = null;

        Socket fromGamer1 = null;
        Socket fromGamer2 = null;

        //initializing server sockets
        try{
            server1 = new ServerSocket(4444);
            server2 = new ServerSocket(4445);
        }
        catch (IOException e){
            System.out.println("Unavailable ports");
            System.exit(-1);
        }


        //receiving client socket and initializing output/input streams
        try{
            fromGamer1 = server1.accept();
            fromGamer2 = server2.accept();
            System.out.println("Clients connected");
            out1 = new PrintWriter(fromGamer1.getOutputStream(), true);
            out2 = new PrintWriter(fromGamer2.getOutputStream(), true);
            in1 = new BufferedReader(new InputStreamReader(fromGamer1.getInputStream()));
            in2 = new BufferedReader(new InputStreamReader(fromGamer2.getInputStream()));
            //Telling clients, that game is starting
            out1.println("Start");
            out2.println("Start");

        }
        catch (IOException e){
            System.out.println("Cannot get the clients");
            System.exit(-1);
        }


        //if both players passes, match ends.
        int passesCount = 0;

        String input;
        //process of transporting information from one client to another
        while (true){
            try {
                //read move from white player
                input = in1.readLine();
                //if first client aborted connection
                if(input == null){
                    //telling second player that he is a winner
                    out2.println("giveup");
                    break;
                }
                //if player passes
                if(input.equals("pass")) {
                    passesCount++;
                    if(passesCount == 2) {
                        //гра закінчується, коли гравці обидва пропускають хід
                        out1.println("end");
                        out2.println("end");
                    }
                }
                else
                    passesCount = 0;
                //sending information to second player
                out2.println(input);
                //read move from black player
                input = in2.readLine();

                if(input == null){
                    out1.println("giveup");
                    break;
                }
                if(input.equals("pass")) {
                    passesCount++;
                    if(passesCount == 2) {
                        //гра закінчується, коли гравці обидва пропускають хід
                        out1.println("end");
                        out2.println("end");
                    }
                }
                else
                    passesCount = 0;

                out1.println(input);

            }
            catch(NumberFormatException e){

                e.printStackTrace();
                break;

            }
            catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
        //closing all streams and sockets
        out1.close();
        in1.close();
        out2.close();
        in2.close();
        fromGamer1.close();
        fromGamer2.close();
        server1.close();
        server2.close();

    }


}
