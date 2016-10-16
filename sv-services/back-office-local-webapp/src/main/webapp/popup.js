// JavaScript Document
function messageWindow(title, msg)
{
  var width="300", height="125";
  var left = (screen.width/2) - width/2;
  var top = (screen.height/2) - height/2;
  var styleStr = 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbar=no,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+',top='+top+',screenX='+left+',screenY='+top;
  var msgWindow = window.open("","msgWindow", styleStr);
  var head = '<head><title>'+title+'</title></head>';
  var body = '<center>'+msg+'<br><p><form><input type="button" value="   Done   " onClick="self.close()"></form>';
  msgWindow.document.write(head + body);
}
var popUpWin=0;
function popUpWindow(URLStr, left, top, width, height)
{
  if(popUpWin)
  {
    if(!popUpWin.closed) popUpWin.close();
  }
  popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
function popUpWindowModal(URLStr, left, top, width, height)
{
  popUpWin = showModalDialog(URLStr, 'popUpWin', 'dialogHeight: ' + (height+30) + 'px; dialogLeft: ' + left + 'px; dialogWidth: ' + width + 'px; help: no; scroll: no; status: no ; status bar: no ;copyhistory=yes');
  //document.location.reload();
  return popUpWin;
}

function popUpWindowModalscroll(URLStr, left, top, width, height)
{
  popUpWin = showModalDialog(URLStr, 'popUpWin', 'dialogHeight: ' + (height+30) + 'px; dialogWidth: ' + width + 'px; help: no; scroll: auto; status: no ; status bar: no ;copyhistory=yes');
  //document.location.reload();
  return popUpWin;
}

function popUpWindowModal2(URLStr, left, top, width, height)
{
  popUpWin = showModalDialog(URLStr, window, 'dialogHeight: ' + (height+30) + 'px; dialogLeft: ' + left + 'px; dialogWidth: ' + width + 'px; help: no; scroll: no; status: no ; status bar: no ;copyhistory=yes');
  //document.location.reload();
  return popUpWin;
}


var popUpWinPR=0;
function popUpWindowPagoRechazado(URLStr, left, top, width, height, namePopup)
{
  if(popUpWinPR)
  {
    //if(!popUpWinPR.closed) popUpWinPR.close();
  }
  popUpWinPR = open(URLStr, namePopup, 'toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
}
