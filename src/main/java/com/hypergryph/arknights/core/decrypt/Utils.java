/*     */ package BOOT-INF.classes.com.hypergryph.arknights.core.decrypt;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.util.Base64;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.spec.IvParameterSpec;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.springframework.util.DigestUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
/*     */   public static byte[] hexToByte(String hex) {
/*  22 */     int m = 0, n = 0;
/*  23 */     int byteLen = hex.length() / 2;
/*  24 */     byte[] ret = new byte[byteLen];
/*  25 */     for (int i = 0; i < byteLen; i++) {
/*  26 */       m = i * 2 + 1;
/*  27 */       n = m + 1;
/*  28 */       int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n)).intValue();
/*  29 */       ret[i] = Byte.valueOf((byte)intVal).byteValue();
/*     */     } 
/*  31 */     return ret;
/*     */   }
/*     */   
/*     */   public static String byteToHex(byte[] bytes) {
/*  35 */     String strHex = "";
/*  36 */     StringBuilder sb = new StringBuilder("");
/*  37 */     for (int n = 0; n < bytes.length; n++) {
/*  38 */       strHex = Integer.toHexString(bytes[n] & 0xFF);
/*  39 */       sb.append((strHex.length() == 1) ? ("0" + strHex) : strHex);
/*     */     } 
/*  41 */     return sb.toString().trim();
/*     */   }
/*     */   public static String aesEncrypt(String src, String key) {
/*  44 */     String encodingFormat = "UTF-8";
/*  45 */     String iv = "0102030405060708";
/*     */     try {
/*  47 */       Cipher cipher = null;
/*  48 */       cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
/*  49 */       byte[] raw = key.getBytes();
/*  50 */       SecretKeySpec secretKeySpec = new SecretKeySpec(raw, "AES");
/*  51 */       IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
/*  52 */       cipher.init(1, secretKeySpec, ivParameterSpec);
/*  53 */       byte[] encrypted = cipher.doFinal(src.getBytes(encodingFormat));
/*  54 */       return Base64.getEncoder().encodeToString(encrypted);
/*  55 */     } catch (Exception e) {
/*  56 */       System.out.println(e);
/*  57 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JSONObject BattleData_decrypt(String EncodeData, String login_time) {
/*  65 */     String LOG_TOKEN_KEY = "pM6Umv*^hVQuB6t&";
/*     */     
/*  67 */     byte[] BattleData = hexToByte(EncodeData.substring(0, EncodeData.length() - 32));
/*  68 */     SecretKeySpec Key = new SecretKeySpec(hexToByte(DigestUtils.md5DigestAsHex((LOG_TOKEN_KEY + login_time).getBytes())), "AES");
/*  69 */     IvParameterSpec Iv = new IvParameterSpec(hexToByte(EncodeData.substring(EncodeData.length() - 32)));
/*     */     try {
/*  71 */       Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
/*  72 */       cipher.init(2, Key, Iv);
/*  73 */       return JSONObject.parseObject(new String(cipher.doFinal(BattleData)));
/*  74 */     } catch (Exception e) {
/*  75 */       System.out.println(e);
/*  76 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static JSONObject BattleReplay_decrypt(String battleReplay) {
/*  81 */     byte[] data = Base64.getDecoder().decode(battleReplay);
/*  82 */     byte[] b = null;
/*     */     try {
/*  84 */       ByteArrayInputStream bis = new ByteArrayInputStream(data);
/*  85 */       ZipInputStream zip = new ZipInputStream(bis);
/*  86 */       while (zip.getNextEntry() != null) {
/*  87 */         byte[] buf = new byte[1024];
/*  88 */         int num = -1;
/*  89 */         ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*  90 */         while ((num = zip.read(buf, 0, buf.length)) != -1) {
/*  91 */           baos.write(buf, 0, num);
/*     */         }
/*  93 */         b = baos.toByteArray();
/*  94 */         baos.flush();
/*  95 */         baos.close();
/*     */       } 
/*  97 */       zip.close();
/*  98 */       bis.close();
/*  99 */       return JSONObject.parseObject(new String(b, "UTF-8"));
/* 100 */     } catch (Exception ex) {
/* 101 */       ex.printStackTrace();
/* 102 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\decrypt\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */