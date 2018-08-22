package com.acmr.model.adv;

/**
 * 统计表格中一个单元格
 * 
 * @author zengqu
 * 
 */
public class CStatsCubeCell {
	private int row; // 所在的行号
	private int col; // 所在的列号
	private String data; // 单元格内容
	private int rowspan; // 合并行数 默认为1
	private int colspan; // 合并列数 默认为1
	private String sort; // 单元格类型 row col cell
	private int leftline; // 左边框类型
	private int topline; // 上边框宽度
	private int rightline; // 右边框宽度
	private int bottomline; // 下边框宽度

	public CStatsCubeCell() {

	}

	public CStatsCubeCell(int row1, int col1, int rowspan1, int colspan1, String sort1, String data1, String lines) {
		this.row = row1;
		this.col = col1;
		this.data = data1;
		this.rowspan = rowspan1;
		this.colspan = colspan1;
		this.sort = sort1;
		if (lines.substring(0, 1).equals("1")) {
			this.leftline = 1;
		} else {
			this.leftline = 0;
		}
		if (lines.substring(1, 2).equals("1")) {
			this.topline = 1;
		} else {
			this.topline = 0;
		}
		if (lines.substring(2, 3).equals("1")) {
			this.rightline = 1;
		} else {
			this.rightline = 0;
		}
		if (lines.substring(3, 4).equals("1")) {
			this.bottomline = 1;
		} else {
			this.bottomline = 0;
		}

	}

	/**
	 * 所在行号
	 * 
	 * @return
	 */
	public int getRow() {
		return row;
	}

	/**
	 * 所在行号
	 * 
	 * @return
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * 所在列号
	 * 
	 * @return
	 */
	public int getCol() {
		return col;
	}

	/**
	 * 所在列号
	 * 
	 * @return
	 */
	public void setCol(int col) {
		this.col = col;
	}

	/**
	 * 单元格值
	 * 
	 * @return
	 */
	public String getData() {
		return data;
	}

	/**
	 * 单元格值
	 * 
	 * @return
	 */
	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 合并行数，默认为1
	 * 
	 * @return
	 */
	public int getRowspan() {
		return rowspan;
	}

	/**
	 * 合并行数，默认为1
	 * 
	 * @return
	 */
	public void setRowspan(int rowspan) {
		this.rowspan = rowspan;
	}

	/**
	 * 合并列数，默认为1
	 * 
	 * @return
	 */
	public int getColspan() {
		return colspan;
	}

	/**
	 * 合并列数，默认为1
	 * 
	 * @return
	 */
	public void setColspan(int colspan) {
		this.colspan = colspan;
	}

	/**
	 * 单元格类型,row,col,cell
	 * 
	 * @return
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * 单元格类型,row,col,cell
	 * 
	 * @return
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}

	/**
	 * 左边框宽度
	 * 
	 * @return
	 */
	public int getLeftline() {
		return leftline;
	}

	/**
	 * 左边框宽度
	 * 
	 * @return
	 */
	public void setLeftline(int leftline) {
		this.leftline = leftline;
	}

	/**
	 * 上边框宽度
	 * 
	 * @return
	 */
	public int getTopline() {
		return topline;
	}

	/**
	 * 上边框宽度
	 * 
	 * @return
	 */
	public void setTopline(int topline) {
		this.topline = topline;
	}

	/**
	 * 右边框宽度
	 * 
	 * @return
	 */
	public int getRightline() {
		return rightline;
	}

	/**
	 * 右边框宽度
	 * 
	 * @return
	 */
	public void setRightline(int rightline) {
		this.rightline = rightline;
	}

	/**
	 * 下边框宽度
	 * 
	 * @return
	 */
	public int getBottomline() {
		return bottomline;
	}

	/**
	 * 下边框宽度
	 * 
	 * @return
	 */
	public void setBottomline(int bottomline) {
		this.bottomline = bottomline;
	}

	@Override
	public String toString() {
		return "CStatsCubeCell [row=" + row + ", col=" + col + ", data=" + data + ", rowspan=" + rowspan + ", colspan=" + colspan + ", sort=" + sort + ", leftline=" + leftline + ", topline=" + topline + ", rightline=" + rightline + ", bottomline=" + bottomline + "]";
	}
}
