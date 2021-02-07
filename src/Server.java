import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server{
    // 端口号 8080
    static final int PORT = 8080;
    // 客户端队列
    static ArrayList<JabberServer> listClient = new ArrayList<JabberServer>();
    public static void main(String[] args) throws IOException{
        // 建立服务端的socket
        ServerSocket s = new ServerSocket(PORT);
        System.out.println("Sever Starting...");
        try {
            while (true) {
                // 等待客户端连接
                Socket socket = s.accept();
                System.out.println("Connect to Client : ");
                try {
                    // 建立一个线程去处理该客户端的请求
                    JabberServer clientThread = new JabberServer(socket);
                    // 运行该线程
                    clientThread.start();
                    // 加入到客户端队列
                    listClient.add(clientThread);
                }catch (IOException e) {
                    socket.close();
                }
            }
        }
        finally{
            s.close();
        }
    }
    // 负责发送消息的线程
    static class SendMsg extends Thread {
        Socket socket;
        String msg;
        String username;
        private BufferedReader in;
        private PrintWriter out;

        public SendMsg(String msg, Socket socket, JabberServer client) throws IOException {
            this.socket = socket;
            this.msg = msg;
            this.username = client.username;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        }
        public void run(){
            super.run();
            // 在各自客户端发送消息
            out.println(username +": " +msg);
        }
    }

    // 处理单个客户单的线程
    static class JabberServer extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        public String username;
        Server server = new Server();

        public JabberServer(Socket s) throws IOException{
            socket = s;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
        }

        public void run(){
            try {
                out.println("Pleaning enter username: ");
                this.username = in.readLine();
                out.println("Hi " +this.username +"! Let's start chatting :P");
                out.println("***********************************");
                // 发送给所有连接上的客户端，有新的客户端连接上
                for(JabberServer Client:listClient ){
                    if(this == Client )
                        continue;
                    new SendMsg("what's up guys!", Client.socket,this).start();
                }

                while(true){
                    System.out.println("There is(are) " + listClient.size() + " Client(s)");
                    System.out.println("***********************************");
                    String line = in.readLine();
                    if(line.equals("END"))
                        break;
                    // 服务器显示信息
                    System.out.println(this.username + " says: " + line);
                    // 广播
                        for(JabberServer client  : listClient){
                            if(client == this)
                                continue;
                            // 指定是this这个客户端
                            new SendMsg(line, client.socket, this).start();
                    }
                    // 在客户端显示
                    out.println("my message: " + line);
                }
                listClient.remove(this);
                System.out.println("closing "  + this.username);
                System.out.println("There is(are) " + listClient.size() + " Client(s)");
                System.out.println("***********************************");
                // 广播给其他客户端
                for(JabberServer client  : listClient){
                    new SendMsg("leave this room", client.socket, this).start();
                }
            }
            catch(IOException e) {}

            finally{
                try {
                    socket.close();
                }
                catch(IOException e) {}
            }
        }
    }

}
