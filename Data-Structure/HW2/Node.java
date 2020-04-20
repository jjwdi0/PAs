

public class Node<T> {
    private T item;
    private Node<T> next;

    public Node(T obj) {
        this.item = obj;
        this.next = null;
    }
    
    public Node(T obj, Node<T> next) {
    	this.item = obj;
    	this.next = next;
    }
    
    public final T getItem() {
    	return item;
    }
    
    public final void setItem(T item) {
    	this.item = item;
    }
    
    public final void setNext(Node<T> next) {
    	this.next = next;
    }
    
    public Node<T> getNext() {
    	return this.next;
    }

    // 현재 노드 오른쪽에 obj 삽입
    public final void insertNext(T obj) {
        Node<T> tmp = next;
        Node<T> tmp2 = new Node<T>(obj);
        tmp2.setNext(tmp);
        next = tmp2;
    }
    
    public final void removeNext() {
        if(next.equals(null)) {
            return;
        }
        Node<T> tmp = next.next;
        next = tmp;
    }
}