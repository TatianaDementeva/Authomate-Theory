

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

public class Main {

    private static Scanner scanIn = new Scanner(System.in);

    private static HashSet<Character> N = new HashSet<Character>();
    private static HashSet<Character> Z = new HashSet<Character>();
    private static Multimap<String,Character> P = ArrayListMultimap.create();
    private static Character S;

    public static void main(String[] args) {

        System.out.println("Грамматика имеет вид G = ( N , ∑ , P , S ), где\n" +
                "N - алфавит нетерминальных символов (может состоять из всего, кроме {',\",-,>,} )\n" +
                "∑ - алфавит терминальных символов (может состоять из всего, кроме N⋃{',\",-,>,} )\n" +
                "P - конечное множество правил\n" +
                "    Правила могут иметь вид a->b , где a∈(N⋃∑)*N(N⋃∑)*, ba∈(N⋃∑)*\n" +
                "S - выделенный символ из N считаем конечным\n\n"+
                "КС-грамматика:каждое правило из Р имеет вид А->a, где A∈N, a∈(N⋃∑)*\n"+
                "e - зарезервированный символ для пустого слова, он может появиться только в правилах!\n\n");

        System.out.println("Введите Контекстно свободную грамматику:");

        // Получаем КС-грамматику и проверяем
        parseN();

        parseZ();

        parseP();

        parseS();


        // Создаем правосторонний автомат
        StoreMemoryMachine storeMemoryMachine = new StoreMemoryMachine(N,Z,P,S);


        // Получаем цепочку для распознавания
        System.out.println("\nВведите строку для распознавания из символов множества ∑");
        String inner_str;
        while (true){
            boolean flag = false;
            inner_str = scanIn.nextLine();
            for (char c:inner_str.toCharArray()) {
                if(!Z.contains(c)){
                    System.out.println("Введен символ, не содержащийся в множестве ∑, попробуйте еще раз");
                    flag = true;
                    break;
                }
            }
            if(!flag){
                break;
            }
        }

        // Распознавание цепочки
        System.out.println("\nПроцесс распознавания:");

        boolean result = storeMemoryMachine.syntaxAnalyzer(inner_str);
        System.out.println("\n____________________________________________");
        System.out.println(result ? "Распознать удалось,ура!!" : "Распознать не удалось,печально");

    }

    private static void parseN(){
        String str;
        while (true){
            boolean flag = false;
            System.out.print("N = ");
            str = scanIn.nextLine();
            String newStr = str.replaceAll("[^->e'\"]", "");
            if ( 0 < newStr.length()) {
                System.out.println("Использован неверный символ, попробуйте еще раз");
            }
            else{
                String[] N_arr = str.split(",");
                for (String value : N_arr) {
                    char[] arr = value.toCharArray();
                    if(arr.length>1){
                        System.out.println("Нельзя использовать пробелы между вводимыми значениями, попробуйте еще раз");
                        flag = true;
                        N.clear();
                        break;
                    }
                    else {
                        N.add(arr[0]);
                    }
                }

                if(!flag){
                    break;
                }
            }
        }
    }

    public static void parseZ(){
        String str;
        while (true){
            boolean flag = false;
            boolean flag1 = false;
            System.out.print("∑ = ");
            str = scanIn.nextLine();
            String newStr = str.replaceAll("[^->e'\"]", "");
            if ( 0 < newStr.length()) {
                System.out.println("Использован неверный символ, попробуйте еще раз");
            }
            else{

                for (Character c:str.toCharArray()) {
                    if(N.contains(c)){
                        System.out.println("Использован символ "+c+" из множества N, попробуйте еще раз");
                        flag=true;
                        break;
                    }
                }

                if(flag){
                    continue;
                }
                String[] Terminal_arr = str.split(",");
                for (String value : Terminal_arr) {
                    char[] arr = value.toCharArray();
                    if(arr.length>1){
                        System.out.println("Нельзя использовать пробелы между вводимыми значениями, попробуйте еще раз");
                        flag1 = true;
                        Z.clear();
                        break;
                    }
                    else {
                        Z.add(arr[0]);
                    }
                }
                if(!flag1){
                    break;
                }
            }
        }
    }

    public static void parseP(){
        String str;
        while (true){
            boolean flag = false;
            System.out.print("P = ");
            str = scanIn.nextLine();

            if(!str.matches("[^'\"]*")){
                System.out.println("Использован неверный символ, попробуйте еще раз");
                continue;
            }

            //[ a->b , c->d ]
            String[] P_arr = str.split(",");

            for (String s:P_arr) {//a->bcd

                String[] P_current = s.split("->");//[ a , bcd ]
                if(P_current.length == 2){
                    char[] A = P_current[0].toCharArray(); // [ a ]
                    if(A.length == 1 && N.contains(A[0])){
                        char[] a = P_current[1].toCharArray();// [ b, c, d ]
                        for (char c:a) {
                            if ((!N.contains(c) && !Z.contains(c)) && c != 'e') {
                                System.out.println("Использовался символ не из множеств, попробуйте еще раз");
                                flag = true;
                                break;
                            }

                        }
                        if(flag){
                            break;
                        }
                        else{
                            P.put(new String(a),A[0]);
                        }
                    }
                    else {
                        System.out.println("Нетерминал не принадлежит множеству N, попробуйте еще раз");
                        flag = true;
                        break;
                    }
                }
                else {
                    System.out.println("Правила должны иметь вид А->a, попробуйте еще раз");
                    flag = true;
                    break;
                }

            }

            if(!flag){
                break;
            }
        }
    }

    public static void parseS(){
        String str;
        while (true){
            System.out.print("S = ");
            str = scanIn.nextLine();
            char[] chars = str.toCharArray();
            if(chars.length !=1){
                System.out.println("Нужен лишь один нетерминал, попробуйте еще раз");
            }
            else if(!N.contains(chars[0])){
                System.out.println("Нетерминал не содержится в множестве N, попробуйте еще раз");
            }
            else {
                S=chars[0];
                break;
            }
        }
    }
}
