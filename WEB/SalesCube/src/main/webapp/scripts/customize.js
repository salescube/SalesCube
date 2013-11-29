var obj = {}

obj.utils = {
    run: function(){
        obj.utils.rollOver();
        obj.utils.imgActive();
        obj.utils.sectionToggle();
    }
    ,rollOver: function(){
        $('.btn').each(function(){
            this.oSrc = $(this).attr("src");
            if(this.oSrc.match(/(_on\.gif|_on\.jpg|_on\.png)$/)){
                this.hSrc = this.oSrc;
            }else{
                this.hSrc = this.oSrc.replace(/(\.gif|\.jpg|\.png)$/, "_on$1");
            }
            this.cache = new Image();
            this.cache.src = this.hSrc;
            this.isStay = false;
            
            this.stay = function(){
                this.isStay = true;
                $(this).attr({src: this.hSrc});
            }
            this.unstay = function(){
                this.isStay = false;
                $(this).attr({src: this.oSrc});
            }
        }).hover(function(){
            if(! this.isStay) $(this).attr('src',this.hSrc);
        },function(){
            if(! this.isStay) $(this).attr('src',this.oSrc);
        });
    }
    ,imgActive: function(){
        $('.active').each(function(){
            this.oSrc = $(this).attr("src");
            if(this.oSrc.match(/(_on\.gif|_on\.jpg|_on\.png)$/)){
                this.hSrc = this.oSrc;
            }else{
                this.hSrc = this.oSrc.replace(/(\.gif|\.jpg|\.png)$/, "_on$1");
            }
            this.cache = new Image();
            this.cache.src = this.hSrc;
            
            $(this).attr('src', this.hSrc);
        });
    }
    ,sectionToggle: function(){
        $('.btn_toggle').click(function(){
            $(this).parent('div').parent('div').find('.section_body').slideToggle('fast');
        });
    }
}

$(function(){
    obj.utils.run();
});