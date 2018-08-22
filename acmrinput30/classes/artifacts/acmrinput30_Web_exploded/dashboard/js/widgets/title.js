'use strict';
var widgets = widgets || {};
widgets.title = widgets.title || {};
widgets.paragraph = widgets.paragraph || {};
widgets.layout = widgets.layout || {};

widgets.type = widgets.type || {};
widgets.page = widgets.page || {};
widgets.image = widgets.image || {};
widgets.table = widgets.table || {};
widgets.chart = widgets.chart || {};

widgets.page.collection = function() {
    this.collection = [];
};
widgets.page.collection.prototype = {
    constructor: widgets.page.collection,
    addItem: function(model) {
        this.collection.push(model);
    },
    getPropById: function(cid) {
        var m = 0,
            len = this.collection.length;
        for (; m < len; m++) {
            if (parseInt(cid) === parseInt(this.collection[m].id)) {
                return this.collection[m];
            }
        }
        return null;
    },
    getItemsByExpress: function(transType) {
        var m = 0,
            tmpcollection = this.collection,
            len = tmpcollection.length,
            tmp = [],
            currentItem,
            type;
        for (; m < len; m++) {
            currentItem = tmpcollection[m];
            type = currentItem['express'];
            if (type && type === transType) {
                tmp.push(currentItem);
            }
        }
        return tmp;
    },
    getItemsByType: function(transType) {
        var m = 0,
            tmpcollection = this.collection,
            len = tmpcollection.length,
            tmp = [],
            currentItem,
            type;
        for (; m < len; m++) {
            currentItem = tmpcollection[m];
            type = currentItem['type'];
            if (type && type === transType) {
                tmp.push(currentItem);
            }
        }
        return tmp;
    },
    removeItemById: function(id) {
        var index = this.getIndexById(id);
        this.collection.splice(index, 1);
    },
    getIndexById: function(cid) {
        var m = 0,
            len = this.collection.length;
        for (; m < len; m++) {
            if (parseInt(cid) === parseInt(this.collection[m].id)) {
                return m;
            }
        }
        return -1;
    },
    updateItemById: function(id, model) {
        var index = this.getIndexById(id);
        this.collection[index] = model;
    },
    updatePropById: function(id, propName, propValue) {
        var index = this.getIndexById(id);
        this.collection[index][propName] = propValue;
    }
};
widgets.title.model = function() {
    this.model = {
        id: '',
        content: '这是默认内容，请输入新标题',
        bg: '#fff'
    };
};
widgets.title.model.prototype = {
    constructor: widgets.title.model

};
widgets.paragraph.model = function() {
    this.model = {
        id: '',
        content: '这是默认内容，请输入新文本',
        bg: '#fff'
    };
};
widgets.paragraph.model.prototype = {
    constructor: widgets.paragraph.model
};
widgets.layout.model = function() {
    this.model = {
        id: '',
        bg: '#fff',
        scale: ['1', '1', '1', '1'],
        enlarge: 3,
        row: '1',
        col: '4'
    };
};
widgets.layout.model.prototype = {
    constructor: widgets.layout.model
};
widgets.image.model = function() {
    this.model = {
        id: '',
        url: ''
    };
};
widgets.image.model.prototype = {
    constructor: widgets.image.model
};
widgets.table.model = function() {
    /**
     * [model description]
     * @type {Object}
     * @property {string} [id] [unique identifer of widget]
     * @property {string} [type] [represent table or graphical]
     * @property {string} [limith] [default equal self-adaption, if has diff value, represent adjust height to this value]
     * @property {string} [report] [database unique identifer of this report]
     */
    this.model = {
        id: '',
        limith: '',
        transId: '',
        transName: '',
        //default context {name:'',code:''}
        defaultInfo: [],
        html: '',
        type: 'report',
        express: 'table'
    };
};
widgets.table.model.prototype = {
    constructor: widgets.table.model
};
widgets.chart.model = function() {
    this.model = {
        id: '',
        limith: '',
        transId: '',
        transName: '',
        //default context {name:'',code:''}
        defaultInfo: [],
        formatJson: '',
        type: 'report',
        express: 'chart'
    }
}
widgets.chart.model.prototype = {
    constructor: widgets.chart.model
}
widgets.type.collection = function() {
    this.typeList = ['title', 'paragraph', 'image', 'layout', 'table', 'chart'];
};
widgets.type.collection.prototype = {
    constructor: widgets.type.collection,
    getModelByType: function(type) {
        var currentModel = null,
            list = this.typeList;
        switch (type) {
            case list[0]:
                currentModel = new widgets.title.model();
                break;
            case list[1]:
                currentModel = new widgets.paragraph.model();
                break;
            case list[2]:
                currentModel = new widgets.image.model();
                break;
            case list[3]:
                currentModel = new widgets.layout.model();
                break;
            case list[4]:
                currentModel = new widgets.table.model();
                break;
            case list[5]:
                currentModel = new widgets.chart.model();
                break;
            default:
                break;
        }
        return currentModel;
    }
}