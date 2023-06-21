/*    */ package BOOT-INF.classes.com.hypergryph.arknights;
/*    */ 
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import org.apache.catalina.connector.Connector;
/*    */ import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public class http
/*    */ {
/* 15 */   int ServerPort = ArknightsApplication.serverConfig.getJSONObject("server").getIntValue("https");
/* 16 */   int HttpPort = ArknightsApplication.serverConfig.getJSONObject("server").getIntValue("http");
/*    */ 
/*    */   
/*    */   @Bean
/*    */   public TomcatServletWebServerFactory servletContainer() {
/* 21 */     Object object = new Object(this);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 33 */     object.addAdditionalTomcatConnectors(new Connector[] { initiateHttpConnector() });
/* 34 */     return (TomcatServletWebServerFactory)object;
/*    */   }
/*    */   
/*    */   private Connector initiateHttpConnector() {
/* 38 */     Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
/* 39 */     connector.setScheme("http");
/* 40 */     connector.setPort(this.HttpPort);
/* 41 */     connector.setRedirectPort(this.ServerPort);
/* 42 */     connector.setSecure(true);
/* 43 */     return connector;
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\http.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */