package com.example.lab004;

public class Student extends Person {
    public int[] attendance;
    public String attendanceString;

    public int[] getAttendance() {
        return attendance;
    }

    public void setAttendance(int[] attendance) {
        this.attendance = attendance;
    }

    public String getAttendanceString() {
        return attendanceString;
    }

    public void setAttendanceString(String attendanceString) {
        this.attendanceString = attendanceString;
    }

    public void setAttendanceString(int[] attendance) {
        this.attendanceString = "";
        for (int i = 0; i < attendance.length; i++) {
            this.attendanceString += attendance[i] + " ";
        }
    }

    @Override
    public String toString() {
        return id + " " + name + " " + attendanceString;
    }
}
