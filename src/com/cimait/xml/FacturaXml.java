package com.cimait.xml;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringEscapeUtils;
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
import com.cimait.DAO.InfoFactura;
import com.cimait.DAO.InfoTributaria;
import com.cimait.Util.Util;
import com.cimait.runtime.Environment;


public class FacturaXml {

	//private static String version;
	private FacEmpresa empresa;	
	private InfoTributaria informacionTributaria = new InfoTributaria();
	private InfoFactura infoFactura = new InfoFactura();
	
	public FacturaXml(String version, FacEmpresa emp, FacEstablecimiento est, FacPuntoEmision emision, String billNo, String secuencial, InfoFactura infFac){
		empresa = emp;
		informacionTributaria.setVersion(version);		
		informacionTributaria.setAmbiente(Integer.parseInt(emision.getTipoAmbiente()));
		informacionTributaria.setTipoEmision(1);
		
		informacionTributaria.setRazonSocial(emp.getRazonSocial());
		informacionTributaria.setNombreComercial(emp.getRazonComercial());
		informacionTributaria.setRuc(emp.getRuc());
		informacionTributaria.setCodDoc("01");
		int numero = Integer.parseInt(est.getCodEstablecimiento());
		
		//int numero = Integer.parseInt("1");
		Formatter fmt = new Formatter();
		fmt.format("%03d", numero);
		informacionTributaria.setEstab(fmt.toString());
		
		numero = Integer.parseInt(emision.getCodPuntoEmision());
		//numero = Integer.parseInt("1");
		fmt = new Formatter();
		fmt.format("%03d", numero);
		informacionTributaria.setPtoEmi(fmt.toString());
		numero = Integer.parseInt(secuencial);
		fmt = new Formatter();
		fmt.format("%09d", numero);
		informacionTributaria.setSecuencial(fmt.toString());
		informacionTributaria.setDirMatriz(emp.getDireccionMatriz());
		
		infoFactura = infFac;
	}
	
	/*public static void main(String[] args) {
		facturaXml fac =  new facturaXml();
		fac.crearFactura();
	}*/
			
	
	public boolean crearFactura(String version) throws Exception{
		//Prueba Inicio
		String tipoDocumento = "FACTURA";
		String ls_tipo_documento = "factura";
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
	        
	        ls_tipo_documento = "factura";
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
	        /*
	        <ambiente>1</ambiente><tipoEmision>1</tipoEmision><razonSocial>SERVICIOS PROFESIONALES CIMA-E S.A.</razonSocial><nombreComercial>CIMA IT</nombreComercial><ruc>0992531940001</ruc><codDoc>01</codDoc><estab>001</estab><ptoEmi>001</ptoEmi><secuencial>000045119</secuencial><dirMatriz>MIGUEL H. ALCIVAR Y ELEODORO ARBOLEDA</dirMatriz></infoTributaria>
	        */
	        
	        //List formatTipDoc = Environment.c.getList("OroVerde.service.facElectronica.ambiente.infoTributaria.documentos");
	        Element ambiente = doc.createElement("ambiente");
		    ambiente.appendChild(doc.createTextNode(new Integer(informacionTributaria.getAmbiente()).toString()));
		    infoTrib.appendChild(ambiente);
		    ///OroVerde.service.facturacion.ambiente.doc
		    	        
	        
		    //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.tipoEmision.documentos");
	        //Obligatorio, conforme tabla 2 Numérico 1
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element tipoEmision = doc.createElement("tipoEmision");
	        tipoEmision.appendChild(doc.createTextNode(new Integer(informacionTributaria.getTipoEmision()).toString()));
	        infoTrib.appendChild(tipoEmision);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.razonSocial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        	Element razonSocial = doc.createElement("razonSocial");
	        	razonSocial.appendChild(doc.createTextNode(informacionTributaria.getRazonSocial()));
	        	infoTrib.appendChild(razonSocial);
	        //}

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.nombreComercial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element nombreComercial = doc.createElement("nombreComercial");
	        nombreComercial.appendChild(doc.createTextNode(informacionTributaria.getNombreComercial()));
	        infoTrib.appendChild(nombreComercial);
	        //}

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.ruc.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element ruc = doc.createElement("ruc");
	        ruc.appendChild(doc.createTextNode(informacionTributaria.getRuc()));
	        infoTrib.appendChild(ruc);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.codDoc.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element codDoc = doc.createElement("codDoc");
	        codDoc.appendChild(doc.createTextNode(informacionTributaria.getCodDoc()));
	        infoTrib.appendChild(codDoc);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.estab.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element estab = doc.createElement("estab");
	        estab.appendChild(doc.createTextNode(informacionTributaria.getEstab()));
	        infoTrib.appendChild(estab);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.ptoEmi.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element ptoEmi = doc.createElement("ptoEmi");
	        ptoEmi.appendChild(doc.createTextNode(informacionTributaria.getPtoEmi()));
	        infoTrib.appendChild(ptoEmi);
	        //}

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.secuencial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        String secuenciaTrx = "";
	        /*
	        if (entorno.equals("PRUEBA")){
	        	secuenciaTrx = informacionTributaria.getSecuencial().replace("000", this.secuencia);
	        	informacionTributaria.setSecuencial(secuenciaTrx);
	        }
	        	*/
	       
	        	secuenciaTrx = informacionTributaria.getSecuencial();
	        
	        Element secuencial = doc.createElement("secuencial");
	        secuencial.appendChild(doc.createTextNode(secuenciaTrx));
	        infoTrib.appendChild(secuencial);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.dirMatriz.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
	        Element dirMatriz = doc.createElement("dirMatriz");
	        dirMatriz.appendChild(doc.createTextNode(informacionTributaria.getDirMatriz()));
	        infoTrib.appendChild(dirMatriz);          
	        //}
	        
	        //////////////////////infoTributaria////////////////////////////        
	        //////////////////////infoFactura////////////////////////////
	        
	        Element infoDocu = doc.createElement("infoFactura");
	        document.appendChild(infoDocu);
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.fechaEmision.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        Element fechaEmision = doc.createElement("fechaEmision");
		        fechaEmision.appendChild(doc.createTextNode(infoFactura.getFechaEmision()));
		        infoDocu.appendChild(fechaEmision);
	        //}

	        if (infoFactura.getDirEstablecimiento().length()>0){
		        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.dirEstablecimiento.documentos");
		        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
			        Element dirEstablecimiento = doc.createElement("dirEstablecimiento");
			        dirEstablecimiento.appendChild(doc.createTextNode(infoFactura.getDirEstablecimiento()));
			        infoDocu.appendChild(dirEstablecimiento);
		        //}
	        }
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.contribuyenteEspecial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if ((Integer.parseInt(infoFactura.getContribuyenteEspecial()) > 99) &&(Integer.parseInt(infoFactura.getContribuyenteEspecial()) <= 99999))
		        {
		  	      Element contribuyenteEspecial = doc.createElement("contribuyenteEspecial");
		  	      contribuyenteEspecial.appendChild(doc.createTextNode(infoFactura.getContribuyenteEspecial()));
		  	      infoDocu.appendChild(contribuyenteEspecial);
		        }
	        //}
	        
	        if (infoFactura.getObligadoContabilidad().length()>0){
	            //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.obligadoContabilidad.documentos");
	            //if (getContextDocu(tipoDocumento,formatTipDoc)){
	    	        Element obligadoContabilidad = doc.createElement("obligadoContabilidad");
	    	        obligadoContabilidad.appendChild(doc.createTextNode(infoFactura.getObligadoContabilidad()));
	    	        infoDocu.appendChild(obligadoContabilidad);
	            //}
	        }
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.tipoIdentificacionComprador.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if (infoFactura.getTipoIdentificacionComprador() != null){
		        Element tipoIdentificacionComprador = doc.createElement("tipoIdentificacionComprador");
		        tipoIdentificacionComprador.appendChild(doc.createTextNode(infoFactura.getTipoIdentificacionComprador()));
		        infoDocu.appendChild(tipoIdentificacionComprador);
		        }
	        //}
	        

	        
	        
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.razonSocialComprador.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
		        System.out.println("razonSocialComprador::"+infoFactura.getRazonSocialComprador());
		        if ((infoFactura.getRazonSocialComprador() != null) && 
		          (infoFactura.getRazonSocialComprador().length() > 0)) {
		          Element razonSocialComprador = doc.createElement("razonSocialComprador");
		          razonSocialComprador.appendChild(doc.createTextNode(Util.normalizeValue(infoFactura.getRazonSocialComprador(),300,"razonSocialComprador")));
		          infoDocu.appendChild(razonSocialComprador);
		        }
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.identificacionComprador.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
		        System.out.println("identificacionComprador::"+infoFactura.getIdentificacionComprador());
		        if ((infoFactura.getIdentificacionComprador() != null) && 
		          (infoFactura.getIdentificacionComprador().length() > 0)) {
		          Element identificacionComprador = doc.createElement("identificacionComprador");
		          identificacionComprador.appendChild(doc.createTextNode(infoFactura.getIdentificacionComprador()));
		          infoDocu.appendChild(identificacionComprador);
		        }
	        //}
	        

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.totalSinImpuestos.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if (infoFactura.getTotalSinImpuestos()==null)
		        	infoFactura.setTotalSinImpuestos(0.0D);
		        if (infoFactura.getTotalSinImpuestos() >= 0.0D) {
		          Element totalSinImpuestos = doc.createElement("totalSinImpuestos");
		          totalSinImpuestos.appendChild(doc.createTextNode(new Double(infoFactura.getTotalSinImpuestos()).toString()));
		          infoDocu.appendChild(totalSinImpuestos);
		        }
	        //}
	    
	    
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.totalDescuento.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if (infoFactura.getTotalDescuento() != null) {
			        if (infoFactura.getTotalDescuento() >= 0.0D) {
			          Element TotalDescuento = doc.createElement("totalDescuento");
			          TotalDescuento.appendChild(doc.createTextNode(new Double(infoFactura.getTotalDescuento()).toString()));
			          infoDocu.appendChild(TotalDescuento);
			        }
		        }else{
		        	infoFactura.setTotalDescuento(0.0);	
		        }
		        
	        //}
	        
	        
	        
	        //}
	        	        
	        //////////////////////totalConImpuestos////////////////////////////
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.totalConImpuestos.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        	        
	        Element totalConImpuestos = doc.createElement("totalConImpuestos");
	        infoDocu.appendChild(totalConImpuestos);        
	          
	          
	          for (int x=0;x<=infoFactura.getListTotalImpuestos().size()-1;x++){
		          Element totalImpuesto = doc.createElement("totalImpuesto");
		          totalConImpuestos.appendChild(totalImpuesto);
		          
		          Element impuestoCodigo = doc.createElement("codigo");
		          impuestoCodigo.appendChild(doc.createTextNode(new Integer(infoFactura.getListTotalImpuestos().get(x).getCodigo()).toString()));
		          totalImpuesto.appendChild(impuestoCodigo);
		          
		          Element impuestoCodigoPorcentaje = doc.createElement("codigoPorcentaje");
		          impuestoCodigoPorcentaje.appendChild(doc.createTextNode(new Integer(infoFactura.getListTotalImpuestos().get(x).getCodigoPorcentaje()).toString()));
		          totalImpuesto.appendChild(impuestoCodigoPorcentaje);
	
		          Formatter fmt = new Formatter();
		  		  fmt.format("%.2f", infoFactura.getListTotalImpuestos().get(x).getBaseImponible());		  		  
		          Element impuestoBaseImponible = doc.createElement("baseImponible");
		          impuestoBaseImponible.appendChild(doc.createTextNode(new Double(infoFactura.getListTotalImpuestos().get(x).getBaseImponible()).toString()));
		          totalImpuesto.appendChild(impuestoBaseImponible);
		          
		          fmt = new Formatter();
		          fmt.format("%.2f", infoFactura.getListTotalImpuestos().get(x).getValor());
		          Element impuestoValor = doc.createElement("valor");
		          impuestoValor.appendChild(doc.createTextNode(new Double(infoFactura.getListTotalImpuestos().get(x).getValor()).toString()));
		          totalImpuesto.appendChild(impuestoValor);
	          }
	          
	        /*  }
	        }*/
	        //}
	        
	        
	       
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.propina.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        Element propina = doc.createElement("propina");
		        propina.appendChild(doc.createTextNode(new Double(infoFactura.getPropina()).toString()));
		        infoDocu.appendChild(propina);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.importeTotal.documentos");
		        //if (getContextDocu(tipoDocumento,formatTipDoc)){
			        Element importeTotal = doc.createElement("importeTotal");
			        importeTotal.appendChild(doc.createTextNode(new Double(infoFactura.getImporteTotal()).toString()));
			        infoDocu.appendChild(importeTotal);
		        //}
		        
	        //}
	        
	      //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.moneda.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
			if (infoFactura.getMoneda()!=null){
	        if (infoFactura.getMoneda().length()>0){
		        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.moneda.documentos");
		        //if (getContextDocu(tipoDocumento,formatTipDoc)){
			          Element moneda = doc.createElement("moneda");
			          moneda.appendChild(doc.createTextNode(infoFactura.getMoneda()));
			          infoDocu.appendChild(moneda);
		        //}
	        }}else{
	        	Element moneda = doc.createElement("moneda");
		          moneda.appendChild(doc.createTextNode("DOLAR"));
		          infoDocu.appendChild(moneda);
	        }
			        
	        //////////////////////////////////////////////////////////
	        /////////////////////////Detalles/////////////////////////
	        //////////////////////////////////////////////////////////
			        int indexLineaCab = 0;
			        int indexLineaDet = 0;               
			        if (infoFactura.getListDetFactura() != null){        
				        if (infoFactura.getListDetFactura().size() > 0){        			
	        
		        Element detalles = doc.createElement("detalles");
		        document.appendChild(detalles);
		        for (int x=0;x<=infoFactura.getListDetFactura().size()-1;x++){
		        	DetalleFactura DetDocTmp = new DetalleFactura(); 
			        DetDocTmp = (DetalleFactura)infoFactura.getListDetFactura().get(x);
			        Element detalle = doc.createElement("detalle");
			        detalles.appendChild(detalle);
			        
			        //indexLineaCab = DetDocTmp.getLineaFactura();
			        String ls_alias = "";
			        if (tipoDocumento.equals("04")){
			        	ls_alias = "codigoInterno";
			        }else{	
			        	ls_alias = "codigoPrincipal";
			        }
			        Element codigoPrincipal = doc.createElement(ls_alias);
			        codigoPrincipal.appendChild(doc.createTextNode(DetDocTmp.getCodigoPrincipal()));
			        detalle.appendChild(codigoPrincipal);
			        
			        if (tipoDocumento.equals("04")){
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
		        
			        /*
		        	if (ListDocumentoDetAdicionales!=null){
		        	if (ListDocumentoDetAdicionales.size()>0){
			        	for (int y=0;y<=ListDocumentoDetAdicionales.size()-1;y++){
			        		DetallesAdicionales DetAdic = new DetallesAdicionales();
			        		DetAdic = (DetallesAdicionales)ListDocumentoDetAdicionales.get(y);		        		
			        		indexLineaDet = DetAdic.getLineaFactura();		        		
			        		if (indexLineaDet == indexLineaCab){
				        		Element detalleAdic = doc.createElement("detallesAdicionales");
				    	        detalle.appendChild(detalleAdic);
				    	        
				    	        Element detAdicional = doc.createElement("detAdicional"); 
				    	        attr = doc.createAttribute("nombre");
				    	        attr.setValue(DetAdic.getNombre());
				    	        detAdicional.setAttributeNode(attr);
				    	        
				    	        attr = doc.createAttribute("valor");
				    	        attr.setValue(DetAdic.getValor());
				    	        detAdicional.setAttributeNode(attr);
				    	        
				    	        detalleAdic.appendChild(detAdicional);
			        		}
			        	}
		        	}
		        	}*/

		        	if (infoFactura.getListDetFactura().get(x).getListImpuestos()!=null){
		        	if (infoFactura.getListDetFactura().get(x).getListImpuestos().size()>0){
		        	Element impuestos = doc.createElement("impuestos");
		            detalle.appendChild(impuestos);
		            
		        	for (int z=0;z<=infoFactura.getListDetFactura().get(x).getListImpuestos().size()-1;z++){
		        		
		        		ImpuestosFactura DocImp = new ImpuestosFactura(); 
		        		   DocImp = (ImpuestosFactura)infoFactura.getListDetFactura().get(x).getListImpuestos().get(z);
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
	        
	        for (int z=0;z<=infoFactura.getListInfoAdicional().size()-1;z++){
	        	InfoAdicional InfoAdic = new InfoAdicional();
		        InfoAdic = infoFactura.getListInfoAdicional().get(z);	        
		        Element campoAdicional = doc.createElement("campoAdicional");
		        String dato= Util.normalizeValue(InfoAdic.getValor(),300,InfoAdic.getNombre().toUpperCase());
		        campoAdicional.appendChild(doc.createTextNode((dato==null?"notiene":(dato.equals("")?"notiene":dato))));		       
		        infoAdicional.appendChild(campoAdicional);
		        attrib = doc.createAttribute("nombre");							        
		        attrib.setValue((InfoAdic.getNombre()==null?"notiene":(InfoAdic.getNombre().equals("")?"notiene":InfoAdic.getNombre())));
		        campoAdicional.setAttributeNode(attrib);		        
	        }
		        
	          TransformerFactory transformerFactory = TransformerFactory.newInstance();
	          Transformer transformer = transformerFactory.newTransformer();
	          DOMSource source = new DOMSource(doc);
	          //empresa.setPathCompGenerados("C:/DataExpress/AVFERNANDEZ/generados/");
	          System.out.println("Ruta:"+empresa.getPathCompGenerados()+informacionTributaria.getAmbiente()+informacionTributaria.getRuc()+informacionTributaria.getCodDoc()+informacionTributaria.getEstab()+informacionTributaria.getPtoEmi()+informacionTributaria.getSecuencial()+".xml");
	          File file = new File(empresa.getPathCompGenerados()+informacionTributaria.getAmbiente()+informacionTributaria.getRuc()+informacionTributaria.getCodDoc()+informacionTributaria.getEstab()+informacionTributaria.getPtoEmi()+informacionTributaria.getSecuencial()+".xml");
	          StreamResult result = new StreamResult(file);
	          
	          //result =  new StreamResult(System.out);
	          transformer.transform(source, result);
	      
	      return true;   	     
	    } catch (ParserConfigurationException pce) {	      
	      pce.printStackTrace();	      
	      //return false;
	      throw new Exception("ERRORGENERAXML,crearFactura Error en el Parseo de "+pce.toString()+",Empresa");
	    } catch (TransformerException tfe) {	    	
	      tfe.printStackTrace();
	      //return false;
	      throw new Exception("ERRORGENERAXML,crearFactura Error en el Transformer de "+tfe.toString()+",Empresa");
		} catch (Exception e){
		  e.printStackTrace();
		  //return false;
		  throw new Exception("ERRORGENERAXML,crearFactura Error en el Exception de "+e.toString()+",Empresa");
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

	public InfoFactura getInfoFac() {
		return infoFactura;
	}

	public void setInfoFac(InfoFactura infoFac) {
		this.infoFactura = infoFac;
	}
	
	/*
	public boolean crearFactura(){
		//Prueba Inicio
		String tipoDocumento = "FACTURA";
		String ls_tipo_documento = "factura";
		String ruta = "C://OroVerde/prueba.xml";
		version ="1.0.0";
		//Prueba Fin
		String fileFirmado = "";
	    String rutaFirmado = "";
	    try {
	    	DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        ArrayList ListDetDocumentosTmp = new ArrayList();

	        //ls_tipo_documento = "";
	        Attr attr = null;
	        
	        //Obtencion del Tab Principal del XML para el tipo de Documento.
	        //ls_tipo_documento = Environment.c.getString("OroVerde.service.facElectronica.tiposDocumentoEtiquetaXml.doc"+tipoDocumento);
	        ls_tipo_documento = "factura";
	        //////////////////////Cabecera Xml///////////////////////////////
	        Document doc = docBuilder.newDocument();
	        Element document = doc.createElement(ls_tipo_documento);
	        doc.appendChild(document);
	        attr = doc.createAttribute("id");
	        attr.setValue("comprobante");
	        document.setAttributeNode(attr);
	        if (!((version != null) || (version.equals(""))))	        
	        version = "1.0.0";	
	        attr = doc.createAttribute("version");
	        attr.setValue(version);
	        document.setAttributeNode(attr);   
	        //////////////////////Cabecera Xml///////////////////////////////
	        
	        //////////////////////infoTributaria////////////////////////////
	        Element infoTrib = doc.createElement("infoTributaria");
	        document.appendChild(infoTrib);
	        
	        
	        //List formatTipDoc = Environment.c.getList("OroVerde.service.facElectronica.ambiente.infoTributaria.documentos");
	        Element ambiente = doc.createElement("ambiente");
		    ambiente.appendChild(doc.createTextNode(new Integer(informacionTributaria.getAmbiente()).toString()));
		    infoTrib.appendChild(ambiente);
		    ///OroVerde.service.facturacion.ambiente.doc
		    	        
	        
		    //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.tipoEmision.documentos");
	        //Obligatorio, conforme tabla 2 Numérico 1
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element tipoEmision = doc.createElement("tipoEmision");
	        tipoEmision.appendChild(doc.createTextNode(new Integer(informacionTributaria.getTipoEmision()).toString()));
	        infoTrib.appendChild(tipoEmision);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.razonSocial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        	Element razonSocial = doc.createElement("razonSocial");
	        	razonSocial.appendChild(doc.createTextNode(informacionTributaria.getRazonSocial()));
	        	infoTrib.appendChild(razonSocial);
	        //}

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.nombreComercial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element nombreComercial = doc.createElement("nombreComercial");
	        nombreComercial.appendChild(doc.createTextNode(informacionTributaria.getNombreComercial()));
	        infoTrib.appendChild(nombreComercial);
	        //}

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.ruc.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element ruc = doc.createElement("ruc");
	        ruc.appendChild(doc.createTextNode(informacionTributaria.getRuc()));
	        infoTrib.appendChild(ruc);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.codDoc.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element codDoc = doc.createElement("codDoc");
	        codDoc.appendChild(doc.createTextNode(informacionTributaria.getCodDoc()));
	        infoTrib.appendChild(codDoc);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.estab.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element estab = doc.createElement("estab");
	        estab.appendChild(doc.createTextNode(informacionTributaria.getEstab()));
	        infoTrib.appendChild(estab);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.ptoEmi.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element ptoEmi = doc.createElement("ptoEmi");
	        ptoEmi.appendChild(doc.createTextNode(informacionTributaria.getPtoEmi()));
	        infoTrib.appendChild(ptoEmi);
	        //}

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.secuencial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        Element secuencial = doc.createElement("secuencial");
	        secuencial.appendChild(doc.createTextNode(informacionTributaria.getSecuencial()));
	        infoTrib.appendChild(secuencial);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoTributaria.dirMatriz.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
	        Element dirMatriz = doc.createElement("dirMatriz");
	        dirMatriz.appendChild(doc.createTextNode(informacionTributaria.getDirMatriz()));
	        infoTrib.appendChild(dirMatriz);          
	        //}
	        
	        //////////////////////infoTributaria////////////////////////////        
	        //////////////////////infoFactura////////////////////////////
	        
	        Element infoDocu = doc.createElement("infoFactura");
	        document.appendChild(infoDocu);
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.fechaEmision.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        Element fechaEmision = doc.createElement("fechaEmision");
		        fechaEmision.appendChild(doc.createTextNode(infoFactura.getFechaEmision()));
		        infoDocu.appendChild(fechaEmision);
	        //}

	        if (infoFactura.getDirEstablecimiento().length()>0){
		        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.dirEstablecimiento.documentos");
		        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
			        Element dirEstablecimiento = doc.createElement("dirEstablecimiento");
			        dirEstablecimiento.appendChild(doc.createTextNode(infoFactura.getDirEstablecimiento()));
			        infoDocu.appendChild(dirEstablecimiento);
		        //}
	        }
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.contribuyenteEspecial.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if ((Integer.parseInt(infoFactura.getContribuyenteEspecial()) > 99) &&(Integer.parseInt(infoFactura.getContribuyenteEspecial()) <= 99999))
		        {
		  	      Element contribuyenteEspecial = doc.createElement("contribuyenteEspecial");
		  	      contribuyenteEspecial.appendChild(doc.createTextNode(infoFactura.getContribuyenteEspecial()));
		  	      infoDocu.appendChild(contribuyenteEspecial);
		        }
	        //}
	        
	        if (infoFactura.getObligadoContabilidad().length()>0){
	            //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.obligadoContabilidad.documentos");
	            //if (getContextDocu(tipoDocumento,formatTipDoc)){
	    	        Element obligadoContabilidad = doc.createElement("obligadoContabilidad");
	    	        obligadoContabilidad.appendChild(doc.createTextNode(infoFactura.getObligadoContabilidad()));
	    	        infoDocu.appendChild(obligadoContabilidad);
	            //}
	        }
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.tipoIdentificacionComprador.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if (infoFactura.getTipoIdentificacionComprador() != null){
		        Element tipoIdentificacionComprador = doc.createElement("tipoIdentificacionComprador");
		        tipoIdentificacionComprador.appendChild(doc.createTextNode(infoFactura.getTipoIdentificacionComprador()));
		        infoDocu.appendChild(tipoIdentificacionComprador);
		        }
	        //}
	        

	        
	        
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.razonSocialComprador.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
		        System.out.println("razonSocialComprador::"+infoFactura.getRazonSocialComprador());
		        if ((infoFactura.getRazonSocialComprador() != null) && 
		          (infoFactura.getRazonSocialComprador().length() > 0)) {
		          Element razonSocialComprador = doc.createElement("razonSocialComprador");
		          razonSocialComprador.appendChild(doc.createTextNode(infoFactura.getRazonSocialComprador()));
		          infoDocu.appendChild(razonSocialComprador);
		        }
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.identificacionComprador.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        
		        System.out.println("identificacionComprador::"+infoFactura.getIdentificacionComprador());
		        if ((infoFactura.getIdentificacionComprador() != null) && 
		          (infoFactura.getIdentificacionComprador().length() > 0)) {
		          Element identificacionComprador = doc.createElement("identificacionComprador");
		          identificacionComprador.appendChild(doc.createTextNode(infoFactura.getIdentificacionComprador()));
		          infoDocu.appendChild(identificacionComprador);
		        }
	        //}
	        

	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.totalSinImpuestos.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if (infoFactura.getTotalSinImpuestos()==null)
		        	infoFactura.setTotalSinImpuestos(0.0D);
		        if (infoFactura.getTotalSinImpuestos() >= 0.0D) {
		          Element totalSinImpuestos = doc.createElement("totalSinImpuestos");
		          totalSinImpuestos.appendChild(doc.createTextNode(new Double(infoFactura.getTotalSinImpuestos()).toString()));
		          infoDocu.appendChild(totalSinImpuestos);
		        }
	        //}
	    
	    
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.totalDescuento.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        if (infoFactura.getTotalDescuento() >= 0.0D) {
		          Element TotalDescuento = doc.createElement("totalDescuento");
		          TotalDescuento.appendChild(doc.createTextNode(new Double(infoFactura.getTotalDescuento()).toString()));
		          infoDocu.appendChild(TotalDescuento);
		        }
	        //}
	        
	        
	        
	        //}
	        	        
	        //////////////////////totalConImpuestos////////////////////////////
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.totalConImpuestos.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){        	        
	        Element totalConImpuestos = doc.createElement("totalConImpuestos");
	        infoDocu.appendChild(totalConImpuestos);        
	        
	          Element totalImpuesto = doc.createElement("totalImpuesto");
	          totalConImpuestos.appendChild(totalImpuesto);
	          
	          Element impuestoCodigo = doc.createElement("codigo");
	          impuestoCodigo.appendChild(doc.createTextNode(new Integer(2).toString()));
	          totalImpuesto.appendChild(impuestoCodigo);
	          
	          Element impuestoCodigoPorcentaje = doc.createElement("codigoPorcentaje");
	          impuestoCodigoPorcentaje.appendChild(doc.createTextNode(new Integer(2).toString()));
	          totalImpuesto.appendChild(impuestoCodigoPorcentaje);

	          Element impuestoBaseImponible = doc.createElement("baseImponible");
	          impuestoBaseImponible.appendChild(doc.createTextNode(new Double(infoFactura.getTotalSinImpuestos()).toString()));
	          totalImpuesto.appendChild(impuestoBaseImponible);


	          Element impuestoValor = doc.createElement("valor");
	          impuestoValor.appendChild(doc.createTextNode(new Double(infoFactura.getTotalImpuestosIva()).toString()));
	          totalImpuesto.appendChild(impuestoValor);
	         
	          
	        
	        //}
	        
	        
	       
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.propina.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
		        Element propina = doc.createElement("propina");
		        propina.appendChild(doc.createTextNode(new Double(infoFactura.getPropina()).toString()));
		        infoDocu.appendChild(propina);
	        //}
	        
	        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.importeTotal.documentos");
		        //if (getContextDocu(tipoDocumento,formatTipDoc)){
			        Element importeTotal = doc.createElement("importeTotal");
			        importeTotal.appendChild(doc.createTextNode(new Double(infoFactura.getImporteTotal()).toString()));
			        infoDocu.appendChild(importeTotal);
		        //}
		        
	        //}
	        
	      //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.moneda.documentos");
	        //if (getContextDocu(tipoDocumento,formatTipDoc)){
	        if (infoFactura.getMoneda().length()>0){
		        //formatTipDoc = Environment.c.getList("facElectronica.format_xml_field.infoDocumento.moneda.documentos");
		        //if (getContextDocu(tipoDocumento,formatTipDoc)){
			          Element moneda = doc.createElement("moneda");
			          moneda.appendChild(doc.createTextNode(infoFactura.getMoneda()));
			          infoDocu.appendChild(moneda);
		        //}
	        }
			        
	        //////////////////////////////////////////////////////////
	        /////////////////////////Detalles/////////////////////////
	        //////////////////////////////////////////////////////////
			        int indexLineaCab = 0;
			        int indexLineaDet = 0;               
			        if (infoFactura.getListDetFactura() != null){        
				        if (infoFactura.getListDetFactura().size() > 0){        			
	        
		        Element detalles = doc.createElement("detalles");
		        document.appendChild(detalles);
		        for (int x=0;x<=infoFactura.getListDetFactura().size()-1;x++){
		        	DetalleFactura DetDocTmp = new DetalleFactura(); 
			        DetDocTmp = (DetalleFactura)infoFactura.getListDetFactura().get(x);
			        Element detalle = doc.createElement("detalle");
			        detalles.appendChild(detalle);
			        
			        //indexLineaCab = DetDocTmp.getLineaFactura();
			        String ls_alias = "";
			        if (tipoDocumento.equals("04")){
			        	ls_alias = "codigoInterno";
			        }else{	
			        	ls_alias = "codigoPrincipal";
			        }
			        Element codigoPrincipal = doc.createElement(ls_alias);
			        codigoPrincipal.appendChild(doc.createTextNode(DetDocTmp.getCodigoPrincipal()));
			        detalle.appendChild(codigoPrincipal);
			        
			        if (tipoDocumento.equals("04")){
			        	ls_alias = "codigoAdicional";
			        }else{	
			        	ls_alias = "codigoAuxiliar";
			        }
			        if (DetDocTmp.getCodigoAuxiliar().length()>0){
			        Element codigoAuxiliar = doc.createElement(ls_alias);
			        codigoAuxiliar.appendChild(doc.createTextNode(DetDocTmp.getCodigoAuxiliar()));
			        detalle.appendChild(codigoAuxiliar);
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
		        
			        

		        	if (infoFactura.getListDetFactura().get(x).getListImpuestos()!=null){
		        	if (infoFactura.getListDetFactura().get(x).getListImpuestos().size()>0){
		        	Element impuestos = doc.createElement("impuestos");
		            detalle.appendChild(impuestos);
		            
		        	for (int z=0;z<=infoFactura.getListDetFactura().get(x).getListImpuestos().size()-1;z++){
		        		
		        		ImpuestosFactura DocImp = new ImpuestosFactura(); 
		        	   DocImp = (ImpuestosFactura)infoFactura.getListDetFactura().get(x).getListImpuestos().get(z);
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
	      return false;
	    } catch (TransformerException tfe) {	    	
	      tfe.printStackTrace();
	      return false;
		}
	}
	*/
	
}
