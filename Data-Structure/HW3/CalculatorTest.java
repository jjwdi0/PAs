import java.io.*;
import java.util.Stack;
import java.util.Vector;

public class CalculatorTest {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("q") == 0)
                    break;

                command(input);
            }
            catch (Exception e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) {
        Stack<String> s = new Stack<>();

        String tmp = input;
        input = "";

        // 연속된 공백을 제거하는 반복문
        for(int i=0; i<tmp.length(); i++) {
            if(i >= 1 && tmp.charAt(i) == ' ' && tmp.charAt(i - 1) == ' ') continue;
            if(i >= 1 && tmp.charAt(i) == ' ' && tmp.charAt(i - 1) == '\t') continue;
            if(i >= 1 && tmp.charAt(i) == '\t' && tmp.charAt(i - 1) == ' ') continue;
            if(i >= 1 && tmp.charAt(i) == '\t' && tmp.charAt(i - 1) == '\t') continue;
            input += tmp.charAt(i);
        }

        tmp = input;
        input = "";

        // 숫자 + 공백 + 숫자 형식의 입력 예외 처리
        for(int i=0; i<tmp.length(); i++) {
            if(tmp.charAt(i) == ' ' || tmp.charAt(i) == '\t') continue;
            if(tmp.charAt(i) >= '0' && tmp.charAt(i) <= '9') {
                if(i >= 2 && (tmp.charAt(i - 1) == ' ' || tmp.charAt(i - 1) == '\t') && tmp.charAt(i - 2) >= '0' && tmp.charAt(i - 2) <= '9') {
                    System.out.println("ERROR");
                    return;
                }
            }
            input += tmp.charAt(i);
        }

        // 괄호가 올바르게 들어왔는지 확인
        for(int i=0; i<input.length(); i++) {
            char c = input.charAt(i);
            if(c == '(') {
                s.push(Character.toString(c));
            }
            else if(c == ')') {
                // ')'가 더 많으면 ERROR 표시
                if(s.empty()) {
                    System.out.println("ERROR");
                    return;
                }
                s.pop();
            }
        }

        // '('가 더 많으면 ERROR 표시
        if(s.size() > 0) {
            System.out.println("ERROR");
            return;
        }

        Vector<String> postfix = new Vector<>(); // 최종 postfix expression을 저장하는 벡터
        Stack<String> operator = new Stack<>(); // 연산자를 담는 스택
        Stack<String> number = new Stack<>(); // 숫자를 담는 스택

        int sgn = 1; // 숫자 숫자 / 연산자 연산자 와 같은 형식을 예외 처리하기 위한 변수

        // infix -> postfix
        for(int i=0; i<input.length(); i++) {
            char c = input.charAt(i);
            // '('일 경우 연산자 스택에 그냥 집어넣음
            if(c == '(') {
                operator.push(Character.toString(c));
            }
            // ')'일 경우 연산자 스택에서 '('를 만날 때까지 연산자를 꺼내서 postfix에 집어넣음
            else if(c == ')') {
                if(sgn == 1) {
                    System.out.println("ERROR");
                    return;
                }
                while(!operator.empty()) {
                    String op = operator.peek(); operator.pop();
                    if(op.equals("(")) break;
                    postfix.add(op);
                }
            }
            // 숫자일 경우 연결된 숫자 모두 합쳐서 postfix에 넣음
            else if('0' <= c && c <= '9') {
                if(sgn == 0) {
                    System.out.println("ERROR");
                    return;
                }
                String num = "";
                int j = i;
                while(j < input.length() && input.charAt(j) >= '0' && input.charAt(j) <= '9') {
                    num += input.charAt(j);
                    j++;
                }
                postfix.add(num);
                i = j - 1;
                sgn = 0;
            }
            // 다른 연산자일 경우
            else {
                // 숫자 앞에 붙은 unary '-'일 경우
                if(sgn == 1 && c == '-') {
                    // 우선순위가 낮은 것이 나올 때까지 연산자 스택에서 꺼내서 postfix에 집어넣음
                    while(!operator.empty()) {
                        String top = operator.peek();
                        if(top != "^") break;
                        postfix.add(top);
                        operator.pop();
                    }
                    operator.push("~");
                }
                // '^'일 경우 그냥 연산자 스택에 집어넣음(우선순위가 가장 높으므로)
                else if(c == '^') {
                    operator.push("^");
                }
                // 우선순위가 같은 '*', '/', '%'일 경우
                else if(c == '*' || c == '/' || c == '%') {
                    // 더 우선순위가 낮은
                    while(!operator.empty()) {
                        String top = operator.peek();
                        // 우선순위가 낮은 것이 나올 때까지 연산자 스택에서 꺼내서 postfix에 집어넣음
                        if(top.equals("+") || top.equals("-") || top.equals("(")) break;
                        postfix.add(top);
                        operator.pop();
                    }
                    operator.push(Character.toString(c));
                }
                // '+', '-'일 경우
                else if(c == '+' || c == '-') {
                    // 연산자 스택에 있는 연산자를 전부 다 postfix에 넣음
                    while(!operator.empty()) {
                        String top = operator.peek();
                        if(top.equals("(")) break;
                        postfix.add(top);
                        operator.pop();
                    }
                    operator.push(Character.toString(c));
                }
                sgn = 1;
            }
            // for(String it : postfix) System.out.print(it + " "); System.out.println("");
        }
        // 남은 연산자는 모두 postfix에 집어넣음
        while(!operator.empty()) {
            postfix.add(operator.peek());
            operator.pop();
        }

        // postfix expression을 이용해 연산
        for(String it : postfix) {
            // unary '-'일 경우
            if(it.equals("~")) {
                // 계산중인 숫자가 없으면 ERROR 처리
                if(number.empty()) {
                    System.out.println("ERROR");
                    return;
                }
                String top = number.peek(); number.pop();
                // 부호 뒤집어줌
                if(top.charAt(0) == '-') {
                    top = top.substring(1);
                }
                else {
                    top = "-" + top;
                }
                number.push(top);
            }
            // binary 연산자
            else if(it.equals("^") || it.equals("*") || it.equals("/") || it.equals("%") || it.equals("+") || it.equals("-")) {
                // operand가 없으면 ERROR 처리
                if(number.empty()) {
                    System.out.println("ERROR");
                    return;
                }
                String top1 = number.peek(); number.pop();
                if(number.empty()) {
                    System.out.println("ERROR");
                    return;
                }
                String top2 = number.peek(); number.pop();
                // 연산자에 맞게 계산해서 number 스택에 다시 집어넣음
                long u = Long.parseLong(top2), v = Long.parseLong(top1);
                if(it.equals("^")) {
                    if(v < 0 && u == 0) {
                        System.out.println("ERROR");
                        return;
                    }
                    long res = (long)Math.pow(u, v);
                    number.push(Long.toString(res));
                }
                else if(it.equals("*")) {
                    long res = u * v;
                    number.push(Long.toString(res));
                }
                else if(it.equals("/")) {
                    if(v == 0) {
                        System.out.println("ERROR");
                        return;
                    }
                    long res = u / v;
                    number.push(Long.toString(res));
                }
                else if(it.equals("%")) {
                    if(v == 0) {
                        System.out.println("ERROR");
                        return;
                    }
                    long res = u % v;
                    number.push(Long.toString(res));
                }
                else if(it.equals("+")) {
                    long res = u + v;
                    number.push(Long.toString(res));
                }
                else if(it.equals("-")) {
                    long res = u - v;
                    number.push(Long.toString(res));
                }
            }
            // 숫자일 경우 number 스택에 그냥 집어넣음
            else {
                number.push(it);
            }
        }

        // 남는 숫자가 한 개가 아니면 ERROR 처리
        if(number.size() != 1) {
            System.out.println("ERROR");
            return;
        }

        // postfix 식에서 마지막 공백 없이 출력
        String last = postfix.lastElement();
        postfix.remove(postfix.size() - 1);

        for(String it : postfix) {
            System.out.print(it + " ");
        }
        System.out.println(last);
        System.out.println(number.peek());
    }
}
