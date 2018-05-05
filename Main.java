import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class Main {
    public static void main(String[] args) {
        readFileAsByte();
        unionFiles();
        unionFilesV2();
        readBigFile();
    }

    //1) Прочитать файл(около 50 байт) в байтовый массив и вывести этот массив в консоль;
    static void readFileAsByte() {
        try (ByteArrayInputStream in = new ByteArrayInputStream(Files.readAllBytes(Paths.get("1.txt")))) {
            int b;
            while ((b = in.read()) != -1) {
                System.out.println(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2) Последовательно сшить 10 файлов в один(файлы также ~100 байт).
    //вариант 1
    static void unionFiles() {
        try (FileOutputStream fos = new FileOutputStream("2.txt")) {
            FileInputStream fis;
            for (int i = 0; i < 10; i++) {
                byte[] arr = Files.readAllBytes(Paths.get("2" + i + ".txt"));
                fos.write(arr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //вариант 2
    static void unionFilesV2() {
        try (FileOutputStream fos = new FileOutputStream("22.txt")) {
            ArrayList<FileInputStream> a1 = new ArrayList<>();
            for (int i = 0; i < 10; i++) a1.add(new FileInputStream("22" + i + ".txt"));
            Enumeration<FileInputStream> e = Collections.enumeration(a1);

            SequenceInputStream sis = new SequenceInputStream(e);

            int b;
            while (true) {
                if ((b = sis.read()) != -1) fos.write(b);
                else break;
            }
            sis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //3) Написать консольное приложение, которое умеет постранично читать текстовые файлы(размером > 10 mb),
    //  вводим страницу, программа выводит ее в консоль(за страницу можно принимаем 1800 символов).
    //  Время чтения файла должно находится в разумных пределах(программа не должна загружаться дольше 10 секунд),
    //  ну и чтение тоже не должно занимать >5 секунд.
    static void readBigFile() {
        try (RandomAccessFile raf = new RandomAccessFile("3.txt", "r");
             BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            long fileLength = raf.length();
            long pageLength = 1800;
            long pagesCount = fileLength/pageLength;
            byte[] b = new byte[1800];
            System.out.println("fileLength: " + fileLength + " | pageLength: " + pageLength + " | pagesCount: " + pagesCount);
            while (true) {
                System.out.println("\nEnter the page between 0 and " + pagesCount + ". -1 to Exit.");
                long p = Long.parseLong(br.readLine());
                if (p <= pagesCount && p >= 0) {
                    raf.seek(p * pageLength);
                    raf.read(b, 0, b.length);
                    for (byte bb : b) System.out.print((char) bb);
                } else if (p == -1) {
                    System.out.println("Bye!");
                    System.exit(0);
                } else {
                    System.out.println("Incorrect page: " + p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
