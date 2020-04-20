import java.io.*;
import java.util.*;

public class Matching {

	static hashTable table = new hashTable(); // 다른 클래스, 메소드에서의 hashTable 접근을 위한 static 선언

	public static void main(String args[]) {
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
		if(input.charAt(0) == '<') { // 파일 경로 들어올 경우
			try {
				table = new hashTable();
				// 파일 입출력
				File file = new File(input.substring(2));
				FileReader filereader = new FileReader(file);
				BufferedReader bufReader = new BufferedReader(filereader);

				String line = "";
				int idx = 0;
				while((line = bufReader.readLine()) != null) {
					idx++;
					// 모든 길이 6의 substring을 table에 저장
					for(int i=0; i+6<=line.length(); i++) {
						String t = line.substring(i, i + 6);
						int hash = 0;
						for(int j=0; j<t.length(); j++) hash += (int)t.charAt(j);
						hash %= 100;
						pair tmp = new pair(idx, i + 1);
						table.add(t, hash, tmp);
					}
				}
			}
			catch(FileNotFoundException e) {} catch(IOException e) {}

			return;
		}
		else if(input.charAt(0) == '@') { // 주어진 해시값에 맞는 string 출력
			int num = Integer.parseInt(input.substring(2));
			table.printAll(num);
		}
		else if(input.charAt(0) == '?') { // 주어진 pattern이 나타나는 위치 모두 출력
			int cnt = 0;
			String pattern = input.substring(2);
			Vector<LinkedList<pair>> v = new Vector<>();
			for(int i=0; i+6<=pattern.length(); i++) v.add(table.find(pattern.substring(i, i + 6))); // pattern 속 모든 길이 6인 substring들이 나타나는 위치를 LinkedList에 저장
			Iterator<pair> it = v.elementAt(0).iterator(); // 맨 첫 substring이 나타나는 위치에 대해서
			while(it.hasNext()) {
				pair cur = it.next();
				int flag = 0;
				for(int i=1; i<v.size(); i++) { // 지금 보고 있는 substring 뒤에 있는 substring인지 확인
					Iterator<pair> it2 = v.elementAt(i).iterator();
					int find = 0;
					while(it2.hasNext()) {
						pair curr = it2.next();
						if(curr.getX() == cur.getX() && curr.getY() == cur.getY() + i) { find = 1; break; } // 지금 찾고 있는 substring 뒤에 붙을 수 있으면 바로 종료
					}
					if(find == 0) { flag = 1; break; } // 만약 뒤에 붙을 수 있는 위치가 없다면 바로 종료
				}
				if(flag == 0) { // flag == 0 <-> 주어진 pattern이 존재
					if(cnt == 0) System.out.print("(" + cur.getX() + ", " + cur.getY() + ")");
					else System.out.print(" (" + cur.getX() + ", " + cur.getY() + ")");
					cnt++;
				}
			}
			if(cnt == 0) System.out.print("(0, 0)");
			System.out.println();
		}
	}
}

class AVL_Tree {
	Node root; // 루트 노드를 가리키는 변수
	int cnt; // 현재 AVL Tree에 저장된 순서쌍 개수
	AVL_Tree() { root = new Node(); cnt = 0; }
	Boolean empty() { return cnt == 0; }
	void add(String string, pair X) {
		cnt++;
		Node here = root;
		while(here != null) {
			String curr = here.getString();
			if(curr.compareTo(string) < 0) { // 삽입하고자 하는 문자열이 현재 문자열보다 사전순으로 뒤에 있을 때
				if(here.getRight() != null) here = here.getRight(); // 오른쪽 자식 노드가 비어있지 않다면 오른쪽 자식 노드로 이동
				else { // 그렇지 않으면 오른쪽에 새로운 노드 저장 후 level 맞춰줌
					Node toAdd = new Node(string, X);
					here.setRight(toAdd);
					toAdd.setParent(here);
					addLevel(here);
					break;
				}
			}
			else if(string.compareTo(curr) < 0) { // 위와 반대 경우
				if(here.getLeft() != null) here = here.getLeft();
				else {
					Node toAdd = new Node(string, X);
					here.setLeft(toAdd);
					toAdd.setParent(here);
					addLevel(here);
					break;
				}
			}
			else { here.add(X); break; } // 현재 위치의 string과 삽입하고자 하는 string이 동일하면 현재의 LinkedList에 순서쌍 삽입
		}
	}
	void addLevel(Node node) { // 현재 노드의 level 계산
		if(node == root) return;
		int left = node.getLeftLevel(), right = node.getRightLevel();
		node.setLevel(Integer.max(left, right) + 1); // 왼쪽 자식과 오른쪽 자식의 level 중 큰 값 + 1로 저장해줌
		if(left >= right + 2 || left + 2 <= right) { // 왼쪽과 오른쪽 높이 차이가 2 이상 발생하면 회전시켜줌
			Node x, y, z;
			Vector<Node> v = new Vector<>();
			x = node; // x는 가장 위에 있는 노드
			y = node.getMaxChild(); // y는 x의 자식 중 level이 큰 노드
			z = node.getMaxChild().getMaxChild(); // z는 y의 자식 중 level이 큰 노드
			// x, y, z를 사전순 배열
			if(x.getString().compareTo(y.getString()) > 0) { Node tmp = x; x = y; y = tmp; }
			if(x.getString().compareTo(z.getString()) > 0) { Node tmp = x; x = z; z = tmp; }
			if(y.getString().compareTo(z.getString()) > 0) { Node tmp = y; y = z; z = tmp; }
			// x, y, z의 자식 중 서로가 아닌 것들을 저장
			if(x.getLeft() == null || (!x.getLeft().getString().equals(y.getString()) && !x.getLeft().getString().equals(z.getString()))) v.add(x.getLeft());
			if(x.getRight() == null || (!x.getRight().getString().equals(y.getString()) && !x.getRight().getString().equals(z.getString()))) v.add(x.getRight());
			if(y.getLeft() == null || (!y.getLeft().getString().equals(x.getString()) && !y.getLeft().getString().equals(z.getString()))) v.add(y.getLeft());
			if(y.getRight() == null || (!y.getRight().getString().equals(x.getString()) && !y.getRight().getString().equals(z.getString()))) v.add(y.getRight());
			if(z.getLeft() == null || (!z.getLeft().getString().equals(x.getString()) && !z.getLeft().getString().equals(y.getString()))) v.add(z.getLeft());
			if(z.getRight() == null || (!z.getRight().getString().equals(x.getString()) && !z.getRight().getString().equals(y.getString()))) v.add(z.getRight());
			Node superNode; // 서브트리 회전 후 서브트리의 부모 노드가 될 노드 지정
			if(x.getLevel() > z.getLevel()) superNode = x.getParent();
			else superNode = z.getParent();
			if(superNode.getString().compareTo(x.getString()) < 0) superNode.setRight(y);
			else superNode.setLeft(y);
			x.setParent(y); y.setLeft(x); // y 왼쪽에 x 연결
			z.setParent(y); y.setRight(z); // y 오른쪽에 z 연결
			y.setParent(superNode); // y가 서브트리의 루트 노드이니 y의 부모를 superNode로 설정
			// x, y, z의 자식 노드였던 것들을 사전순으로 x와 z에 연결
			if(v.elementAt(0) != null) { v.elementAt(0).setParent(x); x.setLeft(v.elementAt(0)); }
			else x.setLeft(null);
			if(v.elementAt(1) != null) { v.elementAt(1).setParent(x); x.setRight(v.elementAt(1)); }
			else x.setRight(null);
			if(v.elementAt(2) != null) { v.elementAt(2).setParent(z); z.setLeft(v.elementAt(2)); }
			else z.setLeft(null);
			if(v.elementAt(3) != null) { v.elementAt(3).setParent(z); z.setRight(v.elementAt(3)); }
			else z.setRight(null);
			// x, y, z 각 노드의 level 설정
			x.setLevel(Integer.max(x.getLeftLevel(), x.getRightLevel()) + 1);
			z.setLevel(Integer.max(z.getLeftLevel(), z.getRightLevel()) + 1);
			y.setLevel(Integer.max(y.getLeftLevel(), y.getRightLevel()) + 1);
		}
		addLevel(node.getParent());
	}
	void preorderTraversal() { // 전위순회
		if(root.hasLeft()) preorderTraversal(root.getLeft());
		else preorderTraversal(root.getRight());
	}
	void preorderTraversal(Node now) {
		if(now.getParent().equals(root)) System.out.print(now.getString());
		else System.out.print(" " + now.getString()); // 현재 string 출력
		if(now.hasLeft()) preorderTraversal(now.getLeft()); // 왼쪽 자식으로 전위순회
		if(now.hasRight()) preorderTraversal(now.getRight()); // 오른쪽 자식으로 전위순회
	}
	LinkedList<pair> find(String string) {
		if(root.hasLeft()) return find(string, root.getLeft());
		else return find(string, root.getRight());
	}
	LinkedList<pair> find(String string, Node now) { // 주어진 string을 찾는 메소드
		if(now == null) return new LinkedList<>();
		if(now.getString().equals(string)) return now.getLinkedList();
		else if(now.getString().compareTo(string) < 0) return find(string, now.getRight());
		else return find(string, now.getLeft());
	}
}

class Node {
	Node left, right, parent;
	LinkedList<pair> X;
	String string;
	int leftLevel, rightLevel, level; // 왼쪽, 오른쪽 자식, 현재 노드의 level
	Node() {
		left = right = parent = null;
		leftLevel = rightLevel = 0;
		level = 1;
		X = new LinkedList<>();
		string = "";
	}
	Node(String string, pair x) {
		left = right = parent = null;
		leftLevel = rightLevel = 0;
		level = 1;
		X = new LinkedList<>();
		X.add(x);
		this.string = string;
	}
	String getString() { return string; }
	LinkedList<pair> getLinkedList() { return X; }
	Node getRight() { return right; }
	Node getLeft() { return left; }
	Node getParent() { return parent; }
	Node getMaxChild() {
		if(getLeft() == null && getRight() == null) return null;
		if(getLeft() == null) return getRight();
		if(getRight() == null) return getLeft();
		if(getLeftLevel() > getRightLevel()) return getLeft();
		return getRight();
	}
	int getLeftLevel() { return leftLevel = (getLeft() == null ? 0 : getLeft().getLevel()); }
	int getRightLevel() { return rightLevel = (getRight() == null ? 0 : getRight().getLevel()); }
	int getLevel() { return level; }
	Boolean hasLeft() { return !(getLeft() == null); }
	Boolean hasRight() { return !(getRight() == null); }
	void setRight(Node right) { this.right = right; }
	void setLeft(Node left) { this.left = left; }
	void setParent(Node parent) { this.parent = parent; }
	void setLevel(int level) { this.level = level; }
	void add(pair x) { X.add(x); }
}

class pair {
	int x, y;
	pair() { this.x = this.y = 0; }
	pair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	int getX() { return x; }
	int getY() { return y; }
}

class hashTable { // hash값에 해당하는 string 삽입
	Vector<AVL_Tree> v;
	hashTable() {
		v = new Vector<>(100);
		for(int i=0; i<100; i++) v.add(new AVL_Tree());
	}
	void add(String string, int hash, pair X) {
		if(v.elementAt(hash) == null) {
			AVL_Tree tmp = new AVL_Tree();
			v.set(hash, tmp);
		}
		v.elementAt(hash).add(string, X);
	}
	void printAll(int index) {
		if(v.elementAt(index).empty()) { System.out.println("EMPTY"); return; }
		v.elementAt(index).preorderTraversal();
		System.out.println();
	}
	LinkedList<pair> find(String string) {
		int hash = 0;
		for(int i=0; i<6; i++) hash += (int)string.charAt(i);
		hash %= 100;
		return v.elementAt(hash).find(string);
	}
}