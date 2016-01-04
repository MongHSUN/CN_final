import java.io.*;
import java.net.*;
 
class PicClient {
  static Socket socket;
  public static void main(String[] args) throws IOException { // 參數為要傳送的檔案名稱
    socket = new Socket("127.0.0.1", 8081);
    sendFile("C://test1.jpg");
  }
 
  private static void sendFile(String fileName) {
    if (fileName == null)
      return; // 增加文件流用來讀取文件中的資料
    File file = new File(fileName);
    System.out.println("文件長度:" + (int) file.length()); // public Socket
                              // accept() throws
    System.out.println("文件名稱:" + (String) file.getName()); // public Socket
                                // accept()
                                // throws
    try {
      FileInputStream fos = new FileInputStream(file); // 增加網絡服務器接受客戶請求
      //ServerSocket ss = new ServerSocket(8081);
      //Socket client = ss.accept(); // 增加網絡輸出流並提供資料包裝器
      OutputStream netOut = socket.getOutputStream();
      OutputStream doc = new BufferedOutputStream(
          netOut); // 增加文件讀取緩衝區
      byte[] buf = new byte[512];
 
      int num = fos.read(buf);
      System.out.println("傳送文件中:" + (String) file.getName()); // public
                                  // Socket
                                  // accept()
                                  // throws
      while (num != -1) { // 是否讀完文件
        doc.write(buf, 0, num); // 把文件資料寫出網絡緩衝區
        doc.flush(); // 重整緩衝區把資料寫往客戶端
        num = fos.read(buf); // 繼續從文件中讀取資料
      }
      fos.close();
      doc.close();
      socket.close();
    } catch (Exception ex) {
      //ex.printStackTrace();
    } finally {
    }
  }
}
}