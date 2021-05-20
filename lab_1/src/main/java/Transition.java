import Exp.Symbol;

import java.util.ArrayDeque;
import java.util.Deque;


public class Transition {

    private final int from;
    private final int to;
    private final char w;

    public Transition(int from,  char w, int to) {
        this.from = from;
        this.to = to;
        this.w = w;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public char getW() {
        return w;
    }

    public static int findMax(Deque<Transition> stack){
        int max = Integer.MIN_VALUE;
        for (Transition p:stack ) {
            max = Math.max(p.to, max);
        }
        return max;
    }


    public static Deque<Transition> shiftOrStates(Deque<Transition> stack, int shift){

        Deque<Transition> stack1 = new ArrayDeque<>();

        for (Transition p:stack) {
            Transition p1;
            if(p.from == 0){
                p1 = new Transition(0,p.w,p.to+shift);
            }
            else{
                p1 = new Transition(p.from+shift,p.w,p.to+shift);
            }
            stack1.add(p1);
        }
        return stack1;
    }

    public static Deque<Transition> orOp(Deque<Transition> stack1, Deque<Transition> stack2){

        int max1 = findMax(stack1);
        int max2 = findMax(stack2);

        Deque<Transition> stack = new ArrayDeque<>(stack1);
        stack.removeLast();
        Transition p1 = stack1.getLast();
        Transition p = new Transition(p1.from,p1.w,max1+max2-1);
        stack.add(p);

        stack2 = shiftOrStates(stack2, max1-1);
        stack.addAll(stack2);

        return stack;
    }

    public static Deque<Transition> kleene(Deque<Transition> stack){
        Deque<Transition> stack1 = new ArrayDeque<>();
        int max = findMax(stack);

        for (Transition p:stack) {
            Transition p1 = (p.to != max)?(new Transition(p.from+1,p.w,p.to+1)):(new Transition(p.from+1,p.w,1));
            stack1.add(p1);
        }

        Transition p1 = new Transition(0,'\0',1);
        Transition p2 = new Transition(1,'\0',max+1);
        stack1.addFirst(p1);
        stack1.addLast(p2);

        return stack1;
    }

    public static Deque<Transition> concat(Deque<Transition> stack1, Deque<Transition> stack2){

        int max1 = findMax(stack1);

        Deque<Transition> stack = new ArrayDeque<>(stack1);

        for (Transition p:stack2) {
            Transition p1 = new Transition(p.from+max1,p.w,p.to + max1);
            stack.addLast(p1);
        }

        return stack;
    }

    public static Deque<Transition> sym(Symbol s){

        Transition p = new Transition(0,s.getS(),1);
        Deque<Transition> TransitionArrayDeque= new ArrayDeque<>();
        TransitionArrayDeque.addLast(p);

        return TransitionArrayDeque;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Transition)) {
            return false;
        }
        Transition other = (Transition) obj;

        return (other.getFrom() == this.getFrom()) && (other.getW() == this.getW()) && ( other.getTo() == this.getTo());
    }

}
