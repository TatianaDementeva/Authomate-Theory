import Exp.Word;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Введите регулярное выражение, состоящее из алфавита 0-9, A-Z, a-z и правил (x|y), (x)*, (x&y)");

        Scanner scanIn = new Scanner(System.in);
        String regExp;
        // Проверяем что ввели нужные символы
        while (true){
            regExp = scanIn.nextLine();
            String emptyRegExp = regExp.replaceAll("[0-9A-Za-z()|*&]", "");
            if ( emptyRegExp.length() > 0) {
                System.out.println("Был использован символ не из алфавита, введите регулярное выражение ещё раз");
            } else{
                break;
            }
        }

        // Строим синтаксическе дерево
        Word lw = LexAnalyzer.parse(regExp);
        if(lw == null){return;}
        System.out.println("\nСтрока, представляющая дерево:");
        System.out.println(lw+" ;");

        // На основе дерева создаем НКА
        NFAMaker nfaMaker = new NFAMaker();
        nfaMaker.makeNFA(lw);

        // Получаем алфавит регулярного выражения
        StringBuilder alphabet = new StringBuilder();
        for (Character c:nfaMaker.getAlphabet() ) {
            alphabet.append(" ").append(c);
        }
        System.out.println("\nВведите строку для распознавания из символов: {"+alphabet+" }");

        // Проверяем что ввели нужные символы
        while (true){
            regExp = scanIn.nextLine();
            String newStr = regExp.replaceAll("["+alphabet+"]", "");
            if ( 0 < newStr.length()) {
                System.out.println("Использован неверный символ, попробуйте еще раз");
            }
            else{
                break;
            }
        }
        // Распознаем цепочку с помощью НКА
        System.out.println("\nПроцесс разпознавания:");

        boolean finish = SyntaxAnalyzer.analyze(nfaMaker,regExp);
        if (!finish){System.out.println("Цепочка не была распознана");}
        else {System.out.println("Цепочка распознана, ура!!!");}
    }


}
