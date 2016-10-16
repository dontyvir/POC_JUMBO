// Copyright (c) 2004, AuthorIT Software Corporation Ltd.  All rights reserved.

// -Block-
function toggleBlock(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    if (myDiv.style.display == 'none'){
      showBlock(pstrID);
    } else{
      hideBlock(pstrID);
    }
  }
}
function showBlock(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    myDiv.style.display = 'block';
    var myImage = document.getElementById('i' + pstrID);
    if (myImage){
      myImage.src = 'arrowdown.gif';
      myImage.alt = '';
    }
  }
}
function hideBlock(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    myDiv.style.display = 'none';
    var myImage = document.getElementById('i' + pstrID);
    if (myImage){
      myImage.src = 'arrowright.gif';
      myImage.alt = '';
    }
  }
}

// -Inline-
function toggleInline(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    if (myDiv.style.display == 'none') 
      showInline(pstrID);
    else 
      hideInline(pstrID);
  }
}
function showInline(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    myDiv.style.display = 'inline';
  }
}
function hideInline(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv) {
    myDiv.style.display = 'none';
  }
}

// -Popup-
function togglePopup(pstrID, pstrHID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    if (myDiv.style.display == 'none'){
      showPopup(pstrID, pstrHID);
    } else{
      hidePopup(pstrID);
    }
  }
}
function showPopup(pstrID, pstrHID){
  var myDiv = document.getElementById('d' + pstrID);
  var myAnchor = document.getElementById(pstrHID);
  if (myDiv && myAnchor){
    myDiv.style.visibility = 'visible';
    myDiv.style.display = '';

    // Calculate x and y position
    var intX = getElementLeft(myAnchor);
    var intY = getElementTop(myAnchor) + myAnchor.offsetHeight;
    var extraX = (intX + myDiv.offsetWidth) - document.body.offsetWidth;
    var extraY = (intY + myDiv.offsetHeight) - document.body.offsetHeight;
    if (extraX > 0) { intX -= extraX; }
    if (extraY > 0) { intY -= myDiv.offsetHeight + (myAnchor.offsetHeight * 1.5); }
    if (intX < 0) { intX = 0; }
    if (intY < 0) { intY = 0; }

    // Set x and y position
    myDiv.style.left = intX + "px";
    myDiv.style.top = intY + "px";
  }
}
function hidePopup(pstrID){
  var myDiv = document.getElementById('d' + pstrID);
  if (myDiv){
    myDiv.style.visibility = 'hidden';
    myDiv.style.display = 'none';
  }
}


// -ShowAll-
function toggleAll(pstrClass, pblnUpdate){
  var myAnchor = document.getElementById('aitsa');
  if (myAnchor.innerHTML == 'Show All') {
    showAll(pstrClass);
    if (pblnUpdate){
      myAnchor.innerHTML = 'Hide All';
      myAnchor.title = 'Hide All';
      var myImage = document.getElementById('aitsaad');
      if (myImage){
        myImage.src = 'arrowdown.gif';
        myImage.alt = 'Hide All';
      }
    }
  } else{
    hideAll(pstrClass);
    if (pblnUpdate){
      myAnchor.innerHTML = 'Show All';
      myAnchor.title = 'Show All';
      var myImage = document.getElementById('aitsaad');
      if (myImage){
        myImage.src = 'arrowright.gif';
        myImage.alt = 'Show All';
      }
    }
  }
}
function showAll(pstrClass) {
  var aElts = document.getElementsByTagName('div');
  setDisplay(pstrClass, aElts, 'show', 'Block');
  aElts = document.getElementsByTagName('span');
  setDisplay(pstrClass, aElts, 'show', 'Inline');
}
function hideAll(pstrClass) {
  var aElts = document.getElementsByTagName('td');
  setDisplay(pstrClass, aElts, 'hide', 'Block');
/*
  var aElts = document.getElementsByTagName('div');
  setDisplay(pstrClass, aElts, 'hide', 'Block');
  aElts = document.getElementsByTagName('span');
  setDisplay(pstrClass, aElts, 'hide', 'Inline');
 */
}
function setDisplay(pstrClass, paElts, pstrDisplay, pstrType){
  for (var i=0; i < paElts.length; i++) {
    if (paElts[i].className == pstrClass) {
      eval(pstrDisplay + pstrType + '("' + paElts[i].id + '")')
      //eval(pstrDisplay + pstrType + '("' + paElts[i].id.slice(1) + '")')
    }
  }
}

// -Fns to determine absolute position of an element-
function getElementLeft(pElt){
  var intX = pElt.offsetLeft;
  while ((pElt = pElt.offsetParent) != null){
    intX += pElt.offsetLeft; 
  }
  return intX;
}
function getElementTop(pElt){
  var intY = pElt.offsetTop;
  while((pElt = pElt.offsetParent) != null){
    intY += pElt.offsetTop;
  }
  return intY;
}

// SKP: permite cambiar los estilos de la barra de menú

var cat_objectSelected = null;
var cat_defaultStyleSelected = null;

function cambiarEstilo(element, newStyle) {
	if (cat_objectSelected != null) {
		if (element != cat_objectSelected)
			element.className = newStyle;
	} else 
		element.className = newStyle;
}

function seleccionarElemento(element, newStyle, nextDefaultStyle) {
	if (cat_objectSelected != null) {
		cat_objectSelected.className = cat_defaultStyleSelected;
	}
	element.className = newStyle;
	cat_objectSelected = element;
	cat_defaultStyleSelected = nextDefaultStyle;
}

