import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Java. RTU. Frequency analysis.
 *
 * Прога, получает текст считает количество каждой буквы, потом ты берешь и текст обрабатываешь шифром цезаря, сдвигом.
 * Делаешь частотный анализ шифра, подставляешь букву.
 * Буквы по частотному анализу.
 * Надо войну и мир проанализировать, затем взять главу, шифирнуть ее и подставить по резултатам буквы.
 *
 * @author Kirill Selivanov
 * @version dated 05.10.2018
 */


public class Main {

    private static final char[] letterSmall = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о',
            'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};
    private static final char[] letterBig = {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О',
            'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я'};

    private static final int shift = 3; // Сдвиг по Цезарю
    private static final int nobR = 1089; // Количество биграм
    private static final int numbers = 15;

    public static void main(String[] args) {

        String[] bigramR = new String[nobR];
        all_bigram(bigramR);


        StringBuffer text = read("res/war_and_peace.txt"); //Считывание файла
        float[] analysisText = count(text.toString()); //Анализ текста
        int[] sortText = sort(analysisText); //Сортировка по номеру

        StringBuffer textHead = read("res/glava.txt"); //Считываение файла
        String textCesar = cesar(textHead.toString()); //Прогон в шифр Цезаря
        float[] analysisTextCesar = count(textCesar); //Анализ текста
        int[] sortTextCesar = sort(analysisTextCesar); //Сортировка по номеру

        String textNoCesar = NO_cesar(textCesar, sortText, sortTextCesar);

        float[] analysisTextBigram = count_bigram(text.toString(), bigramR);
        float[] analysisTextCesarBigram = count_bigram(textNoCesar, bigramR);

        int[] sortTextBigram = sort_bigram(analysisTextBigram);
        int[] sortTextCesarBigram = sort_bigram(analysisTextCesarBigram);

        String textNoCesar2 = NO_cesar_bigram(textNoCesar, sortTextBigram, sortTextCesarBigram, bigramR);

        System.out.println("Буквы в исходном тексте: ");
        for (int i = 0; i < letterSmall.length; i++)
            System.out.print(letterSmall[sortText[i]] + " = " + analysisText[sortText[i]] + " % |");


        System.out.println("\n" + "Буквы в шифре Цезаря: ");
        for (int i = 0; i < letterSmall.length; i++)
            System.out.print(letterSmall[sortTextCesar[i]] + " = " + analysisTextCesar[sortTextCesar[i]] + " % |");

        System.out.println("\n" + "Текст главы: " + textHead);
        System.out.println("Шифр главы: " + textCesar);
        System.out.println("Дешифр главы : " + textNoCesar);
        System.out.println();
        System.out.println("Биграмы в исходном тексте: ");
        for (int i = 0; i < numbers; i++)
            System.out.print(bigramR[sortTextBigram[i]] + " = " + analysisTextBigram[sortTextBigram[i]] + " % |");


        System.out.println("\n" + "Биграмы в шифре Цезаря: ");
        for (int i = 0; i < numbers; i++)
            System.out.print(bigramR[sortTextCesarBigram[i]] + " = " + analysisTextCesarBigram[sortTextCesarBigram[i]] + " % |");

        System.out.println("\n" + "Текст главы: " + textHead);
        System.out.println("Шифр главы: " + textNoCesar);
        System.out.println("Дешифр главы: " + textNoCesar2);


    }

    //Считывание из файла
    private static StringBuffer read(String filename) {

        StringBuffer text = new StringBuffer();

        try {
            Scanner in = new Scanner(new File(filename));
            while (in.hasNext())
                text.append(in.nextLine());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        return text;
    }

    //Анализ
    private static float[] count(String text) {

        char[] char_in_text = text.toCharArray(); //Перевод из текста в массив
        float[] analysis = new float[letterSmall.length]; //Анализ
        int[] count_letter = new int[letterSmall.length]; //Подсчет букв
        int all_letters = 0; //Всего букв для формулы

        for (int i = 0; i < char_in_text.length; i++)
            for (int j = 0; j < letterSmall.length; j++)
                if (char_in_text[i] == letterSmall[j] || char_in_text[i] == letterBig[j]) {
                    count_letter[j]++;
                    break;
                }

        for (int i = 0; i < letterSmall.length; i++)
            all_letters = all_letters + count_letter[i]; //Подсчет количества букв


        for (int i = 0; i < letterSmall.length; i++)
            analysis[i] = (float) 100 / all_letters * count_letter[i]; //Подсчет процентов

        return analysis;
    }

    //Шифр Цезаря
    private static String cesar(String text) {

        char[] char_in_text = text.toCharArray(); //Перевод из текста в массив
        char[] char_in_text_cesar = text.toCharArray(); //Перевод из текста в массив
        String text_cesar;

        int k;

        for (int i = 0; i < char_in_text.length; i++) {
            for (int j = 0; j < letterSmall.length; j++) {
                if (char_in_text[i] == letterSmall[j] || char_in_text[i] == letterBig[j]) {
                    if (j + shift < letterSmall.length)
                        k = j + shift;
                    else k = j + shift - letterSmall.length;

                    if (char_in_text[i] == letterSmall[j])
                        char_in_text_cesar[i] = letterSmall[k];
                    else char_in_text_cesar[i] = letterBig[k];
                }
            }
        }

        text_cesar = String.valueOf(char_in_text_cesar); //Перевод из массива в текст

        return text_cesar;
    }

    // Сортировка пузырьком
    private static int[] sort(float[] text) {

        float[] text_sort = text.clone();

        int[] number_sort = new int[letterSmall.length];
        for (int i = 0; i < letterSmall.length; i++) {
            number_sort[i] = i;
        }

        for (int i = letterSmall.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (text_sort[j] < text_sort[j + 1]) {
                    float tmp = text_sort[j];
                    text_sort[j] = text_sort[j + 1];
                    text_sort[j + 1] = tmp;
                    int tmp1 = number_sort[j];
                    number_sort[j] = number_sort[j + 1];
                    number_sort[j + 1] = tmp1;

                }
            }
        }

        return number_sort;
    }

    private static String NO_cesar(String text, int[] sortT, int[] sortTC ) {

        char[] textNOcesar = text.toCharArray();

        for (int i = 0; i < textNOcesar.length; i++) {
            for (int j = 0; j < letterSmall.length; j++) {
                if (textNOcesar[i] == letterSmall[sortTC[j]] || textNOcesar[i] == letterBig[sortTC[j]]) {
                    if (textNOcesar[i] == letterSmall[sortTC[j]])
                        textNOcesar[i] = letterSmall[sortT[j]];
                    else textNOcesar[i] = letterBig[sortT[j]];
                    break;
                }
            }
        }

        return String.valueOf(textNOcesar);
    }

    private static void all_bigram(String[] bigram) {

        int k = 0;

        for (int i = 0; i < letterSmall.length; i++) {
            for (int j = 0; j < letterSmall.length; j++) {
                bigram[k] = String.valueOf(letterSmall[i]) + String.valueOf(letterSmall[j]);
                k++;
            }
        }
    }

    private static float[] count_bigram(String text, String[] bigram) {

        float[] analysis = new float[nobR]; //Анализ
        int[] count_letter = new int[nobR]; //Подсчет букв
        int all_letters = 0; //Всего букв для формулы


        for (int i = 0; i < text.length()-1; i++)
            for (int j = 0; j < bigram.length; j++)

                if (text.substring(i, i+2).equalsIgnoreCase(bigram[j])) {
                    count_letter[j]++;
                    break;
                }

        for (int i = 0; i < bigram.length; i++)
            all_letters += count_letter[i]; //Подсчет количества букв

        for (int i = 0; i < bigram.length; i++)
            analysis[i] = (float) 100 / all_letters * count_letter[i]; //Подсчет процентов

        return analysis;
    }

    // Сортировка пузырьком
    private static int[] sort_bigram(float[] text) {

        float[] text_sort = text.clone();

        int[] number_sort = new int[nobR];
        for (int i = 0; i < nobR; i++) {
            number_sort[i] = i;
        }

        for (int i = nobR - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (text_sort[j] < text_sort[j + 1]) {
                    float tmp = text_sort[j];
                    text_sort[j] = text_sort[j + 1];
                    text_sort[j + 1] = tmp;
                    int tmp1 = number_sort[j];
                    number_sort[j] = number_sort[j + 1];
                    number_sort[j + 1] = tmp1;

                }
            }
        }

        return number_sort;
    }

    private static String NO_cesar_bigram(String text, int[] sortT, int[] sortTC, String[] bigram) {

        char[] textNOcesar = text.toCharArray();

        for (int i = 0; i < text.length()-1; i++) {
            for (int j = 0; j < numbers; j++) {
                if (text.substring(i, i+2).equalsIgnoreCase(bigram[sortTC[j]])) {
                    textNOcesar[i] = bigram[sortT[j]].charAt(0);
                    textNOcesar[i+1] = bigram[sortT[j]].charAt(1);
                    break;
                }
            }
        }

        return String.valueOf(textNOcesar);
    }

}