JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
        $(JC) $(JFLAGS) $*.java

CLASSES = \
        Chunk.java \
        ChunkDB.java \
        Client.java \
        Message.java \
        PeerConnection.java \
        PeerInfo.java \
        PeerRmi.java \
        PeerServer.java \
        

default: classes

classes: $(CLASSES:.java=.class)

clean:
        $(RM) *.class
