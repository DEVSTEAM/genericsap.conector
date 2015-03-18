package com.cimait.DAO;

public class OperaFolio {
		
		private String billNo;
		private String billNoOpera;
		private String resort;
		private String businessDate; 
		private String billGenerationDate; 
		private String insertDate;
		private String updateDate;
		private String billGenerationTime; 
		private String queueName;
		private int	   folio_no;
		
		public OperaFolio(String billNo,
						  String billNoOpera,
						  String resort, 
						  String businessDate, 
						  String billGenerationDate,
						  String insertDate,
						  String updateDate,
						  String billGenerationTime,
						  String queueName,
						  int folio_no){
			this.billNo = billNo;
			this.billNoOpera = billNoOpera;
			this.resort = resort;
			this.businessDate = businessDate; 
			this.billGenerationDate = billGenerationDate;
			this.insertDate = insertDate;
			this.updateDate = updateDate;
			this.billGenerationTime = billGenerationTime;
			this.queueName = queueName;
			this.folio_no = folio_no;
		}
		
		public void mostrar(){
			System.out.println("billNo:" + billNo); 
			System.out.println("billNoOpera:" + billNoOpera); 
			System.out.println("resort:" + resort);
			System.out.println("businessDate:" + businessDate);
			System.out.println("billGenerationDate:" + billGenerationDate);
			System.out.println("insertDate:" + insertDate);
			System.out.println("updateDate:" + updateDate);
			System.out.println("billGenerationTime:" + billGenerationTime);
			System.out.println("queueName:" + queueName);
			System.out.println("folio_no:" + folio_no);
		}
		
		public String getBillNo() {
			return billNo;
		}
		public void setBillNo(String billNo) {
			this.billNo = billNo;
		}		
		public String getBillNoOpera() {
			return billNoOpera;
		}
		public void setBillNoOpera(String billNoOpera) {
			this.billNoOpera = billNoOpera;
		}
		public String getResort() {
			return resort;
		}
		public void setResort(String resort) {
			this.resort = resort;
		}
		public String getBusinessDate() {
			return businessDate;
		}
		public void setBusinessDate(String businessDate) {
			this.businessDate = businessDate;
		}
		public String getBillGenerationDate() {
			return billGenerationDate;
		}
		public void setBillGenerationDate(String billGenerationDate) {
			this.billGenerationDate = billGenerationDate;
		}
		public String getInsertDate() {
			return insertDate;
		}
		public void setInsertDate(String insertDate) {
			this.insertDate = insertDate;
		}
		public String getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}
		public String getBillGenerationTime() {
			return billGenerationTime;
		}
		public void setBillGenerationTime(String billGenerationTime) {
			this.billGenerationTime = billGenerationTime;
		}
		public String getQueueName() {
			return queueName;
		}
		public void setQueueName(String queueName) {
			this.queueName = queueName;
		}

		public int getFolio_no() {
			return folio_no;
		}

		public void setFolio_no(int folio_no) {
			this.folio_no = folio_no;
		}
		
}