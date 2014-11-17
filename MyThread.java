
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


package minibrowsermulti;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author BALL
 */
public class MyThread extends Thread{
    private MiniBrowserJframe mini;
    public String url;
    public int win;
    public MyThread(String url,int win,MiniBrowserJframe minit){
    
        this.url=url;
        this.win=win;
        this.mini=minit;
    }

    
    @Override
    public void run(){
        String thrdName=Thread.currentThread().getName();
        recogOutputToWindows(thrdName+" :start. ",this.win);
        recogOutputToWindows(thrdName+" :running...",this.win);
        recogOutputToWindows(thrdName+" :Get page URL = "+this.url,this.win);
        recogOutputToWindows(thrdName+" :Show Response header...",this.win);
        displayPage(url,win);
        try {
            getHeaderfromURL(this.url);
        } catch (IOException ex) {
            Logger.getLogger(MyThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        recogOutputToWindows(thrdName+" :stopped...",this.win);
        
    }
    
    public void displayPage(String url,int win){
        mini.showPageByThread(url,win);
        
    }
    
    public void recogOutputToWindows(String txt,int win){
    
       if(win==1)
           mini.appendOutputA(txt);
       else
           mini.appendOutputB(txt);
    
    }
    
    @SuppressWarnings("empty-statement")
    private void getHeaderfromURL(String urlName) throws IOException{
        
        //analyst string
        String myString = urlName;
        String replace = myString.replace("http://","").replace("https://","");;
        
        
        String hostname = replace ;
        int port = 80;

        Socket socket = null;
        PrintWriter writer = null;
        BufferedReader reader = null;

        try {
            socket = new Socket(hostname, port);
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("GET / HTTP/1.1");
            writer.println("Host: " + hostname);
            writer.println("Accept: */*");
            writer.println("User-Agent: Java"); // Be honest.
            writer.println(""); // Important, else the server will expect that there's more into the request.
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            for (String line; (line = reader.readLine()) != null;) {
                if (line.isEmpty()) break; // Stop when headers are completed. We're not interested in all the HTML.
                    //if(win==1)
                        recogOutputToWindows("         "+line,this.win);
                   // else
                        //recogOutputToWindows("         "+line,2);
                //System.out.println(line);
            }
        } finally {
            if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {} 
            if (writer != null) { writer.close(); }
            if (socket != null) try { socket.close(); } catch (IOException logOrIgnore) {} 
        }
    }
    
    
    
    
    
}
