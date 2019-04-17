define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax=require('pjax'),
        common = require('common'),
        editjsp = require('editjsp'),
        main = require('js/func/zhzs/zsjhEdit/main');


    (function($){
        $.fn.yflockTable = function(options){
            var opts;
            var PLUGIN = $.fn.yflockTable;
            var options = options || {};

            opts = $.extend(true,{},PLUGIN.defaults,options);
            var yflockTable = new yfjs.yflockTable(opts);
            yflockTable.$applyTo = $(this);
            yflockTable.init();
            return yflockTable;

        };
        /*默认参数*/
        $.fn.yflockTable.defaults = {
            //锁定栏
            fixColumnNumber:1
        }
        var yfjs = yfjs || {};
        yfjs.yflockTable = function(options){
            /*设置参数*/
            this.options = options;
            /*起作用的对象*/
            this.$applyTo = this.options.applyTo && $(this.options.applyTo) || null;
            /*要锁定列数*/
            this.fixColumnNumber = options.fixColumnNumber;
            /*显示宽度和高度*/
            this.width = this.options.width;
            this.height = this.options.height;
            /*滚动体宽度*/
            this.scrollWidth = 0;
            this.scrollHeight=0;
        };
        yfjs.yflockTable.prototype = {
            /*初始化*/
            init : function(){
                this.originalTable = this.$applyTo.children("table");
                this.creationHtml();
                this.setHtmlCss();
                this.setSynchronous();
            },
            //把所需的html结构框架动态生成出来
            creationHtml : function(){
                this.$applyTo.append(
                    '<div class="lockTable_container" style="width: '+this.width+'px; height: '+this.height+'px;overflow: hidden;position: relative;">' +
                    '<div class="table_container_fix" style="overflow: hidden;position: absolute;z-index: 100;top:0;left:0;"></div>'+
                    '<div class="table_container_head" style="overflow: hidden;position: absolute;z-index: 50;top:0;left:0;"></div>'+
                    '<div class="table_container_column" style="overflow: hidden;position: absolute;z-index: 40;top:0;left:0;"></div>'+
                    '<div class="table_container_main" style="overflow: auto;width: '+this.width+'px;height: '+this.height * 0.95+'px;"></div>'+

                    //调试用
                    /*'<div class="table_container_fix" style="overflow: hidden;position: absolute;z-index: 100;top:0;left:0;"></div>'+
                    '<div class="table_container_head" style="overflow: hidden;position: absolute;z-index: 50;top:0;left:350px;"></div>'+
                    '<div class="table_container_column" style="overflow: hidden;position: absolute;z-index: 40;top:300px;left:0;"></div>'+
                     '<div class="table_container_main" style="overflow: auto;width: '+this.width+'px;height: '+this.height+'px;position: absolute;z-index: 40;top:300px;left:350px;"></div>'+*/
                    '</div>'
                );

                this.tableFix = this.$applyTo.find(".table_container_fix").append(this.originalTable.clone().addClass("table_fix"));      //左上角固定不动的容器
                this.tableHead = this.$applyTo.find(".table_container_head").append(this.originalTable.clone().addClass("table_head"));    //最上面的头部表容器
                this.tableColumn = this.$applyTo.find(".table_container_column").append(this.originalTable.clone().addClass("table_column"));    //左边列表容器
                this.table_main = this.$applyTo.find(".table_container_main");        //原始表容器
                this.table_main.append(this.originalTable.addClass("table_main").attr("id","table_main"));
                //判断是否出现滚动条
                if(this.table_main.children("table").outerHeight() > this.height){
                    this.scrollWidth=17;
                }
                //判断是否出现横向滚动条
                if(this.table_main.children("table").outerWidth() > this.width){
                    this.scrollHeight=17;
                }

            },
            //设置html  css
            setHtmlCss : function(){
                //判断ie7
                var width = $.support.getSetAttribute ?  0 : this.scrollWidth;
                var lockWidth = 0;
                var lockHeight = this.originalTable.children("thead").outerHeight()+1;
                for(var i=0;i<this.fixColumnNumber;i++){
                    var w = this.originalTable.children("tbody").find("tr:eq(0)").children().eq(i).outerWidth();
                    lockWidth += w;
                };
                lockWidth+=1;
                this.$applyTo.css({"white-space": "nowrap"});

                this.table_main.css({position:"relative"});
                this.tableFix.css({"height":lockHeight,"width":lockWidth}).children("table").css({"width":this.width-width});
                this.tableColumn.css({"height":this.height * 0.95-17,"width":lockWidth-width}).children("table").css({"width":this.width-width});
                this.tableHead.css({"height":lockHeight,"width":this.width-this.scrollWidth}).children("table").css({"width":this.width-width});

                //调试用
                /*this.tableFix.css({"height":250,"width":300}).children("table").css({"width":this.width-width});
                this.tableColumn.css({"height":250,"width":300}).children("table").css({"width":this.width-width});
                this.tableHead.css({"height":250,"width":this.width-this.scrollWidth}).children("table").css({"width":this.width-width});*/
            },
            //设置同步滚动
            setSynchronous : function(){
                var self = this;
                this.table_main.scroll(function () {
                    self.tableHead.scrollLeft($(this).scrollLeft());
                    self.tableColumn.scrollTop($(this).scrollTop());
                });

                if( $(".createtype").size() > 0 ){//如果是作图页面，重锁时记住滚动条位置
                    //$(".table_container_main").scrollLeft();
                }
            },
            anewlock : function(curTable){
                //重新获取表格
                this.originalTable = curTable.clone().removeAttr("id class").addClass("table table-striped");
                //this.originalTable = this.$applyTo.find(".table_main").clone().removeAttr("id").removeClass("table_main");
                this.width=this.$applyTo.find(".table_container_main").width();
                //清空容器表格
                this.$applyTo.html("");
                this.creationHtml();
                this.setHtmlCss();
                this.setSynchronous();
            }
        };
    })($);
    //绑定点击复选框事件
    $('.J_zsjh_rangedata_table').on('pjax:success',function () {
        console.log("pjaxsuccess")

        setTimeout(setTable,500);
        //setTable()

    })
    
    function setTable() {
        var lockCol=$('input[name=lockColNum]').val();
        var w = $("#rangeTable").width(),
            h=$("#rangeTable thead").height(),
            h2=$("#rangeTable tbody").height();
        h2 = h2 >= 650 ? 650 : h2+18;
        $("#rangeTable > table").css('width',w);
        $("#rangeTable").yflockTable({"width":w,"height":h+h2,"fixColumnNumber":lockCol});


        update()
        $(".zb_checkbox").each(function () {
            $(this).change(update)
        })
        $(".reg_checkbox").each(function () {
            $(this).change(update)
        })
    }


    function update(){
        //console.log("change")
        var regnum=$(".table_container_column .reg_checkbox:checked").length;
        var zbnum=$(".table_container_head  .zb_checkbox:checked").length;
        var info="注：已选择地区数"+regnum+"个，已选择指标数"+zbnum+"个"
        $("#check_info").text(info)
    }


})