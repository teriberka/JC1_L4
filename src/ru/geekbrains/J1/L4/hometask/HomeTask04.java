package ru.geekbrains.J1.L4.hometask;

import java.util.Random;
import java.util.Scanner;

public class HomeTask04 {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = 'O';
    private static final char DOT_EMPTY = '_';

    private static int fieldSizeX;
    private static int fieldSizeY;
    private static int fieldWinLen;
    private static char[][] field;

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random RANDOM = new Random();

    private static void initField() {
        fieldSizeY = 5;
        fieldSizeX = 5;
        fieldWinLen = 4;
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        System.out.println("------");
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }
    }

    private static boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >=0 && y < fieldSizeY;
    }

    private static boolean isEmptyCell(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    private static void humanTurn() {
        int x;
        int y;
        do {
            System.out.println("Введите координаты хода X и Y (от 1 до 3) через пробел >>> ");
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        } while (!isValidCell(x, y) || !isEmptyCell(x, y));
        field[y][x] = DOT_HUMAN;
    }

//    private static void aiTurn() {
//        int x;
//        int y;
//        do {
//            x = RANDOM.nextInt(fieldSizeX);
//            y = RANDOM.nextInt(fieldSizeY);
//        } while (!isEmptyCell(x, y));
//        field[y][x] = DOT_AI;
//    }

    private static void aiTurn() {
        int x;
        int y;
        boolean sendFlag = false;
        for (int i = 0; i < field.length; i++){
            for (int j = 0; j < field.length; j++){
                if (field[i][j] == DOT_HUMAN){
                    do {
                        x = i + generateShift();
                        y = j + generateShift();
//                        System.out.println("aiTurn, x:" + x + " y: " + y);
                    } while (!isValidCell(x, y));
                    if (isEmptyCell(x, y)) {
//                        System.out.println("aiTurn, turn x:" + x + " y: " + y);
                        field[y][x] = DOT_AI;
                        sendFlag = true;
                        break;
                    }
                }
            }
            if (sendFlag) break;
        }
        if (!sendFlag) {
            do {
                x = RANDOM.nextInt(fieldSizeX);
                y = RANDOM.nextInt(fieldSizeY);
            } while (!isEmptyCell(x, y));
            field[y][x] = DOT_AI;
        }
    }

    public static int generateShift(){
        if(Math.random() < 0.5) return 1;
        return -1;
    }

    private static boolean isFieldFull() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

//    private static boolean checkWin(char c) {
//        if (field[0][0] == c && field[0][1] == c && field[0][2] == c) return true;
//        if (field[1][0] == c && field[1][1] == c && field[1][2] == c) return true;
//        if (field[2][0] == c && field[2][1] == c && field[2][2] == c) return true;
//
//        if (field[0][0] == c && field[1][0] == c && field[2][0] == c) return true;
//        if (field[0][1] == c && field[1][1] == c && field[2][1] == c) return true;
//        if (field[0][2] == c && field[1][2] == c && field[2][2] == c) return true;
//
//        if (field[0][0] == c && field[1][1] == c && field[2][2] == c) return true;
//        if (field[0][2] == c && field[1][1] == c && field[2][0] == c) return true;
//        return false;
//    }

// 1. Полностью разобраться с кодом урока;
// - done
// 2. Переделать проверку победы, чтобы она не была реализована просто набором условий.
    private static boolean checkWin(char c){
        if (checkWinDiagonalV1(c) || checkWinLineV1(c)) return true;
        return false;
    }

    private static boolean checkWinDiagonalV1(char c) {
        boolean resultD1 = true, resultD2 = true;
        for (int i = 0; i < field.length; i++){
            resultD1 = resultD1 & (field[i][i] == c);
            resultD2 = resultD2 & (field[i][field.length - i - 1] == c);
        }

        if (resultD1 || resultD2) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWinLineV1(char c) {
        boolean resultLX, resultLY;

        for (int x = 0; x < field.length; x++) {
            resultLX = true;
            resultLY = true;

            for (int y = 0; y < field.length; y++) {
                resultLX = resultLX & (field[x][y] == c);
                resultLY = resultLY & (field[y][x] == c);
            }

            if (resultLX || resultLY) return true;
        }

        return false;
    }

// 3. Попробовать переписать логику проверки победы, чтобы она работала для поля 5х5 и количества символов
// 4. Очень желательно не делать это просто набором условий для каждой из возможных ситуаций;
    private static boolean checkWin2 (char с) {
        int checkLen = field.length - fieldWinLen + 1;
//        System.out.println("checkLen: " + checkLen);
        for (int x = 0; x < checkLen; x++) {
            for (int y = 0; y < checkLen; y++) {
                if (checkWinDiagonalV2(с, x, y) || checkWinLineV1ineV2(с, x, y)) return true;
            }
        }
        return false;
    }

    private static boolean checkWinDiagonalV2(char c, int startX, int startY) {
        System.out.println("checkWinDiagonalV2: " + startX + " " + startY);
        boolean resultD1 = true, resultD2 = true;
        int n, m;
        n = 0;
        for (int i = startX; i < startX + fieldWinLen; i++){
            m = 0;
            for (int j = startY; j < startY + fieldWinLen; j++) {
//                System.out.println("checkWinDiagonalV2, i: " + i + " j: " + j);
//                System.out.println("checkWinDiagonalV2, n: " + n + " m: " + m);
                if (n == m) {
                    resultD1 = resultD1 & (field[i][j] == c);
                    resultD2 = resultD2 & (field[i][fieldWinLen - j] == c);
                }
                m += 1;
            }
            n += 1;
        }

        if (resultD1 || resultD2) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean checkWinLineV1ineV2(char c, int startX, int startY) {
        boolean resultLX, resultLY;
//        System.out.println("checkWinLineV1ineV2: " + startX + " " + startY);
        for (int x = startX; x < startX + fieldWinLen; x++) {
            resultLX = true;
            resultLY = true;

            for (int y = startY; y < startY + fieldWinLen; y++) {
                resultLX = resultLX & (field[x][y] == c);
                resultLY = resultLY & (field[y][x] == c);
            }

            if (resultLX || resultLY) return true;
        }

        return false;
    }



    public static void main(String[] args) {
//        while (true) {
        playOneRound();
//            System.out.println("Play again?");
//            if (SCANNER.next().equals("no"))
//                break;
//        }
    }

    private static void playOneRound() {
        initField();
        printField();
        while (true) {
            humanTurn();
            printField();
            if (checkWin2(DOT_HUMAN)) {
                System.out.println("Выиграл игрок!");
                break;
            }
            if (isFieldFull()) {
                System.out.println("Ничья!");
                break;
            }
            aiTurn();
            printField();
            if (checkWin2(DOT_AI)) {
                System.out.println("Выиграл компьютер!");
                break;
            }
            if (isFieldFull()) {
                System.out.println("Ничья!");
                break;
            }
        }
    }
}
