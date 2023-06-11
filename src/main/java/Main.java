import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    private static int PORT = 8989;
    public static final String pdfsDir = "pdfs";
    public static final File stopTxt = new File("stop-ru.txt");

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));
        //  System.out.println(engine.search("бизнес"));
        System.out.println("Сервер запущен");

        try (ServerSocket serverSocket = new ServerSocket(PORT);) { // стартуем сервер один(!) раз
            while (true) { // в цикле(!) принимаем подключения

                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {

                    out.println("Сервер запущен");

                    System.out.println("Новый запрос");
                    String str = in.readLine();
                    System.out.println(str);

                    GsonBuilder builder = new GsonBuilder();
                    out.write(builder.setPrettyPrinting().create().toJson(engine.search(str)));

                }
            }
        } catch (
                IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}

