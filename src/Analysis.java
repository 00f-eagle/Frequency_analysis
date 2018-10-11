public class Analysis {

    private static float[] count(String text, char[] charsS, char[] charsB) {

        char[] char_in_text = text.toCharArray(); //Перевод из текста в массив
        float[] analysis = new float[charsS.length]; //Анализ
        int[] count_letter = new int[charsS.length]; //Подсчет букв
        int all_letters = 0; //Всего букв для формулы

        for (int i = 0; i < char_in_text.length; i++)
            for (int j = 0; j < charsS.length; j++)
                if (char_in_text[i] == charsS[j] || char_in_text[i] == charsB[j]) {
                    count_letter[j]++;
                    break;
                }

        for (int i = 0; i < charsS.length; i++)
            all_letters = all_letters + count_letter[i]; //Подсчет количества букв


        for (int i = 0; i < charsS.length; i++)
            analysis[i] = (float) 100 / all_letters * count_letter[i]; //Подсчет процентов

        return analysis;
    }

}
