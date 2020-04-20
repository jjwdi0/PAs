import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution4 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution3.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output3.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution3

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution3
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution3   // 0.5초 수행
       timeout 1 java Solution3     // 1초 수행
 */

class Solution3 {

    static int n;
    static String s;
    static int Answer;

    static int dp[][];

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input3.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output3.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input3.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output3.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 파일에서 읽어옵니다.
               첫 번째 행에 쓰여진 문자열의 길이를 n에 읽어들입니다.
               그 다음 행에 쓰여진 문자열을 s에 한번에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            s = br.readLine();

            /////////////////////////////////////////////////////////////////////////////////////////////

            dp = new int[5005][5005];
            // 주어진 문자열 s와 s를 뒤집은 s'이 있다고 하자.
            // s에서 가장 긴 팰린드롬 부분서열은 s'에서도 부분서열로 나타난다.
            // 즉 s와 s'의 Longest Common Subsequence를 구하면 된다.
            // dp(i, j) := s[1:i]와 s'[1:j] 간의 LCS 길이
            // dp(i, j) = dp(i - 1, j - 1) + 1                  s[i] == s'[j])
            // dp(i, j) = max(dp(i - 1, j), dp(i, j - 1))       otherwise
            // 구해야 할 총 상태가 O(n^2)개, 하나의 상태를 계산할 때 O(1)이 걸리므로
            // 최종 시간복잡도: O(n^2)

            String rev = "";
            for(int i=0; i<n; i++) rev += s.charAt(n - i - 1);

            for(int i=1; i<=n; i++) for(int j=1; j<=n; j++) {
                if(s.charAt(i - 1) == rev.charAt(j - 1)) dp[i][j] = dp[i - 1][j - 1] + 1;
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
            Answer = dp[n][n];


            // output3.txt로 답안을 출력합니다.
            pw.println("#" + test_case + " " + Answer);
			/*
			   아래 코드를 수행하지 않으면 여러분의 프로그램이 제한 시간 초과로 강제 종료 되었을 때,
			   출력한 내용이 실제로 파일에 기록되지 않을 수 있습니다.
			   따라서 안전을 위해 반드시 flush() 를 수행하시기 바랍니다.
			 */
            pw.flush();
        }

        br.close();
        pw.close();
    }
}

