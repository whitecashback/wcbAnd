package com.cashback.data;

/**
 * Created by jack on 16/03/2017.
 */

public class Store {

    public String store_id;
        public String name,cashback,status,url,image,desc,favourite,cashbackrates;
        public Store(String store_id, String name,String cashback,String status,String url,String image,String desc,String fav,String rates)
        {
            this.store_id=store_id;
            this.name=name;
            this.cashback=cashback;
            this.status=status;
            this.url=url;
            this.image=image;
            this.desc=desc;
            this.favourite=fav;
            this.cashbackrates=rates;
        }


    }



