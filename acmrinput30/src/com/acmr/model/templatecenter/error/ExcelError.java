package com.acmr.model.templatecenter.error;
/**
 * 错误信息类
 * @author caosl
 */
public class ExcelError {
	int row;	//行数
	int col;	//列数
	private String pos;//前台识别A3
	private String ErrorCode;	//错误编码
	private String ErrorMsg;	//错误信息
	public ExcelError(){}
	public ExcelError(int row,int col){
		this.row = row;
		this.col = col;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public String getErrorCode() {
		return ErrorCode;
	}
	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}
	public String getErrorMsg() {
		return ErrorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	
}
