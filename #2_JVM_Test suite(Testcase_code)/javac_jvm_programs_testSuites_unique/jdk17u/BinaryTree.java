import java.io.*;

public class BinaryTree {

    public static void main(String[] argv) {
        System.err.println("\nRegression test for testing of " + "serialization/deserialization of objects " + "with BinaryTree types \n");
        try {
            BinaryTreeTest base = new BinaryTreeTest(2);
            FileOutputStream ostream = new FileOutputStream("piotest3.tmp");
            try {
                ObjectOutputStream p = new ObjectOutputStream(ostream);
                p.writeObject(null);
                p.writeObject(base);
                p.flush();
            } finally {
                ostream.close();
            }
            FileInputStream istream = new FileInputStream("piotest3.tmp");
            try {
                ObjectInputStream q = new ObjectInputStream(istream);
                Object n = q.readObject();
                if (n != null) {
                    System.err.println("\nnull read as " + n);
                }
                BinaryTreeTest nbase = (BinaryTreeTest) q.readObject();
                if (!base.equals(nbase)) {
                    System.err.println("\nTEST FAILED: BinaryTree read " + "incorrectly.");
                    throw new Error();
                }
            } finally {
                istream.close();
            }
            System.err.println("\nTEST PASSED");
        } catch (Exception e) {
            System.err.print("TEST FAILED: ");
            e.printStackTrace();
            throw new Error();
        }
    }
}

class BinaryTreeTest implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public BinaryTreeTest left;

    public BinaryTreeTest right;

    public int id;

    public int level;

    private static int count = 0;

    public BinaryTreeTest(int l) {
        id = count++;
        level = l;
        if (l > 0) {
            left = new BinaryTreeTest(l - 1);
            right = new BinaryTreeTest(l - 1);
        }
    }

    public void print(int levels) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.err.println("node " + id);
        if (level <= levels && left != null) {
            left.print(levels);
        }
        if (level <= levels && right != null) {
            right.print(levels);
        }
    }

    public boolean equals(BinaryTreeTest other) {
        if (other == null) {
            return false;
        }
        if (id != other.id) {
            return false;
        }
        if (left != null && !left.equals(other.left)) {
            return false;
        }
        if (right != null && !right.equals(other.right)) {
            return false;
        }
        return true;
    }
}
