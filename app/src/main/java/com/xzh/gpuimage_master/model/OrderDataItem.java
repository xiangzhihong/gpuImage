package com.xzh.gpuimage_master.model;

import android.text.TextUtils;


import com.xzh.gpuimage_master.utils.DateUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class OrderDataItem implements Serializable {

    public static final long serialVersionUID = 8607324404045612391L;
    public int Version = 1;
    public String SellerId;  //卖家Id
    public String OrderId;  //订单编号
    public String OrderGroupId;  //主单号
    public String BuyerId;  //买家名称
    public String BuyerName;  //买家名称
    public String BuyerLogoUrl;  //买家头像地址
    public String LeaveWord;  //买家留言
    public String Remarks;  //卖家备注
    public String OrderTime;  //下单时间
    public int TradingStatus;  //订单状态
    public String TradingStatusText;  //订单状态名称
    public String LogisticsNewStatus = "";  //物流最新状态
    public double ExpectedTotalPayAmount;  //订单应收
    public double ExpectedPayAmount;  //首付款应收(如订金，只需支付一次时=订单应收)
    public double ExpectedPostpayAmount;  //尾款应收
    public double OriginalPostpayAmount;//原始尾款金额(调价前)
    public double Discount;//卖家调价（加价为正，降价为负）
    public double GiftAmount;//红包使用金额
    public double TotalProductPrice;//商品总价（下单时）
    public String CouponCode; //卖家优惠券编号
    public double CouponAmount;//卖家优惠券使用金额
    public String PlatformCouponCode; //平台优惠券编号(保持兼容，和接口字段不一样)
    public double PlatformCouponAmount; //平台优惠券使用总金额(保持兼容，和接口字段不一样)
    public boolean IsBlack;//是否已被列入黑名单(true=已被列入黑名单、false=未被列入黑名单)
    public String ApplyPostPayTime; //发起补款的时间
    public double FreightPrice; //运费
    public boolean BuyerIDCardUploaded; //是否上传身份证
    public boolean BuyerIDCardNeeded; //是否需要上传身份证
    public List<OrderProductEntity> Products; //商品信息列表
    public double ModifyBeforeTailAmount; //原始(修改前)尾款金额(未经调整)
    public boolean PaidInFull; //是否全款支付
    public boolean IsMultiProduct;
    public boolean OnlyNeedPayOnce;//是否需要支付尾款（true，不需要支付尾款，false，需要支付尾款）
    public int IsCancelOrder;//是否可以取消订单(0：可以取消订单、1：不可以取消订单)
    public int TotalProductCount;//订单商品总数


    public String getOrderDate(String dataString) {
        String date = DateUtil.formatDate(dataString, "yyyy.MM.dd HH:mm:ss");
        if (TextUtils.isEmpty(date)) date = dataString;
        return date;
    }

    public List<OrderProductEntity> getProducts() {
        if (Products == null) Products = new ArrayList<>();
        return Products;
    }

    public boolean isMultiProduct() {
        return getMultiProductCount() > 1;
    }

    public int getMultiProductCount(){
        return getProducts().size();
    }

    /*------------------私信会话订单卡片扩展参数----------------*/
    public int Platform = 1; //订单类型(1：C,2：M)
    public boolean isSelected = false;
}
