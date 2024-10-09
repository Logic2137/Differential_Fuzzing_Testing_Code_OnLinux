



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManySourcesAndTargets {
    public static void main(String[] args) throws Exception {

        
        InetAddress lh = InetAddress.getLocalHost();
        InetAddress lb = InetAddress.getLoopbackAddress();
        List<InetAddress> addresses = Stream.of(lh, lb)
                .map(ManySourcesAndTargets::networkInterface)
                .flatMap(Optional::stream)
                .flatMap(NetworkInterface::inetAddresses)
                .filter(ia -> !ia.isAnyLocalAddress())
                .distinct()
                .collect(Collectors.toList());

        
        try (DatagramChannel reader = DatagramChannel.open()) {
            
            reader.bind(new InetSocketAddress(0));
            for (InetAddress address : addresses) {
                System.out.format("%n-- %s --%n", address.getHostAddress());

                
                testSend(3, address, reader);
            }
        }

        
        try (DatagramChannel sender = DatagramChannel.open()) {
            
            sender.bind(new InetSocketAddress(0));
            for (InetAddress address : addresses) {
                System.out.format("%n-- %s --%n", address.getHostAddress());

                
                testReceive(3, sender, address);
            }
        }
    }

    
    static void testSend(int count, InetAddress address, DatagramChannel reader) throws Exception {
        int remotePort = reader.socket().getLocalPort();
        InetSocketAddress remote = new InetSocketAddress(address, remotePort);

        try (DatagramChannel sender = DatagramChannel.open()) {
            sender.bind(new InetSocketAddress(address, 0));

            SocketAddress local = sender.getLocalAddress();
            byte[] bytes = serialize(local);

            SocketAddress previousSource = null;
            for (int i = 0; i < count; i++) {
                System.out.format("send %s -> %s%n", local, remote);
                sender.send(ByteBuffer.wrap(bytes), remote);

                ByteBuffer bb = ByteBuffer.allocate(1000);
                SocketAddress source = reader.receive(bb);
                System.out.format("received datagram from %s%n", source);

                
                SocketAddress payload = deserialize(bb.array());
                if (!source.equals(local))
                    throw new RuntimeException("source=" + source + ", expected=" + local);
                if (!payload.equals(local))
                    throw new RuntimeException("payload=" + payload + ", expected=" + local);

                
                if (previousSource == null) {
                    previousSource = source;
                } else if (source != previousSource) {
                    throw new RuntimeException("Cached SocketAddress not returned");
                }
            }
        }
    }

    
    static void testReceive(int count, DatagramChannel sender, InetAddress address) throws Exception {
        SocketAddress local = sender.getLocalAddress();

        try (DatagramChannel reader = DatagramChannel.open()) {
            
            reader.bind(new InetSocketAddress(address, 0));

            SocketAddress remote = reader.getLocalAddress();

            for (int i = 0; i < count; i++) {
                System.out.format("send %s -> %s%n", local, remote);
                sender.send(ByteBuffer.allocate(32), remote);

                ByteBuffer bb = ByteBuffer.allocate(1000);
                SocketAddress source = reader.receive(bb);
                System.out.format("received datagram from %s%n", source);
            }
        }
    }

    private static byte[] serialize(SocketAddress address) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(address);
        oos.close();
        return baos.toByteArray();
    }

    private static SocketAddress deserialize(byte[] bytes) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        return (SocketAddress) ois.readObject();
    }

    private static Optional<NetworkInterface> networkInterface(InetAddress ia) {
        try {
            return Optional.ofNullable(NetworkInterface.getByInetAddress(ia));
        } catch (SocketException e) {
            return Optional.empty();
        }
    }
}
