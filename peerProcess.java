import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// TODO: create custom exceptions
// TODO: modify readme.md
// TODO: can add setup enviornment function



class CommonConfigData {
	
  int preferredNumberOfNeighbours;
  int intervalOfUnchoking;
  int intervalOfOptimisticUnchoking;
  String myFileName;
  int fileSize;
  int chunkSize;

  public CommonConfigData() {
    this.preferredNumberOfNeighbours = 0;
    this.intervalOfUnchoking = 0;
    this.intervalOfOptimisticUnchoking = 0;

    this.fileSize = 0;
    this.chunkSize = 0;
  }

  public int getUnchokingInterval() {
    return intervalOfUnchoking;
  }

  public void parseAndUpdateData(ArrayList<String> rawDataRows) {
    if (rawDataRows.size() < 6) {
      // throw error, create custom exception
      System.out.println("Less number of rows");
      return;

    }
    updatePreferredNumberOfNeighbours(rawDataRows.get(0));
    updateIntervalOfUnchoking(rawDataRows.get(1));
    updateIntervalOfOptimisticUnchoking(rawDataRows.get(2));
    updateMyFileName(rawDataRows.get(3));
    updateFileSize(rawDataRows.get(4));
    updateChunkSize(rawDataRows.get(5));
  }

  public int getOptimisticUnchokingInterval() {
    return intervalOfOptimisticUnchoking;
  }

  private void updatePreferredNumberOfNeighbours(String key) {
    String[] words = key.split(" ");
    this.preferredNumberOfNeighbours = Integer.parseInt(words[1]);
  }

  private void updateIntervalOfUnchoking(String key) {
    String[] words = key.split(" ");
    this.intervalOfUnchoking = Integer.parseInt(words[1]);
  }

  private void updateIntervalOfOptimisticUnchoking(String key) {
    String[] words = key.split(" ");
    this.intervalOfOptimisticUnchoking = Integer.parseInt(words[1]);
  }

  private void updateMyFileName(String key) {
    String[] words = key.split(" ");
    this.myFileName = words[1];
  }

  private void updateFileSize(String key) {
    String[] words = key.split(" ");
    this.fileSize = Integer.parseInt(words[1]);
  }

  public int getFileSize() {
    return this.fileSize;
  }

  private void updateChunkSize(String key) {
    String[] words = key.split(" ");
    this.chunkSize = Integer.parseInt(words[1]);
  }

  public int getChunkSize() {
    return this.chunkSize;
  }

  public int getPreferredNumberOfNeighbours() {
    return preferredNumberOfNeighbours;
  }

  public String getMyFileName() {
    return myFileName;
  }
  
  public int calculateTotalChunks() {
  	int totalChunks = (int) Math.ceil((double) this.getFileSize() / this.getChunkSize());
  	return totalChunks;
  }

  public void printIt() {
    System.out.println("Preferred Neigbours " + this.preferredNumberOfNeighbours);
    System.out.println("UnchokingInterval " + this.intervalOfUnchoking);
    System.out.println("OptimisticUnchokingInterval " + this.intervalOfOptimisticUnchoking);
    System.out.println("FileName " + this.myFileName);
    System.out.println("FileSize " + this.fileSize);
    System.out.println("PieceSize " + this.chunkSize);
  }
}

class CommonConfigReader {

  String rootPath;

  public CommonConfigReader() {
    rootPath = System.getProperty("user.dir");
    rootPath = rootPath.concat("/");
  }

  public ArrayList<String> parseConfigFile(String fileName) throws IOException {
    String filePath = rootPath.concat(fileName);
    System.out.println(filePath);
    FileReader fr = new FileReader(filePath);
    BufferedReader br = new BufferedReader(fr);
    ArrayList<String> rows = new ArrayList<>();
    for (Object row : br.lines().toArray()) {
      rows.add((String) row);
    }

    br.close();
    return rows;
  }

  public String getRootPath() {
    return this.rootPath;
  }

}

class GlobalMessageTypes {
	private HashMap<String, Character> map;
	
	public GlobalMessageTypes() {
		map = new HashMap<>();
		  map.put("UNCHOKE" , '1');
		  map.put("BITFIELD" , '5');
		  map.put("INTERESTED" , '2');
		  map.put("NOT_INTERESTED" , '3');
		  map.put("REQUEST" , '6');
		  map.put("PIECE" , '7');
		  map.put("HAVE" , '4');
		  map.put("CHOKE" , '0');
	}
	
	public char getChokeChar() {
		char ch = map.get("CHOKE");
		return ch;
	}
	
	public char getUnchokeChar() {
		char ch = map.get("UNCHOKE");
		return ch;
	}
	
	public char getInterestedChar() {
		char ch = map.get("INTERESTED");
		System.out.println(ch + " -----------------------------------------------------");
		return ch;
	}
	
	public char getNotInterestedChar() {
		char ch = map.get("NOT_INTERESTED");
		System.out.println(ch + " -----------------------------------------------------");
		return ch;
	}
	
	public char getBitFieldChar() {
		char ch = map.get("BITFIELD");
		System.out.println(ch + " -----------------------------------------------------");
		return ch;
	}
	public char getRequestChar() {
		char ch = map.get("REQUEST");
		System.out.println(ch + " -----------------------------------------------------");
		return ch;
	}
	public char getPeiceChar() {
		char ch = map.get("PIECE");
		System.out.println(ch + " -----------------------------------------------------");
		return ch;
	}
	
	public char getHaveChar() {
		char ch = map.get("HAVE");
		System.out.println(ch + " -----------------------------------------------------");
		return ch;
	}
}
class GlobalConstants {

  private final static String TORRENT_FILE = "TheFile.dat";
  private final static String CONFIG_FILE_NAME = "Common.cfg";
  private final static String PEER_CONFIG_FILE_NAME = "PeerInfo.cfg";

  private final static String handShakeHeader = "P2PFILESHARINGPROJ";
  private final static String tenZeros = "0000000000";
  

  public static String getHandShakeHeader() {
    return handShakeHeader;
  }

  public static String getTenZeros() {
    return tenZeros;
  }

  public static String getTorrentFileName() {
    return TORRENT_FILE;
  }

  public static String getCommonConfigFileName() {
    return CONFIG_FILE_NAME;
  }

  public static String getPeerConfigFileName() {
    return PEER_CONFIG_FILE_NAME;
  }
}

class GlobalHelperFunctions {
	
  public static byte[] generateHandshakePacket(int currentNodeId) {
    byte[] handShakePacket = new byte[32];

    int index = 0;

    String handShakeHeader = GlobalConstants.getHandShakeHeader();
    byte[] headerInBytes = new String(handShakeHeader).getBytes();
    for (int j = 0; j < headerInBytes.length; j++) {
      handShakePacket[index] = headerInBytes[j];
      index++;
    }
    
    String zeros = GlobalConstants.getTenZeros();
    byte[] zerosInBytes = new String(zeros).getBytes();
    for (int j = 0; j < zerosInBytes.length; j++) {
      handShakePacket[index] = zerosInBytes[j];
      index++;
    }
    
    byte[] peerIdInBytes = ByteBuffer.allocate(4).putInt(currentNodeId).array();

    for (int j = 0; j < peerIdInBytes.length; j++) {
      handShakePacket[index] = peerIdInBytes[j];
      index++;
    }

    return handShakePacket;
  }

  public static byte[] copyByteArray(byte[] original, int from, int to) {
    int newLength = to - from;
    if (newLength < 0)
      throw new IllegalArgumentException(from + " > " + to);
    byte[] copy = new byte[newLength];
    System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
    return copy;
  }

  public static boolean checkMissingChunksInMe(int[] thisPeerBitfield, int[] connectedPeerBitfield, int len) {
    int i;
    for (i = 0; i < len; i++) {
      if (thisPeerBitfield[i] == 0 && connectedPeerBitfield[i] == 1) {
        return true;
      }
    }
    return false;
  }

  public static int getRandomFileChunk(int[] thisPeerBitfield, int[] connectedPeerBitfield, int len) {
    ArrayList<Integer> chunksneededByMe = new ArrayList<>();
    for (int i = 0; i < len; i++) {
      if (thisPeerBitfield[i] == 0 && connectedPeerBitfield[i] == 1) {
    	  chunksneededByMe.add(i);
      }
    }
    int chunksNeedLen = chunksneededByMe.size();
    
    if (chunksNeedLen <= 0) {
    	// You dont need anything, everything is cool.
        return -1;
    } else {
        Random randObj = new Random();
      int randIndex = Math.abs(randObj.nextInt() % chunksNeedLen); 
      int ans = chunksneededByMe.get(randIndex);
      return ans;
    }
  }

  // Added logging functions here

  public static void logger(BufferedWriter writer, int id1, int id2, String message_type) {
    Date time;
    time = new Date();

    StringBuffer write_log;
    write_log = new StringBuffer();
    DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

    if (message_type == "CHOKE") {
      write_log.append(timeFormat.format(time)+": Peer [" + id1  +"] is choked by ["+ id2 +"].") ;
      
    } else if (message_type == "UNCHOKE") {
      write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] is unchoked by [" + id2 + "].");

    } else if (message_type == "connectionTo") {
      write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] makes a connection to Peer [" + id2 + "].");

    } else if (message_type == "connectionFrom") {
      write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] is connected from Peer [" + id2 + "].");

    } else if (message_type == "changeOptimisticallyUnchokedNeighbor") {
      write_log.append(timeFormat.format(time)+": Peer [" + "] has the optimistically unchoked neighbor [" + id2 + "].");


    } else if (message_type == "INTERESTED") {
      write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] received the 'interested' message from [" + id2 + "]." );

    } else if (message_type == "NOTINTERESTED") {
      write_log.append(timeFormat.format(time)+": Peer [" +id1 + "] received the 'not interested' message from [" + id2 + "]." );

    }
    try {
      String final_value;
      final_value = write_log.toString() ;
      writer.write(final_value);
      writer.newLine();
      writer.flush();
    }catch(FileNotFoundException e){
     
    }catch(IOException e){

    }


  }

  public static void logger_receive_have(BufferedWriter writer, int id1, int id2, int index) {

    Date time;
    time = new Date();

    StringBuffer write_log; 
    DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

    write_log = new StringBuffer();
    write_log.append(timeFormat.format(time) + ": Peer [" + id1 +"] received 'have' message from [" + id2+ "] for the piece: " + index + '.' );
    try {
      String final_value;
      final_value = write_log.toString() ;
      writer.write(final_value);
      writer.newLine();
      writer.flush();
    } catch (Exception e) {
      // e.printStackTrace();
    }
  }

  public static void logger_change_preferred_neighbors(BufferedWriter writer, int id1, int[] id_list) {

    Date time;
    time = new Date();
    DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

    StringBuffer write_log;
    write_log = new StringBuffer();
    write_log.append(timeFormat.format(time) +": Peer [" + id1 + "] has the preferred neighbors [" );
    
    for (int i = 0;i <id_list.length ; i++) {
      write_log.append(id_list[i]);
      if (i < (id_list.length - 1) )
          write_log.append(',');
    
        }
    //writer_log.deleteCharAt(writer_log.length() - 1);
    write_log.append("].");
    try {
      String final_value;
      final_value = write_log.toString() ;
      writer.write(final_value);
      writer.newLine();
      writer.flush();
    } catch(FileNotFoundException e){
     
    }catch(IOException e){

    }
  }

  public static void logger_download_piece(BufferedWriter writer, int id1, int id2, int index, int number_of_pieces) {
    Date time;
    time = new Date();

    StringBuffer write_log;
    write_log = new StringBuffer();
    DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

    write_log.append(timeFormat.format(time) + ": Peer [" + id1 + "] has downloaded the piece " + index + " from [" + id2 + "]. " +"Now the number of pieces it has is : "+ number_of_pieces + '.');
    
    try {
      String final_value;
      final_value = write_log.toString() ;
      writer.write(final_value);
      writer.newLine();
      writer.flush();
    } catch(FileNotFoundException e){
     
    }catch(IOException e){

    }
  }

  public static void logger_completed_downloading(BufferedWriter writer, int id1) {
    Date time;
    time = new Date();
    DateFormat timeFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");

    StringBuffer write_log;
    write_log = new StringBuffer();
    write_log.append(timeFormat.format(time)+": Peer [" + id1 + "] has downloaded complete file ");
    
    try {
      String final_value;
      final_value = write_log.toString() ;
      writer.write(final_value);
      writer.newLine();
      writer.flush();
    } catch(FileNotFoundException e){
     
    }catch(IOException e){

    }
  }
  // logging functions end

}



class ConnectedPeerNode {
  // private boolean haveFile;
  private int peerId;
  private String nameOfHost;
  private int portNumber;
  private int hasFile;
  private int[] chunkMarker;
  private int numberOfPieces;

  public ConnectedPeerNode() {
    this.numberOfPieces = 0;
  }

  public int initializePeerObject(String rawPeerData) {
    String[] words = rawPeerData.split(" ");
    this.peerId = Integer.parseInt(words[0]);
    this.nameOfHost = words[1];
    this.portNumber = Integer.parseInt(words[2]);
    this.hasFile = Integer.parseInt(words[3]);

    return this.peerId;
  }

  public boolean hasFile() {
	  
	if (this.hasFile == 1) {
		return true;
	}
    
	return false;
  }

  public void setHaveFile(int haveFile) {
    this.hasFile = haveFile;
  }
  
  public void putFileOne() {
	  this.hasFile = 1;
  }
  
  
  public void putFileZero() {
	  this.hasFile = 0;
  }
  
  public int getPeerId() {
    return this.peerId;
  }

  
  public void downCompOne() {
	  this.hasFile = 1;
  }
  
  public void downCompZero() {
	  this.hasFile = 0;
  }
  
  public void fileDownloaded() {
	  this.hasFile = 1;
  }

  public int getHasFile() {
    return this.hasFile;
  }

  public boolean updateHasFile(int val) {
    this.hasFile = val;
    return this.hasFile == 1;
  }

  public void setChunkMarker(int[] chunkMarker) {
    this.chunkMarker = chunkMarker;
  }

  public void updateChunkMarker(int[] chunkMarker) {
    System.out.println("updating bit field for " + this.peerId);
    this.chunkMarker = chunkMarker;
  }

  public int getStoredChunks() {
    int count = 0;
    for (int i = 0; i < chunkMarker.length; i++) {
      if (chunkMarker[i] == 1)
        count++;
    }

    return count;
  }

  public int getChunksLength() {
    return this.chunkMarker.length;
  }

  public void updateNumOfPieces() {
    this.numberOfPieces++;
    int currP = this.numberOfPieces;
    int bitFLen = this.chunkMarker.length;
    
    // check if all the chunks have been recived. 
    if (currP == bitFLen) {
      this.hasFile = 1;
    }
  }

  public int getNumOfPieces() {
    return this.numberOfPieces;
  }

  public int getportNumber() {
    return this.portNumber;
  }

  public String getNameOfHost() {
    return this.nameOfHost;
  }

  public int[] getBitField() {
    return this.chunkMarker;
  }

  public void updateBitfield(int index) {
    this.chunkMarker[index] = 1;
  }

  @Override
  public String toString() {
    return (" peerId " + this.peerId + " hostName: " + this.nameOfHost + " portNumber: " + this.portNumber
        + " hasFile: " + this.hasFile + "\n");
  }
}

public class peerProcess {


  static GlobalMessageTypes globalMessageTypes;
  static CommonConfigReader globalConfigReader;
  static CommonConfigData commonConfigData;
  static LinkedHashMap<Integer, ConnectedPeerNode> peerMap;
  static int currentNodeId;
  static ConnectedPeerNode currentNode;
  static byte[][] currentFileChunks;
  static int peersWithCompleteFiles = 0;
  static File currentNodeDir;
  private static ConcurrentHashMap<Integer, AdjacentConnectionNode> connectionsMap;
  private static File log_file;
  private static String fileFormat;
  private static String mainFileName;

  private static class MainBackgroundThread extends Thread {
    private AdjacentConnectionNode peer;

    public MainBackgroundThread(AdjacentConnectionNode peer) {
      this.peer = peer;
    }
    
    public void showDownloadData() {
    	int chunksTillNow = currentNode.getNumOfPieces();
    	int totalChunks = commonConfigData.calculateTotalChunks();
        double downloadedPercentage = ((chunksTillNow * 100.0)/totalChunks);
        String outputRes = chunksTillNow + "/" + totalChunks + " downloaded: " + downloadedPercentage +"% ";
        System.out.println(outputRes);
    }

    @Override
    public void run() {
      // try {
      // Thread.sleep(5000);
      // } catch (InterruptedException e1) {
      // // TODO Auto-generated catch block
      // e1.printStackTrace();
      // }
      synchronized (this) {

        try {

          DataInputStream dataInputStream = new DataInputStream(peer.getConnection().getInputStream());
          System.out.println("Sending bit field msg ... ");
          peer.sendBitFieldMsg();
          int ccc = 0;
          BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));

          while (peersWithCompleteFiles < peerMap.size()) {
            // System.out.println(ccc + " ---- dekhte hain loop " + " ccompleted files " +
            // peersWithCompleteFiles + "/" + peerMap.size());
        	  
            int msgLength = dataInputStream.readInt();
            
            byte[] msg = null, inputMsg = null;
            boolean vvv = true;
            if (vvv == true) {
                msg = new byte[msgLength - 1];
                inputMsg = new byte[msgLength];
                // System.out.println("Comes here");
            }
            double startTime = (System.nanoTime() / 100000000.0);
            dataInputStream.readFully(inputMsg);
            double endTime = (System.nanoTime() / 100000000.0);
            
            char msgType = (char) (inputMsg[0]);
            {
                int index = 0;
                for (int i = 1; i < msgLength; i++) {
                  msg[index++] = inputMsg[i];
                }
            }
            
            writer.flush();


            // ConnectedPeerNode connectedPeerObject;
            // int randomChunkIndex;
            // System.out.println("This is my message type " + msgType + " entering
            // switch..");

            if (msgType == globalMessageTypes.getBitFieldChar()) {
              // System.out.println("BITFIELD aya hain + " + currentNodeId + " iske thread
              // waale functio mein hun");
               int inde = 0;

              int[] bitfield = new int[msg.length / 4];
              for (int i = 0; i < msg.length; i += 4) { // 
                byte[] tempByteArray = GlobalHelperFunctions.copyByteArray(msg, i, i + 4);
                bitfield[inde++] = ByteBuffer.wrap(tempByteArray).getInt();
              }

              // update the bitfield/chunks
              ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());
              connectedPeerObject.updateChunkMarker(bitfield);
              int currentPeerChunks = connectedPeerObject.getStoredChunks();

              if (currentPeerChunks == currentNode.getChunksLength()) {
                boolean ans = connectedPeerObject.updateHasFile(1);
                peersWithCompleteFiles++;
              } else {
                boolean ans = connectedPeerObject.updateHasFile(0);
              }

              boolean checkMissingChunksInMe = GlobalHelperFunctions.checkMissingChunksInMe(currentNode.getBitField(),
                  connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

              // System.out.println("Kya " + currentNodeId + " mere chunks missing hain ?" +
              // checkMissingChunksInMe + " ? ");
              // set interested msg.

              if (checkMissingChunksInMe) {
                // System.out.println("iss connection pe interested tick laga do");
                peer.sendInterestedMessage(); // mein iss waale connection say interested hun lenen kay liye, toh ye
                                              // connection kee property hain.
              } else {
                // System.out.println("Mujhe iss connection say kuch nai chaiye");
                peer.sendNotInterestedMsg() ;
              }

            } else if (msgType == globalMessageTypes.getInterestedChar()) {
              System.out.println("Mera peer mere say lene mein interested hain.... last wala");
              peer.fetchIntres();
              GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.peerId, "INTERESTED");

            } else if (msgType == globalMessageTypes.getNotInterestedChar()) {
              System.out.println("Meer peer mujhe bol rha hain, kee vo interested nai hain ,,,, last wala);");
              peer.putNotIntres();
              GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.peerId, "NOTINTERESTED");

              // System.out.println("Server say msg .... CHOKCET status ");
              if (!peer.isChoked()) {
                System.out.println("Mere peer choked nai hain,, ab usse choke kar do");
                peer.chokeConnection();
                peer.sendChokeMessage();
              }
            } else if (msgType == globalMessageTypes.getUnchokeChar()) {
              peer.unChokeConnection();
              GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.peerId, "UNCHOKE");

              System.out.println(peer.getPeerId() + " is unchoked on rcv side");
              // System.out.println("Connection unchoked....");

              // Request peice from your sender now.
              ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());

              int randomChunkIndex = GlobalHelperFunctions.getRandomFileChunk(currentNode.getBitField(),
                  connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

              if (randomChunkIndex == -1) {
                System.out.println("Nothing found, maybee there is no file I (RECEIVER) dont want to haves");
                System.out.println(peer.isChoked() + " <----- " + peer.isInterested());
              } else {
                // System.out.println("Now, I(RECEIVER) will request for the chunk after
                // unchoking");
                peer.sendRequestMessage(randomChunkIndex);
              }

            } else if (msgType == globalMessageTypes.getRequestChar()) {
              int fileChunkIndex = ByteBuffer.wrap(msg).getInt();
              peer.sendPieceMessage(fileChunkIndex);
            } else if (msgType == globalMessageTypes.getPeiceChar()) {
              System.out.println("PIECE rcv from " + peer.getPeerId());

              {

                // System.out.println("Ye index mila " + recvFileChunkIndex);
                
            	int recvFileChunkIndex = ByteBuffer.wrap(GlobalHelperFunctions.copyByteArray(msg, 0, 4)).getInt();
                ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());
                currentFileChunks[recvFileChunkIndex] = new byte[msg.length - 4];

                int ci = 0;
                for (int i = 4; i < msg.length; i++) {
	                byte[] currFileRow = currentFileChunks[recvFileChunkIndex];
	                currFileRow[ci++] = msg[i];
                }
                
                currentNode.updateBitfield(recvFileChunkIndex);
                
                currentNode.updateNumOfPieces();
                
                if (peer.isChoked() == false) {

                  int randomChunkIndex = GlobalHelperFunctions.getRandomFileChunk(currentNode.getBitField(),
                      connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

                  if (randomChunkIndex == -1) {
                    // System.out.println("Nothing found, maybee there is no file I (RECEIVER) want
                    // to fetch");
                    // System.out.println("Hasfile variable " + currentNode.hasFile());
                  } else {
                    // System.out.println("Now, I(RECEIVER) will request for the another chunk after
                    // unchoking with " + randomChunkIndex);
                    peer.sendRequestMessage(randomChunkIndex);
                  }
                } else {
                  // System.out.println("PEER RCVED PEICE IN CHOKED STATE");
                }
                double rate = ((double) (msg.length + 5) / (endTime - startTime));
                int hasFile = connectedPeerObject.getHasFile();
                if (hasFile == 1) {
                	// the file is completed..
                  peer.putDownloadSpeed(-1);
                } else {
                	//
                  peer.putDownloadSpeed(rate);
                }
                GlobalHelperFunctions.logger_download_piece(writer, currentNode.getPeerId(), peer.getPeerId(),
                recvFileChunkIndex, currentNode.getNumOfPieces());

                
                showDownloadData();
                peer.checkCompleted(recvFileChunkIndex);
                // Now check have msg ...
                // System.out.println("hello -- bro -----, I am coming back to peices -- " +
                // currentNode.hasFile());

                for (int connection : connectionsMap.keySet()) {
                  AdjacentConnectionNode pc = connectionsMap.get(connection);
                  pc.sendHaveMessage(recvFileChunkIndex);
                }
                //
              }
            } else if (msgType == globalMessageTypes.getHaveChar()) {
              // System.out.println("Bhaiya mein server hun mere paas HAVE aya hain");
              int haveChunkIndex = ByteBuffer.wrap(msg).getInt();
              ConnectedPeerNode connectedPeerObject = peerMap.get(peer.getPeerId());

              connectedPeerObject.updateBitfield(haveChunkIndex);

              // now check if
              int totalBits = connectedPeerObject.getStoredChunks();
              if (totalBits == currentNode.getChunksLength()) {
                connectedPeerObject.setHaveFile(1);
                peersWithCompleteFiles++;
              }

              {
                // IMPORTANT: Yaha par mein ye
                boolean checkMissingChunksInMe = GlobalHelperFunctions.checkMissingChunksInMe(currentNode.getBitField(),
                    connectedPeerObject.getBitField(), connectedPeerObject.getChunksLength());

                // System.out.println("KYa mere ander chunks missing hain ? " +
                // checkMissingChunksInMe);
                if (checkMissingChunksInMe) {
                  // System.out.println("Mein INTERSTED hun ....");
                  peer.sendInterestedMessage(); // mein iss waale connection say interested hun
                  // lenen kay liye, toh ye
                  // connection kee property hain.
                } else {
                  // System.out.println("Mein Intersetd nai hun....");
                  peer.sendNotInterestedMsg() ;
                }

              }
              GlobalHelperFunctions.logger_receive_have(writer, currentNode.getPeerId(), peer.getPeerId(), haveChunkIndex);

            } else if (msgType == globalMessageTypes.getChokeChar()) {
              GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), peer.peerId, "CHOKE");

              peer.chokeConnection();
              System.out.println("PEER is choked");
            } else {
              System.out.println("KUCH PANGA HAI");
              ccc++;
            }

          }

          System.out.println("bhaiya meient oh khatam ho gya hun....");
         
          writer.close();
          Thread.sleep(5000);
          //System.exit(0);
        } catch (Exception e) {
          //e.printStackTrace();
        }
      }
    }
  }

  private static class AdjacentConnectionNode {
	  
    private Socket conn;
    private int peerId;
    private boolean isIntrested = false;
    private boolean isChoked = true;
    private double doubleDownloadRate = 0;
    private boolean isOptimisticallyUnchoked = false;

    public AdjacentConnectionNode(Socket conn, int peerId) {
      this.conn = conn;
      this.peerId = peerId;
      (new MainBackgroundThread(this)).start();

    }

    public double fetchDownloadSpeed() {
      return doubleDownloadRate;
    }

    public void putDownloadSpeed(double doubleDownloadRate) {
      this.doubleDownloadRate = doubleDownloadRate;
    }

    public boolean isChoked() {
      return this.isChoked;
    }

    public void chokeConnection() {
      this.isChoked = true;
    }

    public boolean isOptimisticallyUnchoked() {
      return isOptimisticallyUnchoked;
    }

    public void unChokeConnection() {
      this.isChoked = false;
    }

    public void optimisticallyUnchoke() {
    	isOptimisticallyUnchoked = true;
    }

    public void optimisticallyChoke() {
    	isOptimisticallyUnchoked = false;
    }

    public boolean isInterested() {
      return this.isIntrested;
    }

    public void putNotIntres() {
      this.isIntrested = false;
    }

    public void fetchIntres() {
      this.isIntrested = true;
    }

    public Socket getConnection() {
      return this.conn;
    }

    public int getPeerId() {
      return this.peerId;
    }


    public byte[] getFileChunk(int pieceIndex, byte[] piece) {
      int index = 0;
      byte[] payload = new byte[4 + piece.length];

      byte[] indexBytes = ByteBuffer.allocate(4).putInt(pieceIndex).array();
      
      for (int i=0;i < indexBytes.length;i++) {
		 payload[index++] = indexBytes[i];
      }
      for (int i=0;i < piece.length;i++) {
	        payload[index++] = piece[i];
      }
      
      byte[] packet = null;
      
      
      try {
          packet =  createPacket((5 + piece.length), globalMessageTypes.getPeiceChar(), payload);
      } catch(WrongPacketException ex) {
      	System.out.println(ex.getMessage());
      	System.exit(0);
      }
      
      return packet;
    }


    public byte[] createPacket(int len, char type, byte[] payload) throws WrongPacketException {
      
      if (type == globalMessageTypes.getInterestedChar() || type == globalMessageTypes.getNotInterestedChar() || type == globalMessageTypes.getUnchokeChar() || type == globalMessageTypes.getChokeChar()) {
          
          byte msgType = (byte) type;
          int index = 0;
          byte[] resultPacket = new byte[len + 4];
          byte[] lHeader = ByteBuffer.allocate(4).putInt(len).array();
          for (int i=0;i < lHeader.length;i++) {
        	byte x = lHeader[i];
        	resultPacket[index++] = x;
          }
          resultPacket[index] = msgType;
          return resultPacket;
      } else if (type == globalMessageTypes.getBitFieldChar() || type == globalMessageTypes.getRequestChar() || type == globalMessageTypes.getPeiceChar() || type == globalMessageTypes.getHaveChar()) {
          
          
          byte msgType = (byte) type;
          int counter = 0;
          byte[] resultPacket = new byte[len + 4];
          byte[] lHeader = ByteBuffer.allocate(4).putInt(len).array();
          //for (byte x : length) {
          for (int i=0;i < lHeader.length;i++) {
        	byte x = lHeader[i];
        	resultPacket[counter++] = x;
          }
          resultPacket[counter++] = msgType;
          for (int i=0;i < payload.length;i++) {
        	byte x = payload[i];
        	resultPacket[counter++] = x;
          }
          
          return resultPacket;
      } else {
    	  System.out.println(" DIffernt type of packet ..............");
    	  throw new WrongPacketException("Wrong packet request " + type);
      }
      
      
    }

    
    public void sendRequestMessage(int chunkIndex) {
    	try {
            DataOutputStream connOutStream = new DataOutputStream(conn.getOutputStream());
            connOutStream.flush();
            
            byte[] payload = ByteBuffer.allocate(4).putInt(chunkIndex).array();
            byte[] requestMsg = null;
            try {
                requestMsg = createPacket(5, globalMessageTypes.getRequestChar(), payload);
            } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            connOutStream.write(requestMsg);
            connOutStream.flush();
    	}  catch (IOException e) {
    		System.out.println("Some error in sending request message");
    	} finally {
    		System.out.println("Request function totally completed...");
    	}

    }
    
    public void sendPieceMessage(int index) {
    	
    	try {
    		
            DataOutputStream connOutStream = new DataOutputStream(conn.getOutputStream());
            //flush the stream..
            boolean notWorking = true;
            if (notWorking) {
                connOutStream.flush();
            }
            connOutStream.write(getFileChunk(index, currentFileChunks[index]));
            connOutStream.flush();
            connOutStream.flush();
            
    	}  catch (IOException exception) {
    		System.out.println("Some error in sending peice message");
    		
    	} finally {
    		System.out.println("sendPieceMessage is completed once....");
    	}
    	
    }
    
    public boolean dummFn() {
    	System.out.println("Thread reachered here");
    	return true;
    }
    public void sendHaveMessage(int pieceIndex) {
    	
    	try {
    		//
            DataOutputStream connOutStream = null;
            // check scope here...
            boolean vvv = dummFn();
            if (vvv == true)
            {
            	connOutStream = new DataOutputStream(conn.getOutputStream());
            }
            connOutStream.flush();
            byte[] payload = ByteBuffer.allocate(4).putInt(pieceIndex).array();
            byte[] haveMsg = null;
            try {
                haveMsg = createPacket(5, globalMessageTypes.getHaveChar(), payload);
            } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            connOutStream.write(haveMsg);
            connOutStream.flush();
            
    	}  catch (IOException exception) {
    		System.out.println("Some error in sending have message");
    	} finally {
    		System.out.println("Send have msg body completed full....");
    	}
    }
    
    
    private boolean allD(int i, int j, String s) {
    	if (s.equals("")) return true;
        int n = s.length();
        if (i < 0 || j >= n) return false;
        if (i > j) return false;
        while(i <= j) {
            if (! isD(s.charAt(i))) return false;
            i++;
        }
        return true;
    }
    
    private boolean isD(char c) {
        return c >= '0' && c <= '9';
    }
    
    public void sendInterestedMessage() {
    	// send interested msg..
    	
    	try {
            DataOutputStream connOutStream = null;
            boolean asss = allD(0, 0, "");
            if (asss == true){
                connOutStream = new DataOutputStream(conn.getOutputStream());
                connOutStream.flush();
            }

            byte[] interestedMsg = null;
            try {
                interestedMsg = createPacket(1, globalMessageTypes.getInterestedChar(), null);
            } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            connOutStream.write(interestedMsg);
            
            connOutStream.flush();
            
    	} catch (IOException exception) {
    		System.out.println("Some error in sending interested message");
    	} finally {
    		System.out.println("sendInterestedMessage body completed...");
    	}
    }
    
    public void sendBitFieldMsg() {
    	// System.out.println("Entered into send bitfield");
    	 try{
    		// fetch conn stream
            DataOutputStream connOutStream = new DataOutputStream(conn.getOutputStream());
            // flush..
            connOutStream.flush();
            int[] bitfield = currentNode.getBitField();
            int newMsgLen = 1 + (4 * currentNode.getBitField().length);
            byte[] payload = new byte[newMsgLen - 1];
            int index = 0;
            for (int i=0;i < bitfield.length;i++) {
            int number = bitfield[i];
              byte[] eachNumberByteArray = ByteBuffer.allocate(4).putInt(number).array();
	        	for (int j=0;j < eachNumberByteArray.length;j++) {
	        	byte eachByte = eachNumberByteArray[j];
	            payload[index++] = eachByte;
	          }
            }
            
            byte[] bitFieldMsg = null;
            
            try {
                bitFieldMsg = createPacket(newMsgLen, globalMessageTypes.getBitFieldChar(), payload);
            } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            connOutStream.write(bitFieldMsg);
            connOutStream.flush();
    	} catch(IOException exception){
    		System.out.println("Some error in sending interested message");
    	} finally {
    		//System.out.println("Whatever happened it complted..");
    	}
    }
    
    public void sendNotInterestedMsg() {
    	try {
            DataOutputStream connOutStream = new DataOutputStream(conn.getOutputStream());
            connOutStream.flush();
            byte[] notInterestedMsg = null;
            
            try {
                notInterestedMsg = createPacket(1, globalMessageTypes.getNotInterestedChar(), null);
            } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            
            connOutStream.write(notInterestedMsg);
            connOutStream.flush();
    	} catch (IOException exception) {
    		System.out.println("Some error in sending not-interested message");
    	} finally {
    		// System.out.println("Cooolll...");
    		
    	}
    }
    
    
    public void sendChokeMessage() {
    	try {
            DataOutputStream connOutStream = new DataOutputStream(conn.getOutputStream());
            connOutStream.flush();
            byte[] chokeMsg = null;
            
            try {
                chokeMsg = createPacket(1, globalMessageTypes.getChokeChar(), null);
           } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            
            connOutStream.write(chokeMsg);
            connOutStream.flush();

    	} catch (IOException exception) {
    		System.out.println("Some error in sending choke message");
    		
    	} finally {
    		
    		System.out.println("sendChoke...");
    	}
    }
    
    
    public void sendUnChokeMessage() {
    	
    	try {
            DataOutputStream dataOutputStream = new DataOutputStream(conn.getOutputStream());
            dataOutputStream.flush();
            byte[] unchokeMsg = null;
            try {
                unchokeMsg = createPacket(1, globalMessageTypes.getUnchokeChar(), null);
           } catch(WrongPacketException ex) {
            	System.out.println(ex.getMessage());
            	System.exit(0);
            }
            dataOutputStream.write(unchokeMsg);
            dataOutputStream.flush();

    	} catch (IOException exception) {
    		System.out.println("Some error in seding unchoke...");
    	} finally {
    		
    		System.out.println("..........unchoke conn msg send");
    	}
    }

    private  void writeToFile(String finalFilePath, byte[] newFileInBytes) throws IOException{
        // Writing the file
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(finalFilePath));
        bufferedOutputStream.write(newFileInBytes);
        bufferedOutputStream.close();
      }
    
    public void checkCompleted(int recvFileChunkIndex) {
    	int totalPeices = 0;
	    int[] currNodeBitFields = currentNode.getBitField();

    	{
    	      for (int i=0;i < currNodeBitFields.length;i++) {
    	        if (currNodeBitFields[i] == 1) {
    	        	totalPeices += 1;
    	        }
    	      }
    	}
    	
      if (totalPeices == currNodeBitFields.length) {
        try { 
	        BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
	        GlobalHelperFunctions.logger_completed_downloading(writer, currentNode.getPeerId());
	        writer.close();
        } catch (IOException e1){
        	e1.printStackTrace();

        }
        int tempIndex = 0;
        int tb = commonConfigData.getFileSize();
        byte[] newFileInBytes = new byte[tb];
        for (int i=0;i < currentFileChunks.length;i++) {
        	for (int j=0; j < currentFileChunks[i].length;j++) {
        		byte currByte = currentFileChunks[i][j];
        		newFileInBytes[tempIndex++] = currByte;
	          }
        }
        try {
          Random rand = new Random();
          int n = rand.nextInt(5000000);
          String finalFilePath = globalConfigReader.getRootPath() + "/peer_" + currentNodeId + "/" + (mainFileName + "." + fileFormat);
          
          // Write file to perferred folder with these bytes.
          writeToFile(finalFilePath, newFileInBytes);
          currentNode.fileDownloaded();
          System.out.println("Phewww, write ho gayi file bosss...");
          peersWithCompleteFiles += 1;

        } catch (Exception exception) {
          exception.printStackTrace();
        } finally {
        	System.out.println("FINALLYPut file in folder thread completed..");
        }
      } else {
          int tempIndex = 0;
          int resultChunkSize = commonConfigData.getChunkSize();
          byte[] newChunkInBytes = new byte[resultChunkSize];
          	for (int j=0; j < currentFileChunks[recvFileChunkIndex].length;j++) {
          		byte currByte = currentFileChunks[recvFileChunkIndex][j];
          		newChunkInBytes[tempIndex++] = currByte;
  	      }
          
          System.out.println("Writing chunk to the folder.... " + recvFileChunkIndex);
          
          try {
              String finalFilePath = globalConfigReader.getRootPath() + "/peer_" + currentNodeId + "/" + ("chunk_" + mainFileName + "_" + recvFileChunkIndex + "." + fileFormat);
              writeToFile(finalFilePath, newChunkInBytes);

            } catch (Exception exception) {
              exception.printStackTrace();
            }
          
          
      }
    }

  }
  
  

  private static class ServerThreadRunnable implements Runnable {
	
	  public boolean isNumber(String s) {
	        
	        if (s.isEmpty()) return false;
	        
	        s = s.trim();
	        int n = s.length();
	        
	        if (n == 0) return false;
	        if (n == 1) return isD(s.charAt(0));
	        
	        int i = 0;
	        int j = n - 1;
	        

	        int dot = 0;
	        int exp = 0;
	        int sign = 0;
	        
	        for(i = 0; i < n; i++) {
	            char c = s.charAt(i);
	            if (c == '.') {
	                dot++;
	            }
	            else if (c == 'e') {
	                exp++;
	            }
	            else if (isSign(c)) {
	                sign++;
	            } else {
	                if (! isD(c)) return false;
	            }
	            
	        }
	        
	        if (exp > 1 || dot > 1 || sign > 2) return false;

	        if (exp == 1) {
	            i = s.indexOf('e');
	            return isExp(i+1, n-1, s) && isDecimal(0, i-1, s);
	        }
	        
	        if (dot == 1) {
	            return isDecimal(0, n-1, s);
	        }
	        
	        //all digits with one leading sigh allowed
	        if (isSign(s.charAt(0))) return allD(1, n-1,s);
	        return allD(0, n-1, s);
	        
	      
	    }
	    
	    private boolean isDecimal(int i, int j, String s) {
	        int n = s.length();
	        if (i < 0 || j >= n) return false;
	        if (i > j) return false;
	        
	        if (isSign(s.charAt(i))) return isDecimal(i+1, j, s);
	        
	        int k = i;
	        for(;k <= j; k++) if (s.charAt(k) == '.') break;
	        
	        if (k > j) return allD(i, j, s);
	        if (k == i) return allD(i+1, j, s);
	        if (k == j) return allD(i, j- 1, s);
	        
	        return allD(i, k-1, s) && allD(k+1, j, s);
	    }
	    
	    
	    private boolean isExp(int i, int j, String s) {
	        int n = s.length();
	        if (i < 0 || j >= n) return false;
	        if (i > j) return false;
	        
	        if (isSign(s.charAt(i))) return allD(i+1,j, s);
	        
	        return allD(i, j, s);
	    }
	    
	    private boolean allD(int i, int j, String s) {
	        int n = s.length();
	        if (i < 0 || j >= n) return false;
	        if (i > j) return false;
	        while(i <= j) {
	            if (! isD(s.charAt(i))) return false;
	            i++;
	        }
	        return true;
	    }
	    
	    private boolean isD(char c) {
	        return c >= '0' && c <= '9';
	    }
	    
	    private boolean isSign(char c) {
	        return c == '-' || c == '+';
	    }  
	
    @Override
    public void run() {
    	
	int checkS = 32;
	byte[] buffer = new byte[checkS];
      try {
    	int currPort = currentNode.getportNumber();
        ServerSocket serverSocket = new ServerSocket(currPort);
        int cMapLen = connectionsMap.size();
        System.out.println("yo size hain bhai.." + cMapLen);
        int pMapLen = peerMap.size();
        System.out.println("yo size saares, conns aa gyae" + cMapLen);
        
//        int cMapLen = connectionsMap.size();
//        System.out.println("yo size hain bhai.." + cMapLen);
//        int pMapLen = peerMap.size();
//        System.out.println("yo size saares, conns aa gyae" + cMapLen);
//        
//        
        
        for (int i = 0; cMapLen < pMapLen - 1;) {
          i++;
          System.out.println("Establishing connection  ..... ");
          Socket conn = serverSocket.accept();

          DataInputStream connInputStream = new DataInputStream(conn.getInputStream());
          System.out.println("Read the conn, inps data");
          connInputStream.readFully(buffer);
          StringBuilder handShakeMsg = new StringBuilder();
          byte[] tempHandshakeBuffer = GlobalHelperFunctions.copyByteArray(buffer, 0, 28);
          handShakeMsg.append(new String(tempHandshakeBuffer));
          byte[] tempIdBuffer = GlobalHelperFunctions.copyByteArray(buffer, 28, 32);
          int peerId = ByteBuffer.wrap(tempIdBuffer).getInt();
          
          try {
              BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
              GlobalHelperFunctions.logger(writer, currentNodeId, peerId, "connectionFrom");
              writer.close();
          } catch(IOException e) {
        	  System.out.println("Could not write to logs in ServerThread");
          }


          handShakeMsg.append(peerId);
          isNumber("");
          System.out.println("Handshake msg on server side : " + handShakeMsg.toString());
          boolean val = true;
          if (val) {
            char chh = 'd';
            isD(chh);
          }
          DataOutputStream connOutputStream = new DataOutputStream(conn.getOutputStream());
          
          byte[] generatedHandShakePacket = GlobalHelperFunctions.generateHandshakePacket(currentNodeId);
          connOutputStream.flush();
          connOutputStream.write(generatedHandShakePacket);

          connectionsMap.put(peerId, new AdjacentConnectionNode(conn, peerId));
          
          cMapLen = connectionsMap.size();
          pMapLen = peerMap.size();
          
        }

      } catch (IOException exception) {
    	  exception.printStackTrace();
      } finally {
    	  System.out.println("Completed.....yeah....");
    	  
      }
      
    }
    
  }
  

  private static class ClientThreadRunnable implements Runnable {
	   
	  
	    @Override
	    public void run() {
	      try {
	        byte[] responsePipe;
	        for (Entry<Integer, ConnectedPeerNode> entry : peerMap.entrySet()) {
	          int key = entry.getKey();
	          if (key == currentNodeId) {
	            break;
	          }
	          System.out.println("Client: spawned foor " + key);
	          ConnectedPeerNode connPeer = peerMap.get(key);
	          
	          String hName = connPeer.getNameOfHost();
	          int pName = connPeer.getportNumber();+
	          Socket clientSocket = new Socket(hName, pName);
	          System.out.println("Client: " + key + " Socket created. Connecting to Server: " + connPeer.getNameOfHost()
	              + " with " + connPeer.getportNumber());
	          DataOutputStream outputPipe = new DataOutputStream(clientSocket.getOutputStream());
	          outputPipe.flush();
	          {
	            outputPipe.flush();
	            byte[] genHandPacketForServer = GlobalHelperFunctions.generateHandshakePacket(currentNodeId);
	            outputPipe.write(genHandPacketForServer);
	            System.out.println("Client: " + key + " Handshake packet sent to Connecting to Server: "
	                + connPeer.getNameOfHost() + " with " + connPeer.getportNumber());
	          }
	          outputPipe.flush();
	          DataInputStream inputPipe = new DataInputStream(clientSocket.getInputStream());
	          responsePipe = new byte[32];
	          inputPipe.readFully(responsePipe);
	          byte[] tempBuffer = GlobalHelperFunctions.copyByteArray(responsePipe, 28, 32);
	          int pid = ByteBuffer.wrap(tempBuffer).getInt();
	          if (pid == key) {
	            
	          try {
	                BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
	                GlobalHelperFunctions.logger(writer, currentNodeId, key, "connectionTo");           
	                writer.close();
	          } catch (IOException exx) {
	            System.out.println("Logs.....operation could not be performed..");
	            
	          }
	              StringBuilder generatedHandShake = new StringBuilder("");
	              tempBuffer = GlobalHelperFunctions.copyByteArray(responsePipe, 0, 28);
	              generatedHandShake.append(new String(tempBuffer));
	              generatedHandShake.append(pid);
	              connectionsMap.put(key, new AdjacentConnectionNode(clientSocket, key));
	              System.out.println("Client: " + key + " Handshake packet received from Server: " + connPeer.getNameOfHost()
	                  + " with " + connPeer.getportNumber() + " Packet = " + generatedHandShake.toString() + " appended to "
	                  + " Updated connections " + connectionsMap.size() + "/" + (peerMap.size() - 1)
	                  + " connections till now");
	            } else {
	              clientSocket.close();
	          }
	        }
	        
	      } catch (IOException exception) {
	        
	        exception.printStackTrace();
	        
	      } finally {
	      System.out.println("donoe & exititng");  
	      }
	    }
	  }
  
  

  // intializer functions
  public static void initializeCommonConfigFile() throws IOException {
    ArrayList<String> parsedCommonConfig = globalConfigReader.parseConfigFile("Common.cfg");
    commonConfigData.parseAndUpdateData(parsedCommonConfig);
  }

  public static void initializePeerInfoConfigFile() throws IOException {
    ArrayList<String> parsedPeerConfig = globalConfigReader.parseConfigFile("PeerInfo.cfg");
    for (String key : parsedPeerConfig) {
      ConnectedPeerNode peer = new ConnectedPeerNode();
      int peerId = peer.initializePeerObject(key);
      peerMap.put(peerId, peer);
    }
  }

  public static void initializepeersWithCompleteFiles() {
    peersWithCompleteFiles = 0;
  }

  public static void intializePeerDirectory() {
    currentNodeDir = new File("peer_" + currentNodeId);
    if (!currentNodeDir.exists()) {
      currentNodeDir.mkdir();
    }
  }

  public static byte[][] initializeFileChunks(int chunks) {
    currentFileChunks = new byte[chunks][];
    return currentFileChunks;
  }

  // **********
  public static void writeFileChunks() throws IOException {
    int fileSize = commonConfigData.getFileSize(), chunkSize = commonConfigData.getChunkSize();
    String totPath = globalConfigReader.getRootPath() + GlobalConstants.getTorrentFileName();
    FileInputStream fr =new FileInputStream(totPath);
    BufferedInputStream file = new BufferedInputStream(fr);
    byte[] fileBytes = new byte[fileSize];
    
    // Read the file here.
    file.read(fileBytes);

    // close the file
    file.close();

    {
      int counter = 0, index = 0;
      for (; counter < fileSize;) {
        if (counter + chunkSize <= fileSize)
          currentFileChunks[index] = GlobalHelperFunctions.copyByteArray(fileBytes, counter, counter + chunkSize);
        else
          currentFileChunks[index] = GlobalHelperFunctions.copyByteArray(fileBytes, counter, fileSize);
        currentNode.updateNumOfPieces();
        counter += chunkSize;
        index++;
      }
    }

  }

  // helper functions for dividing the files
  public static void calculateChunks() throws IOException {
    int fileSize = commonConfigData.getFileSize(), chunkSize = commonConfigData.getChunkSize();

    double chunks = fileSize * 1.0 / chunkSize;
    int roundOffChunks = (int) (Math.ceil(chunks));
    currentFileChunks = initializeFileChunks(roundOffChunks);

    // Will signify number of chuncks currently received in the file.
    int[] chunkMarker = new int[roundOffChunks];

    // Check if this peer, already has a file.
    boolean isCompleted = currentNode.hasFile();
    if (!isCompleted) {
      for (int i = 0; i < chunkMarker.length; i++) {
        chunkMarker[i] = 0;
      }
      currentNode.setChunkMarker(chunkMarker);
    } else {
      peersWithCompleteFiles++;
      // mark all chunks to 1, because we have the entire file.
      for (int i = 0; i < chunkMarker.length; i++) {
        chunkMarker[i] = 1;
      }
      currentNode.setChunkMarker(chunkMarker);
      // main part remaining
      // ----> start from here.
      writeFileChunks();

    }
  }

  public static void setFileNameAndFormat() {
	  String fileName = GlobalConstants.getTorrentFileName();
	  String[] ips = fileName.split("\\.");
	  fileFormat = ips[1];
	  mainFileName = ips[0];
  }
  public static void main(String[] args) throws IOException {
	 
	globalMessageTypes = new GlobalMessageTypes();//for messages
	setFileNameAndFormat();
    // Awesome
    globalConfigReader = new CommonConfigReader();//config file reader
    // common config initizlied
    commonConfigData = new CommonConfigData();//common cfg data
    initializeCommonConfigFile();
    commonConfigData.printIt();

    // peer config read
    peerMap = new LinkedHashMap<>();//peerinfo cfg hash map
    initializePeerInfoConfigFile();
    System.out.println(peerMap);

    // host received
    currentNodeId = Integer.parseInt(args[0]);
    currentNode = peerMap.get(currentNodeId);

    // 1. create directory
    intializePeerDirectory();

    // 2. create logs
    log_file = new File(System.getProperty("user.dir") + "/" + "log_peer_" + currentNodeId + ".log");
    if (log_file.exists() == false)
      log_file.createNewFile();



    // Create map
    connectionsMap = new ConcurrentHashMap<>();


    // 3. divideFilesIntoChunks & Write if the file is completed
    calculateChunks();

    // .....
    Thread clientThread = new Thread(new ClientThreadRunnable());
    Thread serverThread = new Thread(new ServerThreadRunnable());
    Thread up = new Thread(new UPRunnable());
    Thread oup = new Thread(new OUPRunnable());

    clientThread.start();
    serverThread.start();
    up.start();
    oup.start();

  }
  static class ValueComparator implements Comparator<Integer> {

    Map<Integer, Double> base;
    public ValueComparator(Map<Integer, Double> base) {
        this.base = base;
    }
  
    public int compare(Integer a, Integer b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        }
    }
  }
  private static class UPRunnable implements Runnable {
	 
	public List<Integer> fetchConnectionIds() {
        List<Integer> connIds = new ArrayList<>();
        for (int key: connectionsMap.keySet()) {
        	connIds.add(key);
        }
        return connIds;
	}
	
	public List<Integer> fetchInterestedConns(List<Integer> connections) {
        List<Integer> interested = new ArrayList<>();
        for (int i=0;i < connections.size();i++) {
        int connection = connections.get(i);
        AdjacentConnectionNode tempConnection = connectionsMap.get(connection);
          if (tempConnection.isInterested()) {
            interested.add(connection);
          }
        }
        return interested;
	}

	public List<Integer> fetchInterestedAccordingToDownloadRate(List<Integer> connections) {
		List<Integer> interestedPeers = new ArrayList<>();
        for (int peer : connections) {
            AdjacentConnectionNode connObj = connectionsMap.get(peer);
            if (connObj.isInterested() && connObj.fetchDownloadSpeed() >= 0)
              interestedPeers.add(peer);
          }
        return interestedPeers;
	}
	 
    @Override
    public void run() {
    	
      while (peersWithCompleteFiles < peerMap.size()) {
        List<Integer> connections = fetchConnectionIds();
        
        // check if the file is prsent or not
        if (currentNode.getHasFile() == 1) {
        	// This peer has the file.
          List<Integer> interestedPeers = fetchInterestedConns(connections);
          System.out.println(interestedPeers.size() + " <----- itne interestd peers hain");
          
          if (interestedPeers.size() <= 0) {
              System.out.println("Koi interested peer bacha hee nai hain.....");
          } else {
        	
        	int prefNeighbours = commonConfigData.getPreferredNumberOfNeighbours();
        	int intresLen = interestedPeers.size();
            if (intresLen <= prefNeighbours) {
                for (int i=0;i < intresLen;i++) {
                	int peer = interestedPeers.get(i);
                	AdjacentConnectionNode tempConnection = connectionsMap.get(peer);
                  if (tempConnection.isChoked()) {
                    System.out.println("SENDING UNCHOKE msg to prefs from sender");
                    tempConnection.unChokeConnection();
                    tempConnection.sendUnChokeMessage();
                  }
                }
              } else {

                System.out.println("When you have more prees than cofig");
                int prefNeigboursSize = commonConfigData.getPreferredNumberOfNeighbours();
                int[] preferredNeighbors = new int[prefNeigboursSize];

                Random randObj = new Random();
                
                for (int i = 0; i < prefNeigboursSize; i++) {
                int randomIndex = Math.abs(randObj.nextInt() % interestedPeers.size());
                  preferredNeighbors[i] = interestedPeers.remove(randomIndex);
                }
                
                for (int i=0;i < prefNeigboursSize;i++) {
                int tempPeerId = preferredNeighbors[i];	
                  AdjacentConnectionNode connObj = connectionsMap.get(tempPeerId);
                  if (connObj.isChoked() == true) {
                    // 
                    connObj.unChokeConnection();
                    connObj.sendUnChokeMessage();
                  }
                }
                for (int i=0;i < interestedPeers.size();i++) {
                int peer = interestedPeers.get(i);
                  AdjacentConnectionNode connObj = connectionsMap.get(peer);
                  boolean isConnChoked = connObj.isChoked(), isConnOppUnchoked = connObj.isOptimisticallyUnchoked();
                  if (isConnChoked == false && isConnOppUnchoked == false) { // What the it is
                    System.out.println("This is choked with OPTIMISTIIC " + peer);
                    connObj.chokeConnection();
                    connObj.sendChokeMessage();
                  }
                }
                System.out.println("Preferred neighbours of node" + currentNodeId + " is " + preferredNeighbors);

                try {
                   BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
                   GlobalHelperFunctions.logger_change_preferred_neighbors(writer, currentNode.getPeerId(), preferredNeighbors);
                   writer.close();
                } catch (IOException e) {
                  // TODO Auto-generated catch block
	            	System.out.println("Could not write logs in UNCHOKING PEER");
	            	e.printStackTrace();
                }
  
              }
          }
        } else {

          System.out.println("What if mere paas file hee na ho");
          List<Integer> interestedPeers = fetchInterestedAccordingToDownloadRate(connections);
          int prefNeigboursSize = commonConfigData.getPreferredNumberOfNeighbours();
          if (interestedPeers.size() <= prefNeigboursSize) {
        	  for (int i=0;i < interestedPeers.size();i++) {
        	    int peer = interestedPeers.get(i);
              AdjacentConnectionNode connObj = connectionsMap.get(peer);
              if (connObj.isChoked() == false) {
                // do nothing...
              } else {
                System.out.println("kya karun....JITNI hain vo bhejta hun.");
                connObj.unChokeConnection();
                connObj.sendUnChokeMessage();
              }
            }
          } else {

            int[] preferredNeighbors = new int[prefNeigboursSize];

            System.out.println("Jab prefs zyada ho..WITHOUT FILE PEER HO");
            HashMap<Integer,Double> map = new HashMap<Integer,Double>();
            ValueComparator bvc =  new ValueComparator(map);
            TreeMap<Integer,Double> sorted_map = new TreeMap<Integer,Double>(bvc);
            for(int i=0;i<interestedPeers.size();i++){
              map.put(interestedPeers.get(i),connectionsMap.get(interestedPeers.get(i)).fetchDownloadSpeed());
            }
            sorted_map.putAll(map);
            List<Integer> sorted_peers = new ArrayList<Integer>();
	        sorted_peers.addAll(sorted_map.keySet());
            for(int j=0;j < prefNeigboursSize;j++){
              int peer= sorted_peers.get(j);
              preferredNeighbors[j]= peer;
              AdjacentConnectionNode tempConnection = connectionsMap.get(peer); 
              if(tempConnection.isChoked()){
            	 tempConnection.unChokeConnection();
            	 tempConnection.sendUnChokeMessage();
              }
              
              interestedPeers.remove(peer);
            }
            for(int m=0;m<interestedPeers.size();m++){
              int peer = interestedPeers.get(m);
              AdjacentConnectionNode tempConnection = connectionsMap.get(peer);
              
              if( tempConnection.isChoked() == false && tempConnection.isOptimisticallyUnchoked() == false){
            	  tempConnection.chokeConnection();
            	  tempConnection.sendChokeMessage();
              }
            }
            System.out.println("Preferred neighbours of node" + currentNodeId + " is " + preferredNeighbors);
            try {
            	BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
            	GlobalHelperFunctions.logger_change_preferred_neighbors(writer, currentNode.getPeerId(), preferredNeighbors);
            	writer.close();
            } catch (IOException e) {
              // TODO Auto-generated catch block
            System.out.println("Could not write to logs in UNCHOKING peer");
              e.printStackTrace();
            }

          }

        }
        try {
          System.out.println("Thread sleeps....");
          int unchokeTime = commonConfigData.getUnchokingInterval();
          int unChokeTimeInSecs = unchokeTime * 1000;
          Thread.sleep(unChokeTimeInSecs);
        } catch (Exception exception) {

           exception.printStackTrace();
        } finally {
          System.out.println("Inner thread completed");
        } 
      }
      try {
    	  
        // Thread.sleep(5000);
        
      } catch (Exception exception) {
        exception.printStackTrace();
      } finally {
        System.out.println("Outer thread completed ....");
      }
      System.exit(0);
    }
  }

  private static class OUPRunnable implements Runnable {
	 
	public List<Integer> fetchConnectionIds() {
        List<Integer> connIds = new ArrayList<>();
        for (int key: connectionsMap.keySet()) {
        	connIds.add(key);
        }
        return connIds;
  }
  
	public List<Integer> fetchInterestedConns(List<Integer> connections) {
        List<Integer> interested = new ArrayList<>();
        for (int i=0;i < connections.size();i++) {
        int connection = connections.get(i);
        AdjacentConnectionNode tempConnection = connectionsMap.get(connection);
          if (tempConnection.isInterested()) {
            interested.add(connection);
          }
        }
        
        return interested;
	}
    @Override
    public void run() {
      while (peersWithCompleteFiles < peerMap.size()) {
    	
    	List<Integer> connections = fetchConnectionIds();
    	List<Integer> interested = fetchInterestedConns(connections);
        
        
        if (interested.size() > 0) {
          Random randObj = new Random();
          int intLen = interested.size();
          int randomIndex = Math.abs(randObj.nextInt() % intLen);
          int randomConnId = interested.get(randomIndex);
          
          AdjacentConnectionNode randomConnection = connectionsMap.get(randomConnId);
          randomConnection.unChokeConnection();
          randomConnection.sendUnChokeMessage();
          randomConnection.optimisticallyUnchoke();
          
          try {
        	BufferedWriter writer = new BufferedWriter(new FileWriter(log_file.getAbsolutePath(), true));
            GlobalHelperFunctions.logger(writer, currentNode.getPeerId(), randomConnection.getPeerId(), "changeOptimisticallyUnchokedNeighbor");
            writer.close();
          } catch (IOException e1) {

            System.out.println("Optimistically unchoked peer logs could not added");
            e1.printStackTrace();
          }

          try {
            System.out.println("Sleeping for unchoking internval....THEN I WILL CHOKE THE CONNECTION");
            int sleepInterval = commonConfigData.getOptimisticUnchokingInterval();

            int sleepIntervalInSecs = sleepInterval * 1000;
            // Sleeping the thread
            Thread.sleep(sleepIntervalInSecs);

            // Choke this connection as well.
            randomConnection.optimisticallyChoke();

          } catch (Exception exception) {
            //exception.printStackTrace();
          } finally {
            System.out.println("Ended..");
          }
        }
      }
      try {
        System.out.println("Thread is sleeping Optimistic");
        Thread.sleep(5000);
      } catch (Exception e) {

      }
      System.exit(0);
    }
  }
}
class WrongPacketException extends Exception {
	public WrongPacketException(String s) {
		super(s);
	}
}