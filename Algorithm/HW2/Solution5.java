import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution5 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution5.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution5

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution5
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution5   // 0.5초 수행
       timeout 1 java Solution5     // 1초 수행
 */

class Solution5 {
    static final int max_n = 1000;

    static final int mod = 1000000;

    static int n, H;
    static int[] h = new int[max_n], d = new int[max_n-1];
    static int Answer;

    static int dp[][][];

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input5.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output5.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input5.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output5.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 블록의 개수와 최대 높이를 각각 n, H에 읽어들입니다.
			   그리고 각 블록의 높이를 h[0], h[1], ... , h[n-1]에 읽어들입니다.
			   다음 각 블록에 파인 구멍의 깊이를 d[0], d[1], ... , d[n-2]에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken()); H = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                h[i] = Integer.parseInt(stk.nextToken());
            }
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n-1; i++) {
                d[i] = Integer.parseInt(stk.nextToken());
            }


            /////////////////////////////////////////////////////////////////////////////////////////////

            dp = new int[2][H + 1][2];
            // 0/1 Knapsack Problem과 비슷하나 연속한 물건을 고를 때의 추가 조건이 있음.
            // dp(i, j, state) := 1~i번 블록으로 키가 j이게 배치했고, i번을 골랐는지 여부가 state인 경우의 수
            // dp(i, j, 0) = dp(i - 1, j, 0) + dp(i - 1, j, 1)
            // dp(i, j, 1) = dp(i - 1, j - h[i] + d[i-1], 1) + dp(i - 1, j - h[i], 0)
            // 구해야 할 총 상태가 O(nH)개, 하나의 상태를 계산할 때 O(1)이 걸리므로
            // 최종 시간복잡도: O(nH)
            // 점화식을 보면 알 수 있듯이, dp(i, j, state)를 계산하기 위해 dp(i - 1, j', state')밖에 참조하지 않음
            // Sliding Window 기법을 이용하면, 공간복잡도를 O(nH)에서 O(H)로 바꿀 수 있음. (아래에 구현해 두었음)
            dp[0][0][0] = 1;

            for(int i=0; i<n; i++) {
                for(int j=0; j<=H; j++) dp[(i + 1) & 1][j][0] = dp[(i + 1) & 1][j][1] = 0;
                for(int j=0; j<=H; j++) {
                    if (dp[i & 1][j][0] > 0) {
                        dp[(i + 1) & 1][j][0] = (dp[(i + 1) & 1][j][0] + dp[i & 1][j][0]) % mod;
                        int next = j + h[i];
                        if (next <= H) dp[(i + 1) & 1][next][1] = (dp[(i + 1) & 1][next][1] + dp[i & 1][j][0]) % mod;
                    }
                    if (dp[i & 1][j][1] > 0) {
                        dp[(i + 1) & 1][j][0] = (dp[(i + 1) & 1][j][0] + dp[i & 1][j][1]) % mod;
                        int next = j + h[i] - d[i - 1];
                        if (next <= H) dp[(i + 1) & 1][next][1] = (dp[(i + 1) & 1][next][1] + dp[i & 1][j][1]) % mod;
                    }
                }
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
            Answer = 0;
            for(int i=1; i<=H; i++) Answer = (Answer + dp[n & 1][i][0] + dp[n & 1][i][1]) % mod;

            // output5.txt로 답안을 출력합니다.
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

