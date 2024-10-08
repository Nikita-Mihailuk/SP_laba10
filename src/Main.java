import java.io.*;

public class Main {
    public static void main(String[] args) {
        try {
            // замеряем начальное время
            long startTime = System.nanoTime();
            //последовательное копирование 2 файлов
            copyFile("input.txt","output.txt");
            copyFile("input.txt","output.txt");

            long time = System.nanoTime() - startTime;
            System.out.println("Последовательное копирование двух файлов заняло " + time + " наносекунд");

            // замеряем начальное время
            startTime = System.nanoTime();
            //параллельное копирование 2 файлов
            CopyFileThread copyFileThread1 = new CopyFileThread( "inputThread.txt", "outputThread.txt");
            CopyFileThread copyFileThread2 = new CopyFileThread( "inputThread1.txt", "outputThread1.txt");

            // запуск потоков
            copyFileThread1.start();
            copyFileThread2.start();

            // ждём завершения потоков, чтоб посчитать время выполнения
            copyFileThread1.join();
            copyFileThread2.join();

            time = System.nanoTime() - startTime;
            System.out.println("Параллельное копирование двух файлов заняло " + time + " наносекунд");

        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void copyFile(String sourcePath, String destPath) throws IOException {
        //специальная конструкция try, которая сама освобождает ресурсы
        try(InputStream is = new FileInputStream(sourcePath);
            OutputStream os = new FileOutputStream(destPath)) {

            // буфер для копирования
            byte[] buffer = new byte[1024];
            int length;
            // чтение из первого файла в буфер и запись того что мы прочитали во втрой буфер
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }
    }
}
// класс для создания потока в котором будет происходить копирование файла
class CopyFileThread extends Thread{
    String sourcePath;
    String destPath;

    public CopyFileThread(String sourcePath, String destPath) {
        this.sourcePath = sourcePath;
        this.destPath = destPath;
    }

    @Override
    public void run(){
        try {
            Main.copyFile(sourcePath,destPath);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}