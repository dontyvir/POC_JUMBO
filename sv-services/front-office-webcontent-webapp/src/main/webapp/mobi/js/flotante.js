function actualizaCarro() {
	var requestOptions = {
		'method': 'post',
		'parameters': "",
		'onSuccess': function(REQUEST) {
			if (REQUEST == null) {
				return;
			}
			if (REQUEST.responseXML.getElementsByTagName("total")[0] == null) {
				return;
			}	
			var total = REQUEST.responseXML.getElementsByTagName("total")[0].childNodes[0].nodeValue;
			var total_sf = REQUEST.responseXML.getElementsByTagName("total_sf")[0].childNodes[0].nodeValue;
			var cant_prod = REQUEST.responseXML.getElementsByTagName("cant_prod")[0].childNodes[0].nodeValue;
			if ($('total_carro_compra')) {
				$('total_carro_compra').value = total;
			}
			if ($('total_compra')) {
				$('total_compra').value = total_sf;
			}
			if ($('cant_prod')) {
				$('cant_prod').value = cant_prod;
			}
		}
	};
	new Ajax.Request('TotalCarro', requestOptions);
}
var floatingMenuId = 'carro';
var floatingMenu = {
    targetX: - window.innerWidth,
    targetY: -65,
    hasInner: typeof(window.innerWidth) == 'number',
    hasElement: document.documentElement
        && document.documentElement.clientWidth,
    menu:
        document.getElementById
        ? document.getElementById(floatingMenuId)
        : document.all
          ? document.all[floatingMenuId]
          : document.layers[floatingMenuId]
};
floatingMenu.move = function () {
    if (document.layers) {
        floatingMenu.menu.left = floatingMenu.nextX;
        floatingMenu.menu.top = floatingMenu.nextY;
    } else {
        floatingMenu.menu.style.left = floatingMenu.nextX + 'px';
        floatingMenu.menu.style.top = floatingMenu.nextY + 'px';
    }
}
floatingMenu.computeShifts = function () {
    var de = document.documentElement;
    floatingMenu.shiftX =
        floatingMenu.hasInner
        ? pageXOffset
        : floatingMenu.hasElement
          ? de.scrollLeft
          : document.body.scrollLeft;
    if (floatingMenu.targetX < 0) {
        if (floatingMenu.hasElement && floatingMenu.hasInner) {
            floatingMenu.shiftX +=
                de.clientWidth > window.innerWidth
                ? window.innerWidth
                : de.clientWidth
        } else {
            floatingMenu.shiftX +=
                floatingMenu.hasElement
                ? de.clientWidth
                : floatingMenu.hasInner
                  ? window.innerWidth
                  : document.body.clientWidth;
        }
    }
    floatingMenu.shiftY = 
        floatingMenu.hasInner
        ? pageYOffset
        : floatingMenu.hasElement
          ? de.scrollTop
          : document.body.scrollTop;
    if (floatingMenu.targetY < 0) {
        if (floatingMenu.hasElement && floatingMenu.hasInner) {
            floatingMenu.shiftY +=
                de.clientHeight > window.innerHeight
                ? window.innerHeight
                : de.clientHeight
        } else {
            floatingMenu.shiftY +=
                floatingMenu.hasElement
                ? document.documentElement.clientHeight
                : floatingMenu.hasInner
                  ? window.innerHeight
                  : document.body.clientHeight;
        }
    }
}
floatingMenu.doFloat = function() {
    var stepX, stepY;
    floatingMenu.computeShifts();
    stepX = (floatingMenu.shiftX + 
        floatingMenu.targetX - floatingMenu.nextX) * .3;
    if (Math.abs(stepX) < .5) {
        stepX = floatingMenu.shiftX +
            floatingMenu.targetX - floatingMenu.nextX;
    }
    stepY = (floatingMenu.shiftY + 
        floatingMenu.targetY - floatingMenu.nextY) * .3;
    if (Math.abs(stepY) < .5) {
        stepY = floatingMenu.shiftY + 
            floatingMenu.targetY - floatingMenu.nextY;
    }
    if (Math.abs(stepX) > 0 || Math.abs(stepY) > 0) {
        floatingMenu.nextX += stepX;
        floatingMenu.nextY += stepY;
        floatingMenu.move();
    }
    setTimeout('floatingMenu.doFloat()', 5);
};
floatingMenu.addEvent = function(element, listener, handler) {
    if(typeof element[listener] != 'function' || typeof element[listener + '_num'] == 'undefined') {
        element[listener + '_num'] = 0;
        if (typeof element[listener] == 'function') {
            element[listener + 0] = element[listener];
            element[listener + '_num']++;
        }
        element[listener] = function(e) {
            var r = true;
            e = (e) ? e : window.event;
            for(var i = element[listener + '_num'] -1; i >= 0; i--) {
                if(element[listener + i](e) == false)
                    r = false;
            }
            return r;
        }
    }
    for(var i = 0; i < element[listener + '_num']; i++)
        if(element[listener + i] == handler)
            return;
    element[listener + element[listener + '_num']] = handler;
    element[listener + '_num']++;
};
floatingMenu.init = function() {
    floatingMenu.initSecondary();
    floatingMenu.doFloat();
};
floatingMenu.initSecondary = function() {
    floatingMenu.computeShifts();
    floatingMenu.nextX = floatingMenu.shiftX + floatingMenu.targetX;
    floatingMenu.nextY = floatingMenu.shiftY + floatingMenu.targetY;
    floatingMenu.move();
}
if (document.layers)
    floatingMenu.addEvent(window, 'onload', floatingMenu.init);
else {
    floatingMenu.init();
    floatingMenu.addEvent(window, 'onload', floatingMenu.initSecondary);
}