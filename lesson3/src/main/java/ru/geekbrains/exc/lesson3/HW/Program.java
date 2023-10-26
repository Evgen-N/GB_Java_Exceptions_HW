package ru.geekbrains.exc.lesson3.HW;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Program {

    /*
       Напишите приложение, которое будет запрашивать у пользователя следующие данные в
       произвольном порядке, разделенные пробелом:
        Фамилия Имя Отчество датарождения номертелефона пол

        Форматы данных:
        фамилия, имя, отчество - строки
        датарождения - строка формата dd.mm.yyyy
        номертелефона - целое беззнаковое число без форматирования
        пол - символ латиницей f или m.

        Приложение должно проверить введенные данные по количеству.
       Если количество не совпадает с требуемым, вернуть код ошибки, обработать его и
       показать пользователю сообщение, что он ввел меньше или больше данных, чем требуется...
     */

        public static void main(String[] args) throws IOException {

            String[] userData = scanArray();
            final int dataLength = 6;
            //userData.userDates  = userData.scanArray();
            //userData  = scanArray();
            //int dataLength = userData.dataLength;

            // Проверка длины введенного массива
            int codeResult = processArray(userData, dataLength);
            switch (codeResult){
                case -1 -> System.out.println("Длина массива более шести символов");
                case -2 -> System.out.println("Длина массива менее шести символов");
                case -3 -> System.out.println("Массив некорректно проинициализирован");
                default -> {
                    System.out.println("Массив успешно обработан.");
                    System.out.printf("Элементов в массиве: %d\n", dataLength);
                }
            }

            if (userData.length != dataLength){
                throw new MyArraySizeException("Кол-во элементов массива должно быть: " + dataLength +
                ", а Вы ввели: " + userData.length);
            }

            // проверка ФИО
            String[] fio  =  {userData[0], userData[1], userData[2]};
            for (String inputString: fio) {
                boolean containsOnlyLetters = Pattern.matches("[а-яА-Я]+", inputString);
                System.out.println("Части ФИО содержат только русские буквы: " + containsOnlyLetters);
                if (!containsOnlyLetters){
                    throw new MyArrayTypeException("ФИО введено неправильно: " + inputString);
                }
            }


            // проверка даты
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try {
              LocalDate.parse(userData[3], dateFormatter);
            } catch (DateTimeParseException e) {
                throw new MyArrayTypeException("Дата рожденья введена неправильно: " + userData[3]);
            }
            System.out.println("Дата введена корректно.");

            // проверка номера телефона
            try {
                Integer.parseInt(userData[4]);
            }
            catch (NumberFormatException e){
                throw new MyArrayTypeException("Телефон введен неправильно: " + userData[4]);
            }
            System.out.println("Телефон введен корректно.");

            // проверка пола
            if (!userData[5].equals("m" + "") && !userData[5].equals("f" + "")){
            throw new MyArrayTypeException("Пол введен неправильно: " + userData[5]);
            }
            System.out.println("Пол введен корректно.");

            // сохранение данных в файл
            prepareFile(userData);
            System.out.println("Запись в файл завершена корректно.");

        }


    static String[] scanArray(){

        System.out.println("Введите следующие данные в произвольном порядке, разделенные пробелом:\n"
        + "Фамилия Имя Отчество датарождения номертелефона пол");
        System.out.println("Форматы данных:\n"
        + "фамилия, имя, отчество - строки\n"
        + "датарождения - строка формата dd.mm.yyyy\n"
        + "номертелефона - целое беззнаковое число без форматирования\n"
        + "пол - символ латиницей f или m.");

        Scanner scanner = new Scanner(System.in);
        String userData = scanner.nextLine();
        scanner.close();
        // System.out.println("UserData: " + userData);
        String[] arrOfStr = userData.split(" ");
        return arrOfStr;
    }


    static int processArray(String[] array, int sizeOfArray){
        if (array == null)
            return -3; // Массив некорректно проинициализирован
        if (array.length < sizeOfArray){
            return -2; // Длина массива менее шести символов
        }
        if (array.length > sizeOfArray){
            return -1; // Длина массива более шести символов
        }
        else return 0; // Длина массива равна шести символам
        }


    static void prepareFile(String[] user) throws IOException{

        String fileName = user[0] + ".txt";
        File file = new File(fileName);

        FileWriter fileWriter = null;
        try  {
            fileWriter = new FileWriter(fileName, file.exists());
            for (String s: user) {
                fileWriter.write(s);
            }
            fileWriter.write("\n");
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally {
            try
            {
                fileWriter.close();
            }
            catch (IOException e){

            }
        }
    }
}
