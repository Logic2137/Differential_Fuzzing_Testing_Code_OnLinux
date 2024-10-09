
package code;

interface HasColor {

    String getColor();
}

class Dimension {

    int x, y, z;
}

class ColoredDimension<T extends Dimension & HasColor> {

    T item;

    ColoredDimension(T item) {
        this.item = item;
    }

    T getItem() {
        return item;
    }

    String f() {
        return item.getColor();
    }

    int getX() {
        return item.x;
    }
}
