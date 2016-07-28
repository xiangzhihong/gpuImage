package com.xzh.gpuimage_master.model;


import com.xzh.gpuimage_master.base.BaseResult;

import java.util.List;

public class DiaryDetailModel extends BaseResult<DiaryDetailModel.ResultEntity> {

    public class ResultEntity {
        public UserInfoEntity UserInfo;
        public FeedInfoEntity FeedInfo;
        public OrderInfoEntity OrderInfo;

        public class UserInfoEntity {
            private String LogoUrl;
            private String UserName;
            private String UserId;
            private boolean IsAttention;
        }

        public class FeedInfoEntity {
            public int CollectionNum;
            public String NoteVersion;
            public String Position;
            public List<TagImage> TagImage;
            public String NoteId;
            public int NoteSource;
            public List<LableGroupInfoEntity> LableGroupInfo;
            public String Content;
            public int CommentCount;
            public String AddTime;
            public int Status = 0;
            public boolean IsCollect = false;
            public List<Tag> CustomTags;
//            public String RecommendedReason;
//            public String LastReviewTime;
//            public String RecommendTime;

            public class LableGroupInfoEntity {
                public int TagValId;
                public String TagVal;
                public int ImageTagType;
            }
        }

        public class OrderInfoEntity {
            public String ProductPic;
            public String OrderId;
            public String SellerId;
            public String ProductDes;
            public String SellerLoginId;
            public String Platform;
            public boolean FreeShipping;
            public String ProductId;
            public int TariffType;
        }

        public Diary buildDiary() {
            Diary diary = new Diary();
            if (FeedInfo != null) {
                diary.NoteId = FeedInfo.NoteId;
                diary.TagImages = FeedInfo.TagImage;
                diary.Status = FeedInfo.Status;
                diary.NoteVersion = FeedInfo.NoteVersion;
                diary.Content = FeedInfo.Content;
                diary.NoteSource = FeedInfo.NoteSource;
                diary.Position = FeedInfo.Position;
                diary.CustomTags = FeedInfo.CustomTags;
            }
            if (UserInfo != null) {
                diary.UserId = UserInfo.UserId;
                diary.UserName = UserInfo.UserName;
            }
            if (OrderInfo != null) {
                diary.ProductDes = OrderInfo.ProductDes;
                diary.OrderId = OrderInfo.OrderId;
                diary.ProductPic = OrderInfo.ProductPic;
            }
            return diary;
        }
    }
}
