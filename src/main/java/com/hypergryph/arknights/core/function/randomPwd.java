/*     */ package BOOT-INF.classes.com.hypergryph.arknights.core.function;
/*     */ 
/*     */ import java.security.SecureRandom;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class randomPwd
/*     */ {
/*     */   private static final String lowStr = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
/*     */   private static final String hashStr = "abcdef";
/*     */   private static final String specialStr = "~!@#$%/";
/*     */   private static final String numStr = "0123456789";
/*     */   
/*     */   private static char getRandomChar(String str) {
/*  20 */     SecureRandom random = new SecureRandom();
/*  21 */     return str.charAt(random.nextInt(str.length()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static char getLowChar() {
/*  26 */     return getRandomChar("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
/*     */   }
/*     */ 
/*     */   
/*     */   private static char getUpperChar() {
/*  31 */     return Character.toUpperCase(getLowChar());
/*     */   }
/*     */ 
/*     */   
/*     */   private static char getNumChar() {
/*  36 */     return getRandomChar("0123456789");
/*     */   }
/*     */   
/*     */   private static char getHashChar() {
/*  40 */     return getRandomChar("abcdef");
/*     */   }
/*     */   
/*     */   private static char getSpecialChar() {
/*  44 */     return getRandomChar("~!@#$%/");
/*     */   }
/*     */ 
/*     */   
/*     */   private static char getRandomChar(int funNum) {
/*  49 */     switch (funNum) {
/*     */       case 0:
/*  51 */         return getLowChar();
/*     */       case 1:
/*  53 */         return getUpperChar();
/*     */       case 2:
/*  55 */         return getNumChar();
/*     */     } 
/*  57 */     return getSpecialChar();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRandomPwd(int num) {
/*  64 */     List<Character> list = new ArrayList<>(num);
/*  65 */     list.add(Character.valueOf(getLowChar()));
/*  66 */     list.add(Character.valueOf(getUpperChar()));
/*  67 */     list.add(Character.valueOf(getNumChar()));
/*  68 */     list.add(Character.valueOf(getSpecialChar()));
/*     */     
/*  70 */     for (int i = 4; i < num; i++) {
/*  71 */       SecureRandom random = new SecureRandom();
/*  72 */       int funNum = random.nextInt(4);
/*  73 */       list.add(Character.valueOf(getRandomChar(funNum)));
/*     */     } 
/*     */     
/*  76 */     Collections.shuffle(list);
/*  77 */     StringBuilder stringBuilder = new StringBuilder(list.size());
/*  78 */     for (Character c : list) {
/*  79 */       stringBuilder.append(c);
/*     */     }
/*     */     
/*  82 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String randomKey(int num) {
/*  87 */     List<Character> list = new ArrayList<>(num);
/*  88 */     list.add(Character.valueOf(getLowChar()));
/*  89 */     list.add(Character.valueOf(getUpperChar()));
/*  90 */     list.add(Character.valueOf(getNumChar()));
/*     */     
/*  92 */     for (int i = 4; i < num; i++) {
/*  93 */       SecureRandom random = new SecureRandom();
/*  94 */       int funNum = random.nextInt(4);
/*  95 */       list.add(Character.valueOf(getRandomChar(funNum)));
/*     */     } 
/*     */     
/*  98 */     Collections.shuffle(list);
/*  99 */     StringBuilder stringBuilder = new StringBuilder(list.size());
/* 100 */     for (Character c : list) {
/* 101 */       stringBuilder.append(c);
/*     */     }
/*     */     
/* 104 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String randomHash(int num) {
/* 109 */     List<Character> list = new ArrayList<>(num);
/* 110 */     for (int i = 0; i < num; i++) {
/* 111 */       list.add(Character.valueOf(getHashChar()));
/*     */     }
/*     */     
/* 114 */     Collections.shuffle(list);
/* 115 */     StringBuilder stringBuilder = new StringBuilder(list.size());
/* 116 */     for (Character c : list) {
/* 117 */       stringBuilder.append(c);
/*     */     }
/*     */     
/* 120 */     return stringBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\core\function\randomPwd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */