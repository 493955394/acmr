define(function (require,exports,module) {
    var $ = require('jquery'),
        tree = require('tree'),
        pjax = require('pjax'),
        common = require('common'),
        editjsp = require('editjsp');
    $(document).ready(function(){
        mc('preview-table',0,0,0);
    })
    /**
     * 合并第一列
     * @param tb的id
     * @param colLength要合并的列数
     */
    function mc(tableId, startRow, endRow, col) {
        var tb = document.getElementById(tableId);
        if (col >= tb.rows[0].cells.length) {//第一行的列数
            return;
        }
        if (col == 0) { endRow = tb.rows.length-1; }
        for (var i = startRow; i < endRow; i++) {
            if (tb.rows[startRow].cells[col].innerHTML == tb.rows[i + 1].cells[0].innerHTML) {
                tb.rows[i + 1].removeChild(tb.rows[i + 1].cells[0]);
                tb.rows[startRow].cells[col].rowSpan = (tb.rows[startRow].cells[col].rowSpan | 0) + 1;
            } else {
                startRow = i + 1;
            }
        }
    }
})