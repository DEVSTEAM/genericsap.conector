package com.cimait.xml;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Formatter;
import java.io.File;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.cimait.DAO.DetalleFactura;
import com.cimait.DAO.FacEmpresa;
import com.cimait.DAO.FacEstablecimiento;
import com.cimait.DAO.FacPuntoEmision;
import com.cimait.DAO.ImpuestosFactura;
import com.cimait.DAO.InfoAdicional;
import com.cimait.DAO.InfoCredito;
import com.cimait.DAO.InfoTributaria;
import com.cimait.Util.Util;

public class CreditoXml {

	private static String version;
	private FacEmpresa empresa;	
	private InfoTributaria informacionTributaria = new InfoTributaria();
	private InfoCredito infoCredito = new InfoCredito();
	
	
	
	public CreditoXml(String version, FacEmpresa emp, FacEstablecimiento est, FacPuntoEmision emision, int billNo, String secuencial, InfoCredito infCre){
		empresa = emp;
		
		informacionTributaria.setVersion(version);		
		informacionTributaria.setAmbiente(Integer.parseInt(emision.getTipoAmbiente()));
		informacionTributaria.setTipoEmision(1);
		
		informacionTributaria.setRazonSocial(emp.getRazonSocial());
		informacionTributaria.setNombreComercial(emp.getRazonComercial());
		informacionTributaria.setRuc(emp.getRuc());
		informacionTributaria.setCodDoc("04");
		int numero = Integer.parseInt(est.getCodEstablecimiento());
		Formatter fmt = new Formatter();
		fmt.format("%03d", numero);
		informacionTributaria.setEstab(fmt.toString());
		
		numero = Integer.parseInt(emision.getCodPuntoEmision());
		fmt = new Formatter();
		fmt.format("%03d", numero);
		informacionTributaria.setPtoEmi(fmt.toString());
		numero = Integer.parseInt(secuencial);
		fmt = new Formatter();
		fmt.format("%09d", numero);
		informacionTributaria.setSecuencial(fmt.toString());
		informacionTributaria.setDirMatriz(emp.getDireccionMatriz());
		
		infoCredito = infCre;
	}

	public boolean crearNotaCredito(String version)throws Exception{
		//Prueba Inicio
		String tipoDocumento = "notaCredito";
		String ls_tipo_documento = "notaCredito";
		String ruta = "";
		//version ="1.0.0";
		//Prueba Fin
		String fileFirmado = "";
	    String rutaFirmado = "";
	    try {
	    	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        ArrayList ListDetDocumentosTmp = new ArrayList();

	        //ls_tipo_documento = "";
	        Attr attr = null;
	        
	        ls_tipo_documento = "notaCredito";
	        //////////////////////Cabecera Xml///////////////////////////////
	        Document doc = docBuilder.newDocument();
	        Element document = doc.createElement(ls_tipo_documento);
	        doc.appendChild(document);
	        attr = doc.createAttribute("id");
	        attr.setValue("comprobante");
	        document.setAttributeNode(attr);
	        if (!((version != null)))	        
		        version = "1.1.0";	
		        if (version.equals(""))
		        	version = "1.1.0";	
	        attr = doc.createAttribute("version");
	        attr.setValue(version);
	        document.setAttributeNode(attr);   
	        //////////////////////Cabecera Xml///////////////////////////////
	        
	        //////////////////////infoTributaria////////////////////////////
	        Element infoTrib = doc.createElement("infoTributaria");
	        document.appendChild(infoTrib);	        
	        
	        Element ambiente = doc.createElement("ambiente");
		    ambiente.appendChild(doc.createTextNode(new Integer(informacionTributaria.getAmbiente()).toString()));
		    infoTrib.appendChild(ambiente);
		    
	        Element tipoEmision = doc.createElement("tipoEmision");
	        tipoEmision.appendChild(doc.createTextNode(new Integer(informacionTributaria.getTipoEmision()).toString()));
	        infoTrib.appendChild(tipoEmision);
	        
        	Element razonSocial = doc.createElement("razonSocial");
        	razonSocial.appendChild(doc.createTextNode(informacionTributaria.getRazonSocial()));
        	infoTrib.appendChild(razonSocial);
        
	        Element nombreComercial = doc.createElement("nombreComercial");
	        nombreComercial.appendChild(doc.createTextNode(informacionTributaria.getNombreComercial()));
	        infoTrib.appendChild(nombreComercial);

	        Element ruc = doc.createElement("ruc");
	        ruc.appendChild(doc.createTextNode(informacionTributaria.getRuc()));
	        infoTrib.appendChild(ruc);
	        
	        /*
	        Element claveAcceso = doc.createElement("claveAcceso");
	        claveAcceso.appendChild(doc.createTextNode("0"));
	        infoTrib.appendChild(claveAcceso);	        
	        */
	        
	        Element codDoc = doc.createElement("codDoc");
	        codDoc.appendChild(doc.createTextNode(informacionTributaria.getCodDoc()));
	        infoTrib.appendChild(codDoc);

	        Element estab = doc.createElement("estab");
	        estab.appendChild(doc.createTextNode(informacionTributaria.getEstab()));
	        infoTrib.appendChild(estab);

	        Element ptoEmi = doc.createElement("ptoEmi");
	        ptoEmi.appendChild(doc.createTextNode(informacionTributaria.getPtoEmi()));
	        infoTrib.appendChild(ptoEmi);

	        Element secuencial = doc.createElement("secuencial");
	        secuencial.appendChild(doc.createTextNode(informacionTributaria.getSecuencial()));
	        infoTrib.appendChild(secuencial);

	        Element dirMatriz = doc.createElement("dirMatriz");
	        dirMatriz.appendChild(doc.createTextNode(informacionTributaria.getDirMatriz()));
	        infoTrib.appendChild(dirMatriz);          
	        
	        //////////////////////infoTributaria////////////////////////////        
	        //////////////////////infoNotaCredito////////////////////////////
	        
	        Element infoDocu = doc.createElement("infoNotaCredito");
	        document.appendChild(infoDocu);

	        Element fechaEmision = doc.createElement("fechaEmision");
	        fechaEmision.appendChild(doc.createTextNode(infoCredito.getFechaEmision()));
	        infoDocu.appendChild(fechaEmision);

	        if (infoCredito.getDirEstablecimiento().length()>0){        
		        Element dirEstablecimiento = doc.createElement("dirEstablecimiento");
		        dirEstablecimiento.appendChild(doc.createTextNode(infoCredito.getDirEstablecimiento()));
		        infoDocu.appendChild(dirEstablecimiento);
	        }

	        if (infoCredito.getTipoIdentificacionComprador() != null){
		        Element tipoIdentificacionComprador = doc.createElement("tipoIdentificacionComprador");
		        tipoIdentificacionComprador.appendChild(doc.createTextNode(infoCredito.getTipoIdentificacionComprador()));
		        infoDocu.appendChild(tipoIdentificacionComprador);
		    }
	        System.out.println("razonSocialComprador::"+infoCredito.getRazonSocialComprador());
	        if ((infoCredito.getRazonSocialComprador() != null) && (infoCredito.getRazonSocialComprador().length() > 0)) {
	          Element razonSocialComprador = doc.createElement("razonSocialComprador");
	          //VPI 
	          razonSocialComprador.appendChild(doc.createTextNode(Util.normalizeValue(infoCredito.getRazonSocialComprador(),300,"razonSocialComprador")));
	          infoDocu.appendChild(razonSocialComprador);
	        }

	        System.out.println("identificacionComprador::"+infoCredito.getIdentificacionComprador());
	        if ((infoCredito.getIdentificacionComprador() != null) && (infoCredito.getIdentificacionComprador().length() > 0)) {
	          Element identificacionComprador = doc.createElement("identificacionComprador");
	          identificacionComprador.appendChild(doc.createTextNode(infoCredito.getIdentificacionComprador()));
	          infoDocu.appendChild(identificacionComprador);
	        }
	        
	        if ((Integer.parseInt(infoCredito.getContribuyenteEspecial()) > 99) &&(Integer.parseInt(infoCredito.getContribuyenteEspecial()) <= 99999))
		        {
		  	      Element contribuyenteEspecial = doc.createElement("contribuyenteEspecial");
		  	      contribuyenteEspecial.appendChild(doc.createTextNode(infoCredito.getContribuyenteEspecial()));
		  	      infoDocu.appendChild(contribuyenteEspecial);
		        }
	        
	        if (infoCredito.getObligadoContabilidad().length()>0){
	    	        Element obligadoContabilidad = doc.createElement("obligadoContabilidad");
	    	        obligadoContabilidad.appendChild(doc.createTextNode(infoCredito.getObligadoContabilidad()));
	    	        infoDocu.appendChild(obligadoContabilidad);
	        }	        
	        /*
<rise>Contribuyente Régimen Simplificado RISE</rise>					Obligatorio cuando corresponda	Alfanumérico	Max 40
<totalSinImpuestos>295000.00</totalSinImpuestos>					Obligatorio	Numérico	Max 14
<valorModificacion>4346920.00</valorModificacion>					Obligatorio	Numérico	Max 14
<moneda>DOLAR</moneda>									Obligatorio cuando corresponda	Alfanumérico	Max 15`
	         * */	        
	        if (infoCredito.getCodDocModificado()!= null){
		        if (infoCredito.getCodDocModificado().length()>0){
	    	        Element codDocModificado = doc.createElement("codDocModificado");
	    	        codDocModificado.appendChild(doc.createTextNode(infoCredito.getCodDocModificado()));
	    	        infoDocu.appendChild(codDocModificado);
		        }
	        }
	        if (infoCredito.getNumDocModificado()!= null){
		        if (infoCredito.getNumDocModificado().length()>0){
	    	        Element numDocModificado = doc.createElement("numDocModificado");
	    	        numDocModificado.appendChild(doc.createTextNode(infoCredito.getNumDocModificado()));
	    	        infoDocu.appendChild(numDocModificado);
		        }
	        }
	        if (infoCredito.getFechaEmisionDocSustento()!= null){
		        if (infoCredito.getFechaEmisionDocSustento().length()>0){
	    	        Element fechaEmisionDocSustento = doc.createElement("fechaEmisionDocSustento");
	    	        fechaEmisionDocSustento.appendChild(doc.createTextNode(infoCredito.getFechaEmisionDocSustento()));
	    	        infoDocu.appendChild(fechaEmisionDocSustento);
		        }
	        }
	        if (infoCredito.getTotalSinImpuestos()==null)
	        	infoCredito.setTotalSinImpuestos(0.0D);
	        if (infoCredito.getTotalSinImpuestos() >= 0.0D) {
	          Element totalSinImpuestos = doc.createElement("totalSinImpuestos");
	          totalSinImpuestos.appendChild(doc.createTextNode(new Double(infoCredito.getTotalSinImpuestos()).toString()));
	          infoDocu.appendChild(totalSinImpuestos);
	        }

	        
	        if (infoCredito.getValorModificacion()==null)
	        	infoCredito.setValorModificacion(0.0D);
	        if (infoCredito.getValorModificacion() >= 0.0D) {
	          Element valorModificacion = doc.createElement("valorModificacion");
	          valorModificacion.appendChild(doc.createTextNode(new Double(infoCredito.getValorModificacion()).toString()));
	          infoDocu.appendChild(valorModificacion);
	        }	        
		     
	        if (infoCredito.getMoneda().length()>0){
    	        Element moneda = doc.createElement("moneda");
    	        moneda.appendChild(doc.createTextNode(infoCredito.getMoneda()));
    	        infoDocu.appendChild(moneda);
	        }else{
	        	Element moneda = doc.createElement("moneda");
    	        moneda.appendChild(doc.createTextNode("DOLAR"));
    	        infoDocu.appendChild(moneda);
	        }
	        
	        
	        //////////////////////totalConImpuestos////////////////////////////
	        Element totalConImpuestos = doc.createElement("totalConImpuestos");
	        infoDocu.appendChild(totalConImpuestos);        
	          for (int x=0;x<=infoCredito.getListTotalImpuestos().size()-1;x++){
		          Element totalImpuesto = doc.createElement("totalImpuesto");
		          totalConImpuestos.appendChild(totalImpuesto);
		          
		          Element impuestoCodigo = doc.createElement("codigo");
		          impuestoCodigo.appendChild(doc.createTextNode(new Integer(infoCredito.getListTotalImpuestos().get(x).getCodigo()).toString()));
		          totalImpuesto.appendChild(impuestoCodigo);
		          
		          Element impuestoCodigoPorcentaje = doc.createElement("codigoPorcentaje");
		          impuestoCodigoPorcentaje.appendChild(doc.createTextNode(new Integer(infoCredito.getListTotalImpuestos().get(x).getCodigoPorcentaje()).toString()));
		          totalImpuesto.appendChild(impuestoCodigoPorcentaje);
	
		          Formatter fmt = new Formatter();
		  		  fmt.format("%.2f", infoCredito.getListTotalImpuestos().get(x).getBaseImponible());		  		  
		          Element impuestoBaseImponible = doc.createElement("baseImponible");
		          impuestoBaseImponible.appendChild(doc.createTextNode(new Double(infoCredito.getListTotalImpuestos().get(x).getBaseImponible()).toString()));
		          totalImpuesto.appendChild(impuestoBaseImponible);
		          
		          fmt = new Formatter();
		          fmt.format("%.2f", infoCredito.getListTotalImpuestos().get(x).getValor());
		          Element impuestoValor = doc.createElement("valor");
		          impuestoValor.appendChild(doc.createTextNode(new Double(infoCredito.getListTotalImpuestos().get(x).getValor()).toString()));
		          totalImpuesto.appendChild(impuestoValor);
	          }
	        		        
	          if (infoCredito.getMotivo()!=null){
	  	        if (infoCredito.getMotivo().length()>0){
	      	        Element motivo = doc.createElement("motivo");
	      	        motivo.appendChild(doc.createTextNode(infoCredito.getMotivo()));
	      	        infoDocu.appendChild(motivo);
	  	        }else{
	  	        	Element motivo = doc.createElement("motivo");
	      	        motivo.appendChild(doc.createTextNode("Nota de Credito"));
	      	        infoDocu.appendChild(motivo);
	  	        }}else{
	  	        	Element motivo = doc.createElement("motivo");
	      	        motivo.appendChild(doc.createTextNode("Nota de Credito"));
	      	        infoDocu.appendChild(motivo);
	  	        }     
	        //////////////////////////////////////////////////////////
	        /////////////////////////Detalles/////////////////////////
	        //////////////////////////////////////////////////////////
			        int indexLineaCab = 0;
			        int indexLineaDet = 0;               
			        if (infoCredito.getListDetFactura() != null){        
				        if (infoCredito.getListDetFactura().size() > 0){        			
	        
		        Element detalles = doc.createElement("detalles");
		        document.appendChild(detalles);
		        for (int x=0;x<=infoCredito.getListDetFactura().size()-1;x++){
		        	DetalleFactura DetDocTmp = new DetalleFactura(); 
			        DetDocTmp = (DetalleFactura)infoCredito.getListDetFactura().get(x);
			        Element detalle = doc.createElement("detalle");
			        detalles.appendChild(detalle);
			        
			        //indexLineaCab = DetDocTmp.getLineaFactura();
			        String ls_alias = "";
			        if (tipoDocumento.equals("notaCredito")){
			        	ls_alias = "codigoInterno";
			        }else{	
			        	ls_alias = "codigoPrincipal";
			        }
			        Element codigoPrincipal = doc.createElement(ls_alias);
			        codigoPrincipal.appendChild(doc.createTextNode(DetDocTmp.getCodigoPrincipal()));
			        detalle.appendChild(codigoPrincipal);
			        
			        if (tipoDocumento.equals("notaCredito")){
			        	ls_alias = "codigoAdicional";
			        }else{	
			        	ls_alias = "codigoAuxiliar";
			        }
			        if (DetDocTmp.getCodigoAuxiliar()!=null){
				        if (DetDocTmp.getCodigoAuxiliar().length()>0){
				        Element codigoAuxiliar = doc.createElement(ls_alias);
				        codigoAuxiliar.appendChild(doc.createTextNode(DetDocTmp.getCodigoAuxiliar()));
				        detalle.appendChild(codigoAuxiliar);
				        }
			        }
			        
			        Element descripcion = doc.createElement("descripcion");
			        descripcion.appendChild(doc.createTextNode(DetDocTmp.getDescripcion()));
			        detalle.appendChild(descripcion);
			        
			        Element cantidad = doc.createElement("cantidad");
			        cantidad.appendChild(doc.createTextNode(new Double(DetDocTmp.getCantidad()).toString()));
			        detalle.appendChild(cantidad);
			        
			        
			        Element precioUnitario = doc.createElement("precioUnitario");
			        precioUnitario.appendChild(doc.createTextNode(new Double(DetDocTmp.getPrecioUnitario()).toString()));
			        detalle.appendChild(precioUnitario);
			        
			        Element descuento = doc.createElement("descuento");
			        descuento.appendChild(doc.createTextNode(new Double(DetDocTmp.getDescuento()).toString()));
			        detalle.appendChild(descuento);
			        
			        Element precioTotalSinImpuesto = doc.createElement("precioTotalSinImpuesto");
			        precioTotalSinImpuesto.appendChild(doc.createTextNode(new Double(DetDocTmp.getPrecioTotalSinImpuesto()).toString()));
			        detalle.appendChild(precioTotalSinImpuesto);

		        	if (infoCredito.getListDetFactura().get(x).getListImpuestos()!=null){
		        	if (infoCredito.getListDetFactura().get(x).getListImpuestos().size()>0){
		        	Element impuestos = doc.createElement("impuestos");
		            detalle.appendChild(impuestos);
		            
		        	for (int z=0;z<=infoCredito.getListDetFactura().get(x).getListImpuestos().size()-1;z++){
		        		
		        		ImpuestosFactura DocImp = new ImpuestosFactura(); 
		        		   DocImp = (ImpuestosFactura)infoCredito.getListDetFactura().get(x).getListImpuestos().get(z);
		        	   //indexLineaDet = DocImp.getLineaFactura();		        		
		        	   //if (indexLineaDet == indexLineaCab){
			     	       Element impuesto = doc.createElement("impuesto");
			     	       impuestos.appendChild(impuesto);	  
			     	        
			     	       	     	       
			     	       Element codigo = doc.createElement("codigo");
			     	       codigo.appendChild(doc.createTextNode(new Integer(DocImp.getCodigo()).toString()));
			     	       impuesto.appendChild(codigo);
			     	       
			     	       
			     	       Element codigoPorcentaje = doc.createElement("codigoPorcentaje");
			     	       codigoPorcentaje.appendChild(doc.createTextNode(new Integer(DocImp.getCodigoPorcentaje()).toString()));
			    	       impuesto.appendChild(codigoPorcentaje);
			    	       
			    	       Element tarifa = doc.createElement("tarifa");
			    	       tarifa.appendChild(doc.createTextNode(new Double(DocImp.getTarifa()).toString()));
			    	       impuesto.appendChild(tarifa);
			    	       
			    	       Element baseImponible = doc.createElement("baseImponible");
			    	       baseImponible.appendChild(doc.createTextNode(new Double(DocImp.getBaseImponible()).toString()));
			    	       impuesto.appendChild(baseImponible);
			    	       
			    	       Element valor = doc.createElement("valor");
			    	       valor.appendChild(doc.createTextNode(new Double(DocImp.getValor()).toString()));
			    	       impuesto.appendChild(valor);
		        	   //}
		        	}
		            }
		        	}
		        //JZURITA DETALLE
		        }
	        }
	        }			        
			//INFORMACION ADICIONAL
			Attr attrib = null;
            Element infoAdicional = doc.createElement("infoAdicional");
	        document.appendChild(infoAdicional);
	        
	        for (int z=0;z<=infoCredito.getListInfoAdicional().size()-1;z++){
	        	InfoAdicional InfoAdic = new InfoAdicional();
		        InfoAdic = infoCredito.getListInfoAdicional().get(z);	        
		        Element campoAdicional = doc.createElement("campoAdicional");
		        campoAdicional.appendChild(doc.createTextNode(Util.normalizeValue(InfoAdic.getValor(),300,InfoAdic.getNombre().toUpperCase())));		       
		        infoAdicional.appendChild(campoAdicional);
		        attrib = doc.createAttribute("nombre");							        
		        attrib.setValue(InfoAdic.getNombre());
		        campoAdicional.setAttributeNode(attrib);		        
	        }
		        
	          TransformerFactory transformerFactory = TransformerFactory.newInstance();
	          Transformer transformer = transformerFactory.newTransformer();
	          DOMSource source = new DOMSource(doc);
	          
	          System.out.println("Ruta:"+empresa.getPathCompGenerados()+informacionTributaria.getAmbiente()+informacionTributaria.getRuc()+informacionTributaria.getCodDoc()+informacionTributaria.getEstab()+informacionTributaria.getPtoEmi()+informacionTributaria.getSecuencial()+".xml");
	          File file = new File(empresa.getPathCompGenerados()+informacionTributaria.getAmbiente()+informacionTributaria.getRuc()+informacionTributaria.getCodDoc()+informacionTributaria.getEstab()+informacionTributaria.getPtoEmi()+informacionTributaria.getSecuencial()+".xml");
	          StreamResult result = new StreamResult(file);
	          
	          //result =  new StreamResult(System.out);
	          transformer.transform(source, result);
	      
	      return true;   
	      
	    } catch (ParserConfigurationException pce) {
	      pce.printStackTrace();			    
	      throw new Exception("ERRORGENERAXML,crearNotaCredito Error en el Parseo de "+pce.getMessage()+",Empresa");
	    } catch (TransformerException tfe) {	    	
	      tfe.printStackTrace();
	      //return false;
	      throw new Exception("ERRORGENERAXML,crearNotaCredito Error en el Transformer de "+tfe.getMessage()+",Empresa");
		} catch (Exception e){
		  e.printStackTrace();
		  //return false;
		  throw new Exception("ERRORGENERAXML,crearNotaCredito Error en el Exception de "+e.getMessage()+",Empresa");
		}
	}
	/*
	private static String normalizeValue(String value, int maxURLLength, String name) {
		String inputValue = value;
		String ret = "";
		if (inputValue != null) {			
			ret = Normalizer.normalize(inputValue.subSequence(0, inputValue.length()), Normalizer.Form.NFKD).replaceAll("[^\\p{ASCII}]+","");
			//VPI se agrega en if 
			//if ((name.equals("DIRECCION"))||(name.equals("OFICINA"))){
			if ((name.equals("DIRECCION"))||(name.equals("OFICINA"))||name.equals("TELEFONO")){
				ret = ret.trim();
				ret = ret.replaceAll("[&,@.+-/:!()*#^'\" 	?=$%;\\<>`]", "|");
				ret = ret.replaceAll("[|]", " ");
				ret = ret.trim();
				ret = ret.replace("  ", " ");
			/*VPI se comenta	
			}else if (name.equals("TELEFONO")){
				ret = ret.trim();
				ret = ret.replaceAll("[&,@.+-/:!()*#^'\" 	?=$%;\\<>]", "|");
				
			}else if (name.equals("EMAIL")){
				ret = ret.trim();
				ret = ret.replaceAll("[&,+-/:!()*#^'\" 	?=$%;\\<>`]", "|");
				ret = ret.trim();
				ret = ret.replace("|", ".");
			}else{
				ret = ret.trim();
				ret = ret.replaceAll("[&,@+-/:!()*#^'\" 	?=$%;\\<>`]", "|");
				ret = ret.replaceAll("[|]", " ");
				ret = ret.trim();
				ret = ret.replace("  ", " ");
			}
			
			if (ret.length() > maxURLLength) {
				ret = ret.substring(0, maxURLLength);
			}
			ret = ret.toLowerCase();
		}
		return ret;
	}
	*/

	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		CreditoXml.version = version;
	}

	public FacEmpresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(FacEmpresa empresa) {
		this.empresa = empresa;
	}

	public InfoTributaria getInfoTrib() {
		return informacionTributaria;
	}

	public void setInfoTrib(InfoTributaria infoTrib) {
		this.informacionTributaria = infoTrib;
	}

	public InfoCredito getInfoFac() {
		return infoCredito;
	}

	public void setInfoFac(InfoCredito infoCredito) {
		this.infoCredito = infoCredito;
	}
	
}
