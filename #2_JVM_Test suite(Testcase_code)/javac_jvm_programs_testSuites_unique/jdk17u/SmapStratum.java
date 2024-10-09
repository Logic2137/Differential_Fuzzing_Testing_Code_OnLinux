
package nsk.share.jdi.sde;

import java.util.ArrayList;
import java.util.List;

public class SmapStratum {

    public static class LineInfo {

        private int inputStartLine = -1;

        private int outputStartLine = -1;

        private int lineFileID = 0;

        private int inputLineCount = 1;

        private int outputLineIncrement = 1;

        private boolean lineFileIDSet = false;

        public void setInputStartLine(int inputStartLine) {
            if (inputStartLine < 0)
                throw new IllegalArgumentException("" + inputStartLine);
            this.inputStartLine = inputStartLine;
        }

        public void setOutputStartLine(int outputStartLine) {
            if (outputStartLine < 0)
                throw new IllegalArgumentException("" + outputStartLine);
            this.outputStartLine = outputStartLine;
        }

        public void setLineFileID(int lineFileID) {
            if (lineFileID < 0)
                throw new IllegalArgumentException("" + lineFileID);
            this.lineFileID = lineFileID;
            this.lineFileIDSet = true;
        }

        public void setInputLineCount(int inputLineCount) {
            if (inputLineCount < 0)
                throw new IllegalArgumentException("" + inputLineCount);
            this.inputLineCount = inputLineCount;
        }

        public void setOutputLineIncrement(int outputLineIncrement) {
            if (outputLineIncrement < 0)
                throw new IllegalArgumentException("" + outputLineIncrement);
            this.outputLineIncrement = outputLineIncrement;
        }

        public String getString() {
            if (inputStartLine == -1 || outputStartLine == -1)
                throw new IllegalStateException();
            StringBuffer out = new StringBuffer();
            out.append(inputStartLine);
            if (lineFileIDSet)
                out.append("#" + lineFileID);
            if (inputLineCount != 1)
                out.append("," + inputLineCount);
            out.append(":" + outputStartLine);
            if (outputLineIncrement != 1)
                out.append("," + outputLineIncrement);
            out.append('\n');
            return out.toString();
        }

        public String toString() {
            return getString();
        }
    }

    private String stratumName;

    private List<String> fileNameList;

    private List<String> filePathList;

    private List<LineInfo> lineData;

    private int lastFileID;

    public SmapStratum(String stratumName) {
        this.stratumName = stratumName;
        fileNameList = new ArrayList<String>();
        filePathList = new ArrayList<String>();
        lineData = new ArrayList<LineInfo>();
        lastFileID = 0;
    }

    public void addFile(String filename) {
        addFile(filename, filename);
    }

    public void addFile(String filename, String filePath) {
        int pathIndex = filePathList.indexOf(filePath);
        if (pathIndex == -1) {
            fileNameList.add(filename);
            filePathList.add(filePath);
        }
    }

    public void addLineData(int inputStartLine, String inputFileName, int inputLineCount, int outputStartLine, int outputLineIncrement) {
        int fileIndex = fileNameList.indexOf(inputFileName);
        if (fileIndex == -1)
            throw new IllegalArgumentException("inputFileName: " + inputFileName);
        if (outputStartLine == 0)
            return;
        LineInfo li = new LineInfo();
        li.setInputStartLine(inputStartLine);
        li.setInputLineCount(inputLineCount);
        li.setOutputStartLine(outputStartLine);
        li.setOutputLineIncrement(outputLineIncrement);
        if (fileIndex != lastFileID)
            li.setLineFileID(fileIndex);
        lastFileID = fileIndex;
        lineData.add(li);
    }

    public String getStratumName() {
        return stratumName;
    }

    public String getString() {
        if (fileNameList.size() == 0 || lineData.size() == 0)
            return null;
        StringBuffer out = new StringBuffer();
        out.append("*S " + stratumName + "\n");
        out.append("*F\n");
        int bound = fileNameList.size();
        for (int i = 0; i < bound; i++) {
            if (filePathList.get(i) != null) {
                out.append("+ " + i + " " + fileNameList.get(i) + "\n");
                String filePath = (String) filePathList.get(i);
                if (filePath.startsWith("/")) {
                    filePath = filePath.substring(1);
                }
                out.append(filePath + "\n");
            } else {
                out.append(i + " " + fileNameList.get(i) + "\n");
            }
        }
        out.append("*L\n");
        bound = lineData.size();
        for (int i = 0; i < bound; i++) {
            LineInfo li = (LineInfo) lineData.get(i);
            out.append(li.getString());
        }
        return out.toString();
    }

    public String toString() {
        return getString();
    }
}
