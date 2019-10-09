package main.java;

import java.text.DecimalFormat;

public class DateTime {

    private int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private String[] monthNames = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};

    private double milliseconds;
    private double seconds;
    private double minutes;
    private double hours;
    private int day;
    private int month;
    private int year;

    DateTime(double ms, double sec, double min, double h, int d, int m, int y) {
        milliseconds = ms;
        seconds = sec;
        minutes = min;
        hours = h;
        day = d;
        month = m;
        year = y;
    }

    public void print() {
        DecimalFormat f = Window.timeFormat;
        System.out.println((day + 1) + " of " + monthNames[month] + ", " + year);
        System.out.println(f.format((int)hours) + ":" + f.format((int)minutes) + ":" + f.format((int)seconds) + ":" + f.format((int)milliseconds));
    }

    // Returns true if calling DateTime is same day, month, and year as other
    public boolean isSameDayAs(DateTime other) {
        return (day == other.day && month == other.month && year == other.year);
    }

    // Returns true if calling DateTime is the same (to the millisecond) as other
    public boolean equalsExactly(DateTime other) {
        return ((milliseconds == other.milliseconds) && (seconds == other.seconds) &&
                (minutes == other.minutes) && (hours == other.hours) && (day == other.day) &&
                (month == other.month) && (year == other.year));
    }

    // Returns true if y is a leap year, false otherwise
    private boolean isLeapYear(int y) {
        return (((y % 400) == 0) || ((y % 4 == 0) && (y % 100 != 0)));
    }

    // Returns number of days in specified month
    // Year is taken as a parameter for the case of February (m = 1) on leap years
    private int getDaysInMonth(int m, int y) {
        if ((m == 1) && isLeapYear(y)) {
            return 29;
        }
        return daysInMonths[m];
    }

    // Checks if any variable has gone over its respective range (minutes > 60, hours > 24, etc.) and adjusts
    // accordingly. E.g: if 60 minutes have passed, reset minutes and increment hours
    private void checkOverflow() {
        while (milliseconds >= 1000) {
            seconds += 1;
            milliseconds -= 1000;
        }
        while (seconds >= 60) {
            minutes += 1;
            seconds -= 60;
        }
        while (minutes >= 60) {
            hours += 1;
            minutes -= 60;
        }
        while (hours >= 24) {
            day += 1;
            hours -= 24;
        }
        while ((month < 12) && day >= getDaysInMonth(month, year)) {
            day -= getDaysInMonth(month, year);
            month += 1;
        }
        while (month >= 12) {
            year += 1;
            month -= 12;
        }
    }

    void addMillis(double ms) {
        milliseconds += ms;
        checkOverflow();
    }

    void addSeconds(double s) {
        seconds += s;
        checkOverflow();
    }

    void addMinutes(double m) {
        minutes += m;
        checkOverflow();
    }

    void addHours(double h) {
        hours += h;
        checkOverflow();
    }

    void addDays(int d) {
        day += d;
        checkOverflow();
    }

    void addMonths(int m) {
        month += m;
        checkOverflow();
    }

    void addYears(int y) {
        year += y;
        checkOverflow();
    }
}