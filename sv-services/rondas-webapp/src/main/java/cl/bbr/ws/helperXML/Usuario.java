package cl.bbr.ws.helperXML;

import org.jdom.Element;

/**
 * Sub-Arbol de datos XML para el usuario.
 */
public class Usuario extends Element{
	/**
	 * Constructor del subarbol XML para el usuario
	 * @param Id_usuario
	 * @param Login
	 * @param Password
	 * @param Perfil
	 */

  public Usuario(String Id_usuario, String Login, String Password, String Perfil) {
  	    super("usuario");
    addContent(new Element("id_usuario").setText(Id_usuario));
    addContent(new Element("login").setText(Login));
    addContent(new Element("password").setText(Password));
    addContent(new Element("perfil").setText(Perfil));   
  }
}
