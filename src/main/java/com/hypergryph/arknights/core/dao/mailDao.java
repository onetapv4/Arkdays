/*    */ package BOOT-INF.classes.com.hypergryph.arknights.core.dao;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*    */ import com.hypergryph.arknights.ArknightsApplication;
/*    */ import com.hypergryph.arknights.core.pojo.Mail;
/*    */ import java.util.List;
/*    */ import org.springframework.jdbc.core.BeanPropertyRowMapper;
/*    */ import org.springframework.jdbc.core.RowMapper;
/*    */ 
/*    */ 
/*    */ public class mailDao
/*    */ {
/*    */   public static List<Mail> queryMails() {
/* 16 */     String sql = "SELECT * from mail";
/* 17 */     BeanPropertyRowMapper<Mail> rowMapper = new BeanPropertyRowMapper(Mail.class);
/* 18 */     return ArknightsApplication.jdbcTemplate.query(sql, (RowMapper)rowMapper);
/*    */   }
/*    */   
/*    */   public static List<Mail> queryMailById(int id) {
/* 22 */     String sql = "SELECT * FROM mail WHERE id = ?";
/* 23 */     BeanPropertyRowMapper<Mail> rowMapper = new BeanPropertyRowMapper(Mail.class);
/* 24 */     Object[] params = { Integer.valueOf(id) };
/* 25 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*    */   }
/*    */   
/*    */   public static List<Mail> queryMailByName(String name) {
/* 29 */     String sql = "SELECT * FROM mail WHERE name = ?";
/* 30 */     BeanPropertyRowMapper<Mail> rowMapper = new BeanPropertyRowMapper(Mail.class);
/* 31 */     Object[] params = { name };
/* 32 */     return ArknightsApplication.jdbcTemplate.query(sql, params, (RowMapper)rowMapper);
/*    */   }
/*    */   
/*    */   public static int setMailName(int id, String name) {
/* 36 */     String sql = "UPDATE mail SET name = ? where id = ?";
/* 37 */     Object[] params = { name, Integer.valueOf(id) };
/* 38 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*    */   }
/*    */   
/*    */   public static int setMailFrom(int id, String from) {
/* 42 */     String sql = "UPDATE mail SET `from` = ? where id = ?";
/* 43 */     Object[] params = { from, Integer.valueOf(id) };
/* 44 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*    */   }
/*    */   
/*    */   public static int setMailSubject(int id, String subject) {
/* 48 */     String sql = "UPDATE mail SET subject = ? where id = ?";
/* 49 */     Object[] params = { subject, Integer.valueOf(id) };
/* 50 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*    */   }
/*    */   
/*    */   public static int setMailContent(int id, String content) {
/* 54 */     String sql = "UPDATE mail SET content = ? where id = ?";
/* 55 */     Object[] params = { content, Integer.valueOf(id) };
/* 56 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*    */   }
/*    */   
/*    */   public static int setMailItems(int id, JSONArray items) {
/* 60 */     String sql = "UPDATE mail SET items = ? where id = ?";
/* 61 */     Object[] params = { JSON.toJSONString(items, new SerializerFeature[] { SerializerFeature.WriteMapNullValue }), Integer.valueOf(id) };
/* 62 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*    */   }
/*    */   
/*    */   public static int createMail(String name) {
/* 66 */     String sql = "insert into mail (`name`, `items`) value (?, '[]');";
/* 67 */     Object[] params = { name };
/* 68 */     return ArknightsApplication.jdbcTemplate.update(sql, params);
/*    */   }
/*    */   
/*    */   public static int insertTable() {
/* 72 */     String sql = "create table mail\n(\n    id      int auto_increment\n        primary key,\n    name  text null,\n    `from`  text null,\n    subject text null,\n    content text null,\n    items   json null\n);";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 82 */     return ArknightsApplication.jdbcTemplate.update(sql);
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\dao\mailDao.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */