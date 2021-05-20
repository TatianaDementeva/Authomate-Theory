import Exp.*;

import java.util.Deque;
import java.util.HashSet;

public class NFAMaker {

    public NFAMaker() {
        alphabet = new HashSet<>();
    }

    private Deque<Transition> nfa;
    private final HashSet<Character> alphabet;

    public Deque<Transition> getNfa() {
        return nfa;
    }

    public HashSet<Character> getAlphabet() {
        return alphabet;
    }


    public void makeNFA(Word w){
        Deque<Transition> TransitionDeque = maker(w);
        System.out.println("\nТаблица переходов:\n"+"| Текущее состояние | "+"Символ | "+"Следующее состояние |");
        for (Transition p:TransitionDeque) {
            char c = (p.getW() == '\0')?'ε':p.getW();
            System.out.println("| "+p.getFrom()+" | "+c+" | "+p.getTo()+" |");
        }

        this.nfa =  TransitionDeque;
    }

    public Deque<Transition> maker(Word w){

        switch (w.getClass().getCanonicalName()){

            case "Exp.Symbol" : {
                alphabet.add(((Symbol) w).getS());
                return Transition.sym((Symbol) w);
            }
            case "Exp.Concatenation":{
                Word w1 = ((Concatenation)w).getS1();
                Word w2 = ((Concatenation)w).getS2();
                Deque<Transition> TransitionDeque1 = maker(w1);
                Deque<Transition> TransitionDeque2 = maker(w2);
                return Transition.concat(TransitionDeque1,TransitionDeque2);
            }
            case "Exp.KleeneStar":{
                Word w1 = ((KleeneStar)w).getS();
                Deque<Transition> TransitionDeque1 = maker(w1);
                return Transition.kleene(TransitionDeque1);
            }
            case "Exp.Or" : {
                Word w1 = ((Or)w).getS1();
                Word w2 = ((Or)w).getS2();
                Deque<Transition> TransitionDeque1 = maker(w1);
                Deque<Transition> TransitionDeque2 = maker(w2);
                return Transition.orOp(TransitionDeque1,TransitionDeque2);
            }
            default:
                System.out.println(w.getClass().getCanonicalName());
                return null;
        }

    }


}
