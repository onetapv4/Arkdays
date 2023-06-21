/*    */ package BOOT-INF.classes.com.hypergryph.arknights.core.file;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileReader;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStreamReader;
/*    */ import java.io.OutputStreamWriter;
/*    */ import java.io.Reader;
/*    */ 
/*    */ public class IOTools {
/*    */   public static String ReadNormalFile(String FilePath) {
/*    */     try {
/* 15 */       File jsonFile = new File(FilePath);
/* 16 */       FileReader fileReader = new FileReader(jsonFile);
/* 17 */       Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
/* 18 */       StringBuffer Buffer = new StringBuffer(); int ReadChar;
/* 19 */       while ((ReadChar = reader.read()) != -1) {
/* 20 */         Buffer.append((char)ReadChar);
/*    */       }
/* 22 */       fileReader.close();
/* 23 */       reader.close();
/* 24 */       return Buffer.toString();
/* 25 */     } catch (IOException e) {
/* 26 */       e.printStackTrace();
/* 27 */       return null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static JSONObject ReadJsonFile(String JsonFilePath) {
/*    */     try {
/* 34 */       File jsonFile = new File(JsonFilePath);
/* 35 */       FileReader fileReader = new FileReader(jsonFile);
/* 36 */       Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "UTF-8");
/* 37 */       StringBuffer Buffer = new StringBuffer(); int ReadChar;
/* 38 */       while ((ReadChar = reader.read()) != -1) {
/* 39 */         Buffer.append((char)ReadChar);
/*    */       }
/* 41 */       fileReader.close();
/* 42 */       reader.close();
/* 43 */       return JSONObject.parseObject(Buffer.toString(), new Feature[] { Feature.OrderedField });
/* 44 */     } catch (IOException e) {
/* 45 */       e.printStackTrace();
/* 46 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Boolean SaveJsonFile(String JsonFilePath, JSONObject JsonData) {
/*    */     try {
/* 52 */       OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(JsonFilePath), "UTF-8");
/* 53 */       osw.write(JSON.toJSONString(JsonData, new SerializerFeature[] { SerializerFeature.PrettyFormat, SerializerFeature.WriteMapNullValue }));
/* 54 */       osw.flush();
/* 55 */       osw.close();
/* 56 */       return Boolean.valueOf(true);
/* 57 */     } catch (IOException e) {
/* 58 */       e.printStackTrace();
/* 59 */       return Boolean.valueOf(false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\file\IOTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */