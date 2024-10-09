

import  javax.sound.midi.MetaMessage;


public class MetaMessageClone {

    private static void printMsg(MetaMessage msg, byte[] data) {
        System.out.println(""+msg.getLength()+" total bytes, type="+msg.getType()+", dataLength="+data.length);
    }

    private static void checkClone(MetaMessage msg) throws Exception {
        System.out.print("Original: ");
        byte[] msgData=msg.getData();
        printMsg(msg, msgData);
        MetaMessage msg2=(MetaMessage) msg.clone();
        byte[] msg2Data=msg2.getData();
        System.out.print("Clone:    ");
        printMsg(msg2, msg2Data);

        if (msg2.getLength()!=msg.getLength()
            || msg.getType()!=msg2.getType()
            || msgData.length!=msg2Data.length) {
                throw new Exception("cloned MetaMessage is not equal.");
        }
        int max=Math.min(msgData.length, 10);
        for (int i=0; i<max; i++) {
            if (msgData[i]!=msg2Data[i]) {
                throw new Exception("Cloned MetaMessage data is not equal.");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        
        MetaMessage msg=new MetaMessage();
        String text="a textmarker";
        msg.setMessage(1, text.getBytes(), text.length());
        checkClone(msg);
        msg.setMessage(0x2E, new byte[0], 0);
        checkClone(msg);
        byte[] data=new byte[17000];
        for (int i=0; i<30; data[i]=(byte) (i++ & 0xFF));
        msg.setMessage(0x02, data, 80); checkClone(msg);
        msg.setMessage(0x02, data, 160); checkClone(msg);
        msg.setMessage(0x02, data, 400); checkClone(msg);
        msg.setMessage(0x02, data, 1000); checkClone(msg);
        msg.setMessage(0x02, data, 10000); checkClone(msg);
        msg.setMessage(0x02, data, 17000); checkClone(msg);
    }
}
