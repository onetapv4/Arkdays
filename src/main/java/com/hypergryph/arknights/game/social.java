/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import com.hypergryph.arknights.core.pojo.SearchUidList;
/*     */ import com.hypergryph.arknights.core.pojo.UserInfo;
/*     */ import java.util.List;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/social"})
/*     */ public class social
/*     */ {
/*  27 */   public JSONObject teamV2 = JSONObject.parseObject("{\"abyssal\":0,\"action4\":0,\"blacksteel\":0,\"bolivar\":0,\"chiave\":0,\"columbia\":0,\"egir\":0,\"followers\":0,\"glasgow\":0,\"higashi\":0,\"iberia\":0,\"karlan\":0,\"kazimierz\":0,\"kjerag\":0,\"laterano\":0,\"lee\":0,\"leithanien\":0,\"lgd\":0,\"lungmen\":0,\"minos\":0,\"penguin\":0,\"reserve1\":0,\"reserve4\":0,\"reserve6\":0,\"rhine\":0,\"rhodes\":0,\"rim\":0,\"sami\":0,\"sargon\":0,\"siesta\":0,\"siracusa\":0,\"student\":0,\"sui\":0,\"sweep\":0,\"ursus\":0,\"victoria\":0,\"yan\":0}\n");
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/setAssistCharList"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject setAssistCharList(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/*  32 */     JSONArray assistCharList = JsonBody.getJSONArray("assistCharList");
/*     */     
/*  34 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  35 */     if (Accounts.size() != 1) {
/*  36 */       JSONObject jSONObject = new JSONObject(true);
/*  37 */       jSONObject.put("result", Integer.valueOf(2));
/*  38 */       jSONObject.put("error", "无法查询到此账户");
/*  39 */       return jSONObject;
/*     */     } 
/*     */     
/*  42 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  44 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  45 */       response.setStatus(500);
/*  46 */       JSONObject jSONObject = new JSONObject(true);
/*  47 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  48 */       jSONObject.put("error", "Bad Request");
/*  49 */       jSONObject.put("message", "error");
/*  50 */       return jSONObject;
/*     */     } 
/*     */     
/*  53 */     JSONObject assistChar = new JSONObject();
/*  54 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/*  56 */     UserSyncData.getJSONObject("social").put("assistCharList", assistCharList);
/*     */     
/*  58 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/*  60 */     for (int i = 0; i < assistCharList.size(); i++) {
/*  61 */       if (assistCharList.getJSONObject(i) != null) {
/*  62 */         JSONObject charInfo = assistCharList.getJSONObject(i);
/*  63 */         String charInstId = charInfo.getString("charInstId");
/*  64 */         String charId = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(charInstId).getString("charId");
/*  65 */         charInfo.put("charId", charId);
/*  66 */         String profession = ArknightsApplication.characterJson.getJSONObject(charId).getString("profession");
/*     */         
/*  68 */         if (!assistChar.containsKey(profession)) assistChar.put(profession, new JSONArray());
/*     */         
/*  70 */         assistChar.getJSONArray(profession).add(charInfo);
/*     */       } 
/*     */     } 
/*     */     
/*  74 */     userDao.setAssistCharListData(uid, assistChar);
/*     */     
/*  76 */     JSONObject result = new JSONObject(true);
/*  77 */     JSONObject playerDataDelta = new JSONObject(true);
/*  78 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  79 */     JSONObject modified = new JSONObject(true);
/*  80 */     modified.put("social", UserSyncData.getJSONObject("social"));
/*  81 */     playerDataDelta.put("modified", modified);
/*  82 */     result.put("playerDataDelta", playerDataDelta);
/*  83 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getSortListInfo"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getSortListInfo(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/*  91 */     int type = JsonBody.getIntValue("type");
/*     */     
/*  93 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  94 */     if (Accounts.size() != 1) {
/*  95 */       JSONObject jSONObject = new JSONObject(true);
/*  96 */       jSONObject.put("result", Integer.valueOf(2));
/*  97 */       jSONObject.put("error", "无法查询到此账户");
/*  98 */       return jSONObject;
/*     */     } 
/*     */     
/* 101 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 103 */     JSONArray resultList = new JSONArray();
/*     */     
/* 105 */     if (type == 0) {
/*     */       
/* 107 */       String nickNumber = JsonBody.getJSONObject("param").getString("nickNumber");
/* 108 */       String nickName = JsonBody.getJSONObject("param").getString("nickName");
/*     */       
/* 110 */       List<SearchUidList> search = userDao.searchPlayer("%" + nickName + "%", "%" + nickNumber + "%");
/*     */       
/* 112 */       for (int i = 0; i < search.size(); i++) {
/* 113 */         if (((SearchUidList)search.get(i)).getUid() != uid.longValue()) {
/* 114 */           JSONObject FriendInfo = new JSONObject(true);
/* 115 */           FriendInfo.put("level", Long.valueOf(((SearchUidList)search.get(i)).getLevel()));
/* 116 */           FriendInfo.put("uid", Long.valueOf(((SearchUidList)search.get(i)).getUid()));
/* 117 */           resultList.add(FriendInfo);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     if (type == 1) {
/* 123 */       JSONArray FriendList = JSONObject.parseObject(((Account)Accounts.get(0)).getFriend()).getJSONArray("list");
/*     */       
/* 125 */       for (int i = 0; i < FriendList.size(); i++) {
/* 126 */         int FriendUid = FriendList.getJSONObject(i).getIntValue("uid");
/* 127 */         List<UserInfo> userInfo = userDao.queryUserInfo(FriendUid);
/* 128 */         JSONObject userStatus = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getStatus());
/* 129 */         JSONObject FriendInfo = new JSONObject(true);
/* 130 */         FriendInfo.put("level", Integer.valueOf(userStatus.getIntValue("level")));
/* 131 */         FriendInfo.put("infoShare", Integer.valueOf(0));
/* 132 */         FriendInfo.put("uid", Integer.valueOf(FriendUid));
/* 133 */         resultList.add(FriendInfo);
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (type == 2) {
/*     */       
/* 139 */       JSONArray FriendRequest = JSONObject.parseObject(((Account)Accounts.get(0)).getFriend()).getJSONArray("request");
/*     */       
/* 141 */       resultList = FriendRequest;
/*     */     } 
/* 143 */     JSONObject result = new JSONObject(true);
/* 144 */     JSONObject playerDataDelta = new JSONObject(true);
/* 145 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 146 */     JSONObject modified = new JSONObject(true);
/* 147 */     playerDataDelta.put("modified", modified);
/* 148 */     result.put("playerDataDelta", playerDataDelta);
/* 149 */     result.put("result", resultList);
/* 150 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getFriendList"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getFriendList(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 158 */     JSONArray idList = JsonBody.getJSONArray("idList");
/*     */     
/* 160 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 161 */     if (Accounts.size() != 1) {
/* 162 */       JSONObject jSONObject = new JSONObject(true);
/* 163 */       jSONObject.put("result", Integer.valueOf(2));
/* 164 */       jSONObject.put("error", "无法查询到此账户");
/* 165 */       return jSONObject;
/*     */     } 
/*     */     
/* 168 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 169 */       response.setStatus(500);
/* 170 */       JSONObject jSONObject = new JSONObject(true);
/* 171 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 172 */       jSONObject.put("error", "Bad Request");
/* 173 */       jSONObject.put("message", "error");
/* 174 */       return jSONObject;
/*     */     } 
/*     */     
/* 177 */     JSONArray friends = new JSONArray();
/*     */     
/* 179 */     JSONArray board = new JSONArray();
/*     */     
/* 181 */     JSONObject medalBoard = new JSONObject(true);
/* 182 */     medalBoard.put("type", "EMPTY");
/* 183 */     medalBoard.put("template", null);
/* 184 */     medalBoard.put("custom", null);
/*     */     
/* 186 */     JSONArray friendAlias = new JSONArray();
/*     */     
/* 188 */     for (int i = 0; i < idList.size(); i++) {
/*     */       
/* 190 */       Long FriendUid = Long.valueOf(idList.getLongValue(i));
/*     */       
/* 192 */       List<UserInfo> userInfo = userDao.queryUserInfo(FriendUid.longValue());
/*     */       
/* 194 */       JSONArray userAssistCharList = JSONArray.parseArray(((UserInfo)userInfo.get(0)).getSocialAssistCharList());
/* 195 */       JSONObject userStatus = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getStatus());
/* 196 */       JSONObject chars = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getChars());
/* 197 */       JSONObject UserFriend = JSONObject.parseObject(((Account)Accounts.get(0)).getFriend());
/*     */       
/* 199 */       JSONObject FriendInfo = new JSONObject(true);
/* 200 */       JSONArray assistCharList = new JSONArray();
/*     */       
/* 202 */       for (int n = 0; n < userAssistCharList.size(); n++) {
/* 203 */         if (userAssistCharList.getJSONObject(n) != null) {
/* 204 */           String charInstId = String.valueOf(userAssistCharList.getJSONObject(n).getIntValue("charInstId"));
/* 205 */           JSONObject chardata = chars.getJSONObject(charInstId);
/* 206 */           chardata.put("skillIndex", Integer.valueOf(userAssistCharList.getJSONObject(n).getIntValue("skillIndex")));
/* 207 */           assistCharList.add(chardata);
/*     */         } else {
/* 209 */           assistCharList.add(null);
/*     */         } 
/*     */       } 
/* 212 */       FriendInfo.put("assistCharList", assistCharList);
/* 213 */       FriendInfo.put("avatarId", Integer.valueOf(userStatus.getIntValue("avatarId")));
/* 214 */       FriendInfo.put("uid", FriendUid);
/* 215 */       FriendInfo.put("board", board);
/* 216 */       FriendInfo.put("medalBoard", medalBoard);
/* 217 */       FriendInfo.put("charCnt", Integer.valueOf(chars.size()));
/* 218 */       FriendInfo.put("friendNumLimit", Integer.valueOf(50));
/* 219 */       FriendInfo.put("furnCnt", Integer.valueOf(0));
/* 220 */       FriendInfo.put("infoShare", Integer.valueOf(0));
/* 221 */       FriendInfo.put("lastOnlineTime", Integer.valueOf(userStatus.getIntValue("lastOnlineTs")));
/* 222 */       FriendInfo.put("level", Integer.valueOf(userStatus.getIntValue("level")));
/* 223 */       FriendInfo.put("mainStageProgress", userStatus.getString("mainStageProgress"));
/* 224 */       FriendInfo.put("nickName", userStatus.getString("nickName"));
/* 225 */       FriendInfo.put("nickNumber", userStatus.getString("nickNumber"));
/* 226 */       FriendInfo.put("avatar", userStatus.getJSONObject("avatar"));
/* 227 */       FriendInfo.put("resume", userStatus.getString("resume"));
/* 228 */       FriendInfo.put("recentVisited", Integer.valueOf(0));
/* 229 */       FriendInfo.put("registerTs", Integer.valueOf(userStatus.getIntValue("registerTs")));
/* 230 */       FriendInfo.put("secretary", userStatus.getString("secretary"));
/* 231 */       FriendInfo.put("secretarySkinId", userStatus.getString("secretarySkinId"));
/* 232 */       FriendInfo.put("serverName", "泰拉");
/* 233 */       FriendInfo.put("teamV2", this.teamV2);
/*     */       
/* 235 */       friends.add(FriendInfo);
/*     */       
/* 237 */       JSONArray FriendList = UserFriend.getJSONArray("list");
/*     */       
/* 239 */       for (int m = 0; m < FriendList.size(); m++) {
/* 240 */         if (FriendList.getJSONObject(m).getIntValue("uid") == FriendUid.longValue()) {
/* 241 */           friendAlias.add(FriendList.getJSONObject(m).getString("alias"));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 246 */     JSONObject result = new JSONObject(true);
/* 247 */     JSONObject playerDataDelta = new JSONObject(true);
/* 248 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 249 */     JSONObject modified = new JSONObject(true);
/* 250 */     playerDataDelta.put("modified", modified);
/* 251 */     result.put("playerDataDelta", playerDataDelta);
/* 252 */     result.put("friends", friends);
/* 253 */     result.put("resultIdList", idList);
/* 254 */     result.put("friendAlias", friendAlias);
/* 255 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/searchPlayer"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject searchPlayer(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 263 */     JSONArray idList = JsonBody.getJSONArray("idList");
/*     */     
/* 265 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 266 */     if (Accounts.size() != 1) {
/* 267 */       JSONObject jSONObject = new JSONObject(true);
/* 268 */       jSONObject.put("result", Integer.valueOf(2));
/* 269 */       jSONObject.put("error", "无法查询到此账户");
/* 270 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 274 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 275 */       response.setStatus(500);
/* 276 */       JSONObject jSONObject = new JSONObject(true);
/* 277 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 278 */       jSONObject.put("error", "Bad Request");
/* 279 */       jSONObject.put("message", "error");
/* 280 */       return jSONObject;
/*     */     } 
/*     */     
/* 283 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 285 */     JSONArray friends = new JSONArray();
/*     */     
/* 287 */     JSONObject medalBoard = new JSONObject(true);
/* 288 */     medalBoard.put("type", "EMPTY");
/* 289 */     medalBoard.put("template", null);
/* 290 */     medalBoard.put("custom", null);
/*     */     
/* 292 */     JSONArray friendStatusList = new JSONArray();
/* 293 */     for (int i = 0; i < idList.size(); i++) {
/* 294 */       long FriendUid = idList.getLongValue(i);
/*     */       
/* 296 */       List<UserInfo> userInfo = userDao.queryUserInfo(FriendUid);
/*     */       
/* 298 */       JSONArray userAssistCharList = JSONArray.parseArray(((UserInfo)userInfo.get(0)).getSocialAssistCharList());
/* 299 */       JSONObject userStatus = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getStatus());
/* 300 */       JSONObject chars = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getChars());
/* 301 */       JSONObject UserFriend = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getFriend());
/*     */       
/* 303 */       JSONObject FriendInfo = new JSONObject(true);
/* 304 */       JSONArray assistCharList = new JSONArray();
/*     */       
/* 306 */       for (int n = 0; n < userAssistCharList.size(); n++) {
/* 307 */         if (userAssistCharList.getJSONObject(n) != null) {
/* 308 */           String charInstId = String.valueOf(userAssistCharList.getJSONObject(n).getIntValue("charInstId"));
/* 309 */           JSONObject chardata = chars.getJSONObject(charInstId);
/* 310 */           chardata.put("skillIndex", Integer.valueOf(userAssistCharList.getJSONObject(n).getIntValue("skillIndex")));
/* 311 */           assistCharList.add(chardata);
/*     */         } else {
/* 313 */           assistCharList.add(null);
/*     */         } 
/*     */       } 
/* 316 */       FriendInfo.put("assistCharList", assistCharList);
/* 317 */       FriendInfo.put("avatarId", Integer.valueOf(userStatus.getIntValue("avatarId")));
/* 318 */       FriendInfo.put("uid", Long.valueOf(FriendUid));
/* 319 */       FriendInfo.put("friendNumLimit", Integer.valueOf(999));
/* 320 */       FriendInfo.put("medalBoard", medalBoard);
/* 321 */       FriendInfo.put("lastOnlineTime", Integer.valueOf(userStatus.getIntValue("lastOnlineTs")));
/* 322 */       FriendInfo.put("level", Integer.valueOf(userStatus.getIntValue("level")));
/* 323 */       FriendInfo.put("nickName", userStatus.getString("nickName"));
/* 324 */       FriendInfo.put("nickNumber", userStatus.getString("nickNumber"));
/* 325 */       FriendInfo.put("avatar", userStatus.getJSONObject("avatar"));
/* 326 */       FriendInfo.put("resume", userStatus.getString("resume"));
/* 327 */       FriendInfo.put("serverName", "泰拉");
/*     */       
/* 329 */       friends.add(FriendInfo);
/*     */ 
/*     */       
/* 332 */       JSONArray FriendRequest = UserFriend.getJSONArray("request");
/* 333 */       JSONArray FriendList = UserFriend.getJSONArray("list");
/*     */       
/* 335 */       Boolean isSet = Boolean.valueOf(false); int j;
/* 336 */       for (j = 0; j < FriendList.size(); j++) {
/* 337 */         if (FriendList.getJSONObject(j).getIntValue("uid") == uid.longValue()) {
/* 338 */           friendStatusList.add(Integer.valueOf(2));
/* 339 */           isSet = Boolean.valueOf(true);
/*     */         } 
/*     */       } 
/* 342 */       for (j = 0; j < FriendRequest.size(); j++) {
/* 343 */         if (FriendRequest.getJSONObject(j).getIntValue("uid") == uid.longValue()) {
/* 344 */           friendStatusList.add(Integer.valueOf(1));
/* 345 */           isSet = Boolean.valueOf(true);
/*     */         } 
/*     */       } 
/* 348 */       if (!isSet.booleanValue()) {
/* 349 */         friendStatusList.add(Integer.valueOf(0));
/*     */       }
/*     */     } 
/*     */     
/* 353 */     JSONObject result = new JSONObject(true);
/* 354 */     JSONObject playerDataDelta = new JSONObject(true);
/* 355 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 356 */     JSONObject modified = new JSONObject(true);
/* 357 */     playerDataDelta.put("modified", modified);
/* 358 */     result.put("playerDataDelta", playerDataDelta);
/* 359 */     result.put("players", friends);
/* 360 */     result.put("resultIdList", idList);
/* 361 */     result.put("friendStatusList", friendStatusList);
/* 362 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getFriendRequestList"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getFriendRequestList(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 369 */     JSONArray idList = JsonBody.getJSONArray("idList");
/*     */     
/* 371 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 372 */     if (Accounts.size() != 1) {
/* 373 */       JSONObject jSONObject = new JSONObject(true);
/* 374 */       jSONObject.put("result", Integer.valueOf(2));
/* 375 */       jSONObject.put("error", "无法查询到此账户");
/* 376 */       return jSONObject;
/*     */     } 
/*     */     
/* 379 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 380 */       response.setStatus(500);
/* 381 */       JSONObject jSONObject = new JSONObject(true);
/* 382 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 383 */       jSONObject.put("error", "Bad Request");
/* 384 */       jSONObject.put("message", "error");
/* 385 */       return jSONObject;
/*     */     } 
/*     */     
/* 388 */     JSONArray friends = new JSONArray();
/*     */     
/* 390 */     JSONArray board = new JSONArray();
/*     */     
/* 392 */     JSONObject medalBoard = new JSONObject(true);
/* 393 */     medalBoard.put("type", "EMPTY");
/* 394 */     medalBoard.put("template", null);
/* 395 */     medalBoard.put("custom", null);
/*     */     
/* 397 */     for (int i = 0; i < idList.size(); i++) {
/* 398 */       long FriendUid = idList.getIntValue(i);
/* 399 */       List<UserInfo> userInfo = userDao.queryUserInfo(FriendUid);
/*     */       
/* 401 */       JSONArray userAssistCharList = JSONArray.parseArray(((UserInfo)userInfo.get(0)).getSocialAssistCharList());
/* 402 */       JSONObject userStatus = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getStatus());
/* 403 */       JSONObject chars = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getChars());
/* 404 */       JSONObject UserFriend = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getFriend());
/*     */       
/* 406 */       JSONObject FriendInfo = new JSONObject(true);
/* 407 */       JSONArray assistCharList = new JSONArray();
/*     */       
/* 409 */       for (int n = 0; n < userAssistCharList.size(); n++) {
/* 410 */         if (userAssistCharList.getJSONObject(n) != null) {
/* 411 */           String charInstId = userAssistCharList.getJSONObject(n).getString("charInstId");
/* 412 */           JSONObject chardata = chars.getJSONObject(charInstId);
/* 413 */           chardata.put("skillIndex", Integer.valueOf(userAssistCharList.getJSONObject(n).getIntValue("skillIndex")));
/* 414 */           assistCharList.add(chardata);
/*     */         } else {
/* 416 */           assistCharList.add(null);
/*     */         } 
/*     */       } 
/* 419 */       FriendInfo.put("assistCharList", assistCharList);
/* 420 */       FriendInfo.put("avatarId", Integer.valueOf(userStatus.getIntValue("avatarId")));
/* 421 */       FriendInfo.put("uid", Long.valueOf(FriendUid));
/* 422 */       FriendInfo.put("board", board);
/* 423 */       FriendInfo.put("medalBoard", medalBoard);
/* 424 */       FriendInfo.put("charCnt", Integer.valueOf(chars.size()));
/* 425 */       FriendInfo.put("friendNumLimit", Integer.valueOf(50));
/* 426 */       FriendInfo.put("furnCnt", Integer.valueOf(0));
/* 427 */       FriendInfo.put("infoShare", Integer.valueOf(0));
/* 428 */       FriendInfo.put("lastOnlineTime", Integer.valueOf(userStatus.getIntValue("lastOnlineTs")));
/* 429 */       FriendInfo.put("level", Integer.valueOf(userStatus.getIntValue("level")));
/* 430 */       FriendInfo.put("mainStageProgress", userStatus.getString("mainStageProgress"));
/* 431 */       FriendInfo.put("nickName", userStatus.getString("nickName"));
/* 432 */       FriendInfo.put("nickNumber", userStatus.getString("nickNumber"));
/* 433 */       FriendInfo.put("avatar", userStatus.getJSONObject("avatar"));
/* 434 */       FriendInfo.put("resume", userStatus.getString("resume"));
/* 435 */       FriendInfo.put("recentVisited", Integer.valueOf(0));
/* 436 */       FriendInfo.put("registerTs", Integer.valueOf(userStatus.getIntValue("registerTs")));
/* 437 */       FriendInfo.put("secretary", userStatus.getString("secretary"));
/* 438 */       FriendInfo.put("secretarySkinId", userStatus.getString("secretarySkinId"));
/* 439 */       FriendInfo.put("serverName", "泰拉");
/* 440 */       FriendInfo.put("teamV2", this.teamV2);
/*     */       
/* 442 */       friends.add(FriendInfo);
/*     */     } 
/*     */     
/* 445 */     JSONObject result = new JSONObject(true);
/* 446 */     JSONObject playerDataDelta = new JSONObject(true);
/* 447 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 448 */     JSONObject modified = new JSONObject(true);
/* 449 */     playerDataDelta.put("modified", modified);
/* 450 */     result.put("playerDataDelta", playerDataDelta);
/* 451 */     result.put("requestList", friends);
/* 452 */     result.put("resultIdList", idList);
/* 453 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/processFriendRequest"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject processFriendRequest(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 461 */     int action = JsonBody.getIntValue("action");
/* 462 */     long friendId = JsonBody.getIntValue("friendId");
/*     */     
/* 464 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 465 */     if (Accounts.size() != 1) {
/* 466 */       JSONObject jSONObject = new JSONObject(true);
/* 467 */       jSONObject.put("result", Integer.valueOf(2));
/* 468 */       jSONObject.put("error", "无法查询到此账户");
/* 469 */       return jSONObject;
/*     */     } 
/*     */     
/* 472 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 474 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 475 */       response.setStatus(500);
/* 476 */       JSONObject jSONObject = new JSONObject(true);
/* 477 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 478 */       jSONObject.put("error", "Bad Request");
/* 479 */       jSONObject.put("message", "error");
/* 480 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 484 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 486 */     JSONObject FriendJson = JSONObject.parseObject(((Account)Accounts.get(0)).getFriend());
/* 487 */     JSONArray FriendRequest = FriendJson.getJSONArray("request");
/* 488 */     JSONArray FriendList = FriendJson.getJSONArray("list");
/*     */     
/* 490 */     for (int i = 0; i < FriendRequest.size(); i++) {
/* 491 */       if (FriendRequest.getJSONObject(i).getIntValue("uid") == friendId) {
/*     */         
/* 493 */         FriendRequest.remove(i);
/* 494 */         FriendJson.put("request", FriendRequest);
/*     */         
/* 496 */         userDao.setFriendData(uid, FriendJson);
/*     */         
/* 498 */         if (action == 1) {
/* 499 */           JSONObject Friend = new JSONObject(true);
/* 500 */           Friend.put("uid", Long.valueOf(friendId));
/* 501 */           Friend.put("alias", null);
/* 502 */           FriendList.add(Friend);
/*     */           
/* 504 */           FriendJson.put("list", FriendList);
/* 505 */           userDao.setFriendData(uid, FriendJson);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 510 */     if (action == 1) {
/*     */       
/* 512 */       List<UserInfo> userInfo = userDao.queryUserInfo(friendId);
/*     */       
/* 514 */       JSONObject FJson = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getFriend());
/* 515 */       JSONArray FList = FJson.getJSONArray("list");
/*     */       
/* 517 */       JSONObject Friend = new JSONObject(true);
/* 518 */       Friend.put("uid", uid);
/* 519 */       Friend.put("alias", null);
/* 520 */       FList.add(Friend);
/*     */       
/* 522 */       FJson.put("list", FList);
/* 523 */       userDao.setFriendData(Long.valueOf(friendId), FJson);
/*     */     } 
/*     */     
/* 526 */     if (FriendRequest.size() == 0) {
/* 527 */       UserSyncData.getJSONObject("pushFlags").put("hasFriendRequest", Integer.valueOf(0));
/*     */     }
/*     */     
/* 530 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 532 */     JSONObject result = new JSONObject(true);
/* 533 */     JSONObject playerDataDelta = new JSONObject(true);
/* 534 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 535 */     JSONObject modified = new JSONObject(true);
/* 536 */     modified.put("pushFlags", UserSyncData.getJSONObject("pushFlags"));
/* 537 */     playerDataDelta.put("modified", modified);
/* 538 */     result.put("playerDataDelta", playerDataDelta);
/* 539 */     result.put("friendNum", Integer.valueOf(FriendList.size()));
/* 540 */     result.put("result", Integer.valueOf(0));
/* 541 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/sendFriendRequest"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject sendFriendRequest(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 549 */     int afterBattle = JsonBody.getIntValue("afterBattle");
/* 550 */     long friendId = JsonBody.getIntValue("friendId");
/*     */     
/* 552 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 553 */     if (Accounts.size() != 1) {
/* 554 */       JSONObject jSONObject = new JSONObject(true);
/* 555 */       jSONObject.put("result", Integer.valueOf(2));
/* 556 */       jSONObject.put("error", "无法查询到此账户");
/* 557 */       return jSONObject;
/*     */     } 
/*     */     
/* 560 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 562 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 563 */       response.setStatus(500);
/* 564 */       JSONObject jSONObject = new JSONObject(true);
/* 565 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 566 */       jSONObject.put("error", "Bad Request");
/* 567 */       jSONObject.put("message", "error");
/* 568 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 572 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 574 */     List<UserInfo> userInfo = userDao.queryUserInfo(friendId);
/*     */     
/* 576 */     JSONObject FriendJson = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getFriend());
/* 577 */     JSONArray FriendRequest = FriendJson.getJSONArray("request");
/* 578 */     JSONArray FriendList = FriendJson.getJSONArray("list");
/*     */     int i;
/* 580 */     for (i = 0; i < FriendList.size(); i++) {
/* 581 */       if (FriendList.getJSONObject(i).getIntValue("uid") == uid.longValue()) {
/* 582 */         JSONObject jSONObject = new JSONObject(true);
/* 583 */         jSONObject.put("result", Integer.valueOf(2));
/* 584 */         jSONObject.put("error", "已添加该好友");
/* 585 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 589 */     for (i = 0; i < FriendRequest.size(); i++) {
/* 590 */       if (FriendRequest.getJSONObject(i).getIntValue("uid") == uid.longValue()) {
/* 591 */         JSONObject jSONObject = new JSONObject(true);
/* 592 */         jSONObject.put("result", Integer.valueOf(2));
/* 593 */         jSONObject.put("error", "已对该博士进行过好友申请");
/* 594 */         return jSONObject;
/*     */       } 
/*     */     } 
/*     */     
/* 598 */     JSONObject Request = new JSONObject(true);
/* 599 */     Request.put("uid", uid);
/* 600 */     FriendRequest.add(Request);
/*     */     
/* 602 */     FriendJson.put("request", FriendRequest);
/* 603 */     userDao.setFriendData(Long.valueOf(friendId), FriendJson);
/*     */     
/* 605 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 607 */     JSONObject result = new JSONObject(true);
/* 608 */     JSONObject playerDataDelta = new JSONObject(true);
/* 609 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 610 */     JSONObject modified = new JSONObject(true);
/* 611 */     playerDataDelta.put("modified", modified);
/* 612 */     result.put("playerDataDelta", playerDataDelta);
/* 613 */     result.put("result", Integer.valueOf(0));
/* 614 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/setFriendAlias"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject setFriendAlias(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 622 */     String alias = JsonBody.getString("alias");
/* 623 */     int friendId = JsonBody.getIntValue("friendId");
/*     */     
/* 625 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 626 */     if (Accounts.size() != 1) {
/* 627 */       JSONObject jSONObject = new JSONObject(true);
/* 628 */       jSONObject.put("result", Integer.valueOf(2));
/* 629 */       jSONObject.put("error", "无法查询到此账户");
/* 630 */       return jSONObject;
/*     */     } 
/*     */     
/* 633 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 635 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 636 */       response.setStatus(500);
/* 637 */       JSONObject jSONObject = new JSONObject(true);
/* 638 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 639 */       jSONObject.put("error", "Bad Request");
/* 640 */       jSONObject.put("message", "error");
/* 641 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 645 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 647 */     List<UserInfo> userInfo = userDao.queryUserInfo(uid.longValue());
/*     */     
/* 649 */     JSONObject FriendJson = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getFriend());
/* 650 */     JSONArray FriendList = FriendJson.getJSONArray("list");
/*     */     
/* 652 */     for (int i = 0; i < FriendList.size(); i++) {
/* 653 */       if (FriendList.getJSONObject(i).getIntValue("uid") == friendId) {
/* 654 */         FriendList.getJSONObject(i).put("alias", alias);
/*     */       }
/*     */     } 
/*     */     
/* 658 */     FriendJson.put("list", FriendList);
/* 659 */     userDao.setFriendData(uid, FriendJson);
/*     */     
/* 661 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 663 */     JSONObject result = new JSONObject(true);
/* 664 */     JSONObject playerDataDelta = new JSONObject(true);
/* 665 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 666 */     JSONObject modified = new JSONObject(true);
/* 667 */     playerDataDelta.put("modified", modified);
/* 668 */     result.put("playerDataDelta", playerDataDelta);
/* 669 */     result.put("result", Integer.valueOf(0));
/* 670 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/deleteFriend"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject deleteFriend(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response) {
/* 678 */     long friendId = JsonBody.getIntValue("friendId");
/*     */     
/* 680 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 681 */     if (Accounts.size() != 1) {
/* 682 */       JSONObject jSONObject = new JSONObject(true);
/* 683 */       jSONObject.put("result", Integer.valueOf(2));
/* 684 */       jSONObject.put("error", "无法查询到此账户");
/* 685 */       return jSONObject;
/*     */     } 
/*     */     
/* 688 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 690 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 691 */       response.setStatus(500);
/* 692 */       JSONObject jSONObject = new JSONObject(true);
/* 693 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 694 */       jSONObject.put("error", "Bad Request");
/* 695 */       jSONObject.put("message", "error");
/* 696 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 700 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 702 */     List<UserInfo> userInfo = userDao.queryUserInfo(uid.longValue());
/*     */     
/* 704 */     JSONObject FriendJson = JSONObject.parseObject(((UserInfo)userInfo.get(0)).getFriend());
/* 705 */     JSONArray FriendList = FriendJson.getJSONArray("list");
/*     */     
/* 707 */     for (int i = 0; i < FriendList.size(); i++) {
/* 708 */       if (FriendList.getJSONObject(i).getIntValue("uid") == friendId) {
/* 709 */         FriendList.remove(i);
/*     */       }
/*     */     } 
/*     */     
/* 713 */     FriendJson.put("list", FriendList);
/* 714 */     userDao.setFriendData(uid, FriendJson);
/*     */     
/* 716 */     List<UserInfo> UserFriend = userDao.queryUserInfo(friendId);
/*     */     
/* 718 */     FriendJson = JSONObject.parseObject(((UserInfo)UserFriend.get(0)).getFriend());
/* 719 */     FriendList = FriendJson.getJSONArray("list");
/*     */     
/* 721 */     for (int j = 0; j < FriendList.size(); j++) {
/* 722 */       if (FriendList.getJSONObject(j).getIntValue("uid") == uid.longValue()) {
/* 723 */         FriendList.remove(j);
/*     */       }
/*     */     } 
/*     */     
/* 727 */     FriendJson.put("list", FriendList);
/* 728 */     userDao.setFriendData(Long.valueOf(friendId), FriendJson);
/*     */     
/* 730 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 732 */     JSONObject result = new JSONObject(true);
/* 733 */     JSONObject playerDataDelta = new JSONObject(true);
/* 734 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 735 */     JSONObject modified = new JSONObject(true);
/* 736 */     playerDataDelta.put("modified", modified);
/* 737 */     result.put("playerDataDelta", playerDataDelta);
/* 738 */     result.put("result", Integer.valueOf(0));
/* 739 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\social.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */