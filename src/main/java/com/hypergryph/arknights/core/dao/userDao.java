/*     */ package BOOT-INF.classes.com.hypergryph.arknights.core.dao;
/*     */ 
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import com.hypergryph.arknights.ArknightsApplication;
/*     */ import com.hypergryph.arknights.core.pojo.Account;
/*     */ import com.hypergryph.arknights.core.pojo.SearchAssistCharList;
/*     */ import com.hypergryph.arknights.core.pojo.SearchUidList;
/*     */ import com.hypergryph.arknights.core.pojo.UserInfo;
/*     */ import java.util.List;
/*     */ import org.springframework.jdbc.core.BeanPropertyRowMapper;
/*     */ import org.springframework.jdbc.core.RowMapper;
/*     */ 
/*     */ 
/*     */ public class userDao
/*     */ {
/*     */   public static List<Account> queryAccountByUid(long uid) {
/*  20 */     String sql = "SELECT * FROM account WHERE uid = ?";
/*  21 */     BeanPropertyRowMapper<Account> rowMapper = new BeanPropertyRowMapper(Account.class);
/*  22 */     Object[] params = { Long.valueOf(uid) };
/*  23 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   public static List<Account> queryAccountBySecret(String secret) {
/*  26 */     String sql = "SELECT * FROM account WHERE secret = ?";
/*  27 */     BeanPropertyRowMapper<Account> rowMapper = new BeanPropertyRowMapper(Account.class);
/*  28 */     Object[] params = { secret };
/*  29 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   public static List<Account> queryAccountByPhone(String phone) {
/*  32 */     String sql = "SELECT * FROM account WHERE phone = ?";
/*  33 */     BeanPropertyRowMapper<Account> rowMapper = new BeanPropertyRowMapper(Account.class);
/*  34 */     Object[] params = { phone };
/*  35 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   
/*     */   public static List<SearchUidList> searchPlayer(String nickName, String nickNumber) {
/*  39 */     String sql = "SELECT uid as uid,user -> '$.status.level' as level FROM account  WHERE user -> '$.status.nickName' LIKE ? AND user -> '$.status.nickNumber' LIKE ?";
/*  40 */     BeanPropertyRowMapper<SearchUidList> rowMapper = new BeanPropertyRowMapper(SearchUidList.class);
/*  41 */     Object[] params = { nickName, nickNumber };
/*  42 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   
/*     */   public static List<UserInfo> queryUserInfo(long uid) {
/*  46 */     String sql = "SELECT uid as uid,user -> '$.status' as status, user -> '$.troop.chars' as chars, user -> '$.social.assistCharList' as socialAssistCharList,assistCharList as assistCharList,friend as friend FROM account WHERE uid = ?";
/*  47 */     BeanPropertyRowMapper<UserInfo> rowMapper = new BeanPropertyRowMapper(UserInfo.class);
/*  48 */     Object[] params = { Long.valueOf(uid) };
/*  49 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   
/*     */   public static List<SearchAssistCharList> SearchAssistCharList(String profession) {
/*  53 */     String sql = "SELECT uid as uid,user -> '$.status' as status, user -> '$.troop.chars' as chars, user -> '$.social.assistCharList' as socialAssistCharList, assistCharList -> ? as assistCharList FROM account WHERE assistCharList -> ?";
/*  54 */     BeanPropertyRowMapper<SearchAssistCharList> rowMapper = new BeanPropertyRowMapper(SearchAssistCharList.class);
/*  55 */     Object[] params = { profession, profession };
/*  56 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   
/*     */   public static List<Account> LoginAccount(String phone, String password) {
/*  60 */     String sql = "SELECT * FROM account  WHERE `phone` = ? and `password` = ?";
/*  61 */     BeanPropertyRowMapper<Account> rowMapper = new BeanPropertyRowMapper(Account.class);
/*  62 */     Object[] params = { phone, password };
/*  63 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*     */   }
/*     */   
/*     */   public static int RegisterAccount(String phone, String password, String secret) {
/*  67 */     String sql = "INSERT INTO account (`phone`, `password`, `secret`, `user`, `mails`, `assistCharList`, `friend`, `ban`) VALUES (?, ?, ?, '{}', '[]', '{}', '{\"list\":[],\"request\":[]}', 0)";
/*  68 */     Object[] params = { phone, password, secret };
/*  69 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*     */   }
/*     */   
/*     */   public static int setAssistCharListData(Long uid, JSONObject assistCharList) {
/*  73 */     String sql = "UPDATE account SET assistCharList = ? WHERE uid = ?";
/*  74 */     Object[] params = { JSON.toJSONString(assistCharList, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }), uid };
/*  75 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*     */   }
/*     */   
/*     */   public static int setMailsData(Long uid, JSONArray mailsData) {
/*  79 */     String sql = "UPDATE account SET mails = ? WHERE uid = ?";
/*  80 */     Object[] params = { JSON.toJSONString(mailsData, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }), uid };
/*  81 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*     */   }
/*     */   
/*     */   public static int setUserData(Long uid, JSONObject userData) {
/*  85 */     String sql = "UPDATE account SET user = ? WHERE uid = ?";
/*  86 */     Object[] params = { JSON.toJSONString(userData, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }), uid };
/*  87 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*     */   }
/*     */   
/*     */   public static int setFriendData(Long uid, JSONObject friendData) {
/*  91 */     String sql = "UPDATE account SET friend = ? WHERE uid = ?";
/*  92 */     Object[] params = { JSON.toJSONString(friendData, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }), uid };
/*  93 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*     */   }
/*     */   
/*     */   public static int setBanStatus(int uid, int status) {
/*  97 */     String sql = "UPDATE account SET ban = ? WHERE uid = ?";
/*  98 */     Object[] params = { JSON.toJSONString(Integer.valueOf(status), new SerializerFeature[] { SerializerFeature.WriteMapNullValue }), Integer.valueOf(uid) };
/*  99 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*     */   }
/*     */   
/*     */   public static String queryMysqlVersion() {
/* 103 */     String sql = "SELECT VERSION()";
/* 104 */     return (String)ArknightsApplication.jdbcTemplate.queryForObject(sql, String.class);
/*     */   }
/*     */   
/*     */   public static List queryNickName(String nickName) {
/* 108 */     String sql = "SELECT uid FROM account WHERE user -> '$.status.nickName' = ?";
/* 109 */     Object[] params = { nickName };
/* 110 */     return ArknightsApplication.jdbcTemplate.queryForList(sql, params);
/*     */   }
/*     */   
/*     */   public static Boolean tableExists(String table_name) {
/* 114 */     String sql = "SHOW TABLES LIKE ?";
/* 115 */     Object[] params = { table_name };
/* 116 */     if (ArknightsApplication.jdbcTemplate.queryForList(sql, params).size() != 0) {
/* 117 */       return Boolean.valueOf(true);
/*     */     }
/* 119 */     return Boolean.valueOf(false);
/*     */   }
/*     */   
/*     */   public static int insertTable() {
/* 123 */     String sql = "\nSET NAMES utf8mb4;\nSET FOREIGN_KEY_CHECKS = 0;\nDROP TABLE IF EXISTS `account`;\nCREATE TABLE `account` (\n\t`uid` INT NOT NULL AUTO_INCREMENT,\n\t`phone` VARCHAR ( 255 ) CHARACTER \n\tSET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,\n\t`password` VARCHAR ( 255 ) CHARACTER \n\tSET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,\n\t`secret` VARCHAR ( 255 ) CHARACTER \n\tSET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,\n\t`user` json NULL,\n\t`mails` json NULL,\n\t`assistCharList` json NULL,\n\t`friend` json NULL,\n\t`ban` INT NULL DEFAULT NULL,\n\tPRIMARY KEY USING BTREE ( `uid` ) \n) ENGINE = INNODB AUTO_INCREMENT = 3 CHARACTER \nSET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;\nSET FOREIGN_KEY_CHECKS = 1;\nALTER TABLE account AUTO_INCREMENT = 10000000;";
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
/*     */     
/* 145 */     return ArknightsApplication.jdbcTemplate.update(sql);
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\da\\userDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */