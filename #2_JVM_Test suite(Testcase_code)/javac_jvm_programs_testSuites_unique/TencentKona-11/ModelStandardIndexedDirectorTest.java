



import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.sun.media.sound.ModelConnectionBlock;
import com.sun.media.sound.ModelDirectedPlayer;
import com.sun.media.sound.ModelPerformer;
import com.sun.media.sound.ModelStandardDirector;
import com.sun.media.sound.ModelStandardIndexedDirector;

public class ModelStandardIndexedDirectorTest {

    private static String treeToString(TreeSet<Integer> set)
    {
        StringBuffer buff = new StringBuffer();
        boolean first = true;
        for(Integer s : set)
        {
            if(!first)
                buff.append(";");
            buff.append(s);
            first = false;
        }
        return buff.toString();
    }

    private static void testDirector(ModelPerformer[] performers) throws Exception
    {
        final TreeSet<Integer> played = new TreeSet<Integer>();
        ModelDirectedPlayer player = new ModelDirectedPlayer()
        {
            public void play(int performerIndex,
                    ModelConnectionBlock[] connectionBlocks) {
                played.add(performerIndex);
            }
        };
        ModelStandardIndexedDirector idirector =
            new ModelStandardIndexedDirector(performers, player);
        ModelStandardDirector director =
            new ModelStandardDirector(performers, player);

        for (int n = 0; n < 128; n++)
        {
            for (int v = 0; v < 128; v++)
            {
                director.noteOn(n, v);
                String p1 = treeToString(played);
                played.clear();
                idirector.noteOn(n, v);
                String p2 = treeToString(played);
                played.clear();
                if(!p1.equals(p2))
                    throw new Exception(
                            "Note = " + n + ", Vel = " + v + " failed");
            }
        }
    }

    private static void testDirectorCombinations(
            ModelPerformer[] performers) throws Exception
    {
        for (int i = 0; i < performers.length; i++) {
            ModelPerformer[] performers2 = new ModelPerformer[i];
            for (int j = 0; j < performers2.length; j++) {
                performers2[j] = performers[i];
            }
            testDirector(performers2);
        }
    }

    private static void addPerformer(
            List<ModelPerformer> performers,
            int keyfrom,
            int keyto,
            int velfrom,
            int velto)
    {
        ModelPerformer performer = new ModelPerformer();
        performer.setKeyFrom(keyfrom);
        performer.setKeyTo(keyto);
        performer.setVelFrom(velfrom);
        performer.setVelTo(velto);
        performers.add(performer);
    }

    public static void main(String[] args) throws Exception
    {
        
        List<ModelPerformer> performers = new ArrayList<ModelPerformer>();
        addPerformer(performers, 0, 0, 0, 127);
        addPerformer(performers, 0, 50, 0, 127);
        addPerformer(performers, 0, 127, 0, 127);
        addPerformer(performers, 21, 21, 0, 127);
        addPerformer(performers, 21, 60, 0, 127);
        addPerformer(performers, 21, 127, 0, 127);
        addPerformer(performers, 50, 50, 0, 127);
        addPerformer(performers, 50, 60, 0, 127);
        addPerformer(performers, 50, 127, 0, 127);
        addPerformer(performers, 73, 73, 0, 127);
        addPerformer(performers, 73, 80, 0, 127);
        addPerformer(performers, 73, 127, 0, 127);
        addPerformer(performers, 127, 127, 0, 127);
        addPerformer(performers, 0, 0, 60, 127);
        addPerformer(performers, 0, 50, 60, 127);
        addPerformer(performers, 0, 127, 60, 127);
        addPerformer(performers, 21, 21, 60, 127);
        addPerformer(performers, 21, 60, 60, 127);
        addPerformer(performers, 21, 127, 60, 127);
        addPerformer(performers, 50, 50, 60, 127);
        addPerformer(performers, 50, 60, 60, 127);
        addPerformer(performers, 50, 127, 60, 127);
        addPerformer(performers, 73, 73, 60, 127);
        addPerformer(performers, 73, 80, 60, 127);
        addPerformer(performers, 73, 127, 60, 127);
        addPerformer(performers, 127, 127, 60, 127);
        addPerformer(performers, 0, 0, 80, 83);
        addPerformer(performers, 0, 50, 80, 83);
        addPerformer(performers, 0, 127, 80, 83);
        addPerformer(performers, 21, 21, 80, 83);
        addPerformer(performers, 21, 60, 80, 83);
        addPerformer(performers, 21, 127, 80, 83);
        addPerformer(performers, 50, 50, 80, 83);
        addPerformer(performers, 50, 60, 80, 83);
        addPerformer(performers, 50, 127, 80, 83);
        addPerformer(performers, 73, 73, 80, 83);
        addPerformer(performers, 73, 80, 80, 83);
        addPerformer(performers, 73, 127, 80, 83);
        addPerformer(performers, 127, 127, 80, 83);


        testDirectorCombinations(
                performers.toArray(
                        new ModelPerformer[performers.size()])
                );

        
        performers.clear();
        addPerformer(performers, 50, 30, 80, 83);
        addPerformer(performers, 30, 30, 50, 30);
        addPerformer(performers, 37, 30, 50, 30);
        testDirector(
                performers.toArray(
                        new ModelPerformer[performers.size()])
                );

        
        performers.clear();
        addPerformer(performers, -20, 6, 0, 127);
        addPerformer(performers, 0, 300, 0, 300);
        addPerformer(performers, -2, -8, -5, -9);

        testDirector(
                performers.toArray(
                        new ModelPerformer[performers.size()])
                );

    }
}
