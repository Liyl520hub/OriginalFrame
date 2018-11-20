package com.baseapp.common.base.config;

/**
 * Created by Administrator on 2018/2/2 0002.
 *
 * @Desc 配置基类
 */

public class BaseConfig {

    //RxBinding点击事件间隔
    public static final int BUTTON_CLICK_INTERVAL = 1000;

    public static final int SINGLE_ITEM_TYPE = 0;

    /**
     * 默认每页加载数据条数
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String STATUS_BAR_HEIGHT = "StatusBarHeight";

    public static final String AES_PASSWORD = "CoolBit";


    public static class BaseSPKey {
        /**
         * 用户token
         */
        public static final String TOKEN = "token";
        /**
         * 用户的userNo
         */
        public static final String USER_NO = "userNo";

        public static final String USER_ID = "user_id";

        public static final String USER_NICK_NAME = "userNickName";

        public static final String USER_COVER = "userCover";

        public static final String USER_PHONE = "userPhone";

        public static final String USER_PHONE_SECRET = "userPhoneSecret";

        public static final String USER_IM_NAME = "userIMUserName";

        public static final String USER_IM_PASSWORD = "userIMUserName";

        /**
         * 是否设置过头像和昵称，1是0否
         */
        public static final String USER_NICK_NAME_AND_PIC_STATE = "userNickNameAndPicState";

        /**
         * 外网ip key值
         */
        public static final String EXTRANET_IP = "extranetIp";

    }
}
