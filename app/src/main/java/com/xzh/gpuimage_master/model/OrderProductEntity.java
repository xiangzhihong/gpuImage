package com.xzh.gpuimage_master.model;

import java.io.Serializable;

public class OrderProductEntity implements Serializable {
    public static final long serialVersionUID = 8607324404045612381L;

    public String ActivityId; //活动ID
    public String ProductPic; //商品图片链接地址
    public String PropertyInfo; //商品规格信息
    public String Catalog;
    public String ProductId; //商品ID
    public int ProductNum; //商品数量
    public String ProductTitle; //商品名称
    public int PriceType; //成交价格类型（0：原价、1：新客价、2：VIP价、3：活动价、4：活动-新人价）
    public double Price; //商品原价
    public double ActuallyPrice; //实际价格
    public int LogisticsType; //配送方式（-1：没有选择、0：暂不选择、1：国内快递、2：直邮、3：贝海直邮、4：卖家保税、5：贝海保税、6：认证直邮、7：拼邮）
    public String LogisticsText; //配送方式名称
    public int ProductRefundChannel; //退货方式(0:不支持本土退货、1:官方本土退货、2:商家本土退货)
    public String ProductRefundText; //退货方式文本
    public boolean ProductRefund; //是否退货
    public String SalesRefundCode; //退货退款单号
    public String SalesRefundStatus; // 退货退款状态
    public double SalesRefundAmount; //退货退款金额
    public double CouponAmount; //平台优惠券商品分摊金额
    public double SellerCouponAmount; //卖家优惠券商品分摊金额
    public double FreightPartialAmount; //运费商品分摊金额
    public double DiscountPartialAmount; //订单折扣商品分摊金额
    public boolean FreeShipping; //包邮(true：包邮、false：不包邮)
    public int TariffType; //是否免税(1：免税、0：不包税)


    public static final int PRICE_TYPE_NORMAL = 0;
    public static final int PRICE_TYPE_NEW_CUSTOMERS = 1;
    public static final int PRICE_TYPE_VIP = 2;
}
