//CLIENT ID 
cmSetClientID("60391686", false, "testdata.coremetrics.com","Jumbo.cl");

 //variable de encendidoapagado coremetrics
 var coreOn=false;

function producto(_ProductID,_ProductName,_Quantity,_UnitPrice,_CategoryID){
	   this.ProductID= _ProductID;
	   this.ProductName=_ProductName;
	   this.Quantity=_Quantity;
	   this.UnitPrice=_UnitPrice; 
	   this.CategoryID=_CategoryID;
	 }
	function registro(_CustomerID, _CustomerEmail, _CustomerCity, _CustomerState,_CustomerPostalCode,_CustomerCountry)
	{
		this.CustomerID =_CustomerID;
		this.CustomerEmail = _CustomerEmail;
		this.CustomerCity = _CustomerCity;
		this.CustomerState = _CustomerState;
		this.CustomerPostalCode =_CustomerPostalCode;
		this.CustomerCountry = _CustomerCountry;
	} 

	function productoPurchase(_ProductID, _ProductName, _Quantity, _UnitPrice, _CustomerID, _OrderID, _OrderSubtotal, _CategoryID)
	{
	    this.ProductID = _ProductID;
		this.ProductName = _ProductName;
		this.Quantity = _Quantity;
		this.UnitPrice = _UnitPrice;
		this.CustomerID = _CustomerID;
		this.OrderID = _OrderID;
		this.OrderSubtotal = _OrderSubtotal;
		this.CategoryID = _CategoryID;
	}
	function order(_OrderID, _OrderSubtotal, _OrderShipping, _CustomerID, _CustomerCity)
	{
		this.OrderID = _OrderID;
		this.OrderSubtotal = _OrderSubtotal;
		this.OrderShipping = _OrderShipping;
		this.CustomerID = _CustomerID;
		this.CustomerCity = _CustomerCity;
	}



function coremetricsTagView(PagId,CategoryID){
        if(coreOn){
                if(CategoryID == null){
                        cmCreatePageviewTag(validatedata(PagId));
                }else{
                        cmCreatePageviewTag(validatedata(PagId),validatedata(CategoryID));
                }
        }
}

function coremetricsTagSearch(PagId,CategoryID, SearchTerm, SearchResults )
{
        if(coreOn)
        {
                cmCreatePageviewTag(validatedata(PagId),validatedata(CategoryID),validatedata(SearchTerm),SearchResults);
        }
}
function coremetricsTagProduct(PagId,CategoryID, AttributeString, ProductID,ProductName)
{
if(coreOn){
	cmCreatePageviewTag('JUMBO/PRODUCTO/' + ProductName,CategoryID);
	cmCreateProductviewTag(ProductID, ProductName, CategoryID, AttributeString);}
}
function coremetricsTagRegister(PagId,CategoryID,CustomerID, CustomerEmail,CustomerCity, CustomerState)
{
if(coreOn){
	cmCreatePageviewTag(PagId,CategoryID);
	cmCreateRegistrationTag(CustomerID, CustomerEmail,CustomerCity,CustomerState);
	}
}
function coremetricsTagPreviewOrder(PagId,CategoryID,Products)
{
if(coreOn)
        {
    var producto_ = new producto();	
	//cmCreatePageviewTag(PagId,CategoryID);	
	for (i=0;i<Products.length;i++){ 	
	  producto_ = Products[i];
	  cmCreateShopAction5Tag(producto_.ProductID,producto_.ProductName, producto_.Quantity, producto_.UnitPrice, producto_.CategoryID);	 
	}
	cmDisplayShop5s();	
}	
}

function coremetricsTagOrder(PagId,CategoryID, Products, Order,Register)
{
if(coreOn)  {
    var producto_ = new productoPurchase();
	var registro_ = new registro();
	var order_ = new order();
	registro_= Register;
	order_ = Order;
	//cmCreatePageviewTag(PagId,CategoryID);
	for (i=0;i<Products.length;i++){ 	
	  producto_ = Products[i];
	  cmCreateShopAction9Tag(producto_.ProductID,validatedata(producto_.ProductName), producto_.Quantity, producto_.UnitPrice, producto_.CustomerID, producto_.OrderID, producto_.OrderSubtotal, producto_.CategoryID);	 
	}
	cmDisplayShop9s();	
    cmCreateOrderTag(order_.OrderID, order_.OrderSubtotal, order_.OrderShipping, order_.CustomerID,  order_.CustomerCity);
	cmCreateRegistrationTag(registro_.CustomerID, registro_.CustomerEmail, registro_.CustomerCity);
	}
}
function coremetricsTagError(_errCode){
if(coreOn){
   var errCode = _errCode;  			
	var page = "";
	if(errCode == "500"){
	 page = "Error Generico (500)";
	}else if(errCode == 'webpay'){
	  page = "Error Webpay";
	}else{
	  page = "Error Generico";
	}	
	cmCreatePageviewTag(page,"ERROR");  
}
}

//encargada de validar los datos para el tag
function validatedata(StringValidate){
        if(StringValidate!="")
                {   
                        var text=StringValidate;
                        text=text.replace(/,/g,' ');
                        text=text.replace(/'/g,' ');
                        text=text.replace(/"/g,' ');
                        text=text.replace(/\r/g,' ');
                        text=text.replace(/\n/g,' ');
                        return text;
           }else{
                           return StringValidate;
           }
}
//funcion para obtener title de la pagina
function getPageTitle(){
   var titulo=document.title;
   
   return titulo;
}


//funcion para obtener page name
function getPageName(){
   var sPath = window.location.pathname;
   //var sPage = sPath.substring(sPath.lastIndexOf('\\') + 1);
   var sPage = sPath.substring(sPath.lastIndexOf('/') + 1);
   sPage=sPage.split(".");
   
   return sPage[0].replace(/_/g," ");
}
//variable obtiene los parametros del url
function getUrlVars()
{
       var vars = [], hash;
       var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
       for(var i = 0; i < hashes.length; i++)
       {
           hash = hashes[i].split('=');
           vars.push(hash[0]);
           vars[hash[0]] = hash[1];
       }
       return vars;
}