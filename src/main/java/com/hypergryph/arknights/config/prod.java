/*     */ package BOOT-INF.classes.com.hypergryph.arknights.config;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.function.randomPwd;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/config/prod"})
/*     */ public class prod
/*     */ {
/*     */   @RequestMapping({"/official/refresh_config"})
/*     */   public JSONObject RefreshConfig() {
/*  20 */     ArknightsApplication.reloadServerConfig();
/*  21 */     JSONObject result = new JSONObject(true);
/*  22 */     result.put("statusCode", Integer.valueOf(200));
/*  23 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/official/remote_config"})
/*     */   public JSONObject RemoteConfig(HttpServletRequest request) {
/*  32 */     return ArknightsApplication.serverConfig.getJSONObject("remote");
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/official/network_config"})
/*     */   public JSONObject NetworkConfig(HttpServletRequest request) {
/*  38 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  39 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /config/prod/official/network_config");
/*     */     
/*  41 */     JSONObject server_network = ArknightsApplication.serverConfig.getJSONObject("network");
/*  42 */     JSONObject network = new JSONObject(true);
/*  43 */     network.put("sign", server_network.getString("sign"));
/*  44 */     JSONObject content = new JSONObject(true);
/*  45 */     JSONObject configs = server_network.getJSONObject("configs");
/*  46 */     content.put("configVer", server_network.getString("configVer"));
/*  47 */     content.put("funcVer", server_network.getString("funcVer"));
/*     */     
/*  49 */     for (Map.Entry entry : configs.entrySet()) {
/*  50 */       JSONObject funcNetwork = configs.getJSONObject(entry.getKey().toString()).getJSONObject("network");
/*  51 */       for (Map.Entry funcNetworkEntry : funcNetwork.entrySet()) {
/*  52 */         String value = funcNetwork.getString(funcNetworkEntry.getKey().toString());
/*  53 */         funcNetwork.put(funcNetworkEntry.getKey().toString(), value.replace("{server}", ArknightsApplication.serverConfig.getJSONObject("server").getString("url")));
/*     */       } 
/*     */     } 
/*     */     
/*  57 */     content.put("configs", configs);
/*  58 */     network.put("content", content.toJSONString());
/*  59 */     return network;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/official/Android/version"})
/*     */   public JSONObject AndroidVersion(HttpServletRequest request) {
/*  67 */     JSONObject version = new JSONObject();
/*  68 */     version.put("resVersion", "22-02-18-07-51-58-" + randomPwd.randomHash(6));
/*  69 */     version.put("clientVersion", ArknightsApplication.serverConfig.getJSONObject("version").getJSONObject("android").getString("clientVersion"));
/*  70 */     return version;
/*     */   }
/*     */   
/*     */   @RequestMapping({"/official/IOS/version"})
/*     */   public JSONObject IosVersion(HttpServletRequest request) {
/*  75 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  76 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /config/prod/official/IOS/version");
/*  77 */     return ArknightsApplication.serverConfig.getJSONObject("version").getJSONObject("ios");
/*     */   }
/*     */   
/*     */   @RequestMapping({"/b/remote_config"})
/*     */   public JSONObject BRemoteConfig(HttpServletRequest request) {
/*  82 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  83 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /config/prod/b/remote_config");
/*     */     
/*  85 */     return ArknightsApplication.serverConfig.getJSONObject("remote");
/*     */   }
/*     */   
/*     */   @RequestMapping({"/b/network_config"})
/*     */   public JSONObject BNetworkConfig(HttpServletRequest request) {
/*  90 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  91 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /config/prod/b/network_config");
/*  92 */     return ArknightsApplication.serverConfig.getJSONObject("network");
/*     */   }
/*     */   
/*     */   @RequestMapping({"/b/Android/version"})
/*     */   public JSONObject BAndroidVersion(HttpServletRequest request) {
/*  97 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  98 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /config/prod/b/Android/version");
/*  99 */     return ArknightsApplication.serverConfig.getJSONObject("version").getJSONObject("android");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/announce_meta/Android/preannouncement.meta.json"})
/*     */   public JSONObject PreAnnouncement(HttpServletRequest request) {
/* 107 */     return ArknightsApplication.serverConfig.getJSONObject("announce").getJSONObject("preannouncement");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/announce_meta/Android/announcement.meta.json"})
/*     */   public JSONObject announcement(HttpServletRequest request) {
/* 114 */     return ArknightsApplication.serverConfig.getJSONObject("announce").getJSONObject("announcement");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/announce_meta/IOS/preannouncement.meta.json"})
/*     */   public JSONObject IOSPreAnnouncement(HttpServletRequest request) {
/* 121 */     return ArknightsApplication.serverConfig.getJSONObject("announce").getJSONObject("preannouncement");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/announce_meta/IOS/announcement.meta.json"})
/*     */   public JSONObject IOSannouncement(HttpServletRequest request) {
/* 128 */     return ArknightsApplication.serverConfig.getJSONObject("announce").getJSONObject("announcement");
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\config\prod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */