package com.kozlov.vladislav;

import java.util.List;

public class Solution implements DatesToCronConverter {
    public Solution() {
    }

    @Override
    public String convert(List<String> dates) throws DatesToCronConvertException {
        // Элементы формата cron
        String elSecond = "* ";
        String elMinute = "* ";
        String elHour = "* ";
        String elDayOfMonth = "* ";
        String elMonth = "* ";
        String elDayOfTheWeek = "*";

        // Создаем массивы элементов составляющих даты
        int[] months = new int[dates.size()];
        int[] daysOfMonth = new int[dates.size()];
        int[] hours = new int[dates.size()];
        int[] minutes = new int[dates.size()];
        int[] seconds = new int[dates.size()];
        int[] daysOfTheWeek = new int[dates.size()];

        // С помощью цикла заполняем массивы
        for(int i = 0; i < dates.size(); i++) {
            months[i] = Integer.parseInt(dates.get(i).substring(5, 6));
            daysOfMonth[i] = Integer.parseInt(dates.get(i).substring(8, 9));
            hours[i] = Integer.parseInt(dates.get(i).substring(13, 14));
            minutes[i] = Integer.parseInt(dates.get(i).substring(16, 17));
            seconds[i] = Integer.parseInt(dates.get(i).substring(19, 20));
            // Т.к. в пункте 6 тз есть "Год в дате не важен", то я взял за основу календарь 2022 года
            daysOfTheWeek[i] = getDayOfTheWeek(months[i], daysOfMonth[i]);
        }

        // Находим шаг между элементами, если он есть, и по необходимости находим диапазон
        // Для секунд
        if(searchStep(seconds) > 0) {
            elSecond = searchRange(seconds) + "/" + searchStep(seconds) + " ";
        } else if(searchStep(seconds) == 0) {
            if(seconds[0] < 10) {
                elSecond = "0" + seconds[0] + " ";
            } else {
                elSecond = "" + seconds[0] + " ";
            }
        } else {
            elSecond = searchRange(seconds);
            if(elSecond.equals("0-59")) {
                elSecond = "* ";
            }
        }
        // Для минут
        if(searchStep(minutes) > 0) {
            elMinute = searchRange(minutes) + "/" + searchStep(minutes) + " ";
        } else if(searchStep(minutes) == 0) {
            if(minutes[0] < 10) {
                elMinute = "0" + minutes[0] + " ";
            } else {
                elMinute = "" + minutes[0] + " ";
            }
        } else {
            elMinute = searchRange(minutes);
            if(elMinute.equals("0-59")) {
                elMinute = "* ";
            }
        }
        // Для часов
        if(searchStep(hours) > 0) {
            elHour = searchRange(hours) + "/" + searchStep(hours) + " ";
        } else if(searchStep(hours) == 0) {
            if(hours[0] < 10) {
                elHour = "0" + hours[0] + " ";
            } else {
                elHour = "" + hours[0] + " ";
            }
        } else {
            elHour = searchRange(hours);
            if(elHour.equals("0-23")) {
                elHour = "* ";
            }
        }
        // Для дней месяца
        if(searchStep(daysOfMonth) > 0) {
            elDayOfMonth = searchRange(daysOfMonth) + "/" + searchStep(daysOfMonth) + " ";
        } else if(searchStep(daysOfMonth) == 0) {
            if(daysOfMonth[0] < 10) {
                elDayOfMonth = "0" + daysOfMonth[0] + " ";
            } else {
                elDayOfMonth = "" + daysOfMonth[0] + " ";
            }
        } else {
            elDayOfMonth = searchRange(daysOfMonth) + " ";
            if(elDayOfMonth.equals("0-31")) {
                elDayOfMonth = "* ";
            } // Проверка для февраля
            else if(months[0] == 2) {
                // без проверки на високсоный год, т.к. в пункте 6 тз есть "Год в дате не важен"
                if(elDayOfMonth.equals("0-28")) {
                    elDayOfMonth = "* ";
                }
            } // Проверка для месяцев с 30 днями
            else if(months[0] == 4 || months[0] == 6 || months[0] == 9 || months[0] == 11) {
                if(elDayOfMonth.equals("0-30")) {
                    elDayOfMonth = "* ";
                }
            }
        }
        // Для месяцев
        if(searchStep(months) > 0) {
            elMonth = searchRange(months) + "/" + searchStep(months) + " ";
        } else if(searchStep(months) == 0) {
            if(months[0] < 10) {
                elMonth = "0" + months[0] + " ";
            } else {
                elMonth = "" + months[0] + " ";
            }
        } else {
            elMonth = searchRange(months);
            if(elMonth.equals("0-12")) {
                elMonth = "* ";
            }
        }
        // Для дней недели
        if(searchStep(daysOfTheWeek) > 0) {
            elDayOfTheWeek = searchRange(daysOfTheWeek) + "/" + searchStep(daysOfTheWeek);
        } else if(searchStep(daysOfTheWeek) == 0) {
            switch (daysOfTheWeek[0]) {
                case 1 :
                    elDayOfTheWeek = "Mon";
                    break;
                case 2 :
                    elDayOfTheWeek = "Tue";
                    break;
                case 3 :
                    elDayOfTheWeek = "Wed";
                    break;
                case 4 :
                    elDayOfTheWeek = "Thu";
                    break;
                case 5 :
                    elDayOfTheWeek = "Fri";
                    break;
                case 6 :
                    elDayOfTheWeek = "Sat";
                    break;
                case 7 :
                    elDayOfTheWeek = "Sun";
                    break;
            }
        } else {
            elDayOfTheWeek = searchRange(daysOfTheWeek);
            if(elDayOfTheWeek.equals("0-7")) {
                elDayOfTheWeek = "*";
            }
        }

        return elSecond + elMinute + elHour + elDayOfMonth + elMonth + elDayOfTheWeek;
    }

    // Метод для вычисления дня недели
    private int getDayOfTheWeek(int month, int num) {
        if(month == 8) { // Месяц, который начинается с пн
            while(num > 7) {
                num -= 7;
            }
            return num;
        } else if(month == 2 || month == 3 || month == 11) { // Месяцы, которые начинаются со вт
            num += 1;
            while(num > 7) {
                num -= 7;
            }
            return num;
        } else if(month == 6) { // Дальше по анологии со ср
            num += 2;
            while(num > 7) {
                num -= 7;
            }
            return num;
        } else if(month == 9 || month == 12) { // С чт
            num += 3;
            while(num > 7) {
                num -= 7;
            }
            return num;
        } else if(month == 4 || month == 7) { // С пт
            num += 4;
            while(num > 7) {
                num -= 7;
            }
            return num;
        } else if(month == 1 || month == 10) { // С сб
            num += 5;
            while(num > 7) {
                num -= 7;
            }
            return num;
        } else if(month == 5) { // С вс
            num += 6;
            while(num > 7) {
                num -= 7;
            }
            return num;
        }
        return 0;
    }

    // Метод для поиска шага
    private int searchStep(int[] array) {
        if(array.length > 2) {
            int sum = 0;
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i + 1] - array[i] >= 0) {
                    sum += array[i + 1] - array[i];
                } else {
                    if (i != array.length - 2) {
                        sum += array[i + 2] - array[i + 1];
                    } else {
                        sum += array[i] - array[i - 1];
                    }
                    i++;
                }
            }
            sum += array[1] - array[0];
            if (sum % array.length == 0) {
                return sum / array.length;
            }
            return -1;
        }
        return -1;
    }

    // Метод для нахождения диапозона
    private String searchRange(int[] array) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for(int el : array) {
            if (max < el) {
                max = el;
            }
            if (min > el) {
                min = el;
            }
        }
        return min + "-" + max;
    }

    @Override
    public String getImplementationInfo() {
        return "Kozlov Vladislav Olegovich, class Solution, package com.kozlov.vladislav, https://github.com/JaguarAztec/test-task";
    }
}
