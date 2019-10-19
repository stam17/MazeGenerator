import java.util.ArrayList;

public class UnionFind {

    public ArrayList<LLAddOnly> headers = new ArrayList<LLAddOnly>();


    public LLAddOnly makeSet(Cell c) {
        LLAddOnly LL = new LLAddOnly();
        LL.add(c);
        return LL;
    }

    public LLAddOnly find(Cell c) {
        return c.head;
    }

    public void union(Cell c1, Cell c2) {
        LLAddOnly head1 = find(c1);
        LLAddOnly head2 = find(c2);
        if (head1 != head2) {
            // connect two sets
            head1.last.next = head2.first;
            head1.last = head2.last;

            // update headers
            Cell c = head2.first;
            while (c != null) {
                c.head = head1;
                c = c.next;
            }

            headers.remove(head2);
        }
    }
}
