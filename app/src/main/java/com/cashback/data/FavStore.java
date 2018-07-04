package com.cashback.data;

/**
 * Created by dev on 5/4/17.
 */

public class FavStore {

    public String store_id;
    public String name,cashback,status,url,image,desc,favourite,shareText;
    public FavStore(String store_id, String name,String cashback,String status,String url,String image,String desc,String fav,String shareTxt)
    {
        this.store_id=store_id;
        this.name=name;
        this.cashback=cashback;
        this.status=status;
        this.url=url;
        this.image=image;
        this.desc=desc;
        this.favourite=fav;
        this.shareText=shareTxt;
    }


}
