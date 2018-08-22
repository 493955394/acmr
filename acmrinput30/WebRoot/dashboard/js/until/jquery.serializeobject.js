jQuery.prototype.serializeObject = function() {
    var serializeResult, objectResult, h, i, formElement, len, currentnName,originValue, currentnValue,cacheRepeatObject;
    serializeResult = this.serializeArray();
    objectResult = {};
    cacheRepeatObject = {};
    h = objectResult.hasOwnProperty;
    len = serializeResult.length;
    i = 0;
    for (; i < len; i++) {
        formElement = serializeResult[i];
        currentnName = formElement.name;
        currentValue = formElement.value;
        if (h.call(objectResult, currentnName)) {
            if (!(objectResult[currentnName] instanceof Array)) {
                cacheRepeatObject[currentnName] = [];
                cacheRepeatObject[currentnName].push(objectResult[currentnName]);
            }
            cacheRepeatObject[currentnName].push(currentValue);
            objectResult[currentnName] = cacheRepeatObject[currentnName];
            continue;
        } else {
            objectResult[currentnName] = currentValue;
        }
    }
    return objectResult;
};