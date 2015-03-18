package com.cimait.DAO;

public class Transaction {
		
		private String idMovimiento;
		private String secuencial;		 		
		private String tipoDocumento;
		private String fechaEmision;
		private String local;
		private String caja;
				
		public Transaction(String idMovimiento,
						   String secuencial,						  		
						   String tipoDocumento,
						   String fecha,
						   String local,
						   String caja){
			this.secuencial=secuencial;
			this.idMovimiento=idMovimiento;
			this.tipoDocumento=tipoDocumento;
			this.fechaEmision=fecha;
			this.local = local;
			this.caja = caja;
		}
		
		public void mostrar(){			
			System.out.println("IdMovimiento:" + idMovimiento); 			 
			System.out.println("Secuencial:" + secuencial);
			System.out.println("Fecha Emision:" + fechaEmision);
			System.out.println("TipoDocumento:" + tipoDocumento);
			System.out.println("Local:" + local);
			System.out.println("Caja:" + caja);
			System.out.println("------------------------------------");
		}

		public String getSecuencial() {
			return secuencial;
		}

		public void setSecuencial(String secuencial) {
			this.secuencial = secuencial;
		}

		public String getTipoDocumento() {
			return tipoDocumento;
		}

		public void setTipoDocumento(String tipoDocumento) {
			this.tipoDocumento = tipoDocumento;
		}

		public String getFechaEmision() {
			return fechaEmision;
		}

		public void setFechaEmision(String fechaEmision) {
			this.fechaEmision = fechaEmision;
		}

		public String getIdMovimiento() {
			return idMovimiento;
		}

		public void setIdMovimiento(String idMovimiento) {
			this.idMovimiento = idMovimiento;
		}

		public String getLocal() {
			return local;
		}

		public void setLocal(String local) {
			this.local = local;
		}

		public String getCaja() {
			return caja;
		}

		public void setCaja(String caja) {
			this.caja = caja;
		}
		
		

}