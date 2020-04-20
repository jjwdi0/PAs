import java.io.BufferedReader;
import java.io.InputStreamReader;
// import java.util.regex.Pattern;

public class BigInteger {
    public static final String QUIT_COMMAND = "quit";
	
    int[] num = new int[202]; // 각 자리수를 저장하는 배열
    int length, sgn; // length: 숫자의 자리수 / sgn: 숫자의 부호(+: 1, -: -1)
    
    // 생성자
    public BigInteger(String s) {
    	if(s.equals("")) {
    		s = "0";
    	}
    	if(s.charAt(0) == '-') {
    		this.sgn = -1;
    		s = s.substring(1);
    	}
    	else {
    		this.sgn = 1;
    		if(s.charAt(0) == '+') s = s.substring(1);
    	}
    	for(int i=0; i<s.length(); i++) {
    		this.num[i] = s.charAt(i) - '0';
    	}
    	this.length = s.length();
    }
    
    // 덧셈 구현
    public BigInteger plus(BigInteger A) {
    	// 양수 + 양수의 경우만 처리하기 위한 예외 처리 코드 
    	if(this.sgn == -1) {
    		BigInteger tmp = this;
    		tmp.sgn = 1;
    		return A.minus(tmp);
    	}
    	else if(A.sgn == -1) {
    		BigInteger tmp = A;
    		tmp.sgn = 1;
    		return this.minus(tmp);
    	}
    	String res = ""; // 결과 수를 저장하기 위한 변수
    	int mx = this.length < A.length ? A.length : this.length;
    	int v = 0; // 받아올림(?) 표시하는 변수
    	for(int i=1; i<=mx; i++) {
    		int x = 0, y = 0;
    		if(this.length - i < 0) x = 0;
    		else x = this.getVal(this.length - i);
    		
    		if(A.length - i < 0) y = 0;
    		else y = A.getVal(A.length - i);
    		
    		int tmp = x + y + v;
    		if(tmp >= 10) {
    			v = 1;
    			tmp -= 10;
    		}
    		else v = 0;
    		
    		res = (char)(tmp + '0') + res;
    	}
    	if(v == 1) res = '1' + res;
    	
    	return new BigInteger(res);
    }
    
    // 뺄셈 구현
    public BigInteger minus(BigInteger A) {
    	// 0 - A = -A
    	if(this.length == 1 && this.getVal(0) == 0) {
    		A.sgn *= -1;
    		return A;
    	}
    	// 양수 - 양수 만을 처리하기 위한 예외 처리 코드
    	if(this.sgn == -1 && A.sgn == 1) {
    		BigInteger tmp = this;
    		tmp.sgn = 1;
    		return new BigInteger("0").minus(tmp.plus(A));
    	}
    	else if(A.sgn == -1) {
    		BigInteger tmp = A;
    		tmp.sgn = 1;
    		return this.plus(tmp);
    	}
    	if(this.length < A.length) {
    		return new BigInteger("0").minus(A.minus(this));
    	}
    	else if(this.length == A.length && this.toString().compareTo(A.toString()) < 0) {
    		return new BigInteger("0").minus(A.minus(this));
    	}
    	
    	String res = "";
    	int mx = this.length;
    	int v = 0;
    	for(int i=1; i<=mx; i++) {
    		int x = 0, y = 0;
    		if(this.length - i < 0) x = 0;
    		else x = this.getVal(this.length - i);
    		
    		if(A.length - i < 0) y = 0;
    		else y = A.getVal(A.length - i);
    		
    		int tmp = x - y - v;
    		if(tmp < 0) {
    			v = 1;
    			tmp += 10;
    		}
    		else v = 0;
    		
    		res = (char)(tmp + '0') + res;
    	}
    	
    	return new BigInteger(res).remake();
    }
  
    // 곱셈 구현
    public BigInteger multiply(BigInteger A) {
    	int x = this.sgn, y = A.sgn;
    	int sgn = (x == y ? 1 : -1);
    	this.sgn = A.sgn = 1;
    	if(this.length == 1 && this.getVal(0) == 0) return this;
    	if(A.length == 1 && A.getVal(0) == 0) return A;
    	
    	BigInteger res = new BigInteger("0");
    	
    	for(int i=0; i<this.length; i++) {
    		int v = 0;
    		String str = "";
    		if(this.getVal(i) != 0) {
	    		for(int j=1; j<=A.length; j++) {
	    			int idx = A.length - j;
	    			int vv = A.getVal(idx) * this.getVal(i);
	    			vv += v;
	    			v = vv / 10;
	    			vv %= 10;
	    			str = (char)(vv + '0') + str;
	    		}
    		}
    		if(v > 0) str = (char)(v + '0') + str;
    		BigInteger tmp = new BigInteger(str);
    		tmp.addZero(this.length - i - 1);
    		res = res.plus(tmp);
    	}
    	res.sgn = sgn;
    	return res;
    }
    
    // BigInteger 뒤에 0을 x개 만큼 추가하는 메소드
    public void addZero(int x) {
    	for(int i=this.length; i<this.length+x; i++) {
    		this.num[i] = 0;
    	}
    	this.length += x;
    }
    
    // Leading Zeroes를 제거하고 숫자 0에 부호가 붙는 문제를 해결하는 메소드
    public BigInteger remake() {
    	BigInteger X = this;
    	if(X.length == 1 && X.getVal(0) == 0) {
    		X.sgn = 1;
    		return X;
    	}
    	int cnt = 0;
    	for(int i=0; i<X.length; i++) {
    		if(X.getVal(i) != 0) break;
    		cnt++;
    	}
    	if(cnt == X.length) {
    		return new BigInteger("0");
    	}
    	BigInteger res = new BigInteger(X.toString().substring(cnt));
    	res.sgn = X.sgn;
    	return res;
    }
    
    // x번째 숫자를 반환하는 메소드
    public int getVal(int x) {
    	if(x < 0 || x >= this.length) return 0;
    	return this.num[x];
    }
    
    // input을 parsing하고 결과를 반환하는 메소드
    public static BigInteger evaluate(String input) throws IllegalArgumentException {
    	String A = "", B = "";
    	char sign = 0;
    	
    	for(int i=0; i<input.length(); i++) {
    		if(input.charAt(i) == ' ') {
    			input = input.substring(0, i) + input.substring(i + 1);
    			i--;
    		}
    	}
    	
    	for(int i=0; i<input.length(); i++) {
    		if(input.charAt(i) == '+' || input.charAt(i) == '-' || input.charAt(i) == '*') {
    			if(i == 0) continue;
    			for(int j=0; j<i; j++) A += input.charAt(j);
    			for(int j=i+1; j<input.length(); j++) B += input.charAt(j);
    			sign = input.charAt(i);
    			break;
    		}
    	}
    	
    	BigInteger X = new BigInteger(A);
    	X.remake();
    	BigInteger Y = new BigInteger(B);
    	Y.remake();
    	
    	if(sign == '+') return X.plus(Y);
    	else if(sign == '-') return X.minus(Y);
    	else return X.multiply(Y);
    }
    
    public String toString() {
    	String res = "";
    	
    	if(this.sgn == -1) res = "-";
    	for(int i=0; i<this.length; i++) {
    		res += this.getVal(i);
    	}
    	
    	return res;
    }
  
    public static void main(String[] args) throws Exception {
        try (InputStreamReader isr = new InputStreamReader(System.in)) {
            try (BufferedReader reader = new BufferedReader(isr)) {
                boolean done = false;
                while (!done) {
                    String input = reader.readLine();
  
                    try {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e) {
                        // System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    public static boolean processInput(String input) throws IllegalArgumentException {
        boolean quit = isQuitCmd(input);
  
        if (quit) {
            return true;
        }
        else {
            BigInteger result = evaluate(input);
            result.remake();
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    public static boolean isQuitCmd(String input) {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
