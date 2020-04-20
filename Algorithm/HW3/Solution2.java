import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution2 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution2.java -encoding UTF8

   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output2.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution2

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution2
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution2   // 0.5초 수행
       timeout 1 java Solution2     // 1초 수행
 */

class Solution2 {
	static final int MAX_N = 20000;
	static final int MAX_E = 80000;

	static int N, E;
	static int[] U = new int[MAX_E], V = new int[MAX_E], W = new int[MAX_E];
	static int Answer;

	static int[] par = new int[MAX_E];
	static int[] sort = new int[MAX_E], tmp = new int[MAX_E];

	static int max(int x, int y) { return x < y ? y : x; }

	static int find(int x) { return par[x] = (par[x] == x ? x : find(par[x])); }
	static void uni(int x, int y) { par[find(x)] = par[find(y)]; }

	static void mergesort(int s, int e) {
		if(s >= e) return;
		int mid = s + e >> 1;
		mergesort(s, mid);
		mergesort(mid + 1, e);
		int l = s, r = mid + 1;
		for(int i=s; i<=e; i++) tmp[i] = sort[i];
		for(int i=s; i<=e; i++) {
			if(l == mid + 1) sort[i] = tmp[r++];
			else if(r == e + 1) sort[i] = tmp[l++];
			else if(W[tmp[l]] < W[tmp[r]]) sort[i] = tmp[r++];
			else sort[i] = tmp[l++];
		}
	}

	public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input2.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output2.txt 로 정답을 출력합니다.
		 */
		BufferedReader br = new BufferedReader(new FileReader("input2.txt"));
		StringTokenizer stk;
		PrintWriter pw = new PrintWriter("output2.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
		for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 정점의 개수와 간선의 개수를 각각 N, E에 읽어들입니다.
			   그리고 각 i번째 간선의 양 끝점의 번호를 U[i], V[i]에 읽어들이고, i번째 간선의 가중치를 W[i]에 읽어들입니다. (0 ≤ i ≤ E-1, 1 ≤ U[i] ≤ N, 1 ≤ V[i] ≤ N)
			 */
			stk = new StringTokenizer(br.readLine());
			N = Integer.parseInt(stk.nextToken()); E = Integer.parseInt(stk.nextToken());
			stk = new StringTokenizer(br.readLine());
			for (int i = 0; i < E; i++) {
				U[i] = Integer.parseInt(stk.nextToken());
				V[i] = Integer.parseInt(stk.nextToken());
				W[i] = Integer.parseInt(stk.nextToken());
			}


			/////////////////////////////////////////////////////////////////////////////////////////////

			// Maximum Spanning Tree를 구하기 위해서는, Minimum Spanning Tree를 구하는 것과 비슷하게 하면 된다.
			// Kruskal Algorithm을 약간 변형하여, 모든 Edge를 가중치 내림차순으로 정렬한다.
			// 앞에서부터 서로 다른 Vertex Set에 속한 Vertices를 MST에 추가한다.
			// 위 알고리즘의 정당성은, 모든 Weight를 뒤집어 Minimum Spanning Tree를 구하면 원래 그래프의 Maximum Spanning Tree와 동치임을 통해 보일 수 있다.
			// 시간복잡도 O(V + E log E)

			for(int i=0; i<E; i++) sort[i] = i; // 간선을 가중치 내림차순으로 정렬하기 위한 배열
			mergesort(0, E - 1); // mergesort를 이용하여 sort[]를 정렬

			Answer = 0;

			for(int i=1; i<=N; i++) par[i] = i; // Disjoint-Set을 이용해 집합 표현
			int cnt = 0;
			for(int i=0; i<E; i++) {
				int u = U[sort[i]], v = V[sort[i]], w = W[sort[i]];
				if(find(u) == find(v)) continue; // 두 정점이 같은 Vertex Set에 있으면 넘어감
				Answer += w; // 답에 현재 간선의 weight 추가
				uni(u, v); // 두 정점이 속한 집합 합침
				if(++cnt == N - 1) break; // MST에 속한 간선이 N - 1개면 반복문 종료
			}


			/////////////////////////////////////////////////////////////////////////////////////////////


			// output2.txt로 답안을 출력합니다.
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

