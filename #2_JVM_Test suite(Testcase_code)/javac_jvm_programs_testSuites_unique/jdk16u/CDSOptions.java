
package jdk.test.lib.cds;

import java.util.ArrayList;



public class CDSOptions {
    public String xShareMode = "on";
    public String archiveName;
    public ArrayList<String> prefix = new ArrayList<String>();
    public ArrayList<String> suffix = new ArrayList<String>();
    public boolean useSystemArchive = false;

    
    public String[] classList;

    
    
    public boolean useVersion = true;


    public CDSOptions() {
    }


    public CDSOptions addPrefix(String... prefix) {
        for (String s : prefix) this.prefix.add(s);
        return this;
    }


    public CDSOptions addSuffix(String... suffix) {
        for (String s : suffix) this.suffix.add(s);
        return this;
    }

    public CDSOptions setXShareMode(String mode) {
        this.xShareMode = mode;
        return this;
    }


    public CDSOptions setArchiveName(String name) {
        this.archiveName = name;
        return this;
    }


    public CDSOptions setUseVersion(boolean use) {
        this.useVersion = use;
        return this;
    }

    public CDSOptions setUseSystemArchive(boolean use) {
        this.useSystemArchive = use;
        return this;
    }

    public CDSOptions setClassList(String[] list) {
        this.classList = list;
        return this;
    }
    public CDSOptions setClassList(ArrayList<String> list) {
        String array[] = new String[list.size()];
        list.toArray(array);
        this.classList = array;
        return this;
    }

}
