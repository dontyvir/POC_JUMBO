function checkCDV( dvr )
{
	dv = dvr + ""
	if ( dv != '0' && dv != '1' && dv != '2' && dv != '3' && dv != '4' && dv != '5' && dv != '6' && dv != '7' && dv != '8' && dv != '9' && dv != 'k'  && dv != 'K')
	{
		alert("Debes ingresar un dígito verificador válido");
		return false;
	}
	return true;
}

//////////////////////////////////////////////////

function checkDV( crut )
{
	largo = crut.length;

	rut = crut.substring(0, largo-1 );

	dv = crut.charAt(largo-1);
	if ( !checkCDV( dv ) )
		return false

	if ( rut == null || dv == null )
		return 0

	var dvr = '0'

	suma = 0
	mul  = 2

	for (i= rut.length -1 ; i >= 0; i--)
	{
		suma = suma + rut.charAt(i) * mul
		if (mul == 7)
			mul = 2
		else    
			mul++
	}

	res = suma % 11
	if (res==1)
		dvr = 'k'
	else if (res==0)
		dvr = '0'
	else
	{
		dvi = 11-res
		dvr = dvi + ""
	}

	if ( dvr != dv.toLowerCase() )
	{
		alert("El RUT es incorrecto, podrías ingresarlo nuevamente");
		return false
	}

   return true
}

///////////////////////////////////////////////////////

function checkRutField( rut_in, rut_campo ) // RUT con digito verificar xxxx-x
{
  var texto = rut_in;
 
  var tmpstr = "";

  for ( i=0; i < texto.length ; i++ )
    if ( texto.charAt(i) != ' ' && texto.charAt(i) != '.' && texto.charAt(i) != '-' )
      tmpstr = tmpstr + texto.charAt(i);
  texto = tmpstr;
  largo = texto.length;

  if ( largo < 7 )
  {
    alert("Debes ingresar el campo RUT completo");
    return false;
  }

  for (i=0; i < largo ; i++ )
  { 
    if ( texto.charAt(i) !="0" && texto.charAt(i) != "1" && texto.charAt(i) !="2" && texto.charAt(i) != "3" && texto.charAt(i) != "4" && texto.charAt(i) !="5" && texto.charAt(i) != "6" && texto.charAt(i) != "7" && texto.charAt(i) !="8" && texto.charAt(i) != "9" && texto.charAt(i) !="k" && texto.charAt(i) != "K" ) 
    {
      alert("El valor ingresado no corresponde a un RUT válido, podrías ingresarlo nuevamente");
      return false;
    }
  }

/*
  var invertido = "";

  for ( i=(largo-1),j=0; i>=0; i--,j++ )
    invertido = invertido + texto.charAt(i);

  var dtexto = "";

  dtexto = dtexto + invertido.charAt(0);
  dtexto = dtexto + '-';
  cnt = 0;

  for ( i=1,j=2; i<largo; i++,j++ )
  {
    if ( cnt == 3 )
    {
      dtexto = dtexto + '.';
      j++;
      dtexto = dtexto + invertido.charAt(i);
      cnt = 1;
    }
    else
    { 
      dtexto = dtexto + invertido.charAt(i);
      cnt++;
    }
  }

  invertido = "";

  for ( i=(dtexto.length-1),j=0; i>=0; i--,j++ )
    invertido = invertido + dtexto.charAt(i);

  rut_campo.value = invertido;
*/
  if ( checkDV(texto) )
   return true;
  return false;
}

