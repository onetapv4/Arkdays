/*     */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*     */ 
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.dao.userDao;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
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
/*     */ @RestController
/*     */ @RequestMapping({"/building"})
/*     */ public class building
/*     */ {
/*     */   @PostMapping(value = {"/sync"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject Sync(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/*  29 */     String clientIp = ArknightsApplication.getIpAddr(request);
/*  30 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/sync");
/*     */     
/*  32 */     if (!ArknightsApplication.enableServer) {
/*  33 */       response.setStatus(400);
/*  34 */       JSONObject jSONObject = new JSONObject(true);
/*  35 */       jSONObject.put("statusCode", Integer.valueOf(400));
/*  36 */       jSONObject.put("error", "Bad Request");
/*  37 */       jSONObject.put("message", "server is close");
/*  38 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  45 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/*  46 */     if (Accounts.size() != 1) {
/*  47 */       JSONObject jSONObject = new JSONObject(true);
/*  48 */       jSONObject.put("result", Integer.valueOf(2));
/*  49 */       jSONObject.put("error", "无法查询到此账户");
/*  50 */       return jSONObject;
/*     */     } 
/*     */     
/*  53 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/*  55 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/*  56 */       response.setStatus(500);
/*  57 */       JSONObject jSONObject = new JSONObject(true);
/*  58 */       jSONObject.put("statusCode", Integer.valueOf(403));
/*  59 */       jSONObject.put("error", "Bad Request");
/*  60 */       jSONObject.put("message", "error");
/*  61 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/*  65 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/*  67 */     JSONObject playerDataDelta = new JSONObject(true);
/*  68 */     JSONObject modified = new JSONObject(true);
/*  69 */     modified.put("building", UserSyncData.getJSONObject("building"));
/*  70 */     modified.put("event", UserSyncData.getJSONObject("event"));
/*     */     
/*  72 */     JSONObject result = new JSONObject(true);
/*  73 */     result.put("ts", Long.valueOf(ArknightsApplication.getTimestamp()));
/*  74 */     playerDataDelta.put("modified", modified);
/*  75 */     playerDataDelta.put("deleted", new JSONObject(true));
/*  76 */     result.put("playerDataDelta", playerDataDelta);
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getInfoShareVisitorsNum"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getInfoShareVisitorsNum() {
/*  83 */     JSONObject result = new JSONObject(true);
/*  84 */     result.put("num", Integer.valueOf(0));
/*  85 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/getRecentVisitors"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject getRecentVisitors() {
/*  91 */     JSONObject result = new JSONObject(true);
/*  92 */     result.put("getRecentVisitors", new JSONArray());
/*  93 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/assignChar"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject assignChar(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 100 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 101 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/assignChar");
/*     */     
/* 103 */     if (!ArknightsApplication.enableServer) {
/* 104 */       response.setStatus(400);
/* 105 */       JSONObject jSONObject = new JSONObject(true);
/* 106 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 107 */       jSONObject.put("error", "Bad Request");
/* 108 */       jSONObject.put("message", "server is close");
/* 109 */       return jSONObject;
/*     */     } 
/*     */     
/* 112 */     String roomSlotId = JsonBody.getString("roomSlotId");
/* 113 */     JSONArray charInstIdList = JsonBody.getJSONArray("charInstIdList");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 120 */     if (Accounts.size() != 1) {
/* 121 */       JSONObject jSONObject = new JSONObject(true);
/* 122 */       jSONObject.put("result", Integer.valueOf(2));
/* 123 */       jSONObject.put("error", "无法查询到此账户");
/* 124 */       return jSONObject;
/*     */     } 
/*     */     
/* 127 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 129 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 130 */       response.setStatus(500);
/* 131 */       JSONObject jSONObject = new JSONObject(true);
/* 132 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 133 */       jSONObject.put("error", "Bad Request");
/* 134 */       jSONObject.put("message", "error");
/* 135 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 139 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 141 */     JSONObject roomSlots = UserSyncData.getJSONObject("building").getJSONObject("roomSlots");
/*     */     
/* 143 */     for (Map.Entry entry : roomSlots.entrySet()) {
/* 144 */       JSONArray roomCharInstIds = roomSlots.getJSONObject(entry.getKey().toString()).getJSONArray("charInstIds");
/* 145 */       for (int i = 0; i < roomCharInstIds.size(); i++) {
/* 146 */         for (int n = 0; n < charInstIdList.size(); n++) {
/* 147 */           if (charInstIdList.get(n) == roomCharInstIds.get(i)) {
/* 148 */             roomCharInstIds.set(i, Integer.valueOf(-1));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 156 */     UserSyncData.getJSONObject("building").getJSONObject("roomSlots").getJSONObject(roomSlotId).put("charInstIds", charInstIdList);
/*     */     
/* 158 */     if (roomSlotId.equals("slot_13")) {
/*     */       
/* 160 */       int trainer = charInstIdList.getIntValue(0);
/* 161 */       int trainee = charInstIdList.getIntValue(1);
/*     */       
/* 163 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainee").put("charInstId", Integer.valueOf(trainee));
/* 164 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainee").put("targetSkill", Integer.valueOf(-1));
/* 165 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainee").put("speed", Integer.valueOf(1000));
/*     */       
/* 167 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainer").put("charInstId", Integer.valueOf(trainer));
/*     */       
/* 169 */       if (trainee == -1) {
/* 170 */         UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainee").put("state", Integer.valueOf(0));
/*     */       } else {
/* 172 */         UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainee").put("state", Integer.valueOf(3));
/*     */       } 
/*     */       
/* 175 */       if (trainer == -1) {
/* 176 */         UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainer").put("state", Integer.valueOf(0));
/*     */       } else {
/* 178 */         UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject(roomSlotId).getJSONObject("trainer").put("state", Integer.valueOf(3));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 183 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 185 */     JSONObject playerDataDelta = new JSONObject(true);
/* 186 */     JSONObject modified = new JSONObject(true);
/* 187 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 188 */     modified.put("event", UserSyncData.getJSONObject("event"));
/*     */     
/* 190 */     JSONObject result = new JSONObject(true);
/* 191 */     playerDataDelta.put("modified", modified);
/* 192 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 193 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 195 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/changeDiySolution"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject changeDiySolution(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 202 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 203 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/changeDiySolution");
/*     */     
/* 205 */     if (!ArknightsApplication.enableServer) {
/* 206 */       response.setStatus(400);
/* 207 */       JSONObject jSONObject = new JSONObject(true);
/* 208 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 209 */       jSONObject.put("error", "Bad Request");
/* 210 */       jSONObject.put("message", "server is close");
/* 211 */       return jSONObject;
/*     */     } 
/*     */     
/* 214 */     String roomSlotId = JsonBody.getString("roomSlotId");
/* 215 */     JSONObject solution = JsonBody.getJSONObject("solution");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 231 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 232 */       response.setStatus(500);
/* 233 */       JSONObject jSONObject = new JSONObject(true);
/* 234 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 235 */       jSONObject.put("error", "Bad Request");
/* 236 */       jSONObject.put("message", "error");
/* 237 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 241 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 243 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("DORMITORY").getJSONObject(roomSlotId).put("diySolution", solution);
/*     */     
/* 245 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 247 */     JSONObject playerDataDelta = new JSONObject(true);
/* 248 */     JSONObject modified = new JSONObject(true);
/* 249 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 250 */     modified.put("event", UserSyncData.getJSONObject("event"));
/*     */     
/* 252 */     JSONObject result = new JSONObject(true);
/* 253 */     playerDataDelta.put("modified", modified);
/* 254 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 255 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 257 */     return result;
/*     */   }
/*     */   
/*     */   public String getFormulaId(String formulaId) {
/* 261 */     if (formulaId.equals("1")) {
/* 262 */       return "2001";
/*     */     }
/* 264 */     if (formulaId.equals("2")) {
/* 265 */       return "2002";
/*     */     }
/* 267 */     if (formulaId.equals("3")) {
/* 268 */       return "2003";
/*     */     }
/* 270 */     if (formulaId.equals("4")) {
/* 271 */       return "3003";
/*     */     }
/* 273 */     if (formulaId.equals("5")) {
/* 274 */       return "3213";
/*     */     }
/* 276 */     if (formulaId.equals("6")) {
/* 277 */       return "3223";
/*     */     }
/* 279 */     if (formulaId.equals("7")) {
/* 280 */       return "3233";
/*     */     }
/* 282 */     if (formulaId.equals("8")) {
/* 283 */       return "3243";
/*     */     }
/* 285 */     if (formulaId.equals("9")) {
/* 286 */       return "3253";
/*     */     }
/* 288 */     if (formulaId.equals("10")) {
/* 289 */       return "3263";
/*     */     }
/* 291 */     if (formulaId.equals("11")) {
/* 292 */       return "3273";
/*     */     }
/* 294 */     if (formulaId.equals("12")) {
/* 295 */       return "3283";
/*     */     }
/* 297 */     if (formulaId.equals("13")) {
/* 298 */       return "3141";
/*     */     }
/* 300 */     if (formulaId.equals("14")) {
/* 301 */       return "3141";
/*     */     }
/* 303 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/changeManufactureSolution"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject changeManufactureSolution(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 310 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 311 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/changeManufactureSolution");
/*     */     
/* 313 */     if (!ArknightsApplication.enableServer) {
/* 314 */       response.setStatus(400);
/* 315 */       JSONObject jSONObject = new JSONObject(true);
/* 316 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 317 */       jSONObject.put("error", "Bad Request");
/* 318 */       jSONObject.put("message", "server is close");
/* 319 */       return jSONObject;
/*     */     } 
/*     */     
/* 322 */     String roomSlotId = JsonBody.getString("roomSlotId");
/* 323 */     String targetFormulaId = JsonBody.getString("targetFormulaId");
/* 324 */     int solutionCount = JsonBody.getIntValue("solutionCount");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 331 */     if (Accounts.size() != 1) {
/* 332 */       JSONObject jSONObject = new JSONObject(true);
/* 333 */       jSONObject.put("result", Integer.valueOf(2));
/* 334 */       jSONObject.put("error", "无法查询到此账户");
/* 335 */       return jSONObject;
/*     */     } 
/*     */     
/* 338 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 340 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 341 */       response.setStatus(500);
/* 342 */       JSONObject jSONObject = new JSONObject(true);
/* 343 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 344 */       jSONObject.put("error", "Bad Request");
/* 345 */       jSONObject.put("message", "error");
/* 346 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 350 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 352 */     int outputSolutionCnt = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).getIntValue("outputSolutionCnt");
/* 353 */     String FormulaId = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).getString("formulaId");
/*     */     
/* 355 */     if (outputSolutionCnt != 0) {
/* 356 */       if (Integer.valueOf(FormulaId).intValue() >= 5 && Integer.valueOf(FormulaId).intValue() <= 12) {
/* 357 */         String itemId = null;
/* 358 */         if (Integer.valueOf(FormulaId).intValue() == 5) {
/* 359 */           itemId = "3212";
/*     */         }
/* 361 */         if (Integer.valueOf(FormulaId).intValue() == 6) {
/* 362 */           itemId = "3222";
/*     */         }
/* 364 */         if (Integer.valueOf(FormulaId).intValue() == 7) {
/* 365 */           itemId = "3232";
/*     */         }
/* 367 */         if (Integer.valueOf(FormulaId).intValue() == 8) {
/* 368 */           itemId = "3242";
/*     */         }
/* 370 */         if (Integer.valueOf(FormulaId).intValue() == 9) {
/* 371 */           itemId = "3252";
/*     */         }
/* 373 */         if (Integer.valueOf(FormulaId).intValue() == 10) {
/* 374 */           itemId = "3262";
/*     */         }
/* 376 */         if (Integer.valueOf(FormulaId).intValue() == 11) {
/* 377 */           itemId = "3272";
/*     */         }
/* 379 */         if (Integer.valueOf(FormulaId).intValue() == 12) {
/* 380 */           itemId = "3282";
/*     */         }
/* 382 */         UserSyncData.getJSONObject("inventory").put(getFormulaId(FormulaId), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(getFormulaId(FormulaId)) + outputSolutionCnt));
/* 383 */         UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - 2 * outputSolutionCnt));
/* 384 */         UserSyncData.getJSONObject("inventory").put("32001", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("32001") - 1 * outputSolutionCnt));
/* 385 */       } else if (Integer.valueOf(FormulaId).intValue() > 12) {
/* 386 */         String itemId = null;
/* 387 */         if (Integer.valueOf(FormulaId).intValue() == 13) {
/* 388 */           itemId = "30012";
/* 389 */           UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") - 1600 * outputSolutionCnt));
/*     */         } 
/* 391 */         if (Integer.valueOf(FormulaId).intValue() == 14) {
/* 392 */           itemId = "30062";
/* 393 */           UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") - 1000 * outputSolutionCnt));
/*     */         } 
/* 395 */         UserSyncData.getJSONObject("inventory").put(getFormulaId(FormulaId), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(getFormulaId(FormulaId)) + outputSolutionCnt));
/* 396 */         UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - 2 * outputSolutionCnt));
/*     */       } else {
/* 398 */         UserSyncData.getJSONObject("inventory").put(getFormulaId(FormulaId), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(getFormulaId(FormulaId)) + outputSolutionCnt));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 403 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("state", Integer.valueOf(1));
/* 404 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("formulaId", targetFormulaId);
/* 405 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("lastUpdateTime", Long.valueOf((new Date()).getTime() / 1000L));
/* 406 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("completeWorkTime", Integer.valueOf(-1));
/* 407 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("remainSolutionCnt", Integer.valueOf(0));
/* 408 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("outputSolutionCnt", Integer.valueOf(solutionCount));
/*     */     
/* 410 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 412 */     JSONObject playerDataDelta = new JSONObject(true);
/* 413 */     JSONObject modified = new JSONObject(true);
/* 414 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 415 */     modified.put("event", UserSyncData.getJSONObject("event"));
/* 416 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 417 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*     */     
/* 419 */     JSONObject result = new JSONObject(true);
/* 420 */     playerDataDelta.put("modified", modified);
/* 421 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 422 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 424 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/settleManufacture"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject settleManufacture(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 431 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 432 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/settleManufacture");
/*     */     
/* 434 */     if (!ArknightsApplication.enableServer) {
/* 435 */       response.setStatus(400);
/* 436 */       JSONObject jSONObject = new JSONObject(true);
/* 437 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 438 */       jSONObject.put("error", "Bad Request");
/* 439 */       jSONObject.put("message", "server is close");
/* 440 */       return jSONObject;
/*     */     } 
/*     */     
/* 443 */     JSONArray roomSlotIdList = JsonBody.getJSONArray("roomSlotIdList");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 449 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 450 */     if (Accounts.size() != 1) {
/* 451 */       JSONObject jSONObject = new JSONObject(true);
/* 452 */       jSONObject.put("result", Integer.valueOf(2));
/* 453 */       jSONObject.put("error", "无法查询到此账户");
/* 454 */       return jSONObject;
/*     */     } 
/*     */     
/* 457 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 459 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 460 */       response.setStatus(500);
/* 461 */       JSONObject jSONObject = new JSONObject(true);
/* 462 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 463 */       jSONObject.put("error", "Bad Request");
/* 464 */       jSONObject.put("message", "error");
/* 465 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 469 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */ 
/*     */     
/* 472 */     for (int i = 0; i < roomSlotIdList.size(); i++) {
/*     */       
/* 474 */       String roomSlotId = roomSlotIdList.getString(i);
/*     */       
/* 476 */       int outputSolutionCnt = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).getIntValue("outputSolutionCnt");
/* 477 */       String FormulaId = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).getString("formulaId");
/*     */       
/* 479 */       if (outputSolutionCnt != 0) {
/* 480 */         if (Integer.valueOf(FormulaId).intValue() >= 5 && Integer.valueOf(FormulaId).intValue() <= 12) {
/* 481 */           String itemId = null;
/* 482 */           if (Integer.valueOf(FormulaId).intValue() == 5) {
/* 483 */             itemId = "3212";
/*     */           }
/* 485 */           if (Integer.valueOf(FormulaId).intValue() == 6) {
/* 486 */             itemId = "3222";
/*     */           }
/* 488 */           if (Integer.valueOf(FormulaId).intValue() == 7) {
/* 489 */             itemId = "3232";
/*     */           }
/* 491 */           if (Integer.valueOf(FormulaId).intValue() == 8) {
/* 492 */             itemId = "3242";
/*     */           }
/* 494 */           if (Integer.valueOf(FormulaId).intValue() == 9) {
/* 495 */             itemId = "3252";
/*     */           }
/* 497 */           if (Integer.valueOf(FormulaId).intValue() == 10) {
/* 498 */             itemId = "3262";
/*     */           }
/* 500 */           if (Integer.valueOf(FormulaId).intValue() == 11) {
/* 501 */             itemId = "3272";
/*     */           }
/* 503 */           if (Integer.valueOf(FormulaId).intValue() == 12) {
/* 504 */             itemId = "3282";
/*     */           }
/* 506 */           UserSyncData.getJSONObject("inventory").put(getFormulaId(FormulaId), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(getFormulaId(FormulaId)) + outputSolutionCnt));
/* 507 */           UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - 2 * outputSolutionCnt));
/* 508 */           UserSyncData.getJSONObject("inventory").put("32001", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("32001") - 1 * outputSolutionCnt));
/* 509 */         } else if (Integer.valueOf(FormulaId).intValue() > 12) {
/* 510 */           String itemId = null;
/* 511 */           if (Integer.valueOf(FormulaId).intValue() == 13) {
/* 512 */             itemId = "30012";
/* 513 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") - 1600 * outputSolutionCnt));
/*     */           } 
/* 515 */           if (Integer.valueOf(FormulaId).intValue() == 14) {
/* 516 */             itemId = "30062";
/* 517 */             UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") - 1000 * outputSolutionCnt));
/*     */           } 
/* 519 */           UserSyncData.getJSONObject("inventory").put(getFormulaId(FormulaId), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(getFormulaId(FormulaId)) + outputSolutionCnt));
/* 520 */           UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - 2 * outputSolutionCnt));
/*     */         } else {
/* 522 */           UserSyncData.getJSONObject("inventory").put(getFormulaId(FormulaId), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(getFormulaId(FormulaId)) + outputSolutionCnt));
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/* 527 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("state", Integer.valueOf(0));
/* 528 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("formulaId", "");
/* 529 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("lastUpdateTime", Long.valueOf((new Date()).getTime() / 1000L));
/* 530 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("completeWorkTime", Integer.valueOf(-1));
/* 531 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("remainSolutionCnt", Integer.valueOf(0));
/* 532 */       UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("MANUFACTURE").getJSONObject(roomSlotId).put("outputSolutionCnt", Integer.valueOf(0));
/*     */     } 
/*     */     
/* 535 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 537 */     JSONObject playerDataDelta = new JSONObject(true);
/* 538 */     JSONObject modified = new JSONObject(true);
/* 539 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 540 */     modified.put("event", UserSyncData.getJSONObject("event"));
/* 541 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 542 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*     */     
/* 544 */     JSONObject result = new JSONObject(true);
/* 545 */     playerDataDelta.put("modified", modified);
/* 546 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 547 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 549 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/workshopSynthesis"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject workshopSynthesis(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 556 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 557 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/workshopSynthesis");
/*     */     
/* 559 */     if (!ArknightsApplication.enableServer) {
/* 560 */       response.setStatus(400);
/* 561 */       JSONObject jSONObject = new JSONObject(true);
/* 562 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 563 */       jSONObject.put("error", "Bad Request");
/* 564 */       jSONObject.put("message", "server is close");
/* 565 */       return jSONObject;
/*     */     } 
/*     */     
/* 568 */     String formulaId = JsonBody.getString("formulaId");
/* 569 */     int workCount = JsonBody.getIntValue("times");
/*     */ 
/*     */ 
/*     */     
/* 573 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 574 */     if (Accounts.size() != 1) {
/* 575 */       JSONObject jSONObject = new JSONObject(true);
/* 576 */       jSONObject.put("result", Integer.valueOf(2));
/* 577 */       jSONObject.put("error", "无法查询到此账户");
/* 578 */       return jSONObject;
/*     */     } 
/*     */     
/* 581 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 583 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 584 */       response.setStatus(500);
/* 585 */       JSONObject jSONObject = new JSONObject(true);
/* 586 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 587 */       jSONObject.put("error", "Bad Request");
/* 588 */       jSONObject.put("message", "error");
/* 589 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 593 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 595 */     JSONObject workshopFormulas = ArknightsApplication.buildingData.getJSONObject(formulaId);
/*     */     
/* 597 */     JSONArray costs = workshopFormulas.getJSONArray("costs");
/* 598 */     for (int i = 0; i < costs.size(); i++) {
/* 599 */       String itemId = costs.getJSONObject(i).getString("id");
/* 600 */       int itemCount = costs.getJSONObject(i).getIntValue("count");
/* 601 */       UserSyncData.getJSONObject("inventory").put(itemId, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(itemId) - itemCount * workCount));
/*     */     } 
/* 603 */     UserSyncData.getJSONObject("inventory").put(workshopFormulas.getString("itemId"), Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(workshopFormulas.getString("itemId")) + workshopFormulas.getIntValue("count") * workCount));
/* 604 */     UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") - workshopFormulas.getIntValue("goldCost") * workCount));
/*     */     
/* 606 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 608 */     JSONObject playerDataDelta = new JSONObject(true);
/* 609 */     JSONObject modified = new JSONObject(true);
/* 610 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 611 */     modified.put("event", UserSyncData.getJSONObject("event"));
/* 612 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 613 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*     */     
/* 615 */     JSONObject result = new JSONObject(true);
/* 616 */     JSONObject results = new JSONObject(true);
/* 617 */     results.put("type", "MATERIAL");
/* 618 */     results.put("id", workshopFormulas.getString("itemId"));
/* 619 */     results.put("count", Integer.valueOf(workCount));
/* 620 */     playerDataDelta.put("modified", modified);
/* 621 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 622 */     result.put("playerDataDelta", playerDataDelta);
/* 623 */     result.put("results", results);
/* 624 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/upgradeSpecialization"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject upgradeSpecialization(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 631 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 632 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/upgradeSpecialization");
/*     */     
/* 634 */     if (!ArknightsApplication.enableServer) {
/* 635 */       response.setStatus(400);
/* 636 */       JSONObject jSONObject = new JSONObject(true);
/* 637 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 638 */       jSONObject.put("error", "Bad Request");
/* 639 */       jSONObject.put("message", "server is close");
/* 640 */       return jSONObject;
/*     */     } 
/*     */     
/* 643 */     int skillIndex = JsonBody.getIntValue("skillIndex");
/*     */     
/* 645 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 646 */     if (Accounts.size() != 1) {
/* 647 */       JSONObject jSONObject = new JSONObject(true);
/* 648 */       jSONObject.put("result", Integer.valueOf(2));
/* 649 */       jSONObject.put("error", "无法查询到此账户");
/* 650 */       return jSONObject;
/*     */     } 
/*     */     
/* 653 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 655 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 656 */       response.setStatus(500);
/* 657 */       JSONObject jSONObject = new JSONObject(true);
/* 658 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 659 */       jSONObject.put("error", "Bad Request");
/* 660 */       jSONObject.put("message", "error");
/* 661 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 665 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 667 */     int charInstId = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").getIntValue("charInstId");
/*     */     
/* 669 */     String charId = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).getString("charId");
/*     */     
/* 671 */     JSONArray levelUpCost = ArknightsApplication.characterJson.getJSONObject(charId).getJSONArray("skills").getJSONObject(skillIndex).getJSONArray("levelUpCostCond").getJSONObject(UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).getJSONArray("skills").getJSONObject(skillIndex).getIntValue("specializeLevel")).getJSONArray("levelUpCost");
/*     */     
/* 673 */     for (int i = 0; i < levelUpCost.size(); i++) {
/* 674 */       String id = levelUpCost.getJSONObject(i).getString("id");
/* 675 */       int count = levelUpCost.getJSONObject(i).getIntValue("count");
/* 676 */       UserSyncData.getJSONObject("inventory").put(id, Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue(id) - count));
/*     */     } 
/*     */     
/* 679 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").put("state", Integer.valueOf(2));
/* 680 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").put("targetSkill", Integer.valueOf(skillIndex));
/*     */     
/* 682 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainer").put("state", Integer.valueOf(2));
/*     */     
/* 684 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").put("lastUpdateTime", Long.valueOf((new Date()).getTime() / 1000L));
/*     */     
/* 686 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 688 */     JSONObject playerDataDelta = new JSONObject(true);
/* 689 */     JSONObject modified = new JSONObject(true);
/* 690 */     JSONObject troop = new JSONObject(true);
/* 691 */     JSONObject chars = new JSONObject(true);
/*     */     
/* 693 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/*     */     
/* 695 */     troop.put("chars", chars);
/* 696 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 697 */     modified.put("event", UserSyncData.getJSONObject("event"));
/* 698 */     modified.put("troop", troop);
/* 699 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/*     */     
/* 701 */     JSONObject result = new JSONObject(true);
/* 702 */     playerDataDelta.put("modified", modified);
/* 703 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 704 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 706 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/completeUpgradeSpecialization"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject completeUpgradeSpecialization(@RequestHeader("secret") String secret, HttpServletResponse response, HttpServletRequest request) {
/* 713 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 714 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/completeUpgradeSpecialization");
/*     */     
/* 716 */     if (!ArknightsApplication.enableServer) {
/* 717 */       response.setStatus(400);
/* 718 */       JSONObject jSONObject = new JSONObject(true);
/* 719 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 720 */       jSONObject.put("error", "Bad Request");
/* 721 */       jSONObject.put("message", "server is close");
/* 722 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 729 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 730 */     if (Accounts.size() != 1) {
/* 731 */       JSONObject jSONObject = new JSONObject(true);
/* 732 */       jSONObject.put("result", Integer.valueOf(2));
/* 733 */       jSONObject.put("error", "无法查询到此账户");
/* 734 */       return jSONObject;
/*     */     } 
/*     */     
/* 737 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 739 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 740 */       response.setStatus(500);
/* 741 */       JSONObject jSONObject = new JSONObject(true);
/* 742 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 743 */       jSONObject.put("error", "Bad Request");
/* 744 */       jSONObject.put("message", "error");
/* 745 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 749 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */ 
/*     */     
/* 752 */     int charInstId = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").getIntValue("charInstId");
/* 753 */     int targetSkill = UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").getIntValue("targetSkill");
/*     */     
/* 755 */     int specializeLevel = UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).getJSONArray("skills").getJSONObject(targetSkill).getIntValue("specializeLevel");
/*     */     
/* 757 */     UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)).getJSONArray("skills").getJSONObject(targetSkill).put("specializeLevel", Integer.valueOf(specializeLevel + 1));
/*     */     
/* 759 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").put("state", Integer.valueOf(3));
/* 760 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainee").put("targetSkill", Integer.valueOf(-1));
/*     */     
/* 762 */     UserSyncData.getJSONObject("building").getJSONObject("rooms").getJSONObject("TRAINING").getJSONObject("slot_13").getJSONObject("trainer").put("state", Integer.valueOf(3));
/*     */     
/* 764 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 766 */     JSONObject playerDataDelta = new JSONObject(true);
/* 767 */     JSONObject modified = new JSONObject(true);
/* 768 */     JSONObject troop = new JSONObject(true);
/* 769 */     JSONObject chars = new JSONObject(true);
/*     */     
/* 771 */     chars.put(String.valueOf(charInstId), UserSyncData.getJSONObject("troop").getJSONObject("chars").getJSONObject(String.valueOf(charInstId)));
/*     */     
/* 773 */     troop.put("chars", chars);
/* 774 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 775 */     modified.put("event", UserSyncData.getJSONObject("event"));
/* 776 */     modified.put("troop", troop);
/* 777 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/*     */     
/* 779 */     JSONObject result = new JSONObject(true);
/* 780 */     playerDataDelta.put("modified", modified);
/* 781 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 782 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 784 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/deliveryOrder"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject deliveryOrder(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 791 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 792 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/deliveryOrder");
/*     */     
/* 794 */     if (!ArknightsApplication.enableServer) {
/* 795 */       response.setStatus(400);
/* 796 */       JSONObject jSONObject = new JSONObject(true);
/* 797 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 798 */       jSONObject.put("error", "Bad Request");
/* 799 */       jSONObject.put("message", "server is close");
/* 800 */       return jSONObject;
/*     */     } 
/*     */     
/* 803 */     String slotId = JsonBody.getString("slotId");
/* 804 */     int orderId = JsonBody.getIntValue("orderId");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 810 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 811 */     if (Accounts.size() != 1) {
/* 812 */       JSONObject jSONObject = new JSONObject(true);
/* 813 */       jSONObject.put("result", Integer.valueOf(2));
/* 814 */       jSONObject.put("error", "无法查询到此账户");
/* 815 */       return jSONObject;
/*     */     } 
/*     */     
/* 818 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 820 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 821 */       response.setStatus(500);
/* 822 */       JSONObject jSONObject = new JSONObject(true);
/* 823 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 824 */       jSONObject.put("error", "Bad Request");
/* 825 */       jSONObject.put("message", "error");
/* 826 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 830 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 832 */     if (slotId.equals("slot_24")) {
/* 833 */       UserSyncData.getJSONObject("inventory").put("3003", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("3003") - 2));
/* 834 */       UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + 1000));
/*     */     } 
/* 836 */     if (slotId.equals("slot_14")) {
/* 837 */       UserSyncData.getJSONObject("inventory").put("3003", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("3003") - 4));
/* 838 */       UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + 2000));
/*     */     } 
/* 840 */     if (slotId.equals("slot_5")) {
/* 841 */       UserSyncData.getJSONObject("inventory").put("3003", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("3003") - 6));
/* 842 */       UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + 3000));
/*     */     } 
/*     */     
/* 845 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 847 */     JSONObject playerDataDelta = new JSONObject(true);
/* 848 */     JSONObject modified = new JSONObject(true);
/* 849 */     modified.put("building", UserSyncData.getJSONObject("building"));
/* 850 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 851 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*     */     
/* 853 */     JSONObject result = new JSONObject(true);
/* 854 */     playerDataDelta.put("modified", modified);
/* 855 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 856 */     result.put("playerDataDelta", playerDataDelta);
/*     */     
/* 858 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping(value = {"/deliveryBatchOrder"}, produces = {"application/json;charset=UTF-8"})
/*     */   public JSONObject deliveryBatchOrder(@RequestHeader("secret") String secret, @RequestBody JSONObject JsonBody, HttpServletResponse response, HttpServletRequest request) {
/* 865 */     String clientIp = ArknightsApplication.getIpAddr(request);
/* 866 */     ArknightsApplication.LOGGER.info("[/" + clientIp + "] /building/deliveryBatchOrder");
/*     */     
/* 868 */     if (!ArknightsApplication.enableServer) {
/* 869 */       response.setStatus(400);
/* 870 */       JSONObject jSONObject = new JSONObject(true);
/* 871 */       jSONObject.put("statusCode", Integer.valueOf(400));
/* 872 */       jSONObject.put("error", "Bad Request");
/* 873 */       jSONObject.put("message", "server is close");
/* 874 */       return jSONObject;
/*     */     } 
/*     */     
/* 877 */     JSONArray slotList = JsonBody.getJSONArray("slotList");
/*     */ 
/*     */ 
/*     */     
/* 881 */     List<Account> Accounts = userDao.queryAccountBySecret(secret);
/* 882 */     if (Accounts.size() != 1) {
/* 883 */       JSONObject jSONObject = new JSONObject(true);
/* 884 */       jSONObject.put("result", Integer.valueOf(2));
/* 885 */       jSONObject.put("error", "无法查询到此账户");
/* 886 */       return jSONObject;
/*     */     } 
/*     */     
/* 889 */     Long uid = Long.valueOf(((Account)Accounts.get(0)).getUid());
/*     */     
/* 891 */     if (((Account)Accounts.get(0)).getBan() == 1L) {
/* 892 */       response.setStatus(500);
/* 893 */       JSONObject jSONObject = new JSONObject(true);
/* 894 */       jSONObject.put("statusCode", Integer.valueOf(403));
/* 895 */       jSONObject.put("error", "Bad Request");
/* 896 */       jSONObject.put("message", "error");
/* 897 */       return jSONObject;
/*     */     } 
/*     */ 
/*     */     
/* 901 */     JSONObject UserSyncData = JSONObject.parseObject(((Account)Accounts.get(0)).getUser());
/*     */     
/* 903 */     JSONObject delivered = new JSONObject(true);
/* 904 */     for (int i = 0; i < slotList.size(); i++) {
/* 905 */       String slotId = slotList.getString(i);
/* 906 */       int count = 0;
/* 907 */       if (slotId.equals("slot_24")) {
/* 908 */         count = 1000;
/* 909 */         UserSyncData.getJSONObject("inventory").put("3003", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("3003") - 2));
/* 910 */         UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + count));
/*     */       } 
/* 912 */       if (slotId.equals("slot_14")) {
/* 913 */         count = 2000;
/* 914 */         UserSyncData.getJSONObject("inventory").put("3003", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("3003") - 4));
/* 915 */         UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + count));
/*     */       } 
/* 917 */       if (slotId.equals("slot_5")) {
/* 918 */         count = 3000;
/* 919 */         UserSyncData.getJSONObject("inventory").put("3003", Integer.valueOf(UserSyncData.getJSONObject("inventory").getIntValue("3003") - 6));
/* 920 */         UserSyncData.getJSONObject("status").put("gold", Integer.valueOf(UserSyncData.getJSONObject("status").getIntValue("gold") + count));
/*     */       } 
/*     */       
/* 923 */       JSONArray itemGet = new JSONArray();
/* 924 */       JSONObject GOLD = new JSONObject(true);
/* 925 */       GOLD.put("type", "GOLD");
/* 926 */       GOLD.put("id", "4001");
/* 927 */       GOLD.put("count", Integer.valueOf(count));
/* 928 */       itemGet.add(GOLD);
/* 929 */       delivered.put(slotId, itemGet);
/*     */     } 
/*     */     
/* 932 */     userDao.setUserData(uid, UserSyncData);
/*     */     
/* 934 */     JSONObject playerDataDelta = new JSONObject(true);
/* 935 */     JSONObject modified = new JSONObject(true);
/* 936 */     modified.put("inventory", UserSyncData.getJSONObject("inventory"));
/* 937 */     modified.put("status", UserSyncData.getJSONObject("status"));
/*     */     
/* 939 */     JSONObject result = new JSONObject(true);
/* 940 */     playerDataDelta.put("modified", modified);
/* 941 */     playerDataDelta.put("deleted", new JSONObject(true));
/* 942 */     result.put("playerDataDelta", playerDataDelta);
/* 943 */     result.put("delivered", delivered);
/* 944 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\building.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */