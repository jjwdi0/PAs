import java.io.*;
import java.util.*;

public class SortingTest {
    public static void main(String args[]) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
            boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
            int[] value;	// 입력 받을 숫자들의 배열
            String nums = br.readLine();	// 첫 줄을 입력 받음
            if (nums.charAt(0) == 'r') {
                // 난수일 경우
                isRandom = true;	// 난수임을 표시

                String[] nums_arg = nums.split(" ");

                int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
                int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
                int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

                Random rand = new Random();	// 난수 인스턴스를 생성한다.

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
                    value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
            }
            else {
                // 난수가 아닐 경우
                int numsize = Integer.parseInt(nums);

                value = new int[numsize];	// 배열을 생성한다.
                for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
                    value[i] = Integer.parseInt(br.readLine());
            }

            // 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
            while (true) {
                int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

                String command = br.readLine();

                long t = System.currentTimeMillis();
                switch (command.charAt(0)) {
                    case 'B':	// Bubble Sort
                        newvalue = DoBubbleSort(newvalue);
                        break;
                    case 'I':	// Insertion Sort
                        newvalue = DoInsertionSort(newvalue);
                        break;
                    case 'H':	// Heap Sort
                        newvalue = DoHeapSort(newvalue);
                        break;
                    case 'M':	// Merge Sort
                        newvalue = DoMergeSort(newvalue);
                        break;
                    case 'Q':	// Quick Sort
                        newvalue = DoQuickSort(newvalue);
                        break;
                    case 'R':	// Radix Sort
                        newvalue = DoRadixSort(newvalue);
                        break;
                    case 'X':
                        return;	// 프로그램을 종료한다.
                    default:
                        throw new IOException("잘못된 정렬 방법을 입력했습니다.");
                }
                if (isRandom) {
                    // 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
                }
                else {
                    // 난수가 아닐 경우 정렬된 결과값을 출력한다.
                    for (int i = 0; i < newvalue.length; i++) {
                        System.out.println(newvalue[i]);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoBubbleSort(int[] value) {
        // TODO : Bubble Sort 를 구현하라.
        // value는 정렬안된 숫자들의 배열이며 value.length 는 배열의 크기가 된다.
        // 결과로 정렬된 배열은 리턴해 주어야 하며, 두가지 방법이 있으므로 잘 생각해서 사용할것.
        // 주어진 value 배열에서 안의 값만을 바꾸고 value를 다시 리턴하거나
        // 같은 크기의 새로운 배열을 만들어 그 배열을 리턴할 수도 있다.
        int N = value.length;
        for(int it=0; it<N-1; it++) {
            boolean f = false; // 인접한 원소들끼리의 교환이 있었는지 체크하는 변수
            for(int i=0; i<N-it-1; i++) {
                if(value[i] > value[i+1]) { // 오른쪽 원소가 더 작으면
                    int tmp = value[i];
                    value[i] = value[i+1];
                    value[i+1] = tmp; // 두 원소 교환
                    f = true;
                }
            }
            if(!f) break; // 교환이 일어나지 않았다면 정렬된 것이므로 종료.
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoInsertionSort(int[] value) {
        // TODO : Insertion Sort 를 구현하라.
        int N = value.length;
        for(int i=1; i<N; i++) {
            int j = i - 1;
            while(j >= 0 && value[j+1] < value[j]) { // 더 작은 숫자를 만날때까지 왼쪽 원소와 교환
                int tmp = value[j+1];
                value[j+1] = value[j];
                value[j] = tmp;
                j--;
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoHeapSort(int[] value) {
        // TODO : Heap Sort 를 구현하라.
        int N = value.length, idx = 0;
        int[] heap = new int[N+1]; // 힙 선언
        for(int i=0; i<N; i++) {
            heap[++idx] = value[i]; // 힙 맨 뒤에 원소 추가
            int j = idx;
            while(j > 1) {
                if(heap[j/2] > heap[j]) { // 부모 원소가 더 클때마다
                    int tmp = heap[j/2];
                    heap[j/2] = heap[j];
                    heap[j] = tmp; // 부모 원소와 자식 원소 교환
                    j /= 2;
                }
                else break;
            }
        }
        while(idx > 0) {
            value[N-idx] = heap[1]; // 가장 작은 수 확인 후
            int tmp = heap[1];
            heap[1] = heap[idx];
            heap[idx] = tmp;
            idx--; // 힙에서 제거
            int i = 1;
            while(i <= idx) {
                int left = i * 2, right = i * 2 + 1, child = 0; // 부모 원소로부터 자식 원소와 비교하여 교환
                if(left > idx) break;
                if(right > idx) child = left;
                else if(heap[left] > heap[right]) child = right;
                else child = left;
                if(heap[i] > heap[child]) { // 자식 원소가 더 작으면
                    int _tmp = heap[i];
                    heap[i] = heap[child];
                    heap[child] = _tmp; // 교환
                    i = child; // 재귀적으로 반복
                }
                else break;
            }
        }
        return (value);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoMergeSort(int[] value) {
        // TODO : Merge Sort 를 구현하라.
        int N = value.length;
        DoMergeSort(value, 0, N - 1); // 재귀함수 이용하기 위해 overloading 이용
        return value;
    }

    private static void DoMergeSort(int[] value, int s, int e) {
        if(s == e) return; // 정렬하고자 하는 배열 크기가 1이면 반환
        int mid = (s + e) / 2; // 가운데 원소를 기준으로 양쪽 부분 배열 정렬
        DoMergeSort(value, s, mid);
        DoMergeSort(value, mid + 1, e);
        int[] merge = new int[e-s+1];
        int l = s, r = mid + 1;
        for(int i=0; i<merge.length; i++) { // 정렬된 두 부분 배열을 앞에서부터 보면서 합쳐줌
            if(l > mid) merge[i] = value[r++];
            else if(r > e) merge[i] = value[l++];
            else if(value[l] < value[r]) merge[i] = value[l++];
            else merge[i] = value[r++];
        }
        for(int i=0; i<merge.length; i++) value[i+s] = merge[i];
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoQuickSort(int[] value) {
        // TODO : Quick Sort 를 구현하라.
        int N = value.length;
        DoQuickSort(value, 0, N - 1); // 재귀함수 이용하기 위해 overloading 이용
        return value;
    }

    private static void DoQuickSort(int[] value, int s, int e) {
        if(s >= e) return; // 정렬하고자 하는 배열 크기가 1 이하면 반환
        if(s >= value.length || e < 0) return; // 정렬하고자 하는 배열 구간의 인덱스가 배열 바깥이면 반환
        int pivot = s, l = s + 1, r = e;
        while(l <= r) {
            if(value[l] <= value[pivot]) l++; // pivot값보다 큰 원소를 만날 때까지 오른쪽으로 이동
            else if(value[r] > value[pivot]) r--; // pivot값보다 작은 원소를 만날 때까지 왼쪽으로 이동
            else {
                int tmp = value[l];
                value[l] = value[r];
                value[r] = tmp; // 두 원소 변경
                l++; r--;
            }
        }
        int tmp = value[r];
        value[r] = value[pivot];
        value[pivot] = tmp;
        DoQuickSort(value, s, r - 1); // pivot 기준 양쪽 정렬
        DoQuickSort(value, r + 1, e);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    private static int[] DoRadixSort(int[] value) {
        // TODO : Radix Sort 를 구현하라.
        int N = value.length;
        Queue<Integer>[] q = new Queue[10]; // 자릿수별 queue 선언
        for(int i=0; i<10; i++) q[i] = new LinkedList<>();

        for(int i=0; i<10; i++) {
            for(int j=0; j<N; j++) {
                Long x = 2147483648L + value[j]; // 음수를 처리하기 위한 연산
                for(int k=0; k<i; k++) x /= 10L;
                q[(int)(x % 10)].add(value[j]); // j번째 자리수에 해당하는 queue에 삽입
            }
            int idx = 0;
            for(int j=0; j<10; j++) { // 자리수 차례대로 꺼내옴
                while(!q[j].isEmpty()) {
                    value[idx++] = q[j].peek();
                    q[j].remove();
                }
            }
        }

        return value;
    }
}
