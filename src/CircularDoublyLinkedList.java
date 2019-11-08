
public class CircularDoublyLinkedList<T> {
	private Node<T> first, last;
	private int size;

	public CircularDoublyLinkedList() {
		size = 0;
		first = null;
		last = null;
	}

	// adds node after most recent add
	public Node<T> add(T data) {
		Node<T> newNode = new Node<T>(last, data, first);

		if (!isEmpty()) {
			last.setNext(newNode);
		}

		if (isEmpty()) {
			first = newNode;
		}

		last = newNode;

		first.setPrev(newNode);
		size++;

		return newNode;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}

	public Node<T> getFirst() {
		return first;
	}

	public Node<T> getLast() {
		return last;
	}
}
