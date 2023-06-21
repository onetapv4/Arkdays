/*    */ package BOOT-INF.classes.com.hypergryph.arknights;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import java.util.Map;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ public class network
/*    */ {
/*    */   @RequestMapping({"/"})
/*    */   public JSONObject network_config(HttpServletRequest request) {
/* 17 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 18 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /config/prod/official/network_config");
/*    */     
/* 20 */     JSONObject server_network = ArknightsApplication.serverConfig.getJSONObject("network");
/* 21 */     JSONObject jSONObject1 = new JSONObject(true);
/* 22 */     jSONObject1.put("sign", server_network.getString("sign"));
/* 23 */     JSONObject content = new JSONObject(true);
/* 24 */     JSONObject configs = server_network.getJSONObject("configs");
/* 25 */     content.put("configVer", server_network.getString("configVer"));
/* 26 */     content.put("funcVer", server_network.getString("funcVer"));
/* 27 */     for (Map.Entry entry : configs.entrySet()) {
/* 28 */       JSONObject funcNetwork = configs.getJSONObject(entry.getKey().toString()).getJSONObject("network");
/* 29 */       for (Map.Entry funcNetworkEntry : funcNetwork.entrySet()) {
/* 30 */         String value = funcNetwork.getString(funcNetworkEntry.getKey().toString());
/* 31 */         funcNetwork.put(funcNetworkEntry.getKey().toString(), value.replace("{server}", ArknightsApplication.serverConfig.getJSONObject("server").getString("url")));
/*    */       } 
/*    */     } 
/*    */     
/* 35 */     content.put("configs", configs);
/* 36 */     jSONObject1.put("content", content.toJSONString());
/* 37 */     return jSONObject1;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\network.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */