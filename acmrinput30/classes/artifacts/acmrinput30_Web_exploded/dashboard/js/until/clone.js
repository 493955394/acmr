var BUILTIN_OBJECT = {
    '[object Function]': 1,
    '[object RegExp]': 1,
    '[object Date]': 1,
    '[object Error]': 1,
    '[object CanvasGradient]': 1
};

function clone(source) {
    if (typeof source == 'object' && source !== null) {
        var result = source;
        if (source instanceof Array) {
            result = [];
            for (var i = 0, len = source.length; i < len; i++) {
                result[i] = this.clone(source[i]);
            }
        } else if (!BUILTIN_OBJECT[Object.prototype.toString.call(source)]) {
            result = {};
            for (var key in source) {
                if (source.hasOwnProperty(key)) {
                    result[key] = this.clone(source[key]);
                }
            }
        }

        return result;
    }
    return source;
}