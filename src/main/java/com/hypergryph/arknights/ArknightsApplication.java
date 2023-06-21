/*     */ package BOOT-INF.classes.com.hypergryph.arknights;
/*     */ 
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.command.CommandManager;
/*     */ import com.hypergryph.arknights.command.ICommandSender;
/*     */ import com.hypergryph.arknights.console;
/*     */ import com.hypergryph.arknights.core.dao.mailDao;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.file.IOTools;
/*     */ import com.hypergryph.arknights.core.function.randomPwd;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.springframework.boot.Banner;
/*     */ import org.springframework.boot.SpringApplication;
/*     */ import org.springframework.boot.autoconfigure.SpringBootApplication;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.jdbc.datasource.DriverManagerDataSource;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
/*     */ public class ArknightsApplication {
/*  38 */   public static final Logger LOGGER = LogManager.getLogger();
/*  39 */   public static JdbcTemplate jdbcTemplate = null;
/*  40 */   public static JSONObject serverConfig = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/config.json");
/*  41 */   public static boolean enableServer = serverConfig.getJSONObject("server").getBooleanValue("enableServer");
/*  42 */   public static JSONObject DefaultSyncData = new JSONObject();
/*  43 */   public static JSONObject characterJson = new JSONObject();
/*  44 */   public static JSONObject roguelikeTable = new JSONObject();
/*  45 */   public static JSONObject stageTable = new JSONObject();
/*  46 */   public static JSONObject itemTable = new JSONObject();
/*  47 */   public static JSONObject mainStage = new JSONObject();
/*  48 */   public static JSONObject normalGachaData = new JSONObject();
/*  49 */   public static JSONObject uniequipTable = new JSONObject();
/*  50 */   public static JSONObject skinGoodList = new JSONObject();
/*  51 */   public static JSONObject skinTable = new JSONObject();
/*  52 */   public static JSONObject charwordTable = new JSONObject();
/*  53 */   public static JSONObject CrisisData = new JSONObject();
/*  54 */   public static JSONObject CashGoodList = new JSONObject();
/*  55 */   public static JSONObject GPGoodList = new JSONObject();
/*  56 */   public static JSONObject LowGoodList = new JSONObject();
/*  57 */   public static JSONObject HighGoodList = new JSONObject();
/*  58 */   public static JSONObject ExtraGoodList = new JSONObject();
/*  59 */   public static JSONObject LMTGSGoodList = new JSONObject();
/*  60 */   public static JSONObject EPGSGoodList = new JSONObject();
/*  61 */   public static JSONObject RepGoodList = new JSONObject();
/*  62 */   public static JSONObject FurniGoodList = new JSONObject();
/*  63 */   public static JSONObject SocialGoodList = new JSONObject();
/*  64 */   public static JSONObject AllProductList = new JSONObject();
/*  65 */   public static JSONObject unlockActivity = new JSONObject();
/*  66 */   public static JSONObject roguelike = new JSONObject();
/*  67 */   public static JSONArray loadedModNameList = new JSONArray();
/*  68 */   public static JSONArray loadedModPathList = new JSONArray();
/*  69 */   public static JSONArray loadedModDownloadList = new JSONArray();
/*  70 */   public static JSONArray loadedModList = new JSONArray();
/*     */   
/*  72 */   public static JSONObject buildingData = new JSONObject();
/*     */   
/*  74 */   public static CommandManager ConsoleCommandManager = new CommandManager();
/*     */ 
/*     */   
/*     */   public static ICommandSender Sender = () -> "Console";
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/*  82 */     String host = serverConfig.getJSONObject("database").getString("host");
/*  83 */     String port = serverConfig.getJSONObject("database").getString("port");
/*  84 */     String dbname = serverConfig.getJSONObject("database").getString("dbname");
/*  85 */     String username = serverConfig.getJSONObject("database").getString("username");
/*  86 */     String password = serverConfig.getJSONObject("database").getString("password");
/*  87 */     String extra = serverConfig.getJSONObject("database").getString("extra");
/*     */ 
/*     */     
/*  90 */     DriverManagerDataSource dataSource = new DriverManagerDataSource();
/*  91 */     dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
/*  92 */     dataSource.setUrl("jdbc:mysql://" + host + ":" + port + "/" + dbname + "?" + extra);
/*  93 */     dataSource.setUsername(username);
/*  94 */     dataSource.setPassword(password);
/*  95 */     jdbcTemplate = new JdbcTemplate((DataSource)dataSource);
/*     */ 
/*     */     
/*  98 */     SpringApplication springApplication = new SpringApplication(new Class[] { com.hypergryph.arknights.ArknightsApplication.class });
/*  99 */     springApplication.setBannerMode(Banner.Mode.OFF);
/*     */     
/* 101 */     String[] disabledCommands = { "--server.port=" + serverConfig.getJSONObject("server").getString("https"), "--spring.profiles.active=default" };
/*     */ 
/*     */     
/* 104 */     String[] fullArgs = StringUtils.concatenateStringArrays(args, disabledCommands);
/* 105 */     springApplication.run(fullArgs);
/*     */     
/* 107 */     reloadServerConfig();
/*     */     
/* 109 */     String MysqlVersion = null;
/* 110 */     LOGGER.info("检测数据库版本中...");
/*     */     try {
/* 112 */       MysqlVersion = userDao.queryMysqlVersion();
/* 113 */     } catch (Exception e) {
/* 114 */       LOGGER.error("无法连接至Mysql数据库");
/* 115 */       System.exit(0);
/*     */     } 
/*     */     
/* 118 */     if (Integer.valueOf(MysqlVersion.substring(0, 1)).intValue() < 8) {
/* 119 */       LOGGER.error("Mysql版本需要 >= 8.0");
/* 120 */       LOGGER.error("请升级后重试");
/* 121 */       System.exit(0);
/*     */     } 
/*     */     
/* 124 */     LOGGER.info("数据库版本 " + MysqlVersion);
/* 125 */     LOGGER.info("服务端版本 1.9.4 Beta 3");
/* 126 */     LOGGER.info("客户端版本 1.7.51");
/* 127 */     LOGGER.info("构建时间 2022年03月15日14时18分");
/* 128 */     if (serverConfig.getJSONObject("server").getString("GMKey") == null) {
/* 129 */       serverConfig.getJSONObject("server").put("GMKey", randomPwd.getRandomPwd(64));
/* 130 */       IOTools.SaveJsonFile(System.getProperty("user.dir") + "/config.json", serverConfig);
/* 131 */       LOGGER.info("已生成新的随机管理员密钥");
/*     */     } 
/* 133 */     LOGGER.info("管理员密钥 " + serverConfig.getJSONObject("server").getString("GMKey"));
/*     */     
/* 135 */     if (!userDao.tableExists("account").booleanValue()) {
/* 136 */       userDao.insertTable();
/* 137 */       LOGGER.info("检测到玩家数据库不存在，已自动生成");
/*     */     } 
/*     */     
/* 140 */     if (!userDao.tableExists("mail").booleanValue()) {
/* 141 */       mailDao.insertTable();
/* 142 */       LOGGER.info("检测到邮件数据库不存在，已自动生成");
/*     */     } 
/*     */     
/* 145 */     getTimestamp();
/*     */ 
/*     */     
/* 148 */     LOGGER.info("启动完成! 如果需要获取帮助,请输入 \"help\"");
/*     */ 
/*     */     
/* 151 */     (new console()).start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void LoadMods() {
/* 157 */     loadedModNameList = new JSONArray();
/* 158 */     loadedModPathList = new JSONArray();
/* 159 */     loadedModDownloadList = new JSONArray();
/* 160 */     loadedModList = new JSONArray();
/*     */     
/* 162 */     JSONArray fileList = new JSONArray();
/*     */     
/* 164 */     searchDirectoryFile(new File(System.getProperty("user.dir") + "/mods"), fileList);
/*     */     
/* 166 */     for (Object filePath : fileList) {
/*     */       
/* 168 */       File modFile = new File(filePath.toString());
/*     */       
/* 170 */       ZipFile zipFile = null;
/*     */       try {
/* 172 */         zipFile = new ZipFile(modFile);
/*     */         
/* 174 */         if (zipFile.size() == 0)
/* 175 */           continue;  Enumeration<? extends ZipEntry> entries = zipFile.entries();
/* 176 */         while (entries.hasMoreElements()) {
/*     */           
/* 178 */           ZipEntry entry = entries.nextElement();
/* 179 */           if (!entry.isDirectory() && 
/* 180 */             entry.getName().indexOf(".ab") != -1) {
/* 181 */             String modName = entry.getName();
/* 182 */             if (loadedModNameList.contains(modName)) {
/* 183 */               LOGGER.error(filePath + " 与已加载的Mod冲突，详细：");
/* 184 */               LOGGER.error(modName);
/*     */               
/*     */               continue;
/*     */             } 
/* 188 */             InputStream modInputStream = zipFile.getInputStream(entry);
/*     */             
/* 190 */             byte[] buff = new byte[1024];
/*     */             
/* 192 */             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); int num;
/* 193 */             while ((num = modInputStream.read(buff, 0, buff.length)) != -1) {
/* 194 */               byteArrayOutputStream.write(buff, 0, num);
/*     */             }
/* 196 */             byte[] modBuff = byteArrayOutputStream.toByteArray();
/* 197 */             byteArrayOutputStream.flush();
/* 198 */             byteArrayOutputStream.close();
/* 199 */             long totalSize = modFile.length();
/* 200 */             long abSize = modBuff.length;
/* 201 */             String modMd5 = DigestUtils.md5DigestAsHex(modBuff);
/*     */             
/* 203 */             JSONObject abInfo = new JSONObject(true);
/* 204 */             abInfo.put("name", modName);
/* 205 */             abInfo.put("hash", modMd5);
/* 206 */             abInfo.put("md5", modMd5);
/* 207 */             abInfo.put("totalSize", Long.valueOf(totalSize));
/* 208 */             abInfo.put("abSize", Long.valueOf(abSize));
/* 209 */             LOGGER.info(modName + " 已加载");
/* 210 */             loadedModList.add(abInfo);
/* 211 */             loadedModPathList.add(modFile.getPath().replace("\\", "/"));
/* 212 */             loadedModNameList.add(modName);
/* 213 */             modName = modName.replace("/", "_");
/* 214 */             modName = modName.replace("#", "__");
/* 215 */             modName = modName.replace(".ab", ".dat");
/* 216 */             loadedModDownloadList.add(modName);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 221 */         zipFile.close();
/* 222 */       } catch (IOException e) {
/* 223 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void searchDirectoryFile(File directoryPath, JSONArray fileList) {
/* 229 */     File[] directory = directoryPath.listFiles();
/* 230 */     for (File file : directory) {
/* 231 */       if (file.isDirectory())
/* 232 */         searchDirectoryFile(file, fileList); 
/* 233 */       if (file.isFile())
/* 234 */         fileList.add(file.getPath().replace("\\", "/")); 
/*     */     } 
/*     */   }
/*     */   public static String getIpAddr(HttpServletRequest request) {
/* 238 */     String ipAddress = null;
/*     */     try {
/* 240 */       ipAddress = request.getHeader("x-forwarded-for");
/* 241 */       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
/* 242 */         ipAddress = request.getHeader("Proxy-Client-IP");
/*     */       }
/* 244 */       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
/* 245 */         ipAddress = request.getHeader("WL-Proxy-Client-IP");
/*     */       }
/* 247 */       if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
/* 248 */         ipAddress = request.getRemoteAddr();
/* 249 */         if (ipAddress.equals("127.0.0.1")) {
/*     */           
/*     */           try {
/* 252 */             ipAddress = InetAddress.getLocalHost().getHostAddress();
/* 253 */           } catch (UnknownHostException e) {
/* 254 */             e.printStackTrace();
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 259 */       if (ipAddress != null) {
/* 260 */         if (ipAddress.contains(",")) {
/* 261 */           return ipAddress.split(",")[0];
/*     */         }
/* 263 */         return ipAddress;
/*     */       } 
/*     */       
/* 266 */       return "";
/*     */     }
/* 268 */     catch (Exception e) {
/* 269 */       e.printStackTrace();
/* 270 */       return "";
/*     */     } 
/*     */   }
/*     */   public static long getTimestamp() {
/* 274 */     long ts = serverConfig.getJSONObject("timestamp").getLongValue(DateUtil.dayOfWeekEnum((Date)DateUtil.date()).toString().toLowerCase());
/* 275 */     if (ts == -1L) {
/* 276 */       ts = (new Date()).getTime() / 1000L;
/*     */     }
/* 278 */     return ts;
/*     */   }
/*     */   public static void reloadServerConfig() {
/* 281 */     long startTime = System.currentTimeMillis();
/* 282 */     LOGGER.info("载入服务器配置...");
/* 283 */     serverConfig = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/config.json");
/* 284 */     enableServer = serverConfig.getJSONObject("server").getBooleanValue("enableServer");
/* 285 */     LOGGER.info("载入游戏数据...");
/* 286 */     DefaultSyncData = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/defaultSyncData.json");
/* 287 */     characterJson = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/character_table.json");
/* 288 */     roguelikeTable = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/roguelike_topic_table.json");
/* 289 */     stageTable = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/stage_table.json").getJSONObject("stages");
/* 290 */     itemTable = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/item_table.json").getJSONObject("items");
/* 291 */     mainStage = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/battle/stage.json").getJSONObject("MainStage");
/* 292 */     normalGachaData = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/normalGacha.json");
/* 293 */     uniequipTable = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/uniequip_table.json").getJSONObject("equipDict");
/* 294 */     skinGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/SkinGoodList.json");
/* 295 */     skinTable = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/skin_table.json").getJSONObject("charSkins");
/* 296 */     charwordTable = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/charword_table.json");
/* 297 */     CashGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/CashGoodList.json");
/* 298 */     GPGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/GPGoodList.json");
/* 299 */     LowGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/LowGoodList.json");
/* 300 */     HighGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/HighGoodList.json");
/* 301 */     ExtraGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/ExtraGoodList.json");
/* 302 */     LMTGSGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/LMTGSGoodList.json");
/* 303 */     EPGSGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/EPGSGoodList.json");
/* 304 */     RepGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/RepGoodList.json");
/* 305 */     FurniGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/FurniGoodList.json");
/* 306 */     SocialGoodList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/SocialGoodList.json");
/* 307 */     AllProductList = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/shop/AllProductList.json");
/* 308 */     unlockActivity = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/unlockActivity.json");
/* 309 */     roguelike = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/roguelike.json");
/*     */     
/* 311 */     CrisisData = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/battle/crisis.json");
/* 312 */     buildingData = IOTools.ReadJsonFile(System.getProperty("user.dir") + "/data/excel/building_data.json").getJSONObject("workshopFormulas");
/*     */     
/* 314 */     LOGGER.info("载入游戏Mod");
/* 315 */     LoadMods();
/*     */     
/* 317 */     long endTime = System.currentTimeMillis();
/* 318 */     LOGGER.info("载入完成，耗时：" + (endTime - startTime) + "ms");
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\ArknightsApplication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */