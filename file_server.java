import java.io. * ;
import java.net. * ;
 
class file_server {
  static ServerSocket ss;
  static Socket server;
    public static void main(String[] args) throws IOException { //參數為要儲存的檔案名稱
      ss = new ServerSocket(8081);
      server = ss.accept(); // 增加網絡輸出流並提供資料包裝器
      C_getFile("output.jpg");
   }
    private static void C_getFile(String strTemp) { //使用本地文件系統接受網絡資料並存為新文件
        try {
            File file = new File(strTemp); //如果文件已經存在，先刪除
           if (file.exists()) file.delete(); //   for (int i = 0; i < 10000; i++) {}
           file.createNewFile();
           RandomAccessFile raf = new RandomAccessFile(file, "rw"); // 通過Socket連接文件服務器
           // Socket server = new Socket("111.255.208.179", 8081); //增加網絡接受流接受服務器文件資料 
           InputStream netIn = server.getInputStream();
            InputStream in =new BufferedInputStream(netIn); //增加緩衝區緩衝網絡資料
           byte[] buf = new byte[512];
           int num = in.read(buf);
           System.out.println("接受文件中:" + (String) file.getName()); // public Socket accept() throws
           while (num !=  - 1) { //是否讀完所有資料
                raf.write(buf, 0, num); //將資料寫往文件
             raf.skipBytes(num); //順序寫文件字元
              num = in.read(buf); //繼續從網絡中讀取文件
         } 
           in .close();
           raf.close();
           ss.close();
      } catch(Exception ex) {
         ex.printStackTrace();
      } finally {}
 }
}