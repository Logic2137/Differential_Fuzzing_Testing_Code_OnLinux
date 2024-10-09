
package com.sun.swingset3.demos.spinner;


import java.awt.*;

public class Palette {

    private final int minColor;
    private final int colorRange;
    private Color[] colors;
    private int[] rgbColors;
    private final int rSteps;
    private final int gSteps;
    private final int bSteps;
    private int totalRange;
    private int rRange;
    private int gRange;
    private int bRange;
    private final double rStart;
    private final double gStart;
    private final double bStart;

    public Palette(int totalRange, int minColor, int maxColor, double rStart,
            double gStart, double bStart, int rSteps, int gSteps, int bSteps) {
        this.minColor = minColor;
        this.colorRange = maxColor - minColor;
        this.rStart = rStart;
        this.gStart = gStart;
        this.bStart = bStart;
        this.rSteps = rSteps;
        this.gSteps = gSteps;
        this.bSteps = bSteps;
        setSize(totalRange);
    }

    public void setSize(int newSize) {
        totalRange = newSize;
        rRange = totalRange / rSteps;
        gRange = totalRange / gSteps;
        bRange = totalRange / bSteps;
        fillColorTable();
    }

    private void fillColorTable() {
        colors = new Color[totalRange];
        rgbColors = new int[totalRange];
        for (int i = 0; i < totalRange; i++) {
            double cosR = Math.cos(i * 2 * Math.PI / rRange + rStart);
            double cosG = Math.cos(i * 2 * Math.PI / gRange + gStart);
            double cosB = Math.cos(i * 2 * Math.PI / bRange + bStart);
            Color color = new Color(
                    (int) ((cosR * colorRange) + colorRange) / 2 + minColor,
                    (int) ((cosG * colorRange) + colorRange) / 2 + minColor,
                    (int) ((cosB * colorRange) + colorRange) / 2 + minColor);
            colors[i] = color;
            rgbColors[i] = color.getRGB();
        }
    }

    public Color getColor(int index) {
        return colors[index];
    }

    public int getRgbColor(int index) {
        return rgbColors[index];
    }

    public int getSize() {
        return totalRange;
    }
}
