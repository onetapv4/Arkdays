/*    */ package BOOT-INF.classes.com.hypergryph.arknights.game;
/*    */ 
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/templateShop"})
/*    */ public class templateShop
/*    */ {
/*    */   @RequestMapping({"/getGoodList"})
/*    */   public JSONObject getGoodList() {
/* 20 */     return JSON.parseObject("{\"data\":{\"shopId\":\"shop_act13side\",\"shopName\":\"商业展销   \",\"type\":\"SHOP_RARITY_GROUP\",\"price\":{\"id\":\"act13side_token_model\",\"count\":1,\"type\":\"ACTIVITY_ITEM\"},\"iconColorCodes\":\"1a57ca\",\"buttonColorCodes\":\"1a57ca\",\"startTime\":1635739200,\"endTime\":1638129599,\"shopGroup\":{\"act13side_tag_1\":{\"shopGroupId\":\"act13side_tag_1\",\"sortId\":1,\"preGroup\":null,\"startTime\":1635739200,\"lockDesc\":null,\"shopGood\":{\"act13side_1\":{\"goodId\":\"act13side_1\",\"displayName\":null,\"sortId\":1,\"goodType\":\"PROGRESS\",\"item\":null,\"progressGoodId\":\"char_wildmn_progress\",\"price\":0,\"availCount\":0},\"act13side_2\":{\"goodId\":\"act13side_2\",\"displayName\":null,\"sortId\":2,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"7003\",\"count\":1,\"type\":\"TKT_GACHA\"},\"progressGoodId\":null,\"price\":150,\"availCount\":3},\"act13side_3\":{\"goodId\":\"act13side_3\",\"displayName\":null,\"sortId\":3,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"mod_unlock_token\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":75,\"availCount\":2},\"act13side_4\":{\"goodId\":\"act13side_4\",\"displayName\":null,\"sortId\":4,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30125\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":100,\"availCount\":5},\"act13side_5\":{\"goodId\":\"act13side_5\",\"displayName\":null,\"sortId\":5,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30044\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":45,\"availCount\":10},\"act13side_6\":{\"goodId\":\"act13side_6\",\"displayName\":null,\"sortId\":6,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30104\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":40,\"availCount\":10},\"act13side_7\":{\"goodId\":\"act13side_7\",\"displayName\":null,\"sortId\":7,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30084\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":40,\"availCount\":10},\"act13side_8\":{\"goodId\":\"act13side_8\",\"displayName\":null,\"sortId\":8,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30014\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":30,\"availCount\":10},\"act13side_9\":{\"goodId\":\"act13side_9\",\"displayName\":null,\"sortId\":9,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_downlight_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":75,\"availCount\":1},\"act13side_10\":{\"goodId\":\"act13side_10\",\"displayName\":null,\"sortId\":10,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_chair2_R_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":75,\"availCount\":1},\"act13side_11\":{\"goodId\":\"act13side_11\",\"displayName\":null,\"sortId\":11,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_chair_R_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":90,\"availCount\":1},\"act13side_12\":{\"goodId\":\"act13side_12\",\"displayName\":null,\"sortId\":12,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_partition_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":160,\"availCount\":1},\"act13side_13\":{\"goodId\":\"act13side_13\",\"displayName\":null,\"sortId\":13,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_wallpaper_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":85,\"availCount\":1},\"act13side_14\":{\"goodId\":\"act13side_14\",\"displayName\":null,\"sortId\":14,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_floor_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":85,\"availCount\":1},\"act13side_15\":{\"goodId\":\"act13side_15\",\"displayName\":null,\"sortId\":15,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"furni_kazimierz_LED_F_01\",\"count\":1,\"type\":\"FURN\"},\"progressGoodId\":null,\"price\":100,\"availCount\":1}},\"progressGoods\":{\"char_wildmn_progress\":[{\"order\":1,\"price\":200,\"displayName\":\"野鬃的信物\",\"item\":{\"id\":\"p_char_496_wildmn\",\"count\":1,\"type\":\"MATERIAL\"}},{\"order\":2,\"price\":240,\"displayName\":\"野鬃的信物\",\"item\":{\"id\":\"p_char_496_wildmn\",\"count\":1,\"type\":\"MATERIAL\"}},{\"order\":3,\"price\":280,\"displayName\":\"野鬃的信物\",\"item\":{\"id\":\"p_char_496_wildmn\",\"count\":1,\"type\":\"MATERIAL\"}},{\"order\":4,\"price\":320,\"displayName\":\"野鬃的信物\",\"item\":{\"id\":\"p_char_496_wildmn\",\"count\":1,\"type\":\"MATERIAL\"}},{\"order\":5,\"price\":360,\"displayName\":\"野鬃的信物\",\"item\":{\"id\":\"p_char_496_wildmn\",\"count\":1,\"type\":\"MATERIAL\"}}]}},\"act13side_tag_2\":{\"shopGroupId\":\"act13side_tag_2\",\"sortId\":2,\"preGroup\":null,\"startTime\":1635739200,\"lockDesc\":null,\"shopGood\":{\"act13side_16\":{\"goodId\":\"act13side_16\",\"displayName\":null,\"sortId\":16,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30053\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":13,\"availCount\":15},\"act13side_17\":{\"goodId\":\"act13side_17\",\"displayName\":null,\"sortId\":17,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"31053\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":12,\"availCount\":15},\"act13side_18\":{\"goodId\":\"act13side_18\",\"displayName\":null,\"sortId\":18,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"31013\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":12,\"availCount\":15},\"act13side_19\":{\"goodId\":\"act13side_19\",\"displayName\":null,\"sortId\":19,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30103\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":15,\"availCount\":15},\"act13side_20\":{\"goodId\":\"act13side_20\",\"displayName\":null,\"sortId\":20,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"4001\",\"count\":5000,\"type\":\"GOLD\"},\"progressGoodId\":null,\"price\":7,\"availCount\":120},\"act13side_21\":{\"goodId\":\"act13side_21\",\"displayName\":null,\"sortId\":21,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"2004\",\"count\":2,\"type\":\"CARD_EXP\"},\"progressGoodId\":null,\"price\":5,\"availCount\":30},\"act13side_22\":{\"goodId\":\"act13side_22\",\"displayName\":null,\"sortId\":22,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"2003\",\"count\":2,\"type\":\"CARD_EXP\"},\"progressGoodId\":null,\"price\":3,\"availCount\":50},\"act13side_23\":{\"goodId\":\"act13side_23\",\"displayName\":null,\"sortId\":23,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"2002\",\"count\":2,\"type\":\"CARD_EXP\"},\"progressGoodId\":null,\"price\":1,\"availCount\":150},\"act13side_24\":{\"goodId\":\"act13side_24\",\"displayName\":null,\"sortId\":24,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"3303\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":4,\"availCount\":25},\"act13side_25\":{\"goodId\":\"act13side_25\",\"displayName\":null,\"sortId\":25,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"3302\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":2,\"availCount\":50}},\"progressGoods\":{}},\"act13side_tag_3\":{\"shopGroupId\":\"act13side_tag_3\",\"sortId\":3,\"preGroup\":null,\"startTime\":1635739200,\"lockDesc\":null,\"shopGood\":{\"act13side_26\":{\"goodId\":\"act13side_26\",\"displayName\":null,\"sortId\":26,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30012\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":3,\"availCount\":40},\"act13side_27\":{\"goodId\":\"act13side_27\",\"displayName\":null,\"sortId\":27,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30022\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":4,\"availCount\":30},\"act13side_28\":{\"goodId\":\"act13side_28\",\"displayName\":null,\"sortId\":28,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30032\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":4,\"availCount\":30},\"act13side_29\":{\"goodId\":\"act13side_29\",\"displayName\":null,\"sortId\":29,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30042\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":5,\"availCount\":25},\"act13side_30\":{\"goodId\":\"act13side_30\",\"displayName\":null,\"sortId\":30,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30052\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":5,\"availCount\":25},\"act13side_31\":{\"goodId\":\"act13side_31\",\"displayName\":null,\"sortId\":31,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"30062\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":6,\"availCount\":20},\"act13side_32\":{\"goodId\":\"act13side_32\",\"displayName\":null,\"sortId\":32,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"3211\",\"count\":1,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":6,\"availCount\":4},\"act13side_33\":{\"goodId\":\"act13side_33\",\"displayName\":null,\"sortId\":33,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"3401\",\"count\":10,\"type\":\"MATERIAL\"},\"progressGoodId\":null,\"price\":2,\"availCount\":200},\"act13side_34\":{\"goodId\":\"act13side_34\",\"displayName\":null,\"sortId\":34,\"goodType\":\"NORMAL\",\"item\":{\"id\":\"4001\",\"count\":20,\"type\":\"GOLD\"},\"progressGoodId\":null,\"price\":1,\"availCount\":-1}},\"progressGoods\":{}}}},\"playerDataDelta\":{\"modified\":{},\"deleted\":{}}}");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @RequestMapping({"/buyGood"})
/*    */   public JSONObject buyGood() {
/* 29 */     return JSON.parseObject("{\"itemList\":[{\"type\":\"CARD_EXP\",\"id\":\"2002\",\"count\":2}],\"playerDataDelta\":{\"modified\":{\"inventory\":{\"2002\":2215},\"activity\":{\"TYPE_ACT13SIDE\":{\"act13side\":{\"token\":125}}},\"tshop\":{\"shop_act13side\":{\"coin\":125,\"info\":[{\"id\":\"act13side_2\",\"count\":3},{\"id\":\"act13side_20\",\"count\":120},{\"id\":\"act13side_26\",\"count\":40},{\"id\":\"act13side_4\",\"count\":5},{\"id\":\"act13side_5\",\"count\":10},{\"id\":\"act13side_3\",\"count\":2},{\"id\":\"act13side_6\",\"count\":10},{\"id\":\"act13side_7\",\"count\":10},{\"id\":\"act13side_10\",\"count\":1},{\"id\":\"act13side_11\",\"count\":1},{\"id\":\"act13side_8\",\"count\":10},{\"id\":\"act13side_9\",\"count\":1},{\"id\":\"act13side_13\",\"count\":1},{\"id\":\"act13side_14\",\"count\":1},{\"id\":\"act13side_15\",\"count\":1},{\"id\":\"act13side_12\",\"count\":1},{\"id\":\"act13side_16\",\"count\":15},{\"id\":\"act13side_17\",\"count\":15},{\"id\":\"act13side_18\",\"count\":15},{\"id\":\"act13side_19\",\"count\":15},{\"id\":\"act13side_21\",\"count\":30},{\"id\":\"act13side_22\",\"count\":50},{\"id\":\"act13side_24\",\"count\":25},{\"id\":\"act13side_25\",\"count\":50},{\"id\":\"act13side_23\",\"count\":41}],\"progressInfo\":{\"char_wildmn_progress\":{\"count\":1,\"order\":5}}}}},\"deleted\":{}}}");
/*    */   }
/*    */ }


/* Location:              C:\Users\administered\Desktop\LocalArknights 1.9.4\hypergryph-1.9.4 Beta 3.jar!\BOOT-INF\classes\com\hypergryph\arknights\game\templateShop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */