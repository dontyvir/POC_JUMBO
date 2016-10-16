// This file and its contents are copyrighted material and
// are the physical and intellectual property of RenderBar Imaging™.
// By distributing, viewing, modifying, or otherwise using this
// material, you are binding yourself to, and are in complete
// agreement with, our stated license agreement found at:
// http://www.renderbar.com/secure/agree.php


// expose object interfaces
Barcode.constructor                 = Barcode;
Barcode.prototype.codeA             = getA;
Barcode.prototype.codeB             = getB;
Barcode.prototype.codeC             = getC;
Barcode.prototype.Shift             = getShift;
Barcode.prototype.FNC1              = getFNC1;
Barcode.prototype.FNC2              = getFNC2;
Barcode.prototype.FNC3              = getFNC3;
Barcode.prototype.FNC4              = getFNC4;
Barcode.prototype.ToA               = getToA;
Barcode.prototype.ToB               = getToB;
Barcode.prototype.ToC               = getToC;
Barcode.prototype.encode            = encode;
Barcode.prototype.codeTableChar     = new Array(
                                                195,
                                                196,
                                                197,
                                                198,
                                                199,
                                                200,
                                                201,
                                                202,
                                                203,
                                                204,
                                                205,
                                                198
                                               );
// A = 203; B = 204; C = 205;
// FNC1 = 202; FNC2 = 197;
// FNC3 = 196; FNC4 = 195;
// ToA = 201; ToB = 200; ToC = 199
// Shift = 198
Barcode.prototype.getCheckDigit     = getCheckDigit;
Barcode.prototype.getCodeTable      = getCodeTable;
Barcode.prototype.getHeight         = getHeight;
Barcode.prototype.getIsReadable     = getIsReadable;
Barcode.prototype.getIsCompressed   = getIsCompressed;
Barcode.prototype.getParentElement  = getParentElement;
Barcode.prototype.getRatio          = getRatio;
Barcode.prototype.getType           = getType;
Barcode.prototype.getValue          = getValue;
Barcode.prototype.getWidth          = getWidth;
Barcode.prototype.hasCheckDigit     = hasCheckDigit;
Barcode.prototype.innerHTML         = innerHTML;
Barcode.prototype.setCodeTable      = setCodeTable;
Barcode.prototype.setHeight         = setHeight;
Barcode.prototype.setIsReadable     = setIsReadable;
Barcode.prototype.setParentElement  = setParentElement;
Barcode.prototype.setRatio          = setRatio;
Barcode.prototype.setType           = setType;
Barcode.prototype.setValue          = setValue;
Barcode.prototype.setWidth          = setWidth;
Barcode.prototype.types             = new Array(
                                                "codabar",
                                                "code128",
                                                "code25",
                                                "code39",
                                                "ean13",
                                                "ean8",
                                                "itf",
                                                "msi",
                                                "upca",
                                                "upce"
                                               );
// 0 - 9 respectively
// default local variable values
var A           = 203;
var B           = 204;
var C           = 205;
var Shift       = 198;
var FNC1        = 202;
var FNC2        = 197;
var FNC3        = 196;
var FNC4        = 195;
var ToA         = 201;
var ToB         = 200;
var ToC         = 199;
var checkDigit  = 0;
var codeTable   = 204;
var compress    = false;
var encoded     = "";
var hasCheck    = false;
var height      = 25;
var html        = "";
var parentEl    = null;
var readable    = true;
var ratio       = "3:1";
var type        = "code39";
var value       = "RenderBar";
var width       = 1;
var img_dir		= "img/";
// interface functionality
function Barcode(){}
function encode(input, type, check, autoCompress, table){
  if (input == null)      return false;
  if (input.length == 0)  return false;  
  setType(type);
  setCodeTable(table);
  value           = input;
  hasCheck        = (check        ? true : false);
  compress        = (autoCompress ? true : false);
  encoded         = "";
  checkDigit = 0;
  switch (type){
    case "codabar" : CodaBar(value);          break;
    case "code128" : Code128(value);          break;
    case "code25"  : Code25(value);           break;
    case "code39"  : Code39(value);           break;
    case "ean13"   : ean13(value);            break;
    case "ean8"    : EAN8(value);             break;
    case "itf"     : ITF(value);              break;
    case "msi"     : MSI(value);              break;
    case "upca"    : UPCA(value);             break;
    case "upce"    : UPCE(value);             break;
  }
  getHTML();
}
function getA()     {return A;}
function getB()     {return B;}
function getC()     {return C;}
function getFNC1()  {return FNC1;}
function getFNC2()  {return FNC2;}
function getFNC3()  {return FNC3;}
function getFNC4()  {return FNC4;}
function getToA()   {return ToA;}
function getToB()   {return ToB;}
function getToC()   {return ToC;}
function getCheckDigit(){return checkDigit;}
function getCodeTable(){
  switch (codeTable){
    case A: return "A"; break;
    case B: return "B"; break;
    case C: return "C"; break;
  }
}
function getHeight(){return height;}
function getHTML(){
  var chr       = 0;
  var chrCount  = 0;
  var gif       = "";
  var htm       = "";
  if (isNaN(height)) h = 25;
  if (isNaN(width)) w = 1;
  if (height < 5) height = 25;
  if (width  < 1) width  = 1;
  for (var i = 0; i < encoded.length; i++){ 
    chr      = parseInt(encoded.substr(i, 1));
    chrCount = 1;
    if (i == encoded.length) break;
    while (chr == parseInt(encoded.substr(i + 1, 1))){
      chrCount++;
      i++;
      if (i > encoded.length) break;
    }
    gif = ((chr == 1) ? "b.gif" : "w.gif");
    htm += "<img src\=\"" + img_dir + gif + "\" style=\"height:'" + height + "'\; width:'" + (width * chrCount) + "'\;\">";
  }
  if (readable){
    if (type == "code128"){ value = value.replace(/(\W|\D)*/, ""); }
    htm += "<br><div style=\"display:'inline'; text-align:'center';\">" + value + "</div>";
  }
  html = "<div style=\"display:'inline'; text-align:'center';\">" + htm + "</div>";
  if (parentEl != null) parentEl.innerHTML = html;
}
function getIsCompressed()  {return compress;}
function getIsReadable()    {return readable;}
function getParentElement() {return parentEl;}
function getRatio()         {return ratio;}
function getShift()         {return Shift;}
function getType()          {return type;}
function getValue()         {return value;}
function getWidth()         {return width;}
function hasCheckDigit()    {return hasCheck;}
function innerHTML()        {return html;}
function setCodeTable(chr){
  if (chr == null) return false;
  chr = (isNaN(chr) ? chr.charCodeAt(0) : chr);
  chr = chr.toString();
  chr = chr.toLowerCase();
  var exp = new RegExp("(^[?i:abc]{1}$)+|(^20[3-5]{1}?$)+");
  if (!exp.test(chr)) return false;
  exp = new RegExp("^[?i:abc]{1}$");
  chr = (exp.test(chr) ? chr.charCodeAt(0) + 106 : chr);
  codeTable = chr;
  return true;
}
function setHeight(h){
  if (isNaN(h)) return;
  if (h < 5) h = 5;
  height = h;
}
function setIsReadable(yes){readable = yes;}
function setParentElement(e){
  parentEl = (document.all ? document.all[e] : document.getElementById(e));
}
function setRatio(r){
  if (!r) return;
  switch (r){
    case "2:1":
    case "3:1": break;
    default:    return;
  }
  ratio = r;
}
function setType(barType){
  var exp = new RegExp("^([0-9]{1}|(codabar|code128|code25|code39|ean13|ean8|itf|msi|upca|upce){1})$");
  var exp = new RegExp();
  if (barType == null) return false;
  barType   = (isNaN(barType) ? barType.toLowerCase() : barType);
  if (!exp.test(barType)) return false;
  type = (isNaN(barType) ? barType : types[barType]);
  return true;
}
function setValue(val){value = val;}
function setWidth(w){
  if (isNaN(w)) return;
  //if (w < 5) w = 5;
  width = w;
}
// barcoding functions
function CodaBar(input){
  var charIndex     = 0;
  var charSet       = new Array();
  var chr           = "";
  var encodeFormat  = "";
  var exp           = new RegExp("^[?i:a-d]{1}[?i:0-9a-d\-\$\:\/\.\+]+[?i:a-d]{1}$");
  var pad           = "0";
  if (!exp.test(input)) return "";
  input             = input.toUpperCase();
  charSet[0]  = "0000011";    // 0
  charSet[1]  = "0000110";    // 1
  charSet[2]  = "0001001";    // 2
  charSet[3]  = "1100000";    // 3
  charSet[4]  = "0010010";    // 4
  charSet[5]  = "1000010";    // 5
  charSet[6]  = "0100001";    // 6
  charSet[7]  = "0100100";    // 7
  charSet[8]  = "0110000";    // 8
  charSet[9]  = "1001000";    // 9
  charSet[10] = "0001100";    // -
  charSet[11] = "0011000";    // $
  charSet[12] = "1000101";    // :
  charSet[13] = "1010001";    // /
  charSet[14] = "1010100";    // .
  charSet[15] = "0010101";    // +
  charSet[16] = "0011010";    // A
  charSet[17] = "0101001";    // B
  charSet[18] = "0001011";    // C
  charSet[19] = "0001110";    // D
  for (var i = 0; i < input.length; i++){
    chr = input.substr(i, 1);
    switch (true){
      case (!isNaN(chr)) : charIndex = parseInt(chr);          break;
      case (chr == "-")  : charIndex = 10;                     break;
      case (chr == "$")  : charIndex = 11;                     break;
      case (chr == ":")  : charIndex = 12;                     break;
      case (chr == "/")  : charIndex = 13;                     break;
      case (chr == ".")  : charIndex = 14;                     break;
      case (chr == "+")  : charIndex = 15;                     break;
      default            : charIndex = chr.charCodeAt(0) - 49; break;
    }
    encoded += charSet[charIndex];
  } 
  input   = encoded;
  encoded = "";
  for (var i = 0; i < input.length; i += 7){
    encodeFormat = input.substr(i, 7);
    for (var j = 0; j < 7; j++){
      if ((j & 1) == 1){
          encoded += ((encodeFormat.substr(j, 1) == 1) ? "00" : "0");
      }else{
          encoded += ((encodeFormat.substr(j, 1) == 1) ? "11" : "1");
      }
    }
    if ((i + 7) < input.length) encoded += pad;
  }
  return encoded;
}
function Code128(input){
  if (input.length == 0) return "";
  var chr          = 0;
  var charSet      = new Array();
  var curCodeTable = 0;
  var startChar    = "";
  var stopChar     = "1100011101011";
  charSet[0]       = "11011001100";
  charSet[1]       = "11001101100";
  charSet[2]       = "11001100110";
  charSet[3]       = "10010011000";
  charSet[4]       = "10010001100";
  charSet[5]       = "10001001100";
  charSet[6]       = "10011001000";
  charSet[7]       = "10011000100";
  charSet[8]       = "10001100100";
  charSet[9]       = "11001001000";
  charSet[10]      = "11001000100";
  charSet[11]      = "11000100100";
  charSet[12]      = "10110011100";
  charSet[13]      = "10011011100";
  charSet[14]      = "10011001110";
  charSet[15]      = "10111001100";
  charSet[16]      = "10011101100";
  charSet[17]      = "10011100110";
  charSet[18]      = "11001110010";
  charSet[19]      = "11001011100";
  charSet[20]      = "11001001110";
  charSet[21]      = "11011100100";
  charSet[22]      = "11001110100";
  charSet[23]      = "11101101110";
  charSet[24]      = "11101001100";
  charSet[25]      = "11100101100";
  charSet[26]      = "11100100110";
  charSet[27]      = "11101100100";
  charSet[28]      = "11100110100";
  charSet[29]      = "11100110010";
  charSet[30]      = "11011011000";
  charSet[31]      = "11011000110";
  charSet[32]      = "11000110110";
  charSet[33]      = "10100011000";
  charSet[34]      = "10001011000";
  charSet[35]      = "10001000110";
  charSet[36]      = "10110001000";
  charSet[37]      = "10001101000";
  charSet[38]      = "10001100010";
  charSet[39]      = "11010001000";
  charSet[40]      = "11000101000";
  charSet[41]      = "11000100010";
  charSet[42]      = "10110111000";
  charSet[43]      = "10110001110";
  charSet[44]      = "10001101110";
  charSet[45]      = "10111011000";
  charSet[46]      = "10111000110";
  charSet[47]      = "10001110110";
  charSet[48]      = "11101110110";
  charSet[49]      = "11010001110";
  charSet[50]      = "11000101110";
  charSet[51]      = "11011101000";
  charSet[52]      = "11011100010";
  charSet[53]      = "11011101110";
  charSet[54]      = "11101011000";
  charSet[55]      = "11101000110";
  charSet[56]      = "11100010110";
  charSet[57]      = "11101101000";
  charSet[58]      = "11101100010";
  charSet[59]      = "11100011010";
  charSet[60]      = "11101111010";
  charSet[61]      = "11001000010";
  charSet[62]      = "11110001010";
  charSet[63]      = "10100110000";
  charSet[64]      = "11100001100";
  charSet[65]      = "10010110000";
  charSet[66]      = "10010000110";
  charSet[67]      = "10000101100";
  charSet[68]      = "10000100110";
  charSet[69]      = "10110010000";
  charSet[70]      = "10110000100";
  charSet[71]      = "10011010000";
  charSet[72]      = "10011000010";
  charSet[73]      = "10000110100";
  charSet[74]      = "10000110010";
  charSet[75]      = "11000010010";
  charSet[76]      = "11001010000";
  charSet[77]      = "11110111010";
  charSet[78]      = "11000010100";
  charSet[79]      = "10001111010";
  charSet[80]      = "10100111100";
  charSet[81]      = "10010111100";
  charSet[82]      = "10010011110";
  charSet[83]      = "10111100100";
  charSet[84]      = "10011110100";
  charSet[85]      = "10011110010";
  charSet[86]      = "11110100100";
  charSet[87]      = "11110010100";
  charSet[88]      = "11110010010";
  charSet[89]      = "11011011110";
  charSet[90]      = "11011110110";
  charSet[91]      = "11110110110";
  charSet[92]      = "10101111000";
  charSet[93]      = "10100011110";
  charSet[94]      = "10001011110";
  charSet[95]      = "10111101000";
  charSet[96]      = "10111100010";
  charSet[97]      = "11110101000";
  charSet[98]      = "11110100010";
  charSet[99]      = "10111011110";
  charSet[100]     = "10111101110";
  charSet[101]     = "11101011110";
  charSet[102]     = "11110101110";
  charSet[103]     = "11010000100";
  charSet[104]     = "11010010000";
  charSet[105]     = "11010011100";
  curCodeTable     = codeTable;
  checkDigit       = codeTable - 100;
  startChar        = charSet[checkDigit];
  if (curCodeTable == C) input += ((input.length % 2 != 0) ? "0" : "");
  for (var i = 0; i < input.length; i++){
    chr = input.charCodeAt(i);
    if (chr == ToA || chr == ToB || chr == ToC){
      switch (curCodeTable){
        case A: if (chr == ToC){curCodeTable = C;}else{if (chr == ToB) curCodeTable = B;}  break;
        case B: if (chr == ToC){curCodeTable = C;}else{if (chr == ToA) curCodeTable = A;}  break;
        case C: if (chr == ToB){curCodeTable = B;}else{if (chr == ToA) curCodeTable = A;}  break;
      }
    }
    if (curCodeTable == A || curCodeTable == B){
      switch (true){
        case chr < 32              : chr += 64;  break;
        case chr > 31 && chr < 128 : chr -= 32;  break;
        case chr > 127             : chr -= 100; break;
      }
    }else{
      chr = ((chr < 200) ? parseInt(input.substr(i, 2)) : chr - 100);
    }
    i++;
    if (curCodeTable == C){
      if (chr < 100){
        checkDigit += chr * (parseInt(i / 2) + 1);
        i++;
      }else{
        checkDigit += chr * i;
      }
    }else{
      checkDigit += chr * i;
    }
    i--;
    encoded += charSet[chr];
  }
  checkDigit %= 103;
  encoded = startChar + encoded + charSet[checkDigit] + stopChar;
  return encoded;
}
function Code25(input){
  var charSet       = new Array();
  var chr           = 0;
  var encodeFormat  = "";
  var evenSum       = 0;
  var exp           = new RegExp("^[0-9]+$");    
  var oddSum        = 0;
  var padd          = "0";
  var startChar     = "1110111010";
  var stopChar      = "111010111";
  if (!exp.test(input)) return ""; 
  // numbers 0 to 9   
  charSet[0] = "00110";
  charSet[1] = "10001";
  charSet[2] = "01001";
  charSet[3] = "11000";
  charSet[4] = "00101";
  charSet[5] = "10100";
  charSet[6] = "01100";
  charSet[7] = "00011";
  charSet[8] = "10010";
  charSet[9] = "01010";
  for (var i = 0; i < input.length; i++){
    chr = parseInt(input.substr(i, 1));
    if (i % 2 == 0){
      oddSum  += chr;
    }else{
      evenSum += chr;
    }
    encoded += charSet[chr];
  }
  input = "";
  checkDigit = 10 - ((evenSum + (oddSum * 3)) % 10);
  if (checkDigit == 10) checkDigit = 0;
  encoded += charSet[checkDigit];
  for (var i = 0; i < encoded.length; i += 5){
    encodeFormat = encoded.substr(i, 5);
    for (var j = 0; j < 5; j++){
      input += ((parseInt(encodeFormat.substr(j, 1)) == 1) ? "111" : "1");
      input += padd;
    }
  }
  encoded = input;
  encoded = startChar + encoded + stopChar;
  return encoded;
}
function Code39(input){
  var charIndex    = 0;
  var charSet      = new Array(43);
  var chr          = "";
  var encodeFormat = "";
  var exp          = new RegExp("[a-zA-Z0-9\-\. \$\/\+\%]");
  var guard        = "010010100";
  var padd         = "0";
  var stripeRatio  = (ratio == "3:1" ? "000" : "00");
  var barRatio     = (ratio == "3:1" ? "111" : "11");
  // numbers 0 to 9
  charSet[0]  = "000110100";
  charSet[1]  = "100100001";
  charSet[2]  = "001100001";
  charSet[3]  = "101100000";
  charSet[4]  = "000110001";
  charSet[5]  = "100110000";
  charSet[6]  = "001110000";
  charSet[7]  = "000100101";
  charSet[8]  = "100100100";
  charSet[9]  = "001100100";
  // letters A to Z
  charSet[10] = "100001001";
  charSet[11] = "001001001";
  charSet[12] = "101001000";
  charSet[13] = "000011001";
  charSet[14] = "100011000";
  charSet[15] = "001011000";
  charSet[16] = "000001101";
  charSet[17] = "100001100";
  charSet[18] = "001001100";
  charSet[19] = "000011100";
  charSet[20] = "100000011";
  charSet[21] = "001000011";
  charSet[22] = "101000010";
  charSet[23] = "000010011";
  charSet[24] = "100010010";
  charSet[25] = "001010010";
  charSet[26] = "000000111";
  charSet[27] = "100000110";
  charSet[28] = "001000110";
  charSet[29] = "000010110";
  charSet[30] = "110000001";
  charSet[31] = "011000001";
  charSet[32] = "111000000";
  charSet[33] = "010010001";
  charSet[34] = "110010000";
  charSet[35] = "011010000";
  // allowed symbols - . SP $ / + %
  charSet[36] = "010000101";
  charSet[37] = "110000100";
  charSet[38] = "011000100";
  charSet[39] = "010101000";
  charSet[40] = "010100010";
  charSet[41] = "010001010";
  charSet[42] = "000101010";
  while (input.indexOf("*") != -1) input = input.replace("*", "");
  input = input.toUpperCase();
  for (i = 0; i < input.length; i++){
    chr = input.substr(i, 1);
    if (!exp.test(chr)){encoded = ""; return "";}
    switch (true){
      case chr == "-"     : charIndex = 36;                     break;
      case chr == "."     : charIndex = 37;                     break;
      case chr == " "     : charIndex = 38;                     break;
      case chr == "$"     : charIndex = 39;                     break;
      case chr == "/"     : charIndex = 40;                     break;
      case chr == "+"     : charIndex = 41;                     break;
      case chr == "%"     : charIndex = 42;                     break;
      case !isNaN(chr)    : charIndex = parseInt(chr);          break;
      default             : charIndex = chr.charCodeAt(0) - 55; break;
    }
    checkDigit += charIndex;
    encoded         += charSet[charIndex];
  }
  checkDigit       %= 43;
  if (hasCheck) encoded += charSet[checkDigit];
  encoded = guard + encoded + guard;
  input  = "";
  for (i = 0; i < encoded.length; i += 9){  
    encodeFormat = encoded.substr(i, 9);
    for (j = 0; j < 9; j++){  
      if ((j & 1) == 1){
        input += ((encodeFormat.substr(j, 1) == 1) ? stripeRatio : "0");
      }else{ 
        input += ((encodeFormat.substr(j, 1) == 1) ? barRatio : "1");
      }  
    }
    input += padd;
  }
  encoded = input;
  return encoded;
}
function ean13(input){
  var center            = "01010";
  var charSet           = new Array();
  var encodeSupplement  = false;
  var evenSum           = 0;
  var exp               = new RegExp("^[0-9]+$");
  var guard             = "101";
  var numberSystem      = 0;
  var oddSum            = 0;
  var parity            = 0;
  var paritySequence    = "";
  var supplement        = "";
  if (!exp.test(input)) return "";
  switch (input.length){
    case 12:
    case 14:
    case 17: if (input.length > 12){
                supplement       = input.substr(12, input.length - 12);
                input            = input.substr(0, 12);
                encodeSupplement = true;
                break;
             }
    case 13:
    case 15:
    case 18: if (input.length > 13){
               supplement       = input.substr(13, input.length - 13);
               checkDigit  = input.substr(12, 1);
               encodeSupplement = True;
             }else{
               checkDigit  = input.substr(input.length - 1, 1);
             }
             input = input.substr(0, 12);
             break;
    default: return ""; break;
  }
  if (encodeSupplement) supplement = EncodeSupplement(supplement);
  // left even parity
  charSet[0]  = "0001101";
  charSet[1]  = "0011001";
  charSet[2]  = "0010011";
  charSet[3]  = "0111101";
  charSet[4]  = "0100011";
  charSet[5]  = "0110001";
  charSet[6]  = "0101111";
  charSet[7]  = "0111011";
  charSet[8]  = "0110111";
  charSet[9]  = "0001011";
  // left odd parity
  charSet[10] = "0100111";
  charSet[11] = "0110011";
  charSet[12] = "0011011";
  charSet[13] = "0100001";
  charSet[14] = "0011101";
  charSet[15] = "0111001";
  charSet[16] = "0000101";
  charSet[17] = "0010001";
  charSet[18] = "0001001";
  charSet[19] = "0010111";
  // right char set
  charSet[20] = "1110010";
  charSet[21] = "1100110";
  charSet[22] = "1101100";
  charSet[23] = "1000010";
  charSet[24] = "1011100";
  charSet[25] = "1001110";
  charSet[26] = "1010000";
  charSet[27] = "1000100";
  charSet[28] = "1001000";
  charSet[29] = "1110100";
  if (input.length == 12){
    for (var i = 0; i < input.length; i++){
      if (i % 2 == 0){
        evenSum += parseInt(input.substr(i, 1));
      }else{
        oddSum  += parseInt(input.substr(i, 1));
      }
    }
    checkDigit = 10 - ((evenSum + (oddSum * 3)) % 10)
    checkDigit = (checkDigit == 10 ? 0 : checkDigit);
    input += checkDigit.toString();
  }
  numberSystem = parseInt(input.substr(0, 1));
  input        = input.substr(1, input.length - 1);
  switch (numberSystem){
    case 0: paritySequence = "000000222222"; break;
    case 1: paritySequence = "001011222222"; break;
    case 2: paritySequence = "001101222222"; break;
    case 3: paritySequence = "001110222222"; break;
    case 4: paritySequence = "010011222222"; break;
    case 5: paritySequence = "011001222222"; break;
    case 6: paritySequence = "011100222222"; break;
    case 7: paritySequence = "010101222222"; break;
    case 8: paritySequence = "010110222222"; break;
    case 9: paritySequence = "011010222222"; break;
  }
  for (var i = 0; i < input.length; i++){
    parity   = parseInt(paritySequence.substr(i, 1)) * 10;
    parity  += parseInt(input.substr(i, 1));
    encoded += charSet[parity];
    if (i == 5) encoded += center;
  }
  encoded = guard + encoded + guard + supplement;
  return encoded;
}
function EAN8(input){
  var center            = "01010";
  var charSet           = new Array();
  var encodeSupplement  = false;
  var evenSum           = 0;
  var exp               = new RegExp("^[0-9]+$");
  var guard             = "101";
  var oddSum            = 0;
  var parity            = 0;
  var supplement        = "";
  if (!exp.test(input)) return "";
  switch (input.length){
    case 7 :
    case 9 :
    case 12: if (input.length > 7){
               supplement       = input.substr(7, input.length - 7);
               input            = input.substr(0, 7);
               encodeSupplement = true;
             }
             break;
    case 8 :
    case 10:
    case 13: if (input.length > 8){
               supplement       = input.substr(8, input.length - 8);
               checkDigit  = parseInt(input.substr(8, 1));
               input            = input.substr(0, 8);
               encodeSupplement = true;
             }
             break;
    default: return ""; break;
  }
  if (encodeSupplement) supplement = EncodeSupplement(supplement);
  // left char set
  charSet[0]  = "0001101";
  charSet[1]  = "0011001";
  charSet[2]  = "0010011";
  charSet[3]  = "0111101";
  charSet[4]  = "0100011";
  charSet[5]  = "0110001";
  charSet[6]  = "0101111";
  charSet[7]  = "0111011";
  charSet[8]  = "0110111";
  charSet[9]  = "0001011";
  // right char set
  charSet[10] = "1110010";
  charSet[11] = "1100110";
  charSet[12] = "1101100";
  charSet[13] = "1000010";
  charSet[14] = "1011100";
  charSet[15] = "1001110";
  charSet[16] = "1010000";
  charSet[17] = "1000100";
  charSet[18] = "1001000";
  charSet[19] = "1110100";
  if (input.length == 8){
    for (var i = 0; i < input.length; i++){
      if (i % 2 == 0){
        oddSum  += parseInt(input.substr(i, 1));
      }else{
        evenSum += parseInt(input.substr(i, 1));
      }
    }
    checkDigit = 10 - ((evenSum + (oddSum * 3)) % 10);
    checkDigit = (checkDigit == 10 ? 0 : checkDigit);
    input += checkDigit.toString();
  }
  for (var i = 0; i < 8; i++){
    parity   = (i < 4 ? 0 : 1) * 10;
    parity  += parseInt(input.substr(i, 1));
    encoded +=  charSet[parity];
    if (i == 3) encoded += center;
  }
  encoded = guard + encoded + guard + supplement;
  return encoded;
}
function EncodeSupplement(input){
  var charSet       = new Array();
  var center        = "01";
  var encode        = "";
  var evenSum       = 0;
  var exp           = new RegExp("^[0-9]+$");
  var guard         = "1011";
  var largeParity   = new Array();
  var oddSum        = 0;
  var padd          = "00000000000000000000";
  var parity        = 0;
  var parityMask    = "";   
  var smallParity   = new Array();
  if (!exp.test(input)) return "";
  // even char set  
  charSet[0]   = "0100111";
  charSet[1]   = "0110011";
  charSet[2]   = "0011011";
  charSet[3]   = "0100001";
  charSet[4]   = "0011101";
  charSet[5]   = "0111001";
  charSet[6]   = "0000101";
  charSet[7]   = "0010001";
  charSet[8]   = "0001001";
  charSet[9]   = "0010111";
  // odd char set  
  charSet[10]  = "0001101";
  charSet[11]  = "0011001";
  charSet[12]  = "0010011";
  charSet[13]  = "0111101";
  charSet[14]  = "0100011";
  charSet[15]  = "0110001";
  charSet[16]  = "0101111";
  charSet[17]  = "0111011";
  charSet[18]  = "0110111";
  charSet[19]  = "0001011"; 
  // populate parity masks
  // 2 digit  
  smallParity[0] = "11";
  smallParity[1] = "10";
  smallParity[2] = "01";
  smallParity[3] = "00";
  // 5 digit  
  largeParity[0] = "00111";
  largeParity[1] = "01011";
  largeParity[2] = "01101";
  largeParity[3] = "01110";
  largeParity[4] = "10011";
  largeParity[5] = "11001";
  largeParity[6] = "11100";
  largeParity[7] = "10101";
  largeParity[8] = "10110";
  largeParity[9] = "11010";
  switch (input.length){
    case 2 : parityMask = smallParity(parseInt(input) % 4);
             for (var i = 0; i < 2; i++){
               parity  = (parseInt(parityMask.substr(i, 1)) * 10);
               parity += parseInt(input.substr(i, 1));
               encode += charSet[parity];
               if (i == 1) encode += center;
             }
             encode = padd + guard + encode;
             break;
    case 5 : for (var i = 0; i < 5; i++){
               if (i % 2 == 0){
                 oddSum  += parseInt(input.substr(i, 1));
               }else{
                 evenSum += parseInt(input.substr(i, 1));
               }
             }
             parity     = ((oddSum * 3) + (evenSum * 9)) % 10;
             parityMask = largeParity(parity);
             for (var i = 1; i < 5; i++){
               parity  = (parseInt(parityMask.substr(i, 1)) * 10);
               parity += parseInt(input.substr(i, 1));
               encode += charSet[parity];
               if (i == 4) break;
               encode += center;
             }
             encode = padd + guard + encode;
             break;
    default: return ""; break;
  }
  return encode;
}
function Interlace(input){
  var left      = "";
  var right     = "";
  var newString = "";
  for (var i = 0; i < input.length; i+= 10){
    left  = input.substr(i, 5);
    right = input.substr(i + 5, 5);
    for (var j = 0; j < 5; j++){
      newString += left.substr(j, 1) + right.substr(j, 1);
    }
  }
  return newString;
}
function ITF(input){
  var chr       = 0;
  var charSet   = new Array();
  var evenSum   = 0;
  var exp       = new RegExp("^[0-9]+$");
  var oddSum    = 0;
  var startChar = "1010";
  var stopChar  = "1101";
  charSet[0]    = "00110";
  charSet[1]    = "10001";
  charSet[2]    = "01001";
  charSet[3]    = "11000";
  charSet[4]    = "00101";
  charSet[5]    = "10100";
  charSet[6]    = "01100";
  charSet[7]    = "00011";
  charSet[8]    = "10010";
  charSet[9]    = "01010";
  if (!exp.test(input)) return "";
  if (hasCheck){
    for (var i = 0; i < input.length; i++){
      if (i % 2 == 0){
        oddSum  += parseInt(input.substr(i, 1));
      }else{
        evenSum += parseInt(input.substr(i, 1));
      }
    }
    checkDigit = 10 - ((evenSum + (oddSum * 3)) % 10);
    checkDigit = (checkDigit == 10 ? 0 : checkDigit);
    input          += checkDigit.toString();
  }
  if ((input.length & 1) == 1) input = "0" + input;
  for (var i = 0; i < input.length; i++){
    encoded += charSet[parseInt(input.substr(i, 1))];
  }
  input   = Interlace(encoded);
  encoded = "";
  for (var i = 0; i < input.length; i++){
    chr = parseInt(input.substr(i, 1));
    if (i % 2 == 0){
      encoded += (chr == 1 ? "11" : "1");
    }else{
      encoded += (chr == 1 ? "00" : "0");
    }
  }
  encoded = startChar + encoded + stopChar;
  return encoded;
}
function MSI(input){
  var charSet   = new Array();
  var chr       = "";
  var exp       = new RegExp("^[0-9]+$");
  var product   = "";
  var oddSum    = 0;
  var startChar = "110";
  var stopChar  = "1001";
  if (!exp.test(input))  return "";   
  if (input.length < 3 || input.length > 14) return "";
  charSet[0]    = "100100100100";
  charSet[1]    = "100100100110";
  charSet[2]    = "100100110100";
  charSet[3]    = "100100110110";
  charSet[4]    = "100110100100";
  charSet[5]    = "100110100110";
  charSet[6]    = "100110110100";
  charSet[7]    = "100110110110";
  charSet[8]    = "110100100100";
  charSet[9]    = "110100100110";
  for (var i = 0; i < input.length; i++){
    chr = input.substr(i, 1);
    if (i % 2 == 0){
      oddSum += parseInt(chr);
    }else{
      product += chr;
    }
  }
  product = parseInt(product) * 2;
  product = String(product);
  for (var i = 0; i < product.length; i++){
    checkDigit += parseInt(product.substr(i, 1));
  }
  checkDigit += oddSum;
  checkDigit = 10 - (checkDigit % 10);
  if (checkDigit == 10) checkDigit = 0;
  input += checkDigit;
  for (var i = 0; i < input.length; i++){
    chr = input.substr(i, 1);
    encoded += charSet[parseInt(chr)];
  }
  encoded = startChar + encoded + stopChar;
}
function UPCA(input){
  var center            = "01010";
  var charSet           = new Array();
  var encodeSupplement  = false;
  var evenSum           = 0;
  var exp               = new RegExp("^[0-9]+$");
  var guard             = "101";
  var oddSum            = 0;
  var parity            = 0;
  var supplement        = "";
  if (!exp.test(input)) return "";
  switch (input.length){
    case 11:
    case 13:
    case 16: if (input.length > 11){
               supplement       = input.substr(11, input.length - 11);
               input            = input.substr(0, 11);
               encodeSupplement = true;
             }
             break;
    case 12:
    case 14:
    case 17: if (input.length > 12){
               supplement       = input.substr(12, input.length - 12);
               input            = input.substr(0, 12);
               encodeSupplement = true;
             }
             break;
    default: return ""; break;
  }
  if (encodeSupplement) suppliment = EncodeSupplement(supplement);
  if (compress){
    // see: http://www.uc-council.org/reflib/01302/d36-2.htm for specifications
    var expFirst  = new RegExp("(^[0-9]{3})([0-2]{1})(0{2})([0-9]{5}$)");
    var expSecond = new RegExp("(^[0-9]{3})([3-9]{1})(0{2})([0-9]{5}$)");
    var expThird  = new RegExp("(^[0-9]{4})([1-9]{1})(0{1})([0-9]{5}$)");
    var expFourth = new RegExp("(^[0-9]{4})([1-9]{2})([0-9]{5}$)");
    switch (true){
      case expFirst.test(input) : expFirst     = new RegExp("(^[0-9]{3})([0-2]{1})([0-9]{4})([0-9]{3}$)");
                                  input        = input.replace(expFirst, "$1$4$2");
                                  break;
      case expSecond.test(input): expSecond    = new RegExp("(^[0-9]{4})([0-9]{5})([0-9]{2}$)");
                                  input        = input.replace(expSecond, "$1$3") + "3";
                                  break;
      case expThird.test(input) : expThird     = new RegExp("(^[0-9]{5})([0-9]{5})([0-9]{1}$)");
                                  input        = input.replace(expThird, "$1$3") + "4";
                                  break;
      case expFourth.test(input): expFourth    = new RegExp("(^[0-9]{6})([0-9]{4})([0-9]{1}$)");
                                  input        = input.replace(expFourth, "$1$3");
                                  break;
      default                   : compress = false; break;
    }
  }
  if (compress){
    input  += suppliment;
    encoded = UPCE(input);
    return encoded;
  }
  compress = false;
  // left char set
  charSet[0]  = "0001101";
  charSet[1]  = "0011001";
  charSet[2]  = "0010011";
  charSet[3]  = "0111101";
  charSet[4]  = "0100011";
  charSet[5]  = "0110001";
  charSet[6]  = "0101111";
  charSet[7]  = "0111011";
  charSet[8]  = "0110111";
  charSet[9]  = "0001011";
  // right char set
  charSet[10] = "1110010";
  charSet[11] = "1100110";
  charSet[12] = "1101100";
  charSet[13] = "1000010";
  charSet[14] = "1011100";
  charSet[15] = "1001110";
  charSet[16] = "1010000";
  charSet[17] = "1000100";
  charSet[18] = "1001000";
  charSet[19] = "1110100";
  if (input.length == 11){
    for (var i = 0; i < input.length; i++){
      if (i % 2 == 0){
        oddSum  += parseInt(input.substr(i, 1));
      }else{
        evenSum += parseInt(input.substr(i, 1));
      }
    }  
    checkDigit = 10 - ((evenSum + (oddSum * 3)) % 10);
    if (checkDigit == 10) checkDigit = 0;
    input += checkDigit.toString();
  }else{
    checkDigit = parseInt(input.substr(11, 1));
  }
  for (var i = 0; i < 12; i++){
    parity   = (i > 5 ? 1 : 0) * 10;
    parity  += parseInt(input.substr(i, 1));
    encoded += charSet[parity];
    if (i == 5) encoded += center;
  }
  encoded = guard + encoded + guard + supplement;
}
function UPCE(input){
  var charSet           = new Array();
  var encodeSupplement  = false;
  var evenSum           = 0;
  var exp               = new RegExp("^0{1}[0-9]+$");
  var leftGuard         = "101";
  var oddSum            = 0;
  var originalNumber    = "";
  var parity            = 0;
  var parityMask        = "";
  var rightGuard        = "010101";
  var supplement        = "";
  if (!exp.test(input))  return "";
  switch (input.length){
    case 7 :
    case 9 :
    case 12: if (input.length > 7){
               supplement       = input.substr(7, input.length - 7);
               input            = input.substr(0, 7);
               encodeSupplement = true;
             }
             break;
    case 8 :
    case 10:
    case 13: if (input.length > 8){
               supplement       = input.substr(8, input.length - 8);
               input            = input.substr(0, 8);
               encodeSupplement = true;
             }
             break;
    default: return ""; break;
  }
  if (encodeSupplement) suppliment = EncodeSupplement(supplement);
  // even parity
  charSet[0]   = "0100111";
  charSet[1]   = "0110011";
  charSet[2]   = "0011011";
  charSet[3]   = "0100001";
  charSet[4]   = "0011101";
  charSet[5]   = "0111001";
  charSet[6]   = "0000101";
  charSet[7]   = "0010001";
  charSet[8]   = "0001001";
  charSet[9]   = "0010111";
  // odd parity
  charSet[10] = "0001101";
  charSet[11] = "0011001";
  charSet[12] = "0010011";
  charSet[13] = "0111101";
  charSet[14] = "0100011";
  charSet[15] = "0110001";
  charSet[16] = "0101111";
  charSet[17] = "0111011";
  charSet[18] = "0110111";
  charSet[19] = "0001011";
  // parity masks
  charSet[20] = "000111";
  charSet[21] = "001011";
  charSet[22] = "001101";
  charSet[23] = "001110";
  charSet[24] = "010011";
  charSet[25] = "011001";
  charSet[26] = "011100";
  charSet[27] = "010101";
  charSet[28] = "010110";
  charSet[29] = "011010";
  if (input.length == 7){
    // reverse engineer the precompressed data to get check digit
    // see: http://www.uc-council.org/reflib/01302/d36-2.htm
    switch (parseInt(input.substr(6, 1))){
      case 0 :
      case 1 :
      case 2 : originalNumber = input.substr(0, 3) +
                                input.substr(input.length -1, 1) +
                                "0000"  + input.substr(3, 3);
                                break;
      case 3 : originalNumber = input.substr(0, 4) +
                                "00000" + input.substr(4, 2);
                                break;
      case 4 : originalNumber = input.substr(0, 5) +
                                "00000" + input.substr(5, 1);
                                break;
      default: originalNumber = input.substr(0, 6) +
                                "0000"  + input.substr(input.length -1, 1);
                                break;
    }
    for (var i = 0; i < 11; i++){
      if (i % 2 == 0){
        oddSum  += parseInt(originalNumber.substr(i, 1));
      }else{
        evenSum += parseInt(originalNumber.substr(i, 1));
      }
    }
    checkDigit = 10 - ((evenSum + (oddSum * 3)) % 10);
    if (checkDigit == 10) checkDigit = 0;
  }else{
    checkDigit = parseInt(input.substr(input.length - 1, 1));
    input           = input.substr(0, 7);
  }
  parityMask = charSet[checkDigit + 20];
  input = input.substr(1, 6);
  for (var i = 0; i < input.length; i++){
    parity   = (parseInt(parityMask.substr(i, 1)) * 10);
    parity  += parseInt(input.substr(i, 1));
    encoded += charSet[parity];
  }
  encoded = leftGuard + encoded + rightGuard + supplement;
  return encoded;
}
