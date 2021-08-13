package CoreMod;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    private static String savePath = "C:\\Users\\Admin\\Desktop\\Games\\savegames";

    public static void main(String[] args) {
        // Создаём три экземпляра класса GameProgress
        GameProgress gp1 = new GameProgress(75, 3, 2, 3.56);
        GameProgress gp2 = new GameProgress(125, 6, 7, 39.5);
        GameProgress gp3 = new GameProgress(200, 9, 15, 81.0);

        // Записываем сохранения в файлы
        try {
            saveGame(savePath + "\\" + "save_1.dat", gp1);
            saveGame(savePath + "\\" + "save_2.dat", gp2);
            saveGame(savePath + "\\" + "save_3.dat", gp3);
        }
        catch (IOException ex){
            System.out.println("Возникла ошибка при записи сохранения");
        }

        // Создаём список, содержащий в себе пути к сохранениям
        List<String> savesList = new ArrayList<>();
        savesList.add(savePath + "\\" + "save_1.dat");
        savesList.add(savePath + "\\" + "save_2.dat");
        savesList.add(savePath + "\\" + "save_3.dat");

        try{
            zipFiles(savePath, savesList);
        }
        catch(IOException ex){
            System.out.println("Ошибка при архивации файла сохранения!");
        }
    }


    public static void saveGame(String savingPath, GameProgress gp) throws IOException {
        FileOutputStream fos = new FileOutputStream(savingPath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(gp);
        oos.close();
        fos.close();
    }


    public static void zipFiles(String pathForZip, List<String> savesList) throws IOException {
        // Создаём поток за запись архива, указываем место где архив будет лежать
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathForZip + "\\archive.zip"));
        for (int i = 1; i <= savesList.size(); i++) {
            // Ентри - 1 единица записи. Указываем в текущем каталоге название файла
            ZipEntry entry = new ZipEntry("save_" + i + ".dat");
            System.out.println(entry.getName());
            // Уведомляем поток о новом элементе архива
            zout.putNextEntry(entry);
            // Создаём поток на чтение
            FileInputStream fis = new FileInputStream(savesList.get(i-1));
            System.out.println(entry.getName());
            // Создаём буфер, в который будем считывать
            byte[] buffer = new byte[fis.available()];
            // Производим чтение из фалйа и записываем в буфер. По сути просто занимаемся переписыванием из одного места в другое
            fis.read(buffer);
            // Записываем в архив то, что считали в буфер
            zout.write(buffer);
            zout.closeEntry();
            fis.close();
        }
        zout.close();

        savesList.forEach(filename -> {
            File saveForDelete = new File(filename);
            if (saveForDelete.delete())
                System.out.println("Файл " + filename + " успешно удалён");
            else
                System.out.println("Файл " + filename + " не удалось удалить");
        });

    }
}
