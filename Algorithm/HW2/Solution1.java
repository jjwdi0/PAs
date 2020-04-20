import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;

/*
   1. 아래와 같은 명령어를 입력하면 컴파일이 이루어져야 하며, Solution1 라는 이름의 클래스가 생성되어야 채점이 이루어집니다.
       javac Solution1.java -encoding UTF8


   2. 컴파일 후 아래와 같은 명령어를 입력했을 때 여러분의 프로그램이 정상적으로 출력파일 output5.txt 를 생성시켜야 채점이 이루어집니다.
       java Solution1

   - 제출하시는 소스코드의 인코딩이 UTF8 이어야 함에 유의 바랍니다.
   - 수행시간 측정을 위해 다음과 같이 time 명령어를 사용할 수 있습니다.
       time java Solution1
   - 일정 시간 초과시 프로그램을 강제 종료 시키기 위해 다음과 같이 timeout 명령어를 사용할 수 있습니다.
       timeout 0.5 java Solution1   // 0.5초 수행
       timeout 1 java Solution1     // 1초 수행
 */

class pair {
    int x, y;
    pair() { x = y = 0; }
    pair(int x, int y) { this.x = x; this.y = y; }
    void swap() { x ^= y; y ^= x; x ^= y; }
}

class Heap {
    private int size;
    private pair[] heap;
    Heap() {
        size = 0;
        heap = new pair[1000001];
    }
    int getSize() { return size; }
    void insert(int x, int y) {
        heap[++size] = new pair(x, y);
        int idx = size / 2;
        while(idx >= 1) {
            int l = idx + idx, r = idx + idx + 1;
            if(r <= size && heap[l].x > heap[r].x) { int tmp = l; l = r; r = tmp; }
            if(heap[l].x > heap[idx].x) break;
            int tmp = heap[l].x; heap[l].x = heap[idx].x; heap[idx].x = tmp;
            tmp = heap[l].y; heap[l].y = heap[idx].y; heap[idx].y = tmp;
            idx >>= 1;
        }
    }
    int top() {
        int res = heap[1].y, idx = 1, tmp;
        heap[1].x = heap[size].x;
        heap[1].y = heap[size].y;
        size--;
        while(idx <= size) {
            int l = idx + idx, r = idx + idx + 1;
            if(l > size) break;
            if(r <= size && heap[l].x > heap[r].x) { tmp = l; l = r; r = tmp; }
            if(heap[l].x > heap[idx].x) break;
            tmp = heap[l].x; heap[l].x = heap[idx].x; heap[idx].x = tmp;
            tmp = heap[l].y; heap[l].y = heap[idx].y; heap[idx].y = tmp;
            idx = l;
        }
        return res;
    }
    boolean empty() { return size == 0; }
}

class Solution1 {
    static final int max_n = 1000000;
    static int[] A = new int[max_n];
    static int[] copied = new int[max_n];
    static int n;
    static int Answer1, Answer2, Answer3;
    static long start;
    static double time1, time2, time3;

    static int[] tmp = new int[max_n];

    static Heap heap = new Heap();

    static void mergesort(int a[], int s, int e) {
        if(s >= e) return;
        int mid = (s + e) / 2;
        mergesort(a, s, mid);
        mergesort(a, mid + 1, e);
        for(int i=s; i<=e; i++) tmp[i] = a[i];
        int l = s, r = mid + 1;
        for(int i=s; i<=e; i++) {
            if(l == mid + 1) a[i] = tmp[r++];
            else if(r == e + 1) a[i] = tmp[l++];
            else if(tmp[l] < tmp[r]) a[i] = tmp[l++];
            else a[i] = tmp[r++];
        }
    }

    // Problem 1-1
    // calls mergesort() to solve
    // T(n) = 2T(n/2) + O(n)
    // T(n) = O(n log n)
    static int merge1(int _A[]) {
        mergesort(_A, 0, n - 1);
        int res = 0;
        for(int i=0; i<n; i+=4) res += _A[i] % 7;
        return res;
    }

    static void mergesort_16naive(int a[], int s, int e) {
        if(14 >= e - s) { mergesort(a, s, e); return; }
        int start[] = new int[16];
        int end[] = new int[16];
        int it[] = new int[16];
        for(int i=0; i<16; i++) it[i] = start[i] = (e - s + 1) / 16 * i + s;
        for(int i=0; i<15; i++) end[i] = start[i + 1] - 1;
        end[15] = e;
        for(int i=0; i<16; i++) mergesort_16naive(a, start[i], end[i]);
        for(int i=s; i<=e; i++) tmp[i] = a[i];
        for(int i=s; i<=e; i++) {
            int val = 2147483647, idx = -1;
            for(int j=0; j<16; j++) if(it[j] <= end[j] && tmp[it[j]] <= val) { idx = j; val = tmp[it[j]]; }
            a[i] = tmp[it[idx]++];
        }
    }

    // Problem 1-2
    // calls mergesort_16naive() to solve
    // T(n) = 16T(n/16) + O(n), but O(n) has a big constant factor
    // T(n) = O(n log n)
    static int merge2(int _A[]) {
        mergesort_16naive(_A, 0, n - 1);
        int res = 0;
        for(int i=0; i<n; i+=4) res += _A[i] % 7;
        return res;
    }

    static void mergesort_16(int a[], int s, int e) {
        if(14 >= e - s) { mergesort(a, s, e); return; }
        int start[] = new int[16];
        int end[] = new int[16];
        int it[] = new int[16];
        for(int i=0; i<16; i++) it[i] = start[i] = (e - s + 1) / 16 * i + s;
        for(int i=0; i<15; i++) end[i] = start[i + 1] - 1;
        end[15] = e;
        for(int i=0; i<16; i++) mergesort_16(a, start[i], end[i]);
        for(int i=s; i<=e; i++) tmp[i] = a[i];
        for(int i=0; i<16; i++) heap.insert(tmp[it[i]], i);
        int idx = s;
        while(!heap.empty()) {
            int r = heap.top();
            a[idx++] = tmp[it[r]];
            if(it[r] == end[r]) continue;
            heap.insert(tmp[++it[r]], r);
        }
    }

    // Problem 1-3
    // calls mergesort_16() to solve
    // T(n) = 16T(n/16) + O(n), but O(n) has a smaller constant factor than Problem 1-2
    // T(n) = O(n log n)
    // 실제로는 1-2보다 더 오래 걸리는 경우가 대부분이었음
    // Heap에서 부가적으로 실행하는 연산이 많아 더 오랜 시간이 걸린 것으로 예상함
    static int merge3(int _A[]) {
        mergesort_16(_A, 0, n - 1);
        int res = 0;
        for(int i=0; i<n; i+=4) res += _A[i] % 7;
        return res;
    }

    public static void main(String[] args) throws Exception {
		/*
		   동일 폴더 내의 input1.txt 로부터 데이터를 읽어옵니다.
		   또한 동일 폴더 내의 output1.txt 로 정답을 출력합니다.
		 */
        BufferedReader br = new BufferedReader(new FileReader("input1.txt"));
        StringTokenizer stk;
        PrintWriter pw = new PrintWriter("output1.txt");

		/*
		   10개의 테스트 케이스가 주어지므로, 각각을 처리합니다.
		 */
        for (int test_case = 1; test_case <= 10; test_case++) {
			/*
			   각 테스트 케이스를 표준 입력에서 읽어옵니다.
			   먼저 배열의 원소의 개수를 n에 읽어들입니다.
			   그리고 각 원소를 A[0], A[1], ... , A[n-1]에 읽어들입니다.
			 */
            stk = new StringTokenizer(br.readLine());
            n = Integer.parseInt(stk.nextToken());
            stk = new StringTokenizer(br.readLine());
            for (int i = 0; i < n; i++) {
                A[i] = Integer.parseInt(stk.nextToken());
            }

            /////////////////////////////////////////////////////////////////////////////////////////////
			/*
			   이 부분에서 여러분의 알고리즘이 수행됩니다. in-place 정렬을 고려하여,
               여러분이 구현한 각각의 함수에 복사한 배열을 입력으로 넣어야 합니다.
			   문제의 답을 계산하여 그 값을 Answer에 저장하는 것을 가정하였습니다.
               함수를 구현하면 Answer1, Answer2, Answer3에 해당하는 주석을 제거하고 제출하세요.

               문제 1은 java 프로그래밍 연습을 위한 과제입니다.
			 */

            /* Problem 1-1 */
            System.arraycopy(A, 0, copied, 0, n);
            start = System.currentTimeMillis();
            Answer1 = merge1(copied);
            time1 = (System.currentTimeMillis() - start) / 1000.;

            /* Problem 1-2 */
            System.arraycopy(A, 0, copied, 0, n);
            start = System.currentTimeMillis();
            Answer2 = merge2(copied);
            time2 = (System.currentTimeMillis() - start) / 1000.;

            /* Problem 1-3 */
            System.arraycopy(A, 0, copied, 0, n);
            start = System.currentTimeMillis();
            Answer3 = merge3(copied);
            time3 = (System.currentTimeMillis() - start) / 1000.;

            /* 여러분의 답안 Answer1, Answer2, Answer3을 비교하는 코드를 아래에 작성 */

            if(Answer1 != Answer2 || Answer2 != Answer3 || Answer3 != Answer1) Answer1 = -1;

            /////////////////////////////////////////////////////////////////////////////////////////////


            // output1.txt로 답안을 출력합니다. Answer1, Answer2, Answer3 중 구현된 함수의 답안 출력
            pw.println("#" + test_case + " " + Answer1 + " " + time1 + " " + time2 + " " + time3);
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

