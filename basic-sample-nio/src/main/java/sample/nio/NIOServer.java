package sample.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author soyona
 * @Package sample.nio
 * @Description:
 * @date 2018/4/20 21:22
 */
public class NIOServer {

    private static int port=8080;
    private static InetSocketAddress address=null;
    private static Selector selector;

    //创建buffer
    private static ByteBuffer buffer = ByteBuffer.allocate(1024);

    public static void main(String[] args) throws IOException {
        address = new InetSocketAddress(port);

        //Channel多路复用代码
        ServerSocketChannel server = ServerSocketChannel.open();

        //Channel监听端口
        server.bind(address);


        //设置为非阻塞
        server.configureBlocking(false);


        //初始化Selector 选择器
        selector = Selector.open();

        //关联Channel和selector
        server.register(selector, SelectionKey.OP_ACCEPT);


        //使用selector进行循环判断，哪些Channel准备好
        while(true){//轮询
            int count = selector.select();
            if(count == 0) {
                continue;
            }

            //?
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while(iterator.hasNext()){

                //
                handle(iterator.next());


                iterator.remove();
            }
        }

    }

    //处理
    private static void handle(SelectionKey key) throws IOException {

        //判断这个Key SelectionKey的状态，
        if(key.isAcceptable()){
            ServerSocketChannel channel = (ServerSocketChannel)key.channel();
            SocketChannel client = channel.accept();
            client.configureBlocking(false);
            //要对这个key进行另外注册
            client.register(selector,SelectionKey.OP_READ);
        }else if(key.isReadable()){
            SocketChannel client = (SocketChannel)key.channel();
            int len = client.read(buffer);
            if(len>0){
                buffer.flip();
                String text = new String(buffer.array(),0,len);
                System.out.println(text);
            }

            client.register(selector,SelectionKey.OP_WRITE);
            buffer.clear();
        }else if(key.isWritable()){
            SocketChannel client = (SocketChannel)key.channel();
            //写入buffer
            client.write(ByteBuffer.wrap("恭喜你".getBytes()));
            client.close();
        }

    }
}
