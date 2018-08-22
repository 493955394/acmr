define(function(require, exports, module) {
	'use strict';

	function Pagination(cfg) {
		var config = {
			totalPage: cfg.totalPage || 1,
			currentPage: cfg.currentPage || 1,
			posePageCount: cfg.posePageCount || 1,
			currentObj: cfg.currentObj || {},
			url:cfg.url
		}
		this.init(config);
	}
	Pagination.prototype = {
		init: function(config) {
			config.currentObj.html(this.buildHTML(config));
		},
		buildHTML: function(config) {
			var paginationHTML = '',
				i = 1,
				currentPage = config.currentPage,
				totalPage = config.totalPage,
				posePageCount = config.posePageCount,
				url = config.url;
			if (currentPage === 1) {
				paginationHTML += '<li class="disabled"><span>首页</span></li>';
				paginationHTML += '<li class="disabled"><span>上一页</span></li>';
			} else {
				paginationHTML += '<li><a href="'+url+'&pageNum=1'+'">首页</a></li>';
				if(currentPage>=2){
					paginationHTML += '<li><a href="'+url+'&pageNum='+(currentPage-1)+'">上一页</a></li>';
				}else{
					paginationHTML += '<li><a href="'+url+'&pageNum=1'+'">上一页</a></li>';
				}
			}
			if (currentPage > posePageCount) {
				for (i = currentPage - posePageCount; i < currentPage; i++) {
					paginationHTML += '<li><a href="'+url+'&pageNum='+i+'">' + i + '</a></li>';
				}
			} else {
				for (i = 1; i < currentPage; i++) {
					paginationHTML += '<li><a href="'+url+'&pageNum='+i+'">' + i + '</a></li>';
				}
			}
			paginationHTML += '<li class="active"><span>' + currentPage + '</span></li>';
			if (totalPage - currentPage > posePageCount) {
				for (i = currentPage + 1; i <= currentPage + posePageCount; i++) {
					paginationHTML += '<li><a href="'+url+'&pageNum='+i+'">' + i + '</a></li>';
				};
			} else {
				for (i = currentPage + 1; i <= totalPage; i++) {
					paginationHTML += '<li><a href="'+url+'&pageNum='+i+'">' + i + '</a></li>';
				}
			}
			if (currentPage === totalPage) {
				paginationHTML += '<li class="disabled"><span>下一页</span></li>';
				paginationHTML += '<li class="disabled"><span>末页</span></li>';
			} else {
				if(currentPage+2<=totalPage){
					paginationHTML += '<li><a href="'+config.url+'&pageNum='+(currentPage+1)+'">下一页</a></li>';
				}else{
					paginationHTML += '<li><a href="'+config.url+'&pageNum='+totalPage+'">下一页</a></li>';
				}
				paginationHTML += '<li><a href="'+config.url+'&pageNum='+totalPage+'">末页</a></li>';
			}
			return paginationHTML;

		}
	}
	module.exports = Pagination;
});