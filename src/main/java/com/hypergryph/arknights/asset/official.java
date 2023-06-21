/*     */ package BOOT-INF.classes.com.hypergryph.arknights.asset;
/*     */ 
/*     */ import cn.hutool.http.HttpUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.file.IOTools;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.springframework.core.io.FileSystemResource;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/assetbundle/official/{os}/assets"})
/*     */ public class official
/*     */ {
/*  40 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */   
/*     */   @RequestMapping({"/{assetsHash}/{fileName}"})
/*     */   public ResponseEntity<FileSystemResource> getFile(@PathVariable("os") String os, @PathVariable("assetsHash") String assetsHash, @PathVariable("fileName") String fileName, HttpServletResponse response, HttpServletRequest request) throws IOException {
/*  45 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*     */     
/*  47 */     Boolean redirect = Boolean.valueOf(ArknightsApplication.serverConfig.getJSONObject("assets").getBooleanValue("enableRedirect"));
/*  48 */     String version = ArknightsApplication.serverConfig.getJSONObject("version").getJSONObject("android").getString("resVersion");
/*     */     
/*  50 */     if (ArknightsApplication.loadedModDownloadList.contains(fileName)) {
/*  51 */       for (int i = 0; i < ArknightsApplication.loadedModDownloadList.size(); i++) {
/*  52 */         if (ArknightsApplication.loadedModDownloadList.getString(i).equals(fileName)) {
/*  53 */           File file1 = new File(ArknightsApplication.loadedModPathList.getString(i));
/*  54 */           if (file1.exists()) {
/*  55 */             return export(file1, assetsHash);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*  61 */     String filePath = System.getProperty("user.dir") + "/assets/" + version + "/direct/";
/*  62 */     if (redirect.booleanValue()) {
/*  63 */       filePath = System.getProperty("user.dir") + "/assets/" + version + "/redirect/";
/*  64 */       JSONArray localFiles = ArknightsApplication.serverConfig.getJSONObject("assets").getJSONArray("localFiles");
/*  65 */       if (!localFiles.contains(fileName)) {
/*  66 */         response.sendRedirect("https://ak.hycdn.cn/assetbundle/official/Android/assets/" + version + "/" + fileName);
/*  67 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     File file = new File(filePath, fileName);
/*  72 */     if (file.exists()) {
/*  73 */       return export(file, assetsHash);
/*     */     }
/*  75 */     LOGGER.warn("正在下载 " + version + "/" + fileName);
/*  76 */     HttpUtil.downloadFile("https://ak.hycdn.cn/assetbundle/official/Android/assets/" + version + "/" + fileName, filePath + fileName);
/*     */     
/*  78 */     file = new File(filePath, fileName);
/*  79 */     if (file.exists()) {
/*  80 */       LOGGER.info("[/" + clientIp + "] /" + version + "/" + fileName);
/*  81 */       return export(file, assetsHash);
/*     */     } 
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String downLoadFromUrl(String urlStr, String fileName, String savePath) {
/*     */     try {
/*  91 */       URL url = new URL(urlStr);
/*  92 */       HttpURLConnection conn = (HttpURLConnection)url.openConnection();
/*     */       
/*  94 */       conn.setConnectTimeout(3000);
/*     */       
/*  96 */       conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
/*     */ 
/*     */       
/*  99 */       InputStream inputStream = conn.getInputStream();
/*     */       
/* 101 */       byte[] getData = readInputStream(inputStream);
/*     */ 
/*     */       
/* 104 */       File saveDir = new File(savePath);
/* 105 */       if (!saveDir.exists()) {
/* 106 */         saveDir.mkdir();
/*     */       }
/*     */       
/* 109 */       File dir = new File(saveDir + File.separator);
/* 110 */       if (!dir.exists()) {
/* 111 */         dir.mkdirs();
/*     */       }
/*     */       
/* 114 */       File file = new File(dir, fileName);
/* 115 */       FileOutputStream fos = new FileOutputStream(file);
/* 116 */       fos.write(getData);
/* 117 */       if (fos != null) {
/* 118 */         fos.close();
/*     */       }
/* 120 */       if (inputStream != null) {
/* 121 */         inputStream.close();
/*     */       }
/*     */       
/* 124 */       return saveDir + File.separator + fileName;
/* 125 */     } catch (Exception e) {
/* 126 */       e.printStackTrace();
/*     */       
/* 128 */       return "";
/*     */     } 
/*     */   }
/*     */   
/*     */   public static byte[] readInputStream(InputStream inputStream) throws IOException {
/* 133 */     byte[] buffer = new byte[1024];
/* 134 */     int len = 0;
/* 135 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/* 136 */     while ((len = inputStream.read(buffer)) != -1) {
/* 137 */       bos.write(buffer, 0, len);
/*     */     }
/* 139 */     bos.close();
/* 140 */     return bos.toByteArray();
/*     */   }
/*     */   
/*     */   public ResponseEntity<FileSystemResource> export(File file, String assetsHash) {
/* 144 */     if (file == null) {
/* 145 */       return null;
/*     */     }
/* 147 */     HttpHeaders headers = new HttpHeaders();
/* 148 */     headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
/* 149 */     headers.add("Content-Disposition", "attachment; filename=" + file.getName());
/* 150 */     headers.add("Pragma", "no-cache");
/* 151 */     headers.add("Expires", "0");
/* 152 */     headers.add("Last-Modified", (new Date()).toString());
/* 153 */     headers.add("ETag", String.valueOf(System.currentTimeMillis()));
/*     */     
/* 155 */     if (file.getName().equals("hot_update_list.json")) {
/* 156 */       JSONObject hot_update_list = IOTools.ReadJsonFile(file.getPath());
/*     */       
/* 158 */       hot_update_list.put("versionId", assetsHash);
/*     */       
/* 160 */       JSONArray newAbInfos = new JSONArray();
/* 161 */       Iterator<Object> iterator = hot_update_list.getJSONArray("abInfos").iterator();
/*     */       
/* 163 */       while (iterator.hasNext()) {
/* 164 */         JSONObject abInfo = (JSONObject)iterator.next();
/* 165 */         if (abInfo.getString("hash").length() == 24) {
/* 166 */           abInfo.put("hash", assetsHash);
/*     */         }
/* 168 */         if (!ArknightsApplication.loadedModNameList.contains(abInfo.getString("name"))) {
/* 169 */           newAbInfos.add(abInfo);
/*     */         }
/*     */       } 
/*     */       
/* 173 */       for (int i = 0; i < ArknightsApplication.loadedModList.size(); i++) {
/* 174 */         newAbInfos.add(ArknightsApplication.loadedModList.getJSONObject(i));
/*     */       }
/*     */       
/* 177 */       hot_update_list.put("abInfos", newAbInfos);
/* 178 */       IOTools.SaveJsonFile(System.getProperty("user.dir") + "/cache/hot_update_list.json", hot_update_list);
/* 179 */       file = new File(System.getProperty("user.dir") + "/cache/hot_update_list.json");
/*     */     } 
/* 181 */     return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(headers)).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\asset\official.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */