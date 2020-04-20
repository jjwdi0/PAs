
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB {
    MyLinkedList<MyLinkedList<MovieDBItem>> DB;
    public MovieDB() {
        DB = new MyLinkedList<MyLinkedList<MovieDBItem>>();
    }

    public void insert(MovieDBItem item) {
        Iterator<MyLinkedList<MovieDBItem>> it = DB.iterator();
        int idx = 0, chk = 0;

        while(it.hasNext()) {
        	idx++;
        	MyLinkedList<MovieDBItem> list = it.next();
            MovieDBItem tmp = list.first();
            int cmp = tmp.compareTo(item);
            if(cmp == -2) {
            	continue;
			}
            else if(cmp >= -1 && cmp <= 1) {
            	Iterator<MovieDBItem> it2 = list.iterator();
            	int idx2 = 0, check = 0;
            	while(it2.hasNext()) {
            		idx2++;
            		MovieDBItem curr = it2.next();
            		int cmp2 = curr.compareTo(item);
            		if(cmp2 == 0) {
            			chk = check = 1;
            			break;
					}
            		else if(cmp2 == 1) {
            			chk = check = 1;
            			list.add(idx2 - 1, item);
            			break;
					}
				}
            	if(check == 0) {
            		list.add(item);
            		chk = 1;
				}
            	break;
			}
            else {
				MyLinkedList<MovieDBItem> toAdd = new MyLinkedList<>();
				toAdd.add(item);
				DB.add(idx - 1, toAdd);
				chk = 1;
				break;
			}
        }
        if(chk == 0) {
			MyLinkedList<MovieDBItem> toAdd = new MyLinkedList<>();
			toAdd.add(item);
			DB.add(toAdd);
		}
    }

    public void delete(MovieDBItem item) {
        // FIXME implement this
        // Remove the given item from the MovieDB.

		Iterator<MyLinkedList<MovieDBItem>> it = DB.iterator();

		while(it.hasNext()) {
			MyLinkedList<MovieDBItem> list = it.next();
			MovieDBItem tmp = list.first();
			int cmp = tmp.compareTo(item);
			if(cmp >= -1 && cmp <= 1) {
				Iterator<MovieDBItem> it2 = list.iterator();
				while(it2.hasNext()) {
					MovieDBItem curr = it2.next();
					int cmp2 = curr.compareTo(item);
					if(cmp2 == 0) {
						it2.remove();
						if(list.size() == 0) {
							it.remove();
						}
						break;
					}
				}
			}
		}
    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.
    	
    	// Printing search results is the responsibility of SearchCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> res = new MyLinkedList<>();

		Iterator<MyLinkedList<MovieDBItem>> it = DB.iterator();
		while(it.hasNext()) {
			MyLinkedList<MovieDBItem> list = it.next();
			Iterator<MovieDBItem> it2 = list.iterator();
			while(it2.hasNext()) {
				MovieDBItem tmp = it2.next();
				if(tmp.getTitle().contains(term)) {
					res.add(tmp);
				}
			}
		}
    	
    	return res;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

		MyLinkedList<MovieDBItem> res = new MyLinkedList<>();

		Iterator<MyLinkedList<MovieDBItem>> it = DB.iterator();
		while(it.hasNext()) {
			MyLinkedList<MovieDBItem> list = it.next();
			Iterator<MovieDBItem> it2 = list.iterator();
			while(it2.hasNext()) {
				MovieDBItem tmp = it2.next();
				res.add(tmp);
			}
		}

    	return res;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {
	public Genre(String name) {
		super(name);
		throw new UnsupportedOperationException("not implemented yet");
	}
	
	@Override
	public int compareTo(Genre o) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList implements ListInterface<String> {	
	public MovieList() {
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String first() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}