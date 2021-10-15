package com.company;

import java.text.SimpleDateFormat;
import java.util.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Main {

        public static void main(String[] args) {
            System.out.println("№1 = " + sameLetterPattern("ABAB", "CDCD"));
            System.out.println("№2 = " + spiderVsFly("H3", "E2"));
            System.out.println("№3 = " + digitsCount(12345));
            System.out.println("№4 = " + totalPoints(new String[] {"cat", "create", "sat"}, "caster"));
            System.out.println("№5 = " + longestRun(new int[] {1, 2, 3, 5, 6, 7, 8, 9}));
            System.out.println("№6 = " +  takeDownAverage(new String[] {"95%", "83%", "90%", "87%", "88%", "93%"}));
            System.out.println("№7 = " + rearrange("Tesh3 th5e 1I lov2e way6 she7 j4ust i8s."));
            System.out.println("№8 = " + maxPossible(9328, 456));
            System.out.println("№9 = " + timeDifference("Los Angeles", "April 1, 2011 23:23", "Canberra"));
            System.out.println("№10 = " + isNew(3));
        }
    /* 1. Создайте функцию, которая возвращает true, если две строки имеют один и тот же
       буквенный шаблон, и false в противном случае. */

    // Используем хэш-таблицу для связей буквенного шаблона - одному виду
    //символа первой строки может быть поставлен только один вид символа второй
    public static boolean sameLetterPattern(String s1, String s2) {
        // Для хранения связей между символами строк
        Map<Character, Character> connections = new HashMap<Character, Character>();
        // Сначала проверим длину строк
        int length = s1.length();
        if (length == s2.length()) {
            // Проходим циклом по символам первой строки, записываем как ключ,
            //значение символ первой строки и символ второй строки в хэш-таблицу
            for (int i = 0; i < length; i++) {
                char keyChar = s1.charAt(i);
                char valueChar = s2.charAt(i);
                if (connections.containsKey(keyChar)) {
                    if (connections.get(keyChar) != valueChar) {
                        return false;
                    }
                } else {
                    connections.put(keyChar, valueChar);
                }
            }
            return true;
        }
        return false;
    }

    /* 2. Паутина определяется кольцами, пронумерованными от 0 до 4 от центра, и
       радиалами, помеченными по часовой стрелке сверху как A-H.
       Создайте функцию, которая принимает координаты паука и мухи и возвращает
       кратчайший путь для паука, чтобы добраться до мухи.
       Стоит отметить, что кратчайший путь должен быть рассчитан "геометрически", а не путем
       подсчета количества точек, через которые проходит этот путь. Мы могли бы это устроить:
       Угол между каждой парой радиалов одинаков (45 градусов).
       Расстояние между каждой парой колец всегда одинаково (скажем, "x"). */

        public static String spiderVsFly(String spider, String fly) {
	    /* У паука есть 2 стратегии:
	       1. спуститься в центр и пойти по нужной ветке
	          это просто сумма номеров колец мухи и паука
	       2. спуститься/подняться до нужного кольца и пойти по кольцу
	          разница между кольцами паука и мухи + (разница между веток) * длина пути на кольце
	          длина пути на кольце = 2 * x * sin(45 / 2) = x * 0.76536686473
	    */

            int sx = spider.charAt(0) - 65;
            int sy = spider.charAt(1) - 48;
            int fx = fly.charAt(0) - 65;
            int fy = fly.charAt(1) - 48;

            double strategyDist1 = sy + fy;
            double strategyDist2 = Math.abs(sy - fy) + ((sx + fx) % 8) * fy * 0.76536686473;

            String path = "";

            if (strategyDist1 <= strategyDist2) {
                for (int i = 0; i < sy; i++) {
                    path += spider.charAt(0);
                    path += sy - i;
                    path += '-';
                }
                path += "A0-";
                for (int i = 0; i < fy; i++) {
                    path += fly.charAt(0);
                    path += i + 1;
                    path += '-';
                }
            } else {
                for (int i = 0; i < Math.abs(sy - fy); i++) {
                    path += spider.charAt(0);
                    if (sy > fy) path += sy - i;
                    else path += sy + i;
                    path += '-';
                }
                for (int i = 0; i <= (sx + fx) % 8; i++) {
                    path += (char)(65 + (sx + i) % 8);
                    path += fly.charAt(1);
                    path += '-';
                }
            }

            return path.substring(0, path.length() - 1);
        }

	/* 3. Создайте функцию, которая будет рекурсивно подсчитывать количество цифр
       числа. Преобразование числа в строку не допускается, поэтому подход является
       рекурсивным. */

        public static int _digitsCount(long number) {
            if (number == 0) return 0;
            return 1 + _digitsCount(number / 10);
        }

        public static int digitsCount(long number) {
            return 1 + _digitsCount(number / 10);
        }

    /* 4. В игроки пытаются набрать очки, формируя слова, используя буквы из 6-
       буквенного скремблированного слова. Они выигрывают раунд, если им удается
       успешно расшифровать слово из 6 букв.
       Создайте функцию, которая принимает в массив уже угаданных слов расшифрованное 6-
       буквенное слово и возвращает общее количество очков, набранных игроком в
       определенном раунде, используя следующую рубрику:
       3-буквенные слова-это 1 очко
       4-буквенные слова-это 2 очка
       5-буквенные слова-это 3 очка
       6-буквенные слова-это 4 очка + 50 пт бонуса (за расшифровку слова)
       Помните, что недопустимые слова (слова, которые не могут быть сформированы из 6-
       буквенных расшифрованных слов) считаются 0 очками.
       */

        public static int[] getCharset(String word) { //getCharset - метод, который создает массив из входящего слова
            int[] charset = new int[127];
            for (char c : word.toCharArray()) //toCharArray()  - преобразование строки в массив
                charset[c]++;
            return charset;
        }

        public static int totalPoints(String[] words, String scramble) {
            int points = 0;
            int[] scrambleCharset = getCharset(scramble); //посимвольный массив изначального слова
            for (int i = 0; i < words.length; i++) {
                int[] wordCharset = getCharset(words[i]); //посимвольный массив получившегося слова
                boolean good = true;
                for (int j = 0; j < 127; j++)
                    if (wordCharset[j] > scrambleCharset[j]) {
                        good = false;
                        break;
                    }
                if (good) {
                    points += words[i].length() - 2;
                    if (words[i].length() == 6) points += 50;
                }
            }
            return points;
        }

	/* 5. Последовательный прогон-это список соседних последовательных целых чисел.
       Этот список может быть как увеличивающимся, так и уменьшающимся. Создайте
       функцию, которая принимает массив чисел и возвращает длину самого длинного
       последовательного запуска. */

        public static int longestRun(int[] arr) {
            int max = 1;
            int cur = 1;
            for (int i = 0; i < arr.length - 1; i++)
                if (arr[i+1] - arr[i] == 1 || arr[i+1] - arr[i] == -1) {
                    cur++;
                    if (max < cur) max = cur;
                } else cur = 1;
            return max;
        }

    /* 6. Какой процент вы можете набрать на тесте, который в одиночку снижает средний
       балл по классу на 5%? Учитывая массив оценок ваших одноклассников, создайте
       функцию, которая возвращает ответ. Округлите до ближайшего процента. */

    // Преобразуем из строк в числа, вычисляем среднее. По формуле вычисляем
    //нужное значение балла
    public static String takeDownAverage(String[] nums) {
        double sum = 0;
        int size = nums.length;
        for (String percent : nums) {
            // Преобразуем из строки в число
            int currentPercent = Integer.parseInt(percent.substring(0, percent.length()-1));
            sum += currentPercent;
        }
        double avg = sum/size;
        // (sum+n)/(size+1) = avg-5; n = (size+1)*(avg-5)-sum
        StringBuilder n = new StringBuilder();
        n.append((int)((size+1)*(avg-5.)-sum));
        n.append('%');
        return n.toString();
    }


    /* 7. Учитывая предложение с числами, представляющими расположение слова,
       встроенного в каждое слово, верните отсортированное предложение */

    // В хэш-таблицу записываем слова со значениями чисел в качестве ключей.
    //Числа извлекаем и получаем чистые слова с помощью метода replaceAll
    public static String rearrange(String s) {
        Map<Integer, String> placement = new HashMap<Integer, String>();
        String[] words = s.split(" ");
        for (String word : words) {
            int i = Integer.parseInt(word.replaceAll("\\D+",""));
            word = word.replaceAll("\\d+","");
            placement.put(i, word);
        }
        StringBuilder sb = new StringBuilder();
        for (String word : placement.values()) {
            sb.append(word);
            sb.append(' ');
        }
        return sb.toString();
    }

    /* 8. Напишите функцию, которая делает первое число как можно больше, меняя его
       цифры на цифры во втором числе.
       Примечание:
       - Каждая цифра во втором числе может быть использована только один раз.
       - Можно использовать ноль для всех цифр второго числа. */

    // Преобразовываем в массивы цифр. Второй массив сортируем.
    //По цифре смотрим первое число и, если возможно и необходимо,
    //записываем цифру из второго числа
    public static int maxPossible(int n1, int n2) {
        int size1 = digitsCount(n1);
        int size2 = digitsCount(n2);
        int[] n1_array = new int[size1];
        int[] n2_array = new int[size2];
        for (int i = 0; i < size2; i++) {
            n2_array[i] = n2%10;
            n2 = n2/10;
        }
        Arrays.sort(n2_array);
        int maxDigitIndex = size2-1;
        int maxDigit = n2_array[maxDigitIndex];
        for (int i = size1-1; i >= 0; i--) {
            n1_array[i] = n1%10;
            n1 = n1/10;
        }
        for (int i = 0; i < size1; i++) {
            int currentDigit = n1_array[i];
            if (currentDigit < maxDigit) {
                currentDigit = maxDigit;
                if (maxDigitIndex > 0) {
                    maxDigit = n2_array[--maxDigitIndex];
                } else {
                    maxDigit = 0;
                }
            }
            n1_array[i] = currentDigit;
        }
        int maxNum = 0;
        for (int n : n1_array) {
            maxNum = maxNum*10+n;
        }
        return maxNum;
    }

    /* 9. В этой задаче цель состоит в том, чтобы вычислить, сколько времени сейчас в двух
       разных городах. Вам дается строка cityA и связанная с ней строка timestamp (time
       in cityA) с датой, отформатированной в полной нотации США, как в этом примере:
       "July 21, 1983 23:01"
       Вы должны вернуть новую метку времени с датой и соответствующим временем в cityB,
       отформатированную как в этом примере:
       "1983-7-22 23:01"
       Список данных городов и их смещения по Гринвичу (среднее время по Гринвичу)
       приведены в таблице ниже.
       GMT City
       - 08:00 Los Angeles
       - 05:00 New York
       - 04:30 Caracas
       - 03:00 Buenos Aires
       00:00 London
       + 01:00 Rome
       + 03:00 Moscow
       + 03:30 Tehran
       + 05:30 New Delhi
       + 08:00 Beijing
       + 10:00 Canberra
       Примечание:
       - Обратите внимание на часы и минуты, ведущий 0 необходим в возвращаемой метке
       времени, когда они представляют собой одну цифру.
       - Обратите внимание на города с получасовыми смещениями. */

    // Используем класс java.time.ZoneOffset; java.time.ZonedDateTime;
    //java.time.format.DateTimeFormatter для преобразования даты по часовым
    //поясам
    private static Map<String, ZoneOffset> offsets = new HashMap<String, ZoneOffset>() {{
        put("Los Angeles",  ZoneOffset.of("-08:00"));
        put("New York",     ZoneOffset.of("-05:00"));
        put("Caracas",      ZoneOffset.of("-04:30"));
        put("Buenos Aires", ZoneOffset.of("-03:00"));
        put("London",       ZoneOffset.of("+00:00"));
        put("Rome",         ZoneOffset.of("+01:00"));
        put("Moscow",       ZoneOffset.of("+03:00"));
        put("Tehrah",       ZoneOffset.of("+03:30"));
        put("New Delhi",    ZoneOffset.of("+05:30"));
        put("Beijing",      ZoneOffset.of("+08:00"));
        put("Canberra",     ZoneOffset.of("+10:00"));
    }};
    public static String timeDifference(String cityA, String time, String cityB) {
        // Разделяем строку времени и форматируем
        String[] parsed = time.split("(,\\s|:|\\s)");
        // Для преобразования в целочисленные значения
        int[] values = new int[parsed.length];
        // Преобразование названия месяца
        switch(parsed[0]) {
            case "January":     values[0] = 1;  break;
            case "February":    values[0] = 2;  break;
            case "March":       values[0] = 3;  break;
            case "April":       values[0] = 4;  break;
            case "May":         values[0] = 5;  break;
            case "June":        values[0] = 6;  break;
            case "July":        values[0] = 7;  break;
            case "August":      values[0] = 8;  break;
            case "September":   values[0] = 9;  break;
            case "October":     values[0] = 10; break;
            case "November":    values[0] = 11; break;
            case "December":    values[0] = 12; break;
        }
        // Преобразование остальных значений даты
        for (int i = 1; i < parsed.length; i++) {
            values[i] = Integer.parseInt(parsed[i]);
        }
        ZoneId timeZoneId = offsets.get(cityA);
        ZoneId newZoneId = offsets.get(cityB);
        ZonedDateTime date = ZonedDateTime
                // year, month, day, hours, minutes, seconds, nanoseconds, zoneId
                .of(values[2], values[0], values[1], values[3], values[4], 0, 0, timeZoneId);
        ZonedDateTime newDate = date.withZoneSameInstant(newZoneId);
        // Форматировать в 1999-9-9 23:59
        return DateTimeFormatter.ofPattern("yyyy-M-d HH:mm").format(newDate);
    }

    /* 10. Новое число-это число, которое не является перестановкой любого меньшего
       числа. 869-это не новое число, потому что это просто перестановка меньших чисел,
       689 и 698. 509-это новое число, потому что оно не может быть образовано
       перестановкой любого меньшего числа (ведущие нули не допускаются).
       Напишите функцию, которая принимает неотрицательное целое число и возвращает true,
       если целое число является новым числом, и false, если это не так. */

    // Нужно найти по порядку цифру меньшую максимальной, тогда число не
    //является новым, кроме случая, когда рассматриваем первую цифру числа и ноль
       public static boolean isNew(int num) {
        int size = digitsCount(num);
        int[] digits = new int[size];
        // Инициализируем массив
        for (int i = 0; i < size; i++) {
            digits[i] = num%10;
            num = num/10;
        }
        // Рассматриваем первый максимум
        boolean firstMax = true;
        int max = digits[size-1];
        if (size > 1) {
            for (int i = size-2 ; i >= 0; i--) {
                //System.out.println(digits[i]);
                if (digits[i] >= max) {
                    max = digits[i];
                    firstMax = false;
                } else if (digits[i] != 0 || !firstMax) {
                    return false;
                }
            }
        }
        return true;
    }
}

