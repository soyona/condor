package sample.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kanglei on 07/07/2017.
 */
public class MyServerSocket {
    public MyServerSocket() throws IOException {
    }

    public static void  main(String[] args){
        try {
            ServerSocket serverSocket =new ServerSocket(10086,3);

            while(true) {
                /**ServerSocket的accept()方法从连接请求队列中取出一个客户的连接请求，
                 然后创建与客户连接的Socket对象，并将它返回 。
                 如果队列中没有连接请求，accept()方法就会一直等待，直到接收到了连接请求才返回。
                 **/
                System.out.println("开始监听客户端...........");
                Socket socket = serverSocket.accept();
                System.out.println("收到客户端信息..........."+"SOCKET:"+socket);
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String info = null;
                while ((info = br.readLine()) != null) {
                    System.out.println("客户端IP:"+socket.getInetAddress()+",客户端端口PORT:"+socket.getPort()+",说：" + info);
                }

                //socket.shutdownInput();//关闭输入流

                //输出给客户端数据
                OutputStream os = socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os);
                pw.write("Hello World！");
                pw.print("Hello World!");
                pw.flush();
                pw.close();

               // Thread.sleep(10000);
                os.close();
                br.close();
                isr.close();
                is.close();

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
