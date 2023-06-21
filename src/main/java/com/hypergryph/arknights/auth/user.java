/*     */ package BOOT-INF.classes.com.hypergryph.arknights.auth;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.function.httpClient;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.util.DigestUtils;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
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
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/user"})
/*     */ public class user
/*     */ {
/*  30 */   private static String Key = "IxMMveJRWsxStJgX";
/*     */ 
/*     */   
/*     */   @RequestMapping({"/info/v1/need_cloud_auth"})
/*     */   public JSONObject need_cloud_auth() {
/*  35 */     JSONObject result = new JSONObject(true);
/*  36 */     result.put("status", Integer.valueOf(0));
/*  37 */     result.put("msg", "faq");
/*  38 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/oauth2/v1/grant"})
/*     */   public JSONObject grant() {
/*  44 */     JSONObject result = new JSONObject(true);
/*  45 */     result.put("result", Integer.valueOf(0));
/*     */     
/*  47 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/v1/guestLogin"})
/*     */   public JSONObject GuestLogin() {
/*  54 */     JSONObject result = new JSONObject(true);
/*  55 */     result.put("result", Integer.valueOf(6));
/*  56 */     result.put("message", "单机版禁止游客登录");
/*  57 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/authenticateUserIdentity"})
/*     */   public JSONObject AuthenticateUserIdentity() {
/*  63 */     JSONObject result = new JSONObject(true);
/*  64 */     result.put("result", Integer.valueOf(0));
/*  65 */     result.put("message", "OK");
/*  66 */     result.put("isMinor", Boolean.valueOf(false));
/*  67 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/updateAgreement"})
/*     */   public JSONObject updateAgreement() {
/*  73 */     JSONObject result = new JSONObject(true);
/*  74 */     result.put("result", Integer.valueOf(0));
/*  75 */     result.put("message", "OK");
/*  76 */     result.put("isMinor", Boolean.valueOf(false));
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/checkIdCard"})
/*     */   public JSONObject CheckIdCard() {
/*  83 */     JSONObject result = new JSONObject(true);
/*  84 */     result.put("result", Integer.valueOf(0));
/*  85 */     result.put("message", "OK");
/*  86 */     result.put("isMinor", Boolean.valueOf(false));
/*  87 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"/sendSmsCode"})
/*     */   public JSONObject SendSmsCode(@RequestBody JSONObject JsonBody, HttpServletRequest request) {
/*  94 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  95 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /v1/sendSmsCode");
/*     */     
/*  97 */     String account = JsonBody.getString("account");
/*     */     
/*  99 */     if (!ArknightsApplication.serverConfig.getJSONObject("server").getBooleanValue("captcha")) {
/* 100 */       JSONObject jSONObject = new JSONObject(true);
/* 101 */       jSONObject.put("result", Integer.valueOf(4));
/* 102 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 106 */     if (httpClient.sentSmsCode(account).getIntValue("code") == 200) {
/* 107 */       JSONObject jSONObject = new JSONObject(true);
/* 108 */       jSONObject.put("result", Integer.valueOf(0));
/* 109 */       return jSONObject;
/*     */     } 
/*     */     
/* 112 */     JSONObject result = new JSONObject(true);
/* 113 */     result.put("result", Integer.valueOf(4));
/* 114 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/register"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject Register(@RequestBody JSONObject JsonBody, HttpServletRequest request) {
/* 121 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 122 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /user/register");
/*     */     
/* 124 */     String account = JsonBody.getString("account");
/* 125 */     String password = JsonBody.getString("password");
/* 126 */     String smsCode = JsonBody.getString("smsCode");
/*     */     
/* 128 */     String secret = DigestUtils.md5DigestAsHex((account + Key).getBytes());
/*     */ 
/*     */     
/* 131 */     if (userDao.queryAccountByPhone(account).size() != 0) {
/* 132 */       JSONObject jSONObject = new JSONObject(true);
/* 133 */       jSONObject.put("result", Integer.valueOf(5));
/* 134 */       jSONObject.put("errMsg", "该用户已存在，请确认注册信息");
/* 135 */       return jSONObject;
/*     */     } 
/*     */     
/* 138 */     if (ArknightsApplication.serverConfig.getJSONObject("server").getBooleanValue("captcha") && 
/* 139 */       httpClient.verifySmsCode(account, smsCode).getIntValue("code") == 503) {
/* 140 */       JSONObject jSONObject = new JSONObject(true);
/* 141 */       jSONObject.put("result", Integer.valueOf(5));
/* 142 */       jSONObject.put("errMsg", "验证码错误");
/* 143 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 148 */     if (userDao.RegisterAccount(account, DigestUtils.md5DigestAsHex((password + Key).getBytes()), secret) != 1) {
/* 149 */       JSONObject jSONObject = new JSONObject(true);
/* 150 */       jSONObject.put("result", Integer.valueOf(5));
/* 151 */       jSONObject.put("errMsg", "注册失败，未知错误");
/* 152 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 156 */     JSONObject result = new JSONObject(true);
/* 157 */     result.put("result", Integer.valueOf(0));
/* 158 */     result.put("uid", Integer.valueOf(0));
/* 159 */     result.put("token", secret);
/* 160 */     result.put("isAuthenticate", Boolean.valueOf(true));
/* 161 */     result.put("isMinor", Boolean.valueOf(false));
/* 162 */     result.put("needAuthenticate", Boolean.valueOf(false));
/* 163 */     result.put("isLatestUserAgreement", Boolean.valueOf(true));
/* 164 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/login"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject Login(@RequestBody JSONObject JsonBody, HttpServletRequest request) {
/* 170 */     String account = JsonBody.getString("account");
/* 171 */     String password = JsonBody.getString("password");
/*     */     
/* 173 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 174 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /user/login");
/*     */ 
/*     */     
/* 177 */     if (userDao.queryAccountByPhone(account).size() == 0) {
/* 178 */       JSONObject jSONObject = new JSONObject(true);
/* 179 */       jSONObject.put("result", Integer.valueOf(2));
/* 180 */       return jSONObject;
/*     */     } 
/*     */     
/* 183 */     List<Account> accounts = userDao.LoginAccount(account, DigestUtils.md5DigestAsHex((password + Key).getBytes()));
/*     */     
/* 185 */     if (accounts.size() != 1) {
/* 186 */       JSONObject jSONObject = new JSONObject(true);
/* 187 */       jSONObject.put("result", Integer.valueOf(1));
/* 188 */       return jSONObject;
/*     */     } 
/*     */     
/* 191 */     JSONObject result = new JSONObject(true);
/* 192 */     result.put("result", Integer.valueOf(0));
/* 193 */     result.put("uid", Long.valueOf(((Account)accounts.get(0)).getUid()));
/* 194 */     result.put("token", ((Account)accounts.get(0)).getSecret());
/* 195 */     result.put("isAuthenticate", Boolean.valueOf(true));
/* 196 */     result.put("isMinor", Boolean.valueOf(false));
/* 197 */     result.put("needAuthenticate", Boolean.valueOf(false));
/* 198 */     result.put("isLatestUserAgreement", Boolean.valueOf(true));
/* 199 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/auth"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject Auth(@RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 206 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 207 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /user/auth");
/*     */ 
/*     */     
/* 210 */     String secret = JsonBody.getString("token");
/*     */     
/* 212 */     if (secret == null && secret.length() < 0) {
/* 213 */       response.setStatus(400);
/* 214 */       JSONObject jSONObject = new JSONObject(true);
/* 215 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 216 */       jSONObject.put("error", "Bad Request");
/* 217 */       jSONObject.put("message", "invalid token");
/* 218 */       return jSONObject;
/*     */     } 
/*     */     
/* 221 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 222 */     if (Accounts.size() != 1) {
/* 223 */       JSONObject jSONObject = new JSONObject(true);
/* 224 */       jSONObject.put("result", Integer.valueOf(2));
/* 225 */       jSONObject.put("error", "无法查询到此账户");
/* 226 */       return jSONObject;
/*     */     } 
/*     */     
/* 229 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 231 */     JSONObject result = new JSONObject(true);
/* 232 */     result.put("uid", uid);
/* 233 */     result.put("isMinor", Boolean.valueOf(false));
/* 234 */     result.put("isAuthenticate", Boolean.valueOf(true));
/* 235 */     result.put("isGuest", Boolean.valueOf(false));
/* 236 */     result.put("needAuthenticate", Boolean.valueOf(false));
/* 237 */     result.put("isLatestUserAgreement", Boolean.valueOf(true));
/* 238 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\aut\\user.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */