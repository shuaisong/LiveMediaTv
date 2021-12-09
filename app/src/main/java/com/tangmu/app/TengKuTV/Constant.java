package com.tangmu.app.TengKuTV;

public class Constant {
    public static final String WXAPP_ID = "wx652797a5d383e165";
    public static final String WXAPP_SECRET = "40f3acdb32113c6063e378ba56259582";
    public static final int PLAYID = 1258540389;
    public static String Pic_IP = "https://tk-1253334841-1258540389.cos.ap-beijing.myqcloud.com/";
    public static String Video_IP = "http://1258540389.vod2.myqcloud.com/";
    public static String img = "http://a3.att.hudong.com/68/61/300000839764127060614318218_950.jpg";
    public static final int[] c_colors = {R.color.c_color1, R.color.c_color2, R.color.c_color3, R.color.c_color4};
    public static final int[] c_bgs = {R.drawable.c_bg1, R.drawable.c_bg2, R.drawable.c_bg3, R.drawable.c_bg4};
    public static String head = img;

//        public static final String IP = "http://tk.quan-oo.com";//测试
//    public static final String IP = "https://api.tengkutv.com";    //正式
        public static final String IP = "http://apitv.tengkutv.com";
    //        public static final String IP = "https://api.ixiaoba.cn";
    //用户密码登录接口
    public static final String passwordLogin = "/Login/passwordLogin";
    //    效验短信验证码及注册接口
    public static final String verifyCodeApiReg = "/Login/verifyCodeApiReg";
    //    发送验证码接口
    public static final String sendCode = "/Login/sendCode";
    //    用户忘记密码接口
    public static final String resetPassword = "/Login/forgetPassword";

    public static final String isBindMobile = "/Login/isBindMobile";
    //   手机号绑定接口
    public static final String mobileBind = "/Login/mobileBind";
    //    第三方登录接口(qq,微信)
    public static final String authCallback = "/Login/authCallback";
    //    视频分类接口
    public static final String videoType = "/Homepage/videoType";
    //    获取首页推荐栏目视频接口
    public static final String homevideoLists = "/Homepage/tvVideoLists";
    //    栏目更换视频接口
    public static final String change = "/Homepage/change";

    //    获取二级分类视频接口
    public static final String twoTypeDetail = "/Homepage/twoTypeDetail";
    //    栏目更多视频接口
    public static final String moreSectionVideo = "/Homepage/moreSectionVideo";
    //    视频详情为你推荐接口
    public static final String recommendVideo = "/Homepage/recommendVideo";
    //    首页视频搜索接口
    public static final String searchs = "/Homepage/searchs";
    //    视频详情接口
    public static final String videoDetail = "/Video/videoDetail";
    //    轮播图接口
    public static final String bannerImg = "/Configuration/bannerImg";
    //    直播间轮播图接口
    public static final String liveBanner = "/Configuration/liveBanner";
    //    听书首页分类
    public static final String bookType = "/Book/bookType";
    //    获取更多图书接口
    public static final String moreBook = "/Book/moreBook";
    //    获取广告接口
    public static final String videoAd = "/Configuration/videoAd";
    //    搜索排名接口
    public static final String searchRanking = "/Homepage/searchRanking";
    //    Tv热搜榜
    public static final String tvHotSearch = "/Homepage/tvVideoHotSearch";
    //    首页视频搜索接口
    public static final String videoSearchs = "/Homepage/videoSearchs";
    //    视频tv搜索
    public static final String tvSearch = "/Configuration/tvVideoSearch";
    //    增加视频搜索量接口
    public static final String addSearchNum = "/Homepage/addSearchNum";
    //    获取二级分类下图书
    public static final String bookTypeDe = "/Book/bookTypeDe";
    //    用户添加收藏接口
    public static final String collect = "/User/addCollection";
    //    用户取消收藏接口
    public static final String unCollect = "/User/unCollection";
    //    获取猜你喜欢接口
    public static final String youMaylikes = "/Book/youMaylikes";
    //    获取听书首页列表接口
    public static final String bookLists = "/Book/bookLists";
    //    获取猜您喜欢更多图书接口
    public static final String youMoreLike = "/Book/youMoreLike";
    //    听书详情接口
    public static final String bookDetail = "/Book/bookDetail";
    //    获取图书搜索排名接口
    public static final String booksearchRanking = "/Book/searchRanking";
    //    增加图书搜索量接口
    public static final String addBookSearchNum = "/Book/addSearchNum";
    //    听书搜索接口
    public static final String bookSearchs = "/Book/bookSearchs";
    //    直播首页接口
    public static final String liveLits = "/Live/liveLits";
    //    获取直播回放接口
    public static final String liveReply = "/Live/liveReply";
    //    获取更多直播接口
    public static final String moreLive = "/Live/moreLive";
    //    获取更多直播回放接口
    public static final String moreReplyLive = "/Live/moreReplyLive";
    //    获取直播详情接口
    public static final String liveDetail = "/Live/liveDetail";
    //    直播预约接口
    public static final String subscribe = "/Live/liveReserve";
    //    取消直播预约接口
    public static final String unLiveReserve = "/Live/unLiveReserve";
    //    获取直播礼物接口
    public static final String liveGift = "/Configuration/liveGift";
    //    获取会员金额接口
    public static final String vipMes = "/Configuration/vipMes";
    //    获取充值金币比例接口
    public static final String gold = "/Configuration/gold";
    //    获取游客id
    public static final String getVisitor = "/Configuration/getVisitorIdentify";
    //    直播礼物消费记录接口
    public static final String liveGiftAdd = "/Live/liveGiftAdd";
    //    获取回放详情
    public static final String getLiveHistory = "/Live/liveReplyDe";
    //    直播评论接口
    public static final String sendEvaluate = "/Live/liveCommentAdd";
    //    直播评论列表接口
    public static final String liveCommentLists = "/Live/liveCommentLists";
    //    直播评论点赞接口
    public static final String liveLike = "/Live/liveLike";
    //    直播评论取消点赞接口
    public static final String unliveLike = "/Live/unliveLike";
    //    获取用户信息接口
    public static final String userMes = "/User/userMes";
    //    配音首页接口
    public static final String dubbingLists = "/Dubbing/dubbingLists";
    //    配音作品 或 评论点赞接口
    public static final String worksLikeAdd = "/Dubbing/worksLikeAdd";
    //    配音作品 或 评论取消点赞接口
    public static final String unworksLike = "/Dubbing/unworksLike";
    //   增加用户作品分享次数
    public static final String shareNum = "/Dubbing/shareNum";
    //    配音作品 或 评论取消点赞接口
    public static final String worksCommentLists = "/Dubbing/worksCommentLists";
    //获取直播精彩回放接口
    public static final String wonderfulReplyLive = "/Live/wonderfulReplyLive";
    //配音素材详情接口
    public static final String dubbingDetail = "/Dubbing/dubbingDetail";
    //获取配音之星接口
    public static final String dubbingStar = "/Dubbing/dubbingStar";
    //用戶是否是配音之星接口
    public static final String isdubbingStar = "/User/dubbingStar";
    //用户作品详情接口
    public static final String userWorksDetail = "/Dubbing/userWorksDetail";
    //    修改用户信息接口
    public static final String ModifyUser = "/User/saveUserMes";
    //    查看个人配音作品接口
    public static final String userWorksLists = "/Dubbing/userWorksLists";
    //    用户作品数量接口
    public static final String userWorksCount = "/User/userWorksCount";
    //    用户添加作品接口
    public static final String addWorks = "/Dubbing/addWorks";
    //    用户反馈信息接口
    public static final String userFeedback = "/User/userFeedback";
    //    用户修改密码接口
    public static final String savePassword = "/User/savePassword";
    //    用户账单接口
    public static final String userBill = "/User/userBill";
    //    用户收藏列表接口
    public static final String collectionList = "/User/collectionList";
    //    启动页广告
    public static final String LaunchAd = "/Configuration/startPageAd";
    //    用户作品评论列表
    public static final String userCOmmentLists = "/User/userCOmmentLists";
    //    用户评论未读数量接口
    public static final String isredNum = "/User/isredNum";
    //    用戶作品点赞列表接口
    public static final String userLikeLists = "/User/userLikeLists";
    //    平台通知消息列表
    public static final String platformMsgList = "/Msg/platformMsgList";
    //    平台通知消息标记为已读
    public static final String platformMsgRead = "/Msg/platformMsgRead";
    //    活动通知消息列表
    public static final String activeMsgList = "/Msg/activeMsgList";
    //    活动通知消息标记为已读
    public static final String activeMsgRead = "/Msg/activeMsgRead";
    //    用户消费信息接口
    public static final String giftList = "/User/giftList";
    //    获取更多配音作品接口
    public static final String moreWorks = "/Dubbing/moreWorks";
    //    tv获取更多配音作品接口
    public static final String worksRecommend = "/Dubbing/worksRecommend";
    //    配音作品评论添加接口
    public static final String worksCommentAdd = "/Dubbing/worksCommentAdd";
    //    获取云点播上传秘钥
    public static final String vodSings = "/Upload/vodSings";
    //    tv广告
    public static final String TvAd = "/Configuration/TvAd";
    //    Tv听书热搜榜
    public static final String tvBookHotSearch = "/Book/tvBookHotSearch";
    //    Tv听书搜索
    public static final String tvBookSearch = "/Book/tvBookSearch";
    //    Tv订单支付回调
    public static final String payCallbacks = "/Order/callbacks";
    //    Tv直播首页数据接口
    public static final String tvLivePage = "/Live/tvLivePage";
    //    Tv直播为你推荐
    public static final String liveReplyRecommend = "/Live/liveReplyRecommend";
    //   检测新版本
    public static final String detectionNewVersion = "/Configuration/detectionNewVersion";
    //   用户领取会员接口
    public static final String userVipReceive = "/Configuration/userVipReceive";


    public static final int changeTheme = 100;
    public static final int SHARE_WECHAT = 1;
    public static final int SHARE_WECHAT_CIRCLE = 2;
    public static final int SHARE_QQ = 3;
    public static final int SHARE_LINK = 4;
    public static final int SHARE_QQ_ZONE = 5;
    public static final int resetPasswordcode = 101;
    public static final int registercode = 102;
    public static final int Login = 103;
    //    获取云文件保存路径
    public static final String getCloudfileSavePath = "/Upload/getCloudfileSavePath1";
    //    获取腾讯云云cos token
    public static final String ossToken = "/Upload/getTemporarySign";
    //    public static String WebSocket = "wss://tk.quan-oo.com/ws:8289";
    public static String WebSocket = "wss://api.ixiaoba.cn/ws1:8289";
    public static String PayFrom = "PayFrom";
    //    下单接口
    public static final String createOrder = "/Order/orderAdd";
    //    支付接口
    public static final String pay = "/Pay/pay";

    //    用户同步信息接口
    public static final String tvRegister = "/TvUser/tvRegister";
    //    下单接口
    public static final String addOrder = "/TvOrder/addOrder";

    //    咪咕鉴权接口
    public static final String authentications = "/TvOrder/authentications";
    //    下单支付
    public static final String payOrder = "/TvOrder/payOrder";
    //    下单支付
    public static final String payStatus = "/TvOrder/payStatus";
    //    产品列表      type 1会员 2单片
    public static final String productLists = "/TvOrder/productLists";

}
