




class a {
    int g = 20;
    float h = 2;
    long b = 6;
}

public class TestLoadOnBackedgeWithPrec {
    int c ;
    a[] i = {new a()};
    float j() {
        a k = new a();
        float l = 5;
        for (int d = 0; d < 8; ++d) {
            for (int e = 0; e < 9; ++e) {
                k = k;
                l *= k.g;
            }
            for (int f = 0; f < 9; ++f) {
                new a();
            }
            {
                a[] m = {
                    new a(), new a(), new a(),
                    new a(), new a(), new a(),
                    new a(), new a(), new a()};
                c = i[0].g + k.g;
            }
        }
        return k.h;
    }
    public static void main(String[] args) {
        TestLoadOnBackedgeWithPrec n = new TestLoadOnBackedgeWithPrec();
        for (int i = 0; i < 5_000; i++) {
            n.j();
        }
    }
}

