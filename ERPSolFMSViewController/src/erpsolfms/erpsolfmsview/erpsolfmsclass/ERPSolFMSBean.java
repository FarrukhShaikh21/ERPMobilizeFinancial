package erpsolfms.erpsolfmsview.erpsolfmsclass;

import erpsolglob.erpsolglobmodel.erpsolglobclasses.ERPSolGlobClassModel;

import erpsolglob.erpsolglobview.erpclass.ERPSolGlobalViewBean;

import java.sql.CallableStatement;

import java.sql.SQLException;
import java.sql.Types;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import oracle.adf.model.AttributeBinding;

import oracle.binding.BindingContainer;
import oracle.adf.model.BindingContext;
import oracle.adf.model.OperationBinding;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCDataControl;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.model.binding.DCIteratorBindingDef;
import oracle.adf.share.ADFContext;

import oracle.adf.view.rich.component.rich.RichPopup;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.event.DialogEvent;

import oracle.jbo.ApplicationModule;
import oracle.jbo.JboException;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.server.DBTransaction;

import org.apache.myfaces.trinidad.model.RowKeySet;
import org.apache.myfaces.trinidad.render.ExtendedRenderKitService;
import org.apache.myfaces.trinidad.util.Service;

public class ERPSolFMSBean {
    public ERPSolFMSBean() {
        super();
    }

    String ERPSolStrUserCode;
    String ERPSolStrUserLocationCode;
    String ERPSolStrUserRegionCode;
    String ERPSolStrUserStoreCode;
    String ERPSolScanType="B";
    String ERPSolProductId;
    String ERPSolSalesOrderId;
    RichPopup ERPSolImeiPopup;
    RichTable ERPSolTable;
    String ERPSolReportName;
    
    
    public void doSetERPSolFMSSessionGlobals() {
        System.out.println("glob user code"+getERPSolStrUserCode());
        if (getERPSolStrUserCode().length()==0) {
           FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Users Defaults are not defined properly. Please Check"));
           throw new JboException("Users Defaults are not defined properly. Please Check");
        }
        ADFContext.getCurrent().getPageFlowScope().put("GLOB_USER_CODE", getERPSolStrUserCode());
        ADFContext.getCurrent().getPageFlowScope().put("GLOB_USER_REGION", getERPSolStrUserRegionCode());
        ADFContext.getCurrent().getPageFlowScope().put("GLOB_USER_LOCATION", getERPSolStrUserLocationCode());
        ADFContext.getCurrent().getPageFlowScope().put("GLOB_USER_STORE", getERPSolStrUserStoreCode());
        ADFContext.getCurrent().getPageFlowScope().put("GLOB_COMPANY_CODE", "D");
    }


    public void setERPSolStrUserCode(String ERPSolStrUserCode) {
        this.ERPSolStrUserCode = ERPSolStrUserCode;
    }

    public String getERPSolStrUserCode() {
        return ERPSolStrUserCode;
    }

    public void setERPSolStrUserLocationCode(String ERPSolStrUserLocationCode) {
        this.ERPSolStrUserLocationCode = ERPSolStrUserLocationCode;
    }

    public String getERPSolStrUserLocationCode() {
        return ERPSolStrUserLocationCode;
    }

    public void setERPSolStrUserRegionCode(String ERPSolStrUserRegionCode) {
        this.ERPSolStrUserRegionCode = ERPSolStrUserRegionCode;
    }

    public String getERPSolStrUserRegionCode() {
        return ERPSolStrUserRegionCode;
    }

    public void setERPSolStrUserStoreCode(String ERPSolStrUserStoreCode) {
        this.ERPSolStrUserStoreCode = ERPSolStrUserStoreCode;
    }

    public String getERPSolStrUserStoreCode() {
        return ERPSolStrUserStoreCode;
    }
//    
    public List<SelectItem> doERPSolGetAutoSuggestedBranchValues(String pStringValues) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        DCIteratorBinding ERPSolIB=(DCIteratorBinding)ERPSolbc.get("ReceiptMasterControlCRUDIterator");
        ApplicationModule ERPSolAM=ERPSolIB.getViewObject().getApplicationModule();
        AttributeBinding ERPLocid =(AttributeBinding)ERPSolbc.getControlBinding("Locationid");
        AttributeBinding ERPSalespersonId =(AttributeBinding)ERPSolbc.getControlBinding("Salespersonid");
        AttributeBinding ERPReceiptMode =(AttributeBinding)ERPSolbc.getControlBinding("ReceiptMode");
        
        
        ViewObject vo=ERPSolAM.findViewObject("VWAllBankBranchesAutoSuggestRO");
        vo.setNamedWhereClauseParam("P_ADF_RECEIPT_MODE", ERPReceiptMode.getInputValue());
        vo.setNamedWhereClauseParam("P_ADF_SALESPERSONID", ERPSalespersonId.getInputValue());
        vo.setNamedWhereClauseParam("P_ADF_LOCATIONID", ERPLocid.getInputValue());
        vo.executeQuery();
        
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "VWAllBankBranchesAutoSuggestRO",
                                                            " UPPER(CONCAT(Branch_Name,Branchid))", "BranchName", "Branchid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }

    
    public List<SelectItem> doERPSolGetAutoSuggestedARUnsubmitDoc(String pStringValues) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        System.out.println("a");
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        DCIteratorBinding ERPSolIB=(DCIteratorBinding)ERPSolbc.get("VwGLFinanceReportROIterator");
        ApplicationModule ERPSolAM=ERPSolIB.getViewObject().getApplicationModule();
        System.out.println("b");
        String ERPLocid=ERPSolGlobClassModel.doGetUserLocationCode();
        AttributeBinding ERPDocType =(AttributeBinding)ERPSolbc.getControlBinding("txtDoctypeId");
        ViewObject vo=ERPSolAM.findViewObject("VwARDocumentIdForUnsubmitAutoSuggestRO");
        vo.setNamedWhereClauseParam("P_ADF_DOCTYPEID", ERPDocType.getInputValue());
        vo.setNamedWhereClauseParam("P_ADF_LOCATIONID", ERPLocid);
        vo.executeQuery();
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "VwARDocumentIdForUnsubmitAutoSuggestRO",
                                                            " UPPER(CONCAT(Receipt_No,Description))", "ReceiptNo", "Description", 10);
        return ResultList;
        
    }
    
    public List<SelectItem> doERPSolGetAutoSuggestedLocationValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        System.out.println("a");
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "AllLocationsAutoSuggestRO",
                                                            "UPPER(CONCAT(Locationid,Location_Description))", "LocationDescription", "Locationid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }
    
    public List<SelectItem> doERPSolGetAutoSuggestedCustomerValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        System.out.println("a");
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        AttributeBinding ERPLocid =(AttributeBinding)ERPSolbc.getControlBinding("Locationid");
        System.out.println("b");
//        DCIteratorBinding ERPSolib =(DCIteratorBinding)ERPSolbc.get("ReceiptMasterControlCRUDIterator");
        System.out.println("c");
//        String ERPLocid=""+ERPSolib.getCurrentRow().getAttribute("Locationid");
        System.out.println("d");
//        System.out.println(ERPLocid);//ERPSolGlobalViewBean.
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "AllCustomersAutoSuggestRO",
                                                            "LOCATIONID='"+ERPLocid+"' AND UPPER(CONCAT(CUSTOMERID,CUSTOMER_NAME))", "CustomerName", "Customerid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }   

    public List<SelectItem> doERPSolGetReceiptAutoSuggestedCustomerValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        System.out.println("a");
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        AttributeBinding ERPLocid =(AttributeBinding)ERPSolbc.getControlBinding("Locationid");
        System.out.println("b");
    //        DCIteratorBinding ERPSolib =(DCIteratorBinding)ERPSolbc.get("ReceiptMasterControlCRUDIterator");
        System.out.println("c");
    //        String ERPLocid=""+ERPSolib.getCurrentRow().getAttribute("Locationid");
        System.out.println("d");
    //        System.out.println(ERPLocid);//ERPSolGlobalViewBean.
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "VwAllCustomerAutoSuggestRO",
                                                            " LOCATIONID='"+ERPLocid+"' AND  UPPER(CONCAT(CUSTOMERID,CUSTOMER_NAME))", "CustomerName", "Customerid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }   


    public List<SelectItem> doERPSolGetAutoSuggestedSalesPersonValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        System.out.println("a");
//        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
//        DCIteratorBinding ERPSolib =(DCIteratorBinding)ERPSolbc.get("ReceiptMasterControlCRUDIterator");
        System.out.println("c");
        
//        String ERPCustomerId=""+ERPSolib.getCurrentRow().getAttribute("Customerid");

        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        AttributeBinding ERPCustomerId =(AttributeBinding)ERPSolbc.getControlBinding("Customerid");
        
        System.out.println("d");
        System.out.println(ERPCustomerId);//ERPSolGlobalViewBean.
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "SoSalesPersonsAutoSuggestRO",
                                                            "SALESPERSONID IN (SELECT ASP.SALESPERSONID from ALL_CUSTOMER_SALESPERSON ASP WHERE ASP.CUSTOMERID='"+ERPCustomerId+"') AND UPPER(CONCAT(Salespersonid,name))", "Name", "Salespersonid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }   
   
    public List<SelectItem> doERPSolGetAutoSuggestedSupplierValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        System.out.println("a");
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        AttributeBinding ERPLocid =(AttributeBinding)ERPSolbc.getControlBinding("Locationid");
        System.out.println("b");
//        DCIteratorBinding ERPSolib =(DCIteratorBinding)ERPSolbc.get("ReceiptMasterControlCRUDIterator");
        System.out.println("c");
//        String ERPLocid=""+ERPSolib.getCurrentRow().getAttribute("Locationid");
        System.out.println("d");
        System.out.println(ERPLocid);//ERPSolGlobalViewBean.
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "PuSuppliersAutoSuggestRO",
                                                            "LOCATIONID='"+ERPLocid+"' AND UPPER(CONCAT(SUPPLIERID,SUPP_NAME))", "SuppName", "Supplierid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }   
     
    
    public List<SelectItem> doERPSolGetAutoSuggestedModelValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "InItemsAutoSuggestRO"," UPPER(CONCAT(Productid,Model_No))", "Productid", "ModelNo", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }   
  
    public List<SelectItem> doERPSolGetAutoSuggestedCOAValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "VWChartOfAccountAutoSuggestRO"," UPPER(CONCAT(GLCODE,Description))", "Description", "Glcode", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }  
    
    public List<SelectItem> doERPSolGetAutoSuggestedCostCenterValues(String pStringValues) {
    //public static List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        //public List<SelectItem> doERPSolGetAutoSuggestedValues(String pSearch,String pViewObjectName,String pWhereColumn,String pAttribute1,String pAttribute2,Integer pNoOfRecordsSuggest) {
        List<SelectItem> ResultList=new ArrayList<SelectItem>();
        ResultList= ERPSolGlobalViewBean.doERPSolGetAutoSuggestedValues(pStringValues, "VWCostCenterAutoSuggestRO"," UPPER(CONCAT(Costcenterid,Description))", "Description", "Costcenterid", 10,"ERPSolFMSAppModuleDataControl");
        return ResultList;
        
    }  
    
    
    
    public void doERPSolDialogConfirm(DialogEvent erpsolde) {
        if (erpsolde.getOutcome()==DialogEvent.Outcome.yes) {
            OperationBinding ob = ERPSolGlobalViewBean.doIsERPSolGerOperationBinding("Commit");
            ob.execute();
            if (!ob.getErrors().isEmpty()) {
                return ;
           }
             ob = ERPSolGlobalViewBean.doIsERPSolGerOperationBinding("doSuperviseControlReceipt");
            ob.execute();
        }
    }

    public void doERPSolArNoteDialogConfirm(DialogEvent erpsolde) {
        if (erpsolde.getOutcome()==DialogEvent.Outcome.yes) {
            OperationBinding ob = ERPSolGlobalViewBean.doIsERPSolGerOperationBinding("Commit");
            ob.execute();
            if (!ob.getErrors().isEmpty()) {
                return ;
           }
             ob = ERPSolGlobalViewBean.doIsERPSolGerOperationBinding("doSuperviseARNoteMaster");
            ob.execute();
        }
    }

    public void setERPSolImeiPopup(RichPopup ERPSolImeiPopup) {
        this.ERPSolImeiPopup = ERPSolImeiPopup;
    }

    public RichPopup getERPSolImeiPopup() {
        return ERPSolImeiPopup;
    }

    public String DoShowERPSolImeiPopup() {
        RichPopup.PopupHints ERPSolHints=new RichPopup.PopupHints();
        getERPSolImeiPopup().show(ERPSolHints);
        return null;
    }
  

    public void setERPSolScanType(String ERPSolScanType) {
        this.ERPSolScanType = ERPSolScanType;
    }

    public String getERPSolScanType() {
        return ERPSolScanType;
    }

    public void setERPSolProductId(String ERPSolProductId) {
        this.ERPSolProductId = ERPSolProductId;
    }

    public String getERPSolProductId() {
        return ERPSolProductId;
    }

    public void setERPSolSalesOrderId(String ERPSolSalesOrderId) {
        this.ERPSolSalesOrderId = ERPSolSalesOrderId;
    }

    public String getERPSolSalesOrderId() {
        return ERPSolSalesOrderId;
    }


    public void setERPSolTable(RichTable ERPSolTable) {
        this.ERPSolTable = ERPSolTable;
    }

    public RichTable getERPSolTable() {
        return ERPSolTable;
    }

    public void doErpSolOpenReportTab(String url) {
    ExtendedRenderKitService erks =
    Service.getRenderKitService(FacesContext.getCurrentInstance(), ExtendedRenderKitService.class);
    StringBuilder strb = new StringBuilder("window.open('" + url + "');");
    erks.addScript(FacesContext.getCurrentInstance(), strb.toString());
    }  
    
    public String doERPSolGetFormatDate(String pDate) {
     
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String fromDate="";
        try {
            Date dt = sdf.parse(pDate);
            sdf=new SimpleDateFormat("dd-MMM-yyyy");
            fromDate=sdf.format(dt).toUpperCase();
            return fromDate;
        }
        catch (NullPointerException npe) {
            System.out.println("null parseexception");
        }
        catch (ParseException e) {
            System.out.println("parseexception");
        }
        /////////////
           
        return null;
    }


    public String doERPSolExecuteARReport() {
        BindingContainer bc = ERPSolGlobalViewBean.doGetERPBindings();
        DCIteratorBinding ib=(DCIteratorBinding)bc.get("SysProgramDetROIterator");
        ApplicationModule am=ib.getViewObject().getApplicationModule();
        ViewObject vo=am.findViewObject("QVOReport");
        if (vo!=null) {
            vo.remove();
       }
        
        vo=am.createViewObjectFromQueryStmt("QVOReport", "select PARAMETER_VALUE FROM so_sales_parameter a where a.Parameter_Id='REPORT_SERVER_URL'");
        vo.executeQuery();
        String pReportUrl=vo.first().getAttribute(0).toString();
        vo.remove();
        vo=am.createViewObjectFromQueryStmt("QVOReport", "select PATH PATH FROM SYSTEM a where a.PROJECTID='AR' ");
        vo.executeQuery();
        String pReportPath=vo.first().getAttribute(0).toString()+"REPORTS\\\\";
        System.out.println(pReportPath);
        pReportPath=pReportPath+ERPSolReportName;
        
    
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        AttributeBinding ERPCompanyid       =(AttributeBinding)ERPSolbc.getControlBinding("Companyid");
        AttributeBinding ERPRegionid        =(AttributeBinding)ERPSolbc.getControlBinding("Regionid");
        AttributeBinding ERPLocationid      =(AttributeBinding)ERPSolbc.getControlBinding("Locationid");
        AttributeBinding ERPDivid           =(AttributeBinding)ERPSolbc.getControlBinding("Divid");
        AttributeBinding ERPSalesTerritory  =(AttributeBinding)ERPSolbc.getControlBinding("txtSalesTerritoryId");
        AttributeBinding ERPCustomerid      =(AttributeBinding)ERPSolbc.getControlBinding("Customerid");
        AttributeBinding ERPSalespersonid   =(AttributeBinding)ERPSolbc.getControlBinding("Salespersonid");
//        AttributeBinding ERPProductgroup    =(AttributeBinding)ERPSolbc.getControlBinding("Productgroup");
//        AttributeBinding ERPProductid       =(AttributeBinding)ERPSolbc.getControlBinding("Productid");
        AttributeBinding ERPFromDate        =(AttributeBinding)ERPSolbc.getControlBinding("txtFromDate");
        AttributeBinding ERPToDate          =(AttributeBinding)ERPSolbc.getControlBinding("txtToDate");
        String reportParameter="";
        reportParameter="COMPANY="+ (ERPCompanyid.getInputValue()==null?"":ERPCompanyid.getInputValue());
        reportParameter+="&P_DIVID="+(ERPDivid.getInputValue()==null?"":ERPDivid.getInputValue());
        reportParameter+="&P_REGID="+(ERPRegionid.getInputValue()==null?"":ERPRegionid.getInputValue());
        reportParameter+="&P_LOCID="+(ERPLocationid.getInputValue()==null?"":ERPLocationid.getInputValue());
        reportParameter+="&P_TERRITORY_ID="+(ERPSalesTerritory.getInputValue()==null?"":ERPSalesTerritory.getInputValue());
        reportParameter+="&CUSTID="+(ERPCustomerid.getInputValue()==null?"":ERPCustomerid.getInputValue());
        reportParameter+="&SALEPERSONID="+(ERPSalespersonid.getInputValue()==null?"":ERPSalespersonid.getInputValue());
//        reportParameter+="&P_PRODUCT_GROUP_ID="+(ERPProductgroup.getInputValue()==null?"":ERPProductgroup.getInputValue());
//        reportParameter+="&P_PRODUCT_ID="+(ERPProductid.getInputValue()==null?"":ERPProductid.getInputValue());
        reportParameter+="&FROM_DATE="+(ERPFromDate.getInputValue()==null?"":doERPSolGetFormatDate(""+ERPFromDate.getInputValue() ) );
        reportParameter+="&TO_DATE="+(ERPToDate.getInputValue()==null?"":doERPSolGetFormatDate(""+ERPToDate.getInputValue())  );
        reportParameter+="&USER="+ERPSolGlobClassModel.doGetUserCode();
        
        pReportUrl=pReportUrl.replace("<P_REPORT_PATH>", pReportPath);
        pReportUrl=pReportUrl.replace("<P_REPORT_PARAMETERS>", reportParameter);
        
        System.out.println(pReportPath);
        System.out.println(reportParameter);
        System.out.println(pReportUrl);
        
        doErpSolOpenReportTab(pReportUrl);
        return null;
    }
    
    public String doERPSolExecuteAR0047Report() {
        BindingContainer bc = ERPSolGlobalViewBean.doGetERPBindings();
        DCIteratorBinding ib=(DCIteratorBinding)bc.get("ReceiptMasterControlCRUDIterator");
        ApplicationModule am=ib.getViewObject().getApplicationModule();
        ViewObject vo=am.findViewObject("QVOReceipt");
        if (vo!=null) {
            vo.remove();
       }
        
        vo=am.createViewObjectFromQueryStmt("QVOReceipt", "select PARAMETER_VALUE FROM so_sales_parameter a where a.Parameter_Id='REPORT_SERVER_URL'");
        vo.executeQuery();
        String pReportUrl=vo.first().getAttribute(0).toString();
        vo.remove();
        vo=am.createViewObjectFromQueryStmt("QVOReceipt", "select PATH PATH FROM SYSTEM a where a.PROJECTID='AR' ");
        vo.executeQuery();
        String pReportPath=vo.first().getAttribute(0).toString()+"REPORTS\\\\";
        System.out.println(pReportPath);
        pReportPath=pReportPath+"RECEIPT_CONTROL";
        
    
        BindingContainer ERPSolbc=ERPSolGlobalViewBean.doGetERPBindings();
        System.out.println("b");
        AttributeBinding ERPCompanyid       =(AttributeBinding)ERPSolbc.getControlBinding("Companyid");
//        AttributeBinding ERPRegionid        =(AttributeBinding)ERPSolbc.getControlBinding("Regionid");
        AttributeBinding ERPReceiptNo        =(AttributeBinding)ERPSolbc.getControlBinding("ReceiptNo");
        AttributeBinding ERPLocationid      =(AttributeBinding)ERPSolbc.getControlBinding("Locationid");
//        AttributeBinding ERPSalesTerritory  =(AttributeBinding)ERPSolbc.getControlBinding("txtSalesTerritoryId");
//        AttributeBinding ERPCustomerid      =(AttributeBinding)ERPSolbc.getControlBinding("Customerid");
//        AttributeBinding ERPSalespersonid   =(AttributeBinding)ERPSolbc.getControlBinding("Salespersonid");
    //        AttributeBinding ERPProductgroup    =(AttributeBinding)ERPSolbc.getControlBinding("Productgroup");
    //        AttributeBinding ERPProductid       =(AttributeBinding)ERPSolbc.getControlBinding("Productid");
//        AttributeBinding ERPFromDate        =(AttributeBinding)ERPSolbc.getControlBinding("txtFromDate");
//        AttributeBinding ERPToDate          =(AttributeBinding)ERPSolbc.getControlBinding("txtToDate");
        String reportParameter="";
        reportParameter="COMPANY="+ (ERPCompanyid.getInputValue()==null?"":ERPCompanyid.getInputValue());
//        reportParameter+="&P_REGID="+(ERPRegionid.getInputValue()==null?"":ERPRegionid.getInputValue());
        reportParameter+="&LOCID="+(ERPLocationid.getInputValue()==null?"":ERPLocationid.getInputValue());
        reportParameter+="&R="+(ERPReceiptNo.getInputValue()==null?"":ERPReceiptNo.getInputValue());
//        reportParameter+="&P_TERRITORY_ID="+(ERPSalesTerritory.getInputValue()==null?"":ERPSalesTerritory.getInputValue());
//        reportParameter+="&CUSTID="+(ERPCustomerid.getInputValue()==null?"":ERPCustomerid.getInputValue());
//        reportParameter+="&SALEPERSONID="+(ERPSalespersonid.getInputValue()==null?"":ERPSalespersonid.getInputValue());
    //        reportParameter+="&P_PRODUCT_GROUP_ID="+(ERPProductgroup.getInputValue()==null?"":ERPProductgroup.getInputValue());
    //        reportParameter+="&P_PRODUCT_ID="+(ERPProductid.getInputValue()==null?"":ERPProductid.getInputValue());
//        reportParameter+="&FROM_DATE="+(ERPFromDate.getInputValue()==null?"":doERPSolGetFormatDate(""+ERPFromDate.getInputValue() ) );
//        reportParameter+="&TO_DATE="+(ERPToDate.getInputValue()==null?"":doERPSolGetFormatDate(""+ERPToDate.getInputValue())  );
        reportParameter+="&USER="+ERPSolGlobClassModel.doGetUserCode();
        
        pReportUrl=pReportUrl.replace("<P_REPORT_PATH>", pReportPath);
        pReportUrl=pReportUrl.replace("<P_REPORT_PARAMETERS>", reportParameter);
        
        System.out.println(pReportPath);
        System.out.println(reportParameter);
        System.out.println(pReportUrl);
        
        doErpSolOpenReportTab(pReportUrl);
        return null;
    }

    public String ErpSolsetFocusOnNewRecord() {
    FacesContext facesCtx = FacesContext.getCurrentInstance();
    RowKeySet rks = getERPSolTable().getSelectedRowKeys();
    String inputId ="";
    if(rks != null && rks.size() > 0) {
    Object rowKey = rks.iterator().next();
    String rowId = getERPSolTable().getClientRowKeyManager().getClientRowKey(facesCtx,  getERPSolTable(), rowKey);
    inputId = getERPSolTable().getClientId(facesCtx) + ":" + rowId + ":" + "it6"; //here it6 is id for inputtext in1st column.
    System.out.println("inputid "+inputId);
    } else {
    // handle error
    }
    ExtendedRenderKitService service = Service.getRenderKitService(facesCtx, ExtendedRenderKitService.class);
    service.addScript(facesCtx, "comp = AdfPage.PAGE.findComponent('"+inputId+"');\n" +
    "comp.focus()");      // javascript method is used
    return null;
    }

    public void setERPSolReportName(String ERPSolReportName) {
        this.ERPSolReportName = ERPSolReportName;
    }

    public String getERPSolReportName() {
        return ERPSolReportName;
    }
    
    
}
