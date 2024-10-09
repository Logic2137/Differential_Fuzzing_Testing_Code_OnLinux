
package gc.gctests.MTLinkedListGC;

class node {

    int[] sink;

    String name;

    node prev;

    node next;

    public node(String threadName) {
        name = threadName;
        sink = new int[1000];
    }
}
