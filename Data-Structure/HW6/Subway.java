import java.io.*;
import java.util.*;

public class Subway {
    static HashMap<String, Integer> codeToNumber; // 역 코드를 숫자로 바꾸는 HashMap
    static HashMap<String, Vector<Integer>> nameToNumber; // 역 이름에 해당하는 숫자 Vector를 저장하는 HashMap
    static Vector<Station> stations; // 숫자에 해당하는 역 정보를 저장하는 Vector
    static Vector<Vector<Edge>> adj, rev; // 인접리스트와 그 역방향 간선을 저장하는 rev
    static int stationNumber = 0; // 역 개수

    public static void main(String args[]) {

        stations = new Vector<>(1);
        stations.addElement(new Station());
        codeToNumber = new HashMap<>();
        nameToNumber = new HashMap<>();

        try {
            File file = new File(args[0]);
            FileReader filereader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(filereader);
            String line = "";
            while((line = bufReader.readLine()) != null) {
                if(line.equals("")) break;
                stationNumber++;

                Vector<String> v = new Vector<>(1);
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreTokens()) {
                    String nextToken = st.nextToken();
                    v.addElement(nextToken);
                }

                // 입력이 역 코드, 역 이름, 라인으로 이루어져 있으므로 순서를 따라서 Station class 생성
                Station now = new Station(v.elementAt(1), v.elementAt(0), v.elementAt(2), stationNumber);

                stations.addElement(now);

                // 주어진 역 코드에 맞는 숫자 저장
                codeToNumber.put(v.elementAt(0), stationNumber);
                if(nameToNumber.containsKey(v.elementAt(1))) {
                    nameToNumber.get(v.elementAt(1)).add(stationNumber);
                }
                else {
                    Vector<Integer> tmp = new Vector<>();
                    tmp.addElement(stationNumber);
                    nameToNumber.put(v.elementAt(1), tmp);
                }
            }

            adj = new Vector<>(stationNumber + 1);
            rev = new Vector<>(stationNumber + 1);
            for(int i=0; i<stationNumber + 1; i++) adj.add(i, new Vector<>());
            for(int i=0; i<stationNumber + 1; i++) rev.add(i, new Vector<>());

            Set<Map.Entry<String, Vector<Integer>>> set = nameToNumber.entrySet();

            for(Map.Entry<String, Vector<Integer>> entry : set) {
                if(entry.getValue().size() == 1) continue;
                Vector<Integer> v = entry.getValue();
                for(int i=0; i<v.size(); i++) for(int j=i+1; j<v.size(); j++) {
                    // 환승 가능 역에서 환승 간선 추가
                    adj.elementAt(v.elementAt(i)).addElement(new Edge(v.elementAt(j), 5));
                    adj.elementAt(v.elementAt(j)).addElement(new Edge(v.elementAt(i), 5));
                    rev.elementAt(v.elementAt(i)).addElement(new Edge(v.elementAt(j), 5));
                    rev.elementAt(v.elementAt(j)).addElement(new Edge(v.elementAt(i), 5));
                }
            }

            while((line = bufReader.readLine()) != null) {
                Vector<String> v = new Vector<>(1);
                StringTokenizer st = new StringTokenizer(line);
                while(st.hasMoreTokens()) {
                    String nextToken = st.nextToken();
                    v.addElement(nextToken);
                }

                int here = codeToNumber.get(v.elementAt(0));
                int there = codeToNumber.get(v.elementAt(1));
                adj.elementAt(here).addElement(new Edge(there, Integer.parseInt(v.elementAt(2)))); // 실제 간선
                rev.elementAt(there).addElement(new Edge(here, Integer.parseInt(v.elementAt(2)))); // 역방향 간선
            }
        }

        catch(FileNotFoundException e) {}
        catch(IOException e) {}

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String input = br.readLine();
                if (input.compareTo("QUIT") == 0)
                    break;

                command(input);
            }
            catch (IOException e) {
                System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
            }
        }
    }

    private static void command(String input) {
        String source, sink; // 출발역, 도착역 이름
        StringTokenizer st = new StringTokenizer(input);
        source = st.nextToken();
        sink = st.nextToken();

        PriorityQueue<Node> pq = new PriorityQueue<>(); // 거리가 낮은 순으로 나오는 PriorityQueue 선언
        Vector<Long> dist = new Vector<>(stationNumber + 1); // 역까지 갈 수 있는 최소 거리
        for(int i=0; i<=stationNumber; i++) dist.add(i, Long.MAX_VALUE); // 처음에는 거리를 무한대로 초기화 해줌

        if(!nameToNumber.containsKey(source) || !nameToNumber.containsKey(sink)) {
            System.out.println("INPUT ERROR");
            return;
        }

        Vector<Integer> start = nameToNumber.get(source);
        Vector<Integer> end = nameToNumber.get(sink);
        for(int i=0; i<start.size(); i++) { // 가능한 시작역 모두 PriorityQueue에 저장
            pq.add(new Node(start.elementAt(i), 0));
            dist.set(start.elementAt(i), 0L);
        }

        int destination = 0;

        while(!pq.isEmpty()) {
            Node now = pq.poll();

            int here = now.getU(); long cost = now.getCost();
            // System.out.println(stations.elementAt(here).getName() + " " + cost);
            if(dist.elementAt(here) < cost) continue;

            if(sink.equals(stations.elementAt(here).getName())) { // 현재 정점이 도착역이면 종료
                destination = here;
                break;
            }

            for(int i=0; i<adj.elementAt(here).size(); i++) {
                int there = adj.elementAt(here).elementAt(i).getU();
                int time = adj.elementAt(here).elementAt(i).getWeight();
                if(dist.elementAt(there) > cost + time) { // 연결된 정점의 현재 최단거리가 갱신 가능하면
                    dist.set(there, cost + time); // 최단거리 갱신하고
                    pq.add(new Node(there, cost + time)); // PriorityQueue에 추가
                }
            }
        }

        Stack<Pair> s = new Stack<>(); // 답의 역추적을 위한 Stack
        s.add(new Pair(destination, 0)); // 도착역에서부터 역추적

        while(!stations.elementAt(s.peek().getX()).getName().equals(source)) {
            int u = s.peek().getX();
            // System.out.println(stations.elementAt(u).getName());
            // try { Thread.sleep(500); } catch(InterruptedException e) {}
            for(int i=0; i<rev.elementAt(u).size(); i++) {
                int next = rev.elementAt(u).elementAt(i).getU();
                int cost = rev.elementAt(u).elementAt(i).getWeight();
                // System.out.println(stations.elementAt(next).getName() + " " + dist.elementAt(next));
                if(dist.elementAt(next) == dist.elementAt(u) - cost) { // 역추적중인 정점에서 갈 수 있는 직전 정점인 경우
                    if(stations.elementAt(next).getName().equals(stations.elementAt(u).getName())) { // 환승역이면
                        s.add(new Pair(next, 1)); // 1로 표시
                    }
                    else { // 아니면
                        s.add(new Pair(next, 0)); // 0으로 표시
                    }
                    break;
                }
            }
        }
        int cnt = 0;
        while(!s.isEmpty()) {
            Pair now = s.peek(); s.pop();
            if(cnt > 0) System.out.print(" ");
            if(now.getY() == 1) { // 환승역으로 표시되었으면
                System.out.print("[" + stations.elementAt(now.getX()).getName() + "]");
                s.pop(); // 출력 후 Stack에서 하나 더 삭제
            }
            else { // 아니면
                System.out.print(stations.elementAt(now.getX()).getName()); // 그냥 출력
            }
            cnt++;
        }
        System.out.println("\n" + dist.elementAt(destination)); // 최단 시간 출력
    }
}

class Station {
    String name, code, line;
    int idx;
    Station() { name = code = line = ""; idx = 0; }
    Station(String name, String code, String line, int idx) {
        this.name = name;
        this.code = code;
        this.line = line;
        this.idx = idx;
    }
    String getName() { return name; }
}

class Edge {
    int u, weight;
    Edge() { u = weight = 0; }
    int getU() { return u; }
    int getWeight() { return weight; }
    Edge(int u, int weight) {
        this.u = u;
        this.weight = weight;
    }
}

class Node implements Comparable<Node>{
    int u;
    long cost;
    Node() { u = 0; cost = 0; }
    Node(int u, long cost) {
        this.u = u;
        this.cost = cost;
    }
    long getCost() { return cost; }
    int getU() { return u; }
    @Override
    public int compareTo(Node A) { // PriorityQueue에서 작은 값이 먼저 나오기 위한 compareTo() 메소드 오버라이딩
        if(cost < A.getCost()) return -1;
        if(cost > A.getCost()) return 1;
        return 0;
    }
}

class Pair {
    int x, y;
    Pair() { x = y = 0; }
    Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }
    int getX() { return x; }
    int getY() { return y; }
}