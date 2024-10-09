

package nsk.share.gc;

public final class IndexPair {
        private int i, j;

        public IndexPair(int i, int j) {
                setI(i);
                setJ(j);
        }

        public int getI() {
                return i;
        }

        public void setI(int i) {
                this.i = i;
        }

        public int getJ() {
                return j;
        }

        public void setJ(int j) {
                this.j = j;
        }

        public boolean equals(IndexPair pair) {
                return (this.i == pair.i && this.j == pair.j);
        }

        public boolean equals(Object o) {
                return o instanceof IndexPair && equals((IndexPair) o);
        }

        public int hashCode() {
                return i << 16 + j;
        }
}
